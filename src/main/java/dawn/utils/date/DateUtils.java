package dawn.utils.date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author HEBO
 * @created 2019-10-08 17:00
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	public static final String FORMAT_YMD = "yyyyMMdd";

	public static final String FORMAT_Y_M_D = "yyyy-MM-dd";

	public static final String FORMAT_Y_M_D_THMS = "yyyy-MM-dd'T'hh:mm:ss";

	public static final String FORMAT_Y_M_D_HMS = "yyyy-MM-dd hh:mm:ss";

	/**
	 * 太平洋时区
	 */
	public static final ZoneId pacificZone = ZoneId.of("America/Los_Angeles");

	/**
	 * 本地时区
	 */
	public static final ZoneId localZone = ZoneId.of("Asia/Shanghai");

	public static String localToPacific(String dateStr) {
		return ZonedDateTime.ofLocal(LocalDateTime.parse(dateStr), pacificZone, null).format(DateTimeFormatter.ISO_DATE_TIME);
	}

	public static String pacificToLocal(String dateStr) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(FORMAT_Y_M_D_THMS);
			format.setTimeZone(TimeZone.getTimeZone(pacificZone));
			Date parse = format.parse(dateStr);
			LocalDateTime dateTime = LocalDateTime.ofInstant(parse.toInstant(), localZone);
			String res = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			return res.replaceAll("T", " ");
		} catch (ParseException e) {
			log.error("[DateUtils] pacificToLocal error", e);
		}
		return null;
	}

	public static Date pacificToLocalDate(String dateStr) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(FORMAT_Y_M_D_THMS);
			format.setTimeZone(TimeZone.getTimeZone(pacificZone));
			return format.parse(dateStr);
		} catch (ParseException e) {
			log.error("[DateUtils] pacificToLocal error", e);
		}
		return null;
	}

	public static String pacificDateNow() {
		return LocalDateTime.now(pacificZone).format(DateTimeFormatter.ISO_DATE);
	}

	public static String plus(String dateStr, String pattern, int d) {
		Date date = parse(dateStr, pattern);
		date = addDays(date, d);
		return DateFormatUtils.format(date, pattern);
	}

	public static String localDateNow() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
	}

	public static String reformat(String dateStr, String pattern, String reformat) {
		Date date = parse(dateStr, pattern);
		return DateFormatUtils.format(date, reformat);
	}

	private static Date parse(String dateStr, String pattern) {
		try {
			return parseDate(dateStr, pattern);
		} catch (ParseException e) {
			log.error("[DateUtils] reformat error", e);
		}
		return null;
	}

}
