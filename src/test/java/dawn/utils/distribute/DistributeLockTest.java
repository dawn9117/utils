package dawn.utils.distribute;

import dawn.utils.BaseTest;
import dawn.utils.distributed.lock.DistributeLock;
import dawn.utils.distributed.helper.LockHelper;
import dawn.utils.distributed.lock.zk.ZkDistributeLockFactory;
import dawn.utils.distributed.lock.zk.ZkSemaphoreMutexLock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author HEBO
 * @created 2019-11-13 11:31
 */
public class DistributeLockTest extends BaseTest {

	@Autowired
	private LockHelper lockHelper;


	@Test
	public void lock(){
		DistributeLock lock = lockHelper.build("1", "2", "3", ZkSemaphoreMutexLock.class);
		lock.lock();
		lock.unlock();
	}


}
