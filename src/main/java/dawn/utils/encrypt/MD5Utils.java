package dawn.utils.encrypt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import dawn.utils.exception.AppRTException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
	 * MD5加密字符串（32位大写）
	 *
	 * @param string 需要进行MD5加密的字符串
	 * @return 加密后的字符串（大写）
	 */
	public static String md5(String string, boolean upper) throws AppRTException {
		String md5;
		try {
			md5 = encrypt(string);
		} catch (Exception e) {
			log.error("[MD5Utils] md5 error", e);
			throw new AppRTException("[MD5Utils] md5 error");
		}
		return upper ? md5.toUpperCase() : md5.toLowerCase();
	}

	/**
	 * MD5加密字符串（32位大写）
	 */
	public static String md5(Map<String, String> params, String key) throws AppRTException {
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
		return MD5Utils.md5(sb.toString());
	}

	/**
	 * MD5加密字符串（32位小写）
	 *
	 * @param string 需要进行MD5加密的字符串
	 * @return 加密后的字符串（小写）
	 */
	public static String md5(String string) throws AppRTException {
		return md5(string, true);
	}

	/**
	 * MD5加密并返回加密的摘要
	 *
	 * @param key
	 * @param obj
	 * @return
	 * @throws AppRTException
	 */
	public static Map<String, String> md5(String key, Object obj) throws AppRTException {
		String json = JSON.toJSONString(obj);
		log.info("[MD5Utils] md5," + obj.getClass().getName() + ":{}", json);
		Map<String, String> params = JSON.parseObject(json, new TypeReference<TreeMap<String, String>>() {
		});
		params.put("sign", MD5Utils.md5(params, key));
		return params;
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

}
