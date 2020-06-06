package dawn.utils.distributed.helper;

import dawn.utils.distributed.lock.DistributeLock;
import dawn.utils.distributed.lock.DistributeLockFactory;
import dawn.utils.distributed.lock.zk.ZkSemaphoreMutexLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分布式锁帮助类
 */
@Slf4j
//@Component
public class LockHelper {

	@Autowired
	private List<DistributeLockFactory> factories;

	private static final String LOCK_NAME_PREFIX = "distribute-lock-%s-%s-%s";

	/**
	 * 获取分布式锁(默认zk不可重入共享锁)
	 *
	 * @param contextName 应用context
	 * @param methodName  需要锁住的方法名
	 * @param bizObj      业务对象,保证key名唯一，举例：partyId
	 * @return zk锁
	 */
	public DistributeLock build(String contextName, String methodName, Object bizObj) {
		return build(contextName, methodName, bizObj, ZkSemaphoreMutexLock.class);
	}

	/**
	 * 获取分布式锁
	 *
	 * @param contextName 应用context
	 * @param methodName  需要锁住的方法名
	 * @param bizObj      业务对象,保证key名唯一，举例：partyId
	 * @param lockClass   锁类型
	 * @return zk锁
	 */
	public DistributeLock build(String contextName, String methodName, Object bizObj, Class<? extends DistributeLock> lockClass) {
		for (DistributeLockFactory factory : factories) {
			if (factory.isSupport(lockClass)) {
				return factory.buildLock(getLockName(contextName, methodName, bizObj), lockClass);
			}
		}
		return null;
	}

	private String getLockName(String contextName, String methodName, Object bizObj) {
		return String.format(LOCK_NAME_PREFIX, contextName, methodName, bizObj);
	}
}
