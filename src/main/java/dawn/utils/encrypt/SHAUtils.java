package dawn.utils.encrypt;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * sha数字签名算法
 */
@Slf4j
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
	 * @param strSrc 要加密的字符串
	 * @return
	 */
	public static String encrypt(String strSrc) {
		return encrypt(strSrc, ENCRYPT_256);
	}

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
			encName = Objects.isNull(encName) ? ENCRYPT_256 : encName;
			MessageDigest md = MessageDigest.getInstance(encName);
			md.update(strSrc.getBytes());
			return bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			log.error("[SHAUtils] encrypt error.", e);
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
}
