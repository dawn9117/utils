package dawn.utils.http;

import dawn.utils.exception.AppRTException;

/**
 * @author HEBO
 * @created 2019-10-17 9:47
 */
public class HttpClientTest {

	public static void main(String[] args) throws AppRTException {
		HttpClientResult result = HttpClientUtils.get("http://baidu.com");
		System.out.println(result);
	}


}
