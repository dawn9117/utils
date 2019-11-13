package dawn.utils.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public class RegexUtils {

	protected static boolean compilePattern(String reg, String content) {
		if (reg == null || content == null) {
			return false;
		}
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(content);
		return matcher.matches();
	}

}
