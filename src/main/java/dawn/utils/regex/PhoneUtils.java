package dawn.utils.regex;


/**
 * 手机号码类
 */
public class PhoneUtils {

	/**
	 * 手机号码验证表达式
	 */
	private static String REGEX_PHONE = "^((\\+86)|(86))?1[3|5|7|8][0-9]{9}$";
	/**
	 * 电话号码验证表达式
	 */
	private static String REGEX_TELEPHONE = "^(\\d{3,4}-)?\\d{7,8}$";

	/**
	 * 验证手机号码
	 *
	 * @param phone
	 * @return
	 */
	public static boolean matchPhone(String phone) {
		return RegexUtils.compilePattern(REGEX_PHONE, phone);
	}

	/**
	 * 验证电话号码
	 *
	 * @param telephone
	 * @return
	 */
	public static boolean matchTelephone(String telephone) {
		return RegexUtils.compilePattern(REGEX_TELEPHONE, telephone);
	}

	public static void main(String[] args) {
		System.out.println(matchPhone("+8615026875534"));
		System.out.println(matchTelephone("0234-32312343"));
	}

}
