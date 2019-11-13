package dawn.utils.distributed.id;

import dawn.utils.config.ZkConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 雪花算法分布式ID的辅助组件(同一台物理机占用一个workId):
 * <p>
 * /machine/sequence-node/sequence00000XXX: 顺序永久节点的编号代表该物理机占用的workID
 * /machine/server/mac地址: 为物理机创建的永久节点
 * </p>
 */
@Slf4j
@Component
public class WorkIdHandler {

	@Autowired
	private ZkConfig zkConfig;

	/**
	 * 当前机器node序号
	 */
	private Integer currentNode;

	/**
	 * 序列节点path
	 */
	private static final String SEQUENCE_PATH = "/machine/sequence-node";
	/**
	 * 服务器节点path
	 */
	private static final String SERVER_PATH = "/machine/server";


	/**
	 * 获取机器workId
	 *
	 * @return
	 */
	public Integer getWorkerId() {
		return this.currentNode;
	}


	@PostConstruct
	public void init() {
		// 获取连接
		try (CuratorFramework conn = getConnection()) {
			// 获取mac地址
			String macAddr = getHardwareAddress();
			// 获取当前mac地址机器下的workId
			if (conn.checkExists().forPath(SERVER_PATH + "/" + macAddr) != null) {
				currentNode = getNum(new String(conn.getData().forPath(SERVER_PATH + "/" + macAddr)));
				return;
			}
			// 当前mac地址机器下的workId不存在则创建
			// 以zk序列节点后两位作为workId
			String sequence = conn.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
					.forPath(SEQUENCE_PATH + "/" + "sequence");
			String seqStr = sequence.substring(sequence.length() - 2);

			conn.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
					.forPath(SERVER_PATH + "/" + macAddr, seqStr.getBytes());
			currentNode = getNum(seqStr);
		} catch (Exception e) {
			log.error("[MachineIdHelper] 创建节点失败", e);
		}
	}

	private Integer getNum(String str) {
		return Integer.parseInt(str);
	}

	/**
	 * 返回的是多例的curator连接
	 *
	 * @return
	 */
	private CuratorFramework getConnection() {
		//1 重试策略：初试时间为1s 重试3次
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		//2 通过工厂创建连接
		CuratorFramework cf = CuratorFrameworkFactory.builder()
				.connectString(zkConfig.getRegistryAddress())
				.sessionTimeoutMs(zkConfig.getSessionTimeout())
				.retryPolicy(retryPolicy)
				.namespace(zkConfig.getMachineNamespace())
				.build();
		//3 开启连接
		cf.start();
		return cf;
	}


	/**
	 * 获取mac地址
	 *
	 * @return
	 */
	private static String getHardwareAddress() {
		byte[] mac = new byte[0];
		try {
			InetAddress ia = InetAddress.getLocalHost();
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (SocketException | UnknownHostException e) {
			log.error("[MachineIdHelper]  获取MAC地址出错：", e);
		}
		if (mac == null) {
			return null;
		}
		// 把mac地址拼装成String
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			//mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		String result = sb.toString();
		if (StringUtils.isBlank(result)) {
			throw new RuntimeException("[MachineIdHelper] 获取mac地址失败");
		}
		return result;
	}
}
