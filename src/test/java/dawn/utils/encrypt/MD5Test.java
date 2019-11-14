package dawn.utils.encrypt;

import dawn.utils.exception.AppRTException;
import org.junit.Test;

/**
 * @author HEBO
 * @created 2019-11-14 15:49
 */
public class MD5Test {
	
	@Test
	public void md5() throws AppRTException {
		String md5 = MD5Utils.md5("123", true);
		System.out.println(md5);
	}

}
