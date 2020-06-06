package dawn.utils.distributed.lock.zk;

import com.alibaba.fastjson.JSON;
import dawn.utils.config.ZkConfig;
import dawn.utils.distributed.lock.DistributeLock;
import dawn.utils.distributed.lock.DistributeLockFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * zk 实现分布式锁
 */
@Slf4j
//@Component
public class ZkDistributeLockFactory implements DistributeLockFactory {

	/**
	 * ZK配置类
	 */
	@Autowired
	private ZkConfig zkConfig;

	/**
	 * 状态监听器
	 */
	@Autowired(required = false)
	private List<ConnectionStateListener> listeners;

	/**
	 * zk 客户端
	 **/
	private CuratorFramework client;

	@PostConstruct
	public void init() {
		log.info("[ZkDistributeLockFactoryImpl] init zkConfig:{}", JSON.toJSONString(zkConfig));
		if (StringUtils.isEmpty(zkConfig.getRegistryAddress())) {
			throw new RuntimeException("[ZkDistributeLockFactoryImpl] 服务器地址不能为空");
		}
		client = CuratorFrameworkFactory.builder().connectString(zkConfig.getRegistryAddress())
				.connectionTimeoutMs(zkConfig.getConnectTimeout())
				.sessionTimeoutMs(zkConfig.getSessionTimeout())
				.namespace(zkConfig.getLockNamespace())
				.retryPolicy(new RetryNTimes(10, 1000)).build();

		// 注册状态监听器, 使用者仅需要实现ConnectionStateListener接口即可被注册
		if (CollectionUtils.isNotEmpty(listeners)) {
			listeners.forEach(listener -> client.getConnectionStateListenable().addListener(listener));
		}
		client.start();
		log.info("[ZkDistributeLockFactoryImpl] 初始化zk客户端成功");
	}

	@Override
	public DistributeLock buildLock(String keyName) {
		return new ZkSemaphoreMutexLock(client, keyName);
	}

	@Override
	public DistributeLock buildLock(String keyName, Class<? extends DistributeLock> lockClass) {
		try {
			Constructor<? extends DistributeLock> constructor = lockClass.getConstructor(CuratorFramework.class, String.class);
			constructor.setAccessible(true);
			return constructor.newInstance(client, keyName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isSupport(Class<? extends DistributeLock> lockClass) {
		return ZkDistributeLock.class.isAssignableFrom(lockClass);
	}

	@PreDestroy
	public void destroy() {
		if (client != null && client.getState() == CuratorFrameworkState.STARTED) {
			client.close();
		}
		log.info("[ZkDistributeLockFactoryImpl] 销毁zk客户端成功");
	}


}