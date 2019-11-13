package dawn.utils.distributed.lock.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 默认使用该锁
 * zk 实现分布式锁  http://colobu.com/2014/12/12/zookeeper-recipes-by-example-2/
 * 不可重入共享锁,同一时刻一个线程只能获取一次,第二次以上没有被释放会被阻塞
 */
@Slf4j
public class ZkSemaphoreMutexLock implements ZkDistributeLock {

	private InterProcessSemaphoreMutex lock;

	public ZkSemaphoreMutexLock(CuratorFramework client, String keyName) {
		if (client == null || StringUtils.isEmpty(keyName)) {
			log.error("[ZkSemaphoreMutexLock] client 或者 keyName 不能为空 client={},keyName={}", client, keyName);
			throw new RuntimeException("client 或者 keyName 不能为空");
		}
		if (!keyName.startsWith("/")) {
			keyName = "/" + keyName;
		}
		lock = new InterProcessSemaphoreMutex(client, keyName);
	}

	public void lock() {
		log.debug("[ZkSemaphoreMutexLock] lock 获取锁开始,线程阻塞ThreadId={}", Thread.currentThread().getId());
		try {
			lock.acquire();
		} catch (Exception e) {
			log.error("[ZkSemaphoreMutexLock] lock 获取锁异常", e);
			throw new RuntimeException(e);
		}
		log.debug("[ZkSemaphoreMutexLock] lock 成功获取锁ThreadId={}", Thread.currentThread().getId());
	}

	public boolean tryLock() {
		log.debug("[ZkSemaphoreMutexLock] tryLock 获取锁开始");
		try {
			return lock.acquire(0, TimeUnit.MICROSECONDS);
		} catch (Exception e) {
			log.error("[ZkSemaphoreMutexLock] tryLock 获取锁异常", e);
			return false;
		}
	}

	public boolean tryLock(long time, TimeUnit unit) {
		try {
			return lock.acquire(time, unit);
		} catch (Exception e) {
			log.error("[ZkSemaphoreMutexLock] tryLock 获取锁异常", e);
			return false;
		}
	}

	public void unlock() {
		log.debug("[ZkSemaphoreMutexLock] unlock 释放锁开始");
		try {
			lock.release();
		} catch (Exception e) {
			log.error("[ZkSemaphoreMutexLock] unlock 释放锁失败", e);
			return;
		}
		log.debug("[ZkSemaphoreMutexLock] unlock 成功释放锁");
	}

}