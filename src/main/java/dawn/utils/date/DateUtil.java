package dawn.utils.date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;

/**
 * 日期格式化工具
 */
public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static final FastDateFormat COMPACT_ACCOUNT_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");

    private static final FastDateFormat DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    /**
     * 年月
     */
    private static final FastDateFormat YEAR_MONTH_FORMAT = FastDateFormat.getInstance("yyyy年MM月");

    public static String format(Date date) {
        return date == null ? DATETIME_FORMAT.format(new Date()) : DATETIME_FORMAT.format(date);
    }

    /**
     * 根据pattern转换日期（不处理pattern转换异常）
     *
     * @param pattern
     * @param date
     * @return
     */
    public static String format(String pattern, Date date) {
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        return instance.format(date);
    }

    /**
     * 获取Date的日期部分
     *
     * @param date
     * @return
     */
    public static Date formatDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static String compactFormat(Date date) {
        return date == null ? null : COMPACT_ACCOUNT_FORMAT.format(date);
    }

    /**
     * 判断一个字符串是否可以解析为yyyy-MM-dd HH:mm:ss格式日期
     *
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
        return parse(date) != null;
    }

    /**
     * 获取格式化实例
     *
     * @param pattern
     * @return
     */
    public static FastDateFormat getDateFormat(String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            return DATETIME_FORMAT;
        }

        return FastDateFormat.getInstance(pattern);
    }

    /**
     * 根据默认的日期格式进行格式化，默认的日期格式为 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static Date parse(String date) {
        try {
            return StringUtils.isEmpty(date) ? null : DATETIME_FORMAT.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断指定日期是否是工作日（周一至周五，不参考法定节假日）
     *
     * @param date
     * @return
     */
    public static boolean isWeekday(Date date) {
        return !isWeekend(date);
    }

    /**
     * 判断指定日期是否是周末（周六，周日，不参考法定节假日）
     *
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * 判断当前时间是否在指定区间内
     *
     * @param start 区间开始，如果为空，则默认为2:00
     * @param end   区间结束，如果为空，则默认为8:00
     * @return
     */
    public static boolean currentTimeBetween(Date start, Date end) {
        Calendar calendar = Calendar.getInstance();

        Date current = calendar.getTime();

        logger.info("====当前时间：{}", format(current));

        Calendar tmp = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (start == null) {
            calendar.set(Calendar.HOUR_OF_DAY, 2);//凌晨2点以后
            calendar.set(Calendar.MINUTE, 0);
        } else {
            tmp.setTime(start);
            calendar.set(Calendar.HOUR_OF_DAY, tmp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
        }
        start = calendar.getTime();

        if (end == null) {
            calendar.set(Calendar.HOUR_OF_DAY, 8);//8点以后
            calendar.set(Calendar.MINUTE, 0);
        } else {
            tmp.setTime(end);
            calendar.set(Calendar.HOUR_OF_DAY, tmp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
        }
        end = calendar.getTime();

        return current.compareTo(start) >= 0 && current.compareTo(end) <= 0;
    }


    /**
     * 获取两个日期之间间隔的天数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long getDays(Date d1, Date d2) {

        if (d1 == null || d2 == null) {
            return 0L;
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);

        long days = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (1000L * 60 * 60 * 24);
        return Math.abs(days);
    }
    /**
     * 获取两个日期之间间隔的小时数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long getDiffHours(Date d1, Date d2) {

        if (d1 == null || d2 == null) {
            return 0L;
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);

        long days = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (1000L * 60 * 60);
        return Math.abs(days);
    }


    /**
     * 根据日历的规则，为给定的日历字段添加或减去指定的时间量。
     *
     * @param date
     * @param field  日历字段。
     * @param amount 为字段添加的日期或时间量
     * @return
     */
    public static Date getDate(Date date, int field, int amount) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 根据日历的规则，为DAY_OF_MONTH字段添加或减去指定的时间量。
     *
     * @param date
     * @param days 为字段添加的日期或时间量
     * @return
     */
    public static Date getDate(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 根据date 增加 amount
     *
     * @param date
     * @param field
     * @param amount
     * @return
     */
    public static Date add(Date date, int field, int amount) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

    public static List<Date> findDateList(Date startDate, Date endDate) {
        try {
            long start = startDate.getTime();
            long end = endDate.getTime();
            if (start > end) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            List<Date> res = new ArrayList<>();
            while (end >= start) {
                res.add(DATE_FORMAT.parse(DATE_FORMAT.format(calendar.getTime())));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                end = calendar.getTimeInMillis();
            }
            return res;
        }catch (ParseException e){
            return null;
        }
    }

    public static LinkedHashSet<Date> findDateSet(Date startDate, Date endDate) {
        try {
            long start = startDate.getTime();
            long end = endDate.getTime();
            if (start > end) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            LinkedHashSet<Date> res = new LinkedHashSet<>();
            while (end >= start) {
                res.add(DATE_FORMAT.parse(DATE_FORMAT.format(calendar.getTime())));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                end = calendar.getTimeInMillis();
            }
            return res;
        }catch (ParseException e){
            return null;
        }
    }

    /**
     * 获取当月第一天
     * @return
     */
    public static Date getFirstDateOfMonth(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,0);
        return formatDate(DateUtil.getDate( c.getTime(), Calendar.DATE, 1));
    }
}

