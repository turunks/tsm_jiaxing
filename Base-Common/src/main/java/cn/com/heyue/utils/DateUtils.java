package cn.com.heyue.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * 日期Util类
 *
 * @author 付祖远
 * @version V1.0
 */
public class DateUtils {
    public static final String defaultDatePattern = "yyyy-MM-dd";

    public static final String FullDatePattern = "yyyy-MM-dd HH:mm:ss";

    public static final String HFDatePattern = "yyyy-MM-dd HH:mm";

    public static final String DATE_FORMAT_PATTERN = "MM/dd/yyyy";

    public static final String FORMAT_FULL = "yyyyMMddHHmmss";

    public static final String FORMAT_TIME = "yyMMddHHmmss";

    public static final String FORMAT_DATE = "yyyyMMdd";

    private static SimpleDateFormat lenientDateFormat = new SimpleDateFormat(defaultDatePattern);

    static {
        // 尝试试从messages_zh_Cn.properties中获取defaultDatePattner.
        try {
            // Locale locale = LocaleContextHolder.getLocale();
            // defaultDatePattern =
            // ResourceBundle.getBundle(Constants.MESSAGE_BUNDLE_KEY,
            // locale).getString("date.default_format");
        } catch (MissingResourceException mse) {
            // do nothing
        }
    }

    /**
     * 获得默认的 date pattern
     *
     * @return String
     */
    public static String getDatePattern() {
        return defaultDatePattern;
    }

    /**
     * 返回预设Format的当前日期字符串
     *
     * @return String
     */
    public static String getToday() {
        return format(now());
    }

    /**
     * 返回当前时间
     *
     * @return Date实例
     */
    public static Date now() {
        return nowCal().getTime();
    }

    /**
     * 当前时间
     *
     * @return Calendar实例
     */
    public static Calendar nowCal() {
        return Calendar.getInstance();
    }

    /**
     * Date型转化到Calendar型
     *
     * @param date
     * @return Calendar
     */
    public static Calendar date2Cal(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    /**
     * 当前时间的下一天
     *
     * @return Calendar
     */
    public static Calendar nextDay() {
        return nextDay(nowCal());
    }

    /**
     * 当前时间的下一月
     *
     * @return Calendar
     */
    public static Calendar nextMonth() {
        return nextMonth(nowCal());
    }

    /**
     * 当前时间的下一年
     *
     * @return Calendar
     */
    public static Calendar nextYear() {
        return nextMonth(nowCal());
    }

    /**
     * 下一天
     *
     * @param cal
     * @return Calendar
     */
    public static Calendar nextDay(Calendar cal) {
        if (cal == null) {
            return null;
        }
        return afterDays(cal, 1);
    }

    /**
     * 下一月
     *
     * @param cal
     * @return Calendar
     */
    public static Calendar nextMonth(Calendar cal) {
        if (cal == null) {
            return null;
        }
        return afterMonths(cal, 1);
    }

    /**
     * 下一年
     *
     * @param cal
     * @return Calendar
     */
    public static Calendar nextYear(Calendar cal) {
        if (cal == null) {
            return null;
        }
        return afterYesrs(cal, 1);
    }

    /**
     * 后n天
     *
     * @param cal
     * @param n
     * @return Calendar
     */
    public static Calendar afterDays(Calendar cal, int n) {
        if (cal == null) {
            return null;
        }
        Calendar c = (Calendar) cal.clone();
        c.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + n);
        return c;
    }

    /**
     * 下N秒
     *
     * @param cal
     * @param n
     * @return
     */
    public static Calendar afterSecond(Calendar cal, int n) {
        if (cal == null) {
            return null;
        }
        Calendar c = (Calendar) cal.clone();
        c.set(Calendar.SECOND, cal.get(Calendar.SECOND) + n);
        return c;
    }

    /**
     * 后n月
     *
     * @param cal
     * @param n
     * @return Calendar
     */
    public static Calendar afterMonths(Calendar cal, int n) {
        if (cal == null) {
            return null;
        }
        Calendar c = (Calendar) cal.clone();
        c.set(Calendar.MONTH, cal.get(Calendar.MONTH) + n);
        return c;
    }

    /**
     * 后n年
     *
     * @param cal
     * @param n
     * @return Calendar
     */
    public static Calendar afterYesrs(Calendar cal, int n) {
        if (cal == null) {
            return null;
        }
        Calendar c = (Calendar) cal.clone();
        c.set(Calendar.YEAR, cal.get(Calendar.YEAR) + n);
        return c;
    }

    /**
     * 使用预设Format格式化Date成字符串
     *
     * @return String
     */
    public static String format(Date date) {
        return date == null ? "" : format(date, getDatePattern());
    }

    /**
     * 使用参数Format格式化Date成字符串
     *
     * @return String
     */
    public static String format(Date date, String pattern) {
        return date == null ? "" : new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 试用参数Format格式化Calendar成字符串
     *
     * @param cal
     * @param pattern
     * @return String
     */
    public static String format(Calendar cal, String pattern) {
        return cal == null ? "" : new SimpleDateFormat(pattern).format(cal.getTime());
    }

    /**
     * 使用预设格式将字符串转为Date
     *
     * @return Date
     */
    public static Date parse(String strDate)
            throws ParseException {
        return StringUtils.isBlank(strDate) ? null : parse(strDate, getDatePattern());
    }

    /**
     * 使用参数Format将字符串转为Date
     *
     * @return Date
     */
    public static Date parse(String strDate, String pattern)
            throws ParseException {
        return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
    }

    /**
     * 在日期上增加数个整月
     *
     * @return Date
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /*
     * 在日期上增加天数
     *
     * @param date 日期
     *
     * @param n 要增加的天数
     *
     * @return
     */

    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();

    }

