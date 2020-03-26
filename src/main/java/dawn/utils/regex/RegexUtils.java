package dawn.utils.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public class RegexUtils {

	public static boolean compilePattern(String reg, String content) {
		if (reg == null || content == null) {
			return false;
		}
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(content);
		return matcher.matches();
	}

	public static void main(String[] args) {
	 	String name1 = "asdas 开源中国";
	 	String name2 = "阿萨德撒打算多撒旦撒娇大敖德萨多撒旦按时";
	 	String name3 = "1)_@@!()@!MMM";
	 	String name4 = "_1";

	 	String reg = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D_a-zA-Z0-9_]{2,16}";

		System.out.println(compilePattern(reg, name1));
		System.out.println(compilePattern(reg, name2));
		System.out.println(compilePattern(reg, name3));
		System.out.println(compilePattern(reg, name4));
	}

}
