package dawn.utils.distributed.lock.redis;

import dawn.utils.distributed.lock.DistributeLock;
import dawn.utils.redis.RedisHelper;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 *
 * @author HEBO
 * @created 2019-11-21 14:31
 */
public class RedisDistributeLock implements DistributeLock {

	private String key;

	private Long expire;

	private String value;

	private RedisHelper redisHelper;

	@Override
	public void lock() {
	}

	@Override
	public boolean tryLock() {
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) {
		return false;
	}

	@Override
	public void unlock() {

	}
}