    /**
     * get String value(MM/dd/yyyy) of time
     *
     * @param d
     * @return String
     */
    public static String dateToString(Date d) {
        if (d == null) {
            return null;
        }
        return lenientDateFormat.format(d);
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     */
    public static long[] getDistanceTime(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new long[]{day, hour, min, sec};
    }

    public static String getCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "";
        }
    }

    /**
     * 得到两时间相差的月份
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static int getMonthDiff(String startDate, String endDate) {
        int syear = Integer.parseInt(startDate.substring(0, 4));
        int smonth = Integer.parseInt(startDate.substring(4, 6));

        int eyear = Integer.parseInt(endDate.substring(0, 4));
        int emonth = Integer.parseInt(endDate.substring(4, 6));

        int result;
        if (syear == eyear) {
            // 两个日期相差几个月，即月份差
            result = emonth - smonth;
        } else {
            // 两个日期相差几个月，即月份差
            result = 12 * (eyear - syear) + emonth - smonth;
        }
        return result;
    }

    public static String getTimeString() {

        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);

        Calendar calendar = Calendar.getInstance();

        return df.format(calendar.getTime());

    }

    public static String getTimeString(long time) {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        return df.format(time);
    }

    public static String getTimeString(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());

    }

    /**
     * 在今天之前
     * @param day 天数
     * @param format 格式
     * @return
     */
    public static String getBeforeDayDate(int day, String format) {

        SimpleDateFormat df = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -day);
        return df.format(calendar.getTime());

    }

    /**
     * 在今天之后
     * @param day 天数
     * @param format 格式
     * @return
     */
    public static String getAfterDayDate(int day, String format) {

        SimpleDateFormat df = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        return df.format(calendar.getTime());

    }


    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                // System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                // System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String getCodeString() {

        SimpleDateFormat df = new SimpleDateFormat(FORMAT_TIME);

        Calendar calendar = Calendar.getInstance();

        return df.format(calendar.getTime());

    }

    public static String getDateString() {

        SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATE);

        Calendar calendar = Calendar.getInstance();

        return df.format(calendar.getTime());

    }

    public static int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    /**
     * 得到一周的起止日期
     *
     * @return
     * @throws Exception
     */
    public static String[] getWeekBeginEndDate()
            throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String beginDate = DateUtils.format(calendar, "yyyy-MM-dd");
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String endDate = DateUtils.format(calendar, "yyyy-MM-dd");
        return new String[]{beginDate, endDate};
    }

    /**
     * 和当前时间进行比较
     *
     * @param cdate
     * @param second
     * @return
     */
    public static boolean comparisonDateAndNow(String cdate, String format, int second) {
        if (StringUtils.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(DateUtils.parse(cdate, format));
        } catch (ParseException e) {
            return false;
        }
        calendar.add(Calendar.SECOND, second);
        Date currentTime = new Date();
        if (calendar.getTime().getTime() >= currentTime.getTime()) {
            return true;
        }
        return false;
    }

    /**
     * Java将Unix时间戳转换成指定格式日期字符串
     *
     * @param timeStamp 如："1473048265";
     * @param formats   要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String timeStamp2Date(Long timeStamp, String formats) {
        if (StringUtils.isBlank(formats)) {
            formats = "yyyy-MM-dd HH:mm:ss";
        }
        Long timestamp = timeStamp * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return 返回空表示失败
     */
    public static String date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long date2TStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return nowTimeStamp
     */
    public static Long getNowTimeStamp() {
        long time = System.currentTimeMillis();
        long nowTimeStamp = time / 1000;
        return nowTimeStamp;
    }

    /**
     * 得到和当前时间相差多少秒
     *
     * @param startDateStr
     * @param format
     * @return
     * @throws Exception
     */
    public static long secondDiff(String startDateStr, String format) throws Exception {
        Date startDate = DateUtils.parse(startDateStr, format);
        return (new Date().getTime() - startDate.getTime()) / 1000;
    }

    /**
     * 得到和当前时间相差多少秒-时间戳
     *
     * @param comDate 需要比较时间
     * @return
     * @throws Exception
     */
    public static long secondDiffTimeStamp(long comDate) throws Exception {
        return Math.abs(System.currentTimeMillis() - comDate) / 1000;
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param date
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public static long compareDateDays(String startDate, String endDate, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format);
            Date dt1 = df.parse(startDate);
            Date dt2 = df.parse(endDate);
            return (dt2.getTime() - dt1.getTime()) / (24 * 3600 * 1000);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return 返回空表示失败
     */
    public static String dateFormat(String dateStr, String format, String format1) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return DateUtils.timeStamp2Date(String.valueOf(sdf.parse(dateStr).getTime()), format1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date   date日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return 返回空表示失败
     */
    public static String dateFormat(Date date, String format, String format1) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return DateUtils.timeStamp2Date(String.valueOf(date.getTime()), format1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(DateUtils.compareDateDays("20210106","20210107","yyyyMMdd"));
//        System.out.println(DateUtils.secondDiff("20210417102600","yyyyMMddHHmmss"));
//        Date d = new Date();
//        System.out.println(DateUtils.format(d, "yyyyMMddHHmmss"));
//        System.out.println(DateUtils.dateFormat(d, DateUtils.FORMAT_FULL, "yyyy-MM-dd'T'HH:mm:ss"));

        long second = DateUtils.secondDiffTimeStamp(1621827074L * 1000);
        System.out.println(second);
    }
}