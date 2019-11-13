package dawn.utils.distributed.lock;

import java.util.concurrent.TimeUnit;

/**
 * ZK分布式锁接口
 * <p>
 * Version		1.0.0
 * 共享锁<br>读写,锁池
 * 互斥锁<br>同一时刻只能有一个客户端拿到锁
 */
public interface DistributeLock {

	/**
	 * 获取锁<br>
	 * 若获不到锁,当前线程阻塞,直到获取锁为止<br>
	 * <pre>
	 * 实例如下：
	 * try{
	 *    lock.lock();
	 *    //执行业务代码
	 * }catch(){
	 *    //异常处理
	 * }finally{
	 *    lock.unlock();
	 * }
	 *
	 * </pre>
	 */
	void lock();

	/**
	 * 尝试获取锁<br>
	 * 若获取到锁,返回true,否则返回false<br>
	 * <pre>
	 * 实例如下：
	 * try{
	 *    if(lock.tryLock()){
	 *        //执行成功获取锁业务代码
	 *    }else{
	 *        //执行获取锁失败业务代码
	 *    }
	 * }catch(){
	 *    //异常处理
	 * }finally{
	 *    lock.unlock();
	 * }
	 *
	 * </pre>
	 */
	boolean tryLock();


	/**
	 * 在限定时间内获取锁<br>
	 * 若获取到锁,返回true,否则返回false<br>
	 * <pre>
	 * 实例如下：
	 * try{
	 *    if(lock.tryLock(1000,TimeUnit.MILLISECONDS)){
	 *        //执行成功获取锁业务代码
	 *    }else{
	 *        //执行获取锁失败业务代码
	 *    }
	 * }catch(){
	 *    //异常处理
	 * }finally{
	 *    lock.unlock();
	 * }
	 *
	 * </pre>
	 *
	 * @param time 等待获取锁最大的时间
	 * @param unit 等待获取锁最大的时间单位
	 * @return
	 */
	public boolean tryLock(long time, TimeUnit unit);

	/**
	 * 释放锁,与lock(),tryLock()成对使用
	 */
	public void unlock();

}
