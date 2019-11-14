package dawn.utils.encrypt;

import org.junit.Test;

/**
 * @author HEBO
 * @created 2019-11-14 16:17
 */
public class SHATest {


	@Test
	public void sha() {
		String str = "踩姑娘的小蘑菇"; // 872189
		String def = SHAUtils.encrypt(str);
		System.out.println(def);
		System.out.println(def.length());
		String s256 = SHAUtils.encrypt(str, SHAUtils.ENCRYPT_256);
		System.out.println(s256);
		System.out.println(s256.length());
		String s384 = SHAUtils.encrypt(str, SHAUtils.ENCRYPT_384);
		System.out.println(s384);
		System.out.println(s384.length());
		String s512 = SHAUtils.encrypt(str, SHAUtils.ENCRYPT_512);
		System.out.println(s512);
		System.out.println(s512.length());
//		这是sha512加密+base64编码
	}
}
