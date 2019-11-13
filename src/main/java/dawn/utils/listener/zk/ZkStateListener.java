package dawn.utils.listener.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.springframework.stereotype.Component;

/**
 * @author HEBO
 * @created 2019-11-13 16:04
 */
@Component
public class ZkStateListener implements ConnectionStateListener {
	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		System.out.println(newState.name());
	}
}
