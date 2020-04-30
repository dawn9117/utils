package dawn.utils.http;

/**
 * @author HEBO
 * @created 2019-10-17 9:47
 */
public class HttpClientTest {

	public static void main(String[] args) throws Exception {
		HttpClientResult result = HttpClientUtils.get("http://baidu.com");
		System.out.println(result);
	}


}
