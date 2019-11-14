package dawn.utils.date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static final String FORMAT_YMD = "yyyyMMdd";

	public static final String FORMAT_Y_M_D = "yyyy-MM-dd";

	public static final String FORMAT_Y_M_D_THMS = "yyyy-MM-dd'T'HH:mm:ss";

	public static final String FORMAT_Y_M_D_HMS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 通用日期格式
	 */
	public static final String[] PARSE_PATTERNS = {FORMAT_YMD, FORMAT_Y_M_D, FORMAT_Y_M_D_HMS};

	/**
	 * 本地时区
	 */
	public static final ZoneId localZone = ZoneId.of("Asia/Shanghai");

	/**
	 * 本地日期转换成到指定时区
	 *
	 * @param dateStr 本地时间字符串 (yyyyMMdd HH:mm:ss)
	 * @param zoneId  指定时区的ZoneId
	 * @return 转换后的指定时区的日期字符串 (yyyyMMdd HH:mm:ss)
	 */
	public static String localToTarget(String dateStr, ZoneId zoneId) {
		return ZonedDateTime.ofLocal(LocalDateTime.parse(dateStr), zoneId, null).format(DateTimeFormatter.ISO_DATE_TIME);
	}

	/**
	 * 异地时区时间转化到本地时间
	 *
	 * @param dateStr 异地时间字符串
	 * @param zoneId  异地时区ZoneId
	 * @return 本地时区的日期字符串 (yyyyMMdd HH:mm:ss)
	 */
	public static String toLocal(String dateStr, ZoneId zoneId) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(FORMAT_Y_M_D_THMS);
			format.setTimeZone(TimeZone.getTimeZone(zoneId));
			Date parse = format.parse(dateStr);
			LocalDateTime dateTime = LocalDateTime.ofInstant(parse.toInstant(), localZone);
			String res = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			return res.replaceAll("T", " ");
		} catch (ParseException e) {
			log.error("[DateUtils] pacificToLocal error", e);
		}
		return null;
	}

	/**
	 * 获取本地当前日期(yyyy-MM-dd)
	 *
	 * @return 当前日期
	 */
	public static String now() {
		return now(localZone);
	}

	/**
	 * 获取当地的当前日期或时间
	 *
	 * @param pattern 格式化
	 * @return 指定时区的日期
	 */
	public static String now(String pattern) {
		return format(new Date(), pattern);
	}

	/**
	 * 获取指定时区当前的日期 (yyyy-MM-dd)
	 *
	 * @param zoneId 指定时区
	 * @return 指定时区的日期
	 */
	public static String now(ZoneId zoneId) {
		return LocalDateTime.now(zoneId).format(DateTimeFormatter.ISO_DATE);
	}

	/**
	 * 获取指定时区当前的日期或时间
	 *
	 * @param zoneId    指定时区
	 * @param formatter 格式化类型
	 * @return 指定时区的日期
	 */
	public static String now(ZoneId zoneId, DateTimeFormatter formatter) {
		return LocalDateTime.now(zoneId).format(formatter);
	}

	/**
	 * 在给定的时间加减天数
	 *
	 * @param dateStr 日期
	 * @param pattern 日期格式
	 * @param d       天数
	 * @return 加减后的日期
	 */
	public static String plus(String dateStr, String pattern, int d) {
		Date date = parse(dateStr, pattern);
		date = addDays(date, d);
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 解析日期
	 *
	 * @param dateStr 日期字符串
	 * @param pattern 日期格式
	 * @return 日期
	 */
	public static Date parse(String dateStr, String... pattern) {
		try {
			return ArrayUtils.isEmpty(pattern) ? parseDate(dateStr, PARSE_PATTERNS) : parseDate(dateStr, pattern);
		} catch (ParseException e) {
			log.error("[DateUtils] reformat error", e);
		}
		return null;
	}

	/**
	 * 异地时间转换为本地date(yyyy-MM-dd'T'HH:mm:ss)
	 *
	 * @param dateStr 异地时间字符串
	 * @param zoneId  异地的时区
	 * @return 本地时间
	 */
	public static Date parse(String dateStr, ZoneId zoneId, String... patterns) {
		if (ArrayUtils.isEmpty(patterns)) {
			patterns = PARSE_PATTERNS;
		}
		for (String pattern : patterns) {
			try {
				FastDateFormat dateFormat = FastDateFormat.getInstance(pattern, TimeZone.getTimeZone(zoneId));
				return dateFormat.parse(dateStr);
			} catch (ParseException e) {
			}
		}
		log.error("[DateUtils] parse error");
		return null;
	}

	/**
	 * 时间戳转成日期(可以是秒, 也可以是毫秒)
	 *
	 * @param timestamp 时间戳字符串
	 * @return
	 */
	public static Date toDate(String timestamp) {
		if (!StringUtils.isNumeric(timestamp)) {
			return null;
		}
		Date date = new Date();
		if (timestamp.length() <= 10) {
			date.setTime(Long.valueOf(timestamp) * 1000);
		}
		if (timestamp.length() >= 13) {
			date.setTime(Long.valueOf(timestamp));
		}
		return date;
	}

	/**
	 * 日期转时间戳(毫秒)
	 *
	 * @param date 日期字符串
	 * @return
	 */
	public static String toTimestamp(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		Date parsed = parse(date);
		return parsed != null ? String.valueOf(parsed.getTime()) : null;
	}

	/**
	 * 格式化日期
	 *
	 * @param date    日期
	 * @param pattern 格式
	 * @return 日期字符串
	 */
	public static String format(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 重新格式化时间字符串
	 *
	 * @param dateStr  日期字符串
	 * @param pattern  原始格式
	 * @param reformat 目标格式
	 * @return
	 */
	public static String reformat(String dateStr, String pattern, String reformat) {
		Date date = parse(dateStr, pattern);
		return DateFormatUtils.format(date, reformat);
	}

}
