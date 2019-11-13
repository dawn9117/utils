package dawn.utils.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * zk 配置类
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "zk")
public class ZkConfig {

	/**
	 * zk注册中心地址
	 **/
	@Value("${zk.registry.address}")
	private String registryAddress;

	/**
	 * zk分布式锁的命名空间
	 **/
	@Value("${zk.namespace.lock}")
	private String lockNamespace;

	/**
	 * zk分布式ID机器编号
	 **/
	@Value("${zk.namespace.machine}")
	private String machineNamespace;

	/**
	 * zk连接超时时间
	 **/
	@Value("${zk.connect.timeout}")
	private int connectTimeout;

	/**
	 * zk session 超时时间
	 **/
	@Value("${zk.session.timeout}")
	private int sessionTimeout;


}
