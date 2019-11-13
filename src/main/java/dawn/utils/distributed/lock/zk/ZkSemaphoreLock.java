package dawn.utils.distributed.lock.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * zk 实现分布式锁  http://colobu.com/2014/12/12/zookeeper-recipes-by-example-2/
 * <p>
 * 一个计数的信号量类似JDK的Semaphore。 JDK中Semaphore维护的一组许可(permits)，而Curator中称之为租约(Lease)。
 * 有两种方式可以决定semaphore的最大租约数。第一种方式是有用户给定的path决定。第二种方式使用SharedCountReader类。
 * 如果不使用SharedCountReader, 所有的实例必须使用相同的numberOfLeases值.
 * <p>
 * 每次调用acquire会返回一个租约对象。 客户端必须在finally中close这些租约对象，否则这些租约会丢失掉。
 * 但是，如果客户端session由于某种原因比如crash丢掉， 那么这些客户端持有的租约会自动close，
 * 这样其它客户端可以继续使用这些租约。
 */
@Slf4j
public class ZkSemaphoreLock implements ZkDistributeLock {

	private InterProcessSemaphoreV2 lock;

	private Lease lease;

	public ZkSemaphoreLock(CuratorFramework client, String keyName) {
		if (client == null || StringUtils.isEmpty(keyName)) {
			log.error("client 或者 keyName 不能为空 client={},keyName={}", client, keyName);
			throw new RuntimeException("client 或者 keyName 不能为空");
		}
		if (!keyName.startsWith("/")) {
			keyName = "/" + keyName;
		}
		lock = new InterProcessSemaphoreV2(client, keyName, 1);
	}

	public void lock() {
		log.debug("获取锁开始,线程阻塞");
		try {
			lease = lock.acquire();
			log.debug("lease={}", lease);
		} catch (Exception e) {
			log.error("获取锁异常", e);
			throw new RuntimeException(e);
		}
		log.debug("成功获取锁");
	}

	public boolean tryLock() {
		log.debug("获取锁开始");
		try {
			lease = lock.acquire(1, TimeUnit.MICROSECONDS);
			log.debug("lease={}", lease);
			if (null != lease) {
				log.debug("成功获取锁");
				return true;
			}
		} catch (Exception e) {
			log.error("获取锁异常", e);
			return false;
		}
		return false;
	}

	public boolean tryLock(long time, TimeUnit unit) {
		try {
			lease = lock.acquire(time, unit);
			log.debug("lease={}", lease);
			if (null != lease) {
				log.debug("成功获取锁");
				return true;
			}
		} catch (Exception e) {
			log.error("获取锁异常", e);
			return false;
		}
		return false;
	}

	public void unlock() {
		log.debug("释放锁开始");
		lock.returnLease(lease);
		log.debug("成功释放锁");
	}

}