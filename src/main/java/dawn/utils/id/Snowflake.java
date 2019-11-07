package dawn.utils.id;

import java.util.HashSet;
import java.util.Set;

/**
 * Twitter的雪花算法SnowFlake，使用Java语言实现。
 * <p>
 * SnowFlake算法产生的ID是一个64位的整型，结构如下（每一部分用“-”符号分隔）：
 * <p>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * 1
 * 1位标识部分，在java中由于long的最高位是符号位，正数是0，负数是1，一般生成的ID为正数，所以为0；
 * <p>
 * 41位时间戳部分，这个是毫秒级的时间，一般实现上不会存储当前的时间戳，而是时间戳的差值（当前时间-固定的开始时间），这样可以使产生的ID从更小值开始；41位的时间戳可以使用69年，(1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69年；
 * <p>
 * 10位节点部分，Twitter实现中使用前5位作为数据中心标识，后5位作为机器标识，可以部署1024个节点；
 * <p>
 * 12位序列号部分，支持同一毫秒内同一个节点可以生成4096个ID；
 * <p>
 * SnowFlake算法生成的ID大致上是按照时间递增的，用在分布式系统中时，需要注意数据中心标识和机器标识必须唯一，这样就能保证每个节点生成的ID都是唯一的。或许我们不一定都需要像上面那样使用5位作为数据中心标识，5位作为机器标识，可以根据我们业务的需要，灵活分配节点部分，如：若不需要数据中心，完全可以使用全部10位作为机器标识；若数据中心不多，也可以只使用3位作为数据中心，7位作为机器标识。
 * <p>
 * snowflake生成的ID整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和workerId作区分），并且效率较高。据说：snowflake每秒能够产生26万个ID。
 */
public class Snowflake {

	/**
	 * 起始的时间戳
	 */
	private final static long START_STAMP = 1480166465631L;

	/**
	 * 机器标识5位和最大值31
	 */
	private final static long MACHINE_BIT = 5;   //机器标识占用的位数
	private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
	/**
	 * 数据中心5位和最大值31
	 */
	private final static long DATA_CENTER_BIT = 5;//数据中心占用的位数
	private final static long MAX_DATA_CENTER_NUM = ~(-1L << DATA_CENTER_BIT);

	/**
	 * 序列12位和最大值4095
	 */
	private final static long SEQUENCE_BIT = 12; //序列号占用的位数
	private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

	/**
	 * 每一部分向左的位移
	 */
	private final static long MACHINE_LEFT = SEQUENCE_BIT;
	private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
	private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

	private long dataCenterId;  //数据中心
	private long machineId;     //机器标识
	private long sequence = 0L; //序列号
	private long lastStamp = -1L;//上一次时间戳

	public Snowflake(long dataCenterId, long machineId) {
		if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
			throw new IllegalArgumentException("dataCenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0");
		}
		if (machineId > MAX_MACHINE_NUM || machineId < 0) {
			throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
		}
		this.dataCenterId = dataCenterId;
		this.machineId = machineId;
	}

	/**
	 * 产生下一个ID
	 *
	 * @return
	 */
	public synchronized long nextId() {
		long currStamp = getNewStamp();
		if (currStamp < lastStamp) {
			throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
		}

		if (currStamp == lastStamp) {
			//相同毫秒内，序列号自增
			sequence = (sequence + 1) & MAX_SEQUENCE;
			//同一毫秒的序列数已经达到最大
			if (sequence == 0L) {
				currStamp = getNextMill();
			}
		} else {
			//不同毫秒内，序列号置为0
			sequence = 0L;
		}

		lastStamp = currStamp;

		return (currStamp - START_STAMP) << TIMESTAMP_LEFT //时间戳部分
				| dataCenterId << DATA_CENTER_LEFT       //数据中心部分
				| machineId << MACHINE_LEFT             //机器标识部分
				| sequence;                             //序列号部分
	}

	private long getNextMill() {
		long mill = getNewStamp();
		while (mill <= lastStamp) {
			mill = getNewStamp();
		}
		return mill;
	}

	private long getNewStamp() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Snowflake snowflake = new Snowflake(1, 1);
		Set<Long> set = new HashSet<>();
		for(int i =0; i< 300000 ; i++) {
			set.add(snowflake.nextId());
		}
		System.out.println("生成ID数量: " + set.size() + ",耗时: "+ (System.currentTimeMillis() - start) + " 毫秒");
	}

}
