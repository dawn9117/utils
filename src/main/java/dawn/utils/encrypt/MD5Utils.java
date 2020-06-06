package dawn.utils.encrypt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import dawn.utils.exception.AppRTException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
public class MD5Utils {

	private static final String ALGORITHM = "MD5";

	private static final String CHARSET = "UTF-8";

	/**
	 * MD5加密字符串
	 *
	 * @param data 需要进行MD5加密的字符串
	 * @return 加密后的字符串
	 */
	public static String md5(String data, boolean upper) throws AppRTException {
		try {
			String md5 = encrypt(data);
			return upper ? md5.toUpperCase() : md5.toLowerCase();
		} catch (Exception e) {
			log.error("[MD5Utils] md5 error", e);
			throw new AppRTException("[MD5Utils] md5 error");
		}
	}

	/**
	 * MD5加密字符串（32位小写）
	 *
	 * @param data 需要进行MD5加密的字符串
	 * @return 加密后的字符串（小写）
	 */
	public static String md5(String data) throws AppRTException {
		return md5(data, true);
	}

	/**
	 * MD5加密(加密内容包含密钥): 用于第三方接口要求MD5加密请求参数的场景
	 * 对数据按照ASCII排序,拼接后用密钥加密
	 *
	 * @param key 密钥
	 * @param obj 加密的数据
	 * @return 包含原始请求参数和签名结果的Map
	 * @throws AppRTException
	 */
	public static Map<String, String> md5(String key, Object obj) throws AppRTException {
		String json = JSON.toJSONString(obj);
		Map<String, String> params = JSON.parseObject(json, new TypeReference<TreeMap<String, String>>() {
		});
		return MD5Utils.md5(params, key);
	}

	/**
	 * MD5加密(加密内容包含密钥): 用于第三方接口要求MD5加密请求参数的场景
	 * 对数据按照ASCII排序,拼接后用密钥加密
	 *
	 * @param params 请求参数
	 * @param key    密钥
	 * @return 包含原始请求参数和签名结果的Map
	 * @throws AppRTException
	 */
	public static Map<String, String> md5(Map<String, String> params, String key) throws AppRTException {
		if (!(params instanceof SortedMap)) {
			params = new TreeMap<>(params);
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if ("sign".equalsIgnoreCase(entry.getKey())) continue;
			if (StringUtils.isNotEmpty(entry.getValue())) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		sb.append("key=").append(key);
		String md5 = MD5Utils.md5(sb.toString());
		params.put("sign", md5);
		return MapUtils.invertMap(params);
	}

	private static String encrypt(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] hash = MessageDigest.getInstance(ALGORITHM).digest(string.getBytes(CHARSET));
		//转换为十六进制字符串
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}


	public static void main(String[] args) throws UnsupportedEncodingException {
//		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
//		textEncryptor.setPassword("pigx");
//		System.out.println(textEncryptor.decrypt("ltJPpR50wT0oIY9kfOe1Iw=="));

		//加密所需的salt(盐)

		//要加密的数据（数据库的用户名或密码）
//		String username = textEncryptor.encrypt("root");
//		String password = textEncryptor.encrypt("root123");
//		System.out.println("username:"+username);

		System.out.println(new String("理想").getBytes("UTF-8").length);
		System.out.println(new String("理想").getBytes("GBK").length);
//		System.out.println("password:"+password);
	}

}
