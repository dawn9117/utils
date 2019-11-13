package dawn.utils.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * sha数字签名算法
 */
public class SHAUtils {

	// 摘要长度64
	public static final String ENCRYPT_256 = "SHA-256";
	// 摘要长度96
	public static final String ENCRYPT_384 = "SHA-384";
	// 摘要长度128
	public static final String ENCRYPT_512 = "SHA-512";

	/**
	 * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
	 *
	 * @param strSrc  要加密的字符串
	 * @param encName 加密类型
	 * @return
	 */
	public static String encrypt(String strSrc, String encName) {
		if (strSrc == null) {
			return null;
		}
		try {
			if (encName == null) {
				encName = ENCRYPT_256;
			}
			MessageDigest md = MessageDigest.getInstance(encName);
			md.update(strSrc.getBytes());
			String strDes = bytes2Hex(md.digest()); // to HexString
			return strDes;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String bytes2Hex(byte[] bts) {
		StringBuilder des = new StringBuilder();
		String tmp;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}

	public static void main(String args[]) {
		String str = ""; // 872189
		String s256 = SHAUtils.encrypt(str, ENCRYPT_256);
		System.out.println(s256);
		System.out.println(s256.length());
		String s384 = SHAUtils.encrypt(str, ENCRYPT_384);
		System.out.println(s384);
		System.out.println(s384.length());
		String s512 = SHAUtils.encrypt(str, ENCRYPT_512);
		System.out.println(s512);
		System.out.println(s512.length());
//		这是sha512加密+base64编码
	}

}
