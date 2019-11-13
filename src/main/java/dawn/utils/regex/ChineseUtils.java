package dawn.utils.regex;

/**
 * 中文匹配
 */
public class ChineseUtils {

	/**
	 * 中文字符串
	 */
	private static String REG_CHINESE = "^[\\u4e00-\\u9fa5]*$";
	/**
	 * 双字节字符
	 */
	private static String REG_TWO_BYTE = "[^\\x00-\\xff]*";

	/**
	 * 验证中文（汉字）
	 *
	 * @param chinese
	 * @return
	 */
	public static boolean matchChinese(String chinese) {
		return RegexUtils.compilePattern(REG_CHINESE, chinese);
	}

	/**
	 * 验证双字节字符（汉字+中文标点符号等）
	 *
	 * @param twoByte
	 * @return
	 */
	public static boolean matchTwoByte(String twoByte) {
		return RegexUtils.compilePattern(REG_TWO_BYTE, twoByte);
	}

	public static void main(String[] args) {
		System.out.println(matchChinese("中国"));
		System.out.println(matchTwoByte("【"));

	}

}
