package dawn.utils.regex;

/**
 * 数字，编号类
 */
public class NumberUtils {
	
	/**
	 * 邮政编码
	 */
	private static String REG_ZIP_CODE = "^[1-9]{1}(\\d+){5}$";
	/**
	 * 身份证号码
	 */
	private static String REG_ID_CARD = "^\\d{18}|\\d{15}$";
	/**
	 * 整数
	 */
	private static String REG_INTEGER = "^\\d+$";
	/**
	 * 浮点数
	 */
	private static String REG_DOUBLE = "^(-?\\d*)\\.?\\d+$";
	/**
	 * 数字
	 */
	private static String REG_NUMBER = "^(-?\\d*)(\\.\\d+)$";
	
	/**
	 * 验证邮编
	 * @param zipCode
	 * @return
	 */
	public static boolean matchZipCode(String zipCode) {
		return RegexUtils.compilePattern(REG_ZIP_CODE, zipCode);
	}
	
	/**
	 * 验证身份证号码
	 * @param idCard
	 * @return
	 */
	public static boolean matchIdCard(String idCard) {
		return RegexUtils.compilePattern(REG_ID_CARD, idCard);
	}
	
	/**
	 * 验证整数
	 * @param integerNum
	 * @return
	 */
	public static boolean matchInteger(String integerNum) {
		return RegexUtils.compilePattern(REG_INTEGER, integerNum);
	}
	
	/**
	 * 验证浮点数
	 * @param doubleNum
	 * @return
	 */
	public static boolean matchDouble(String doubleNum) {
		return RegexUtils.compilePattern(REG_DOUBLE, doubleNum);
	}
	
	/**
	 * 验证数字
	 * @param number
	 * @return
	 */
	public static boolean matchNumber(String number) {
		return RegexUtils.compilePattern(REG_NUMBER, number);
	}

}
