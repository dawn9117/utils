package dawn.utils.distributed.lock;

/**
 * 分布式锁实现工厂
 */
public interface DistributeLockFactory {

	/**
	 * 创建共享锁
	 *
	 * @param keyName 锁名称
	 * @return
	 */
	DistributeLock buildLock(String keyName);

	/**
	 * 创建指定类型的分布式锁
	 *
	 * @param keyName   锁名称
	 * @param lockClass 锁类型
	 * @return
	 */
	DistributeLock buildLock(String keyName, Class<? extends DistributeLock> lockClass);


	/**
	 * 判断是否能够创建该类型的锁
	 *
	 * @param lockClass
	 * @return
	 */
	boolean isSupport(Class<? extends DistributeLock> lockClass);
}