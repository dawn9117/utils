package dawn.utils.distributed.helper;

import dawn.utils.distributed.id.Snowflake;
import dawn.utils.distributed.id.WorkIdHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 全局分布式ID组件
 * <p>
 * 该版本workId占用5位, 数据仓库占用5位
 * 最大支持32台物理机使用, 需要扩大的话需要增加workId占用的长度, 最大1024
 * </>
 */
//@Component
@Slf4j
public class IdHelper {

	private Snowflake snowflake;

	@Autowired
	private WorkIdHandler machineId;

	@PostConstruct
	private void init() {
		this.snowflake = new Snowflake(machineId.getWorkerId(), 0L);
	}

	/**
	 * 下一个long类型的id
	 *
	 * @return 32位的id
	 */
	public long nextId() {
		return snowflake.nextId();
	}

	/**
	 * 下一个String类型的id
	 *
	 * @return String类型的id
	 */
	public String nextCode() {
		return nextCode(null);
	}

	/**
	 * 下一个String类型的id，并将prefix设置为前缀
	 *
	 * @param prefix 前缀
	 * @return 以prefix为前缀的下一个String类型的id
	 */
	public String nextCode(String prefix) {
		return snowflake.nextCode(prefix);
	}

}
