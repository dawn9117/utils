package dawn.utils.distribute;

import dawn.utils.BaseTest;
import dawn.utils.distributed.helper.IdHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

/**
 * 分布式ID生成
 *
 * @author HEBO
 * @created 2019-11-13 18:02
 */
public class IdTest extends BaseTest {

	@Autowired
	private IdHelper idHelper;

	@Test
	public void getId() {
		long start = System.currentTimeMillis();
		Map<Long, Object> ids = new ConcurrentHashMap<>();
		IntStream.range(1, 5).parallel().forEach(thread -> {
			System.out.println(String.format("线程:%s开始执行", thread));
			for (int i = 0; i < 300000; i++) {
				long id = idHelper.nextId();
				if (ids.containsKey(id)) {
					System.out.println(String.format("生成ID重复,ID:%s", id));
					continue;
				}
				ids.put(id, id);
			}
		});
		System.out.println(String.format("生成ID数量:%s, 耗时: %s", ids.size(), System.currentTimeMillis() - start));
	}


}
