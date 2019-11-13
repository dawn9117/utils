package dawn.utils.regex;

import org.apache.commons.lang3.StringUtils;

/**
 * 邮箱工具类
 */
public class EmailUtils {

	/**
	 * 邮箱匹配规则
	 */
	private static final String REGEX_EMAIL = "^\\S+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";


	/**
	 * 判断是否为邮箱
	 *
	 * @param email
	 * @return true 表示为邮箱号码     false 表示不为邮箱号
	 */
	public static boolean isEmail(String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		}
		return RegexUtils.compilePattern(REGEX_EMAIL, email);
	}
}
