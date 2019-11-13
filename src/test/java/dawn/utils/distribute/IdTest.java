package dawn.utils.distribute;

import dawn.utils.BaseTest;
import dawn.utils.distributed.helper.IdHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author HEBO
 * @created 2019-11-13 18:02
 */
public class IdTest extends BaseTest {

	@Autowired
	private IdHelper idHelper;

	@Test
	public void getId() {
		long start = System.currentTimeMillis();
		Set<Long> ids = new HashSet<>();
		for (int i = 0; i < 300000; i++) {
			ids.add(idHelper.nextId());
		}
		System.out.println(String.format("生成ID数量:%s, 耗时: %s", ids.size(), System.currentTimeMillis() - start));
	}


}
