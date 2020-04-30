package dawn.utils.random;

import com.alibaba.fastjson.JSON;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class RandomUtils {

	/**
	 * 数字, 字母大小写
	 */
	private static char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	/**
	 * 数字
	 */
	private static char[] CHARS_DIGIT = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	/**
	 * 小写字母
	 */
	private static char[] CHARS_LETTER_LOWER = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	/**
	 * 大写字母
	 */
	private static char[] CHARS_LETTER_UPPER = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	/**
	 * 特殊符号
	 */
	private static char[] CHARS_SPECIAL = {'_', '!', '@', '#', '$', '%', '^', '&', '*'};
	/**
	 * 组合
	 */
	private static char[][] CHARS_ARR = {CHARS_DIGIT, CHARS_LETTER_LOWER, CHARS_LETTER_UPPER, CHARS_SPECIAL};


	/**
	 * 生成长度是多少的字符串
	 *
	 * @param num
	 * @param isDigit 是否纯数字
	 * @return
	 */
	public static String random(int num, boolean isDigit) {
		StringBuilder sb = new StringBuilder();
		SecureRandom random = new SecureRandom();
		int index = CHARS.length;
		if (isDigit) {
			index = 9;
		}
		for (int i = 0; i < num; i++) {
			char ch = CHARS[random.nextInt(index)];
			sb.append(ch);
		}
		return sb.toString();
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	public static String random(int length) {
		StringBuilder sb = new StringBuilder();
		SecureRandom random = new SecureRandom();

		for (int i = 0; i < length; i++) {
			int charsIndex = random.nextInt(CHARS_ARR.length);
			sb.append(CHARS_ARR[charsIndex][random.nextInt(CHARS_ARR[charsIndex].length)]);
		}
		return sb.toString();
	}

	/**
	 * 生成随机字符串
	 *
	 * @param length  长度
	 * @param include 指定最少包含几种类型
	 * @return
	 */
	public static String random(int length, int include) {
		if (!(include <= CHARS_ARR.length && include > 0 && include <= length)) {
			throw new RuntimeException("参数异常");
		}
		StringBuilder sb = new StringBuilder();
		SecureRandom random = new SecureRandom();

		Set<Integer> included = new HashSet<>(include);
		for (; ; ) {
			// 长度满足, 退出
			if (sb.length() >= length) {
				return sb.toString();
			}

			// 将随机到的类型加入到set
			int charsIndex = random.nextInt(CHARS_ARR.length);
			included.add(charsIndex);

			// 如果剩余长度 > 缺少类型数量, 添加该类型的随机数, 否则继续随机
			if (length - sb.length() > include - included.size()) {
				sb.append(CHARS_ARR[charsIndex][random.nextInt(CHARS_ARR[charsIndex].length)]);
			}
		}


	}

}
