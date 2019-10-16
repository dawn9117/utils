package dawn.utils;

import dawn.utils.exception.AppRTException;
import dawn.utils.http.HttpClientResult;
import dawn.utils.http.HttpClientUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilsApplicationTests {

	@Test
	public void contextLoads() throws AppRTException {
		HttpClientResult result = HttpClientUtils.get("http://www.baidu.com");
		System.out.println(result.getContent());

	}

}
