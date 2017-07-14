package library.lanshifu.com.lsf_library.utils;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import library.lanshifu.com.lsf_library.R;

/**
 * 时间工具类
 */
public final class DateUtil {
    private static final String TAG = DateUtil.class.getSimpleName();
    private static final long MINUTE = 60L;// 每分钟的秒数
    private static final long HOUR = MINUTE * 60;// 每小时的秒数
    private static final long DAY = HOUR * 24;// 每天的秒数
    private static final long MONTH = DAY * 30;// 每月(按30天计)的秒数
    private static final long YEAR = DAY * 365;// 每年(按365天计)的秒数

    public static final String DATE_TIME_yyyy_M_d_H_mm = "yyyy-M-d H:mm";

    public static final String DATE_TIME_yyyy_M_d_H_mm_ss = "yyyy-M-d H:mm:ss";

    public static final String DATE_TIME_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";

    public static final String DATE_FORMAT_YEAR_MONTH = "yyyy-MM";

    public static final String DATE_FORMAT_yyyyMMdd = "yyyyMMdd";

    public static final String DATE_FORMAT_yyyy年M月d日 = "yyyy年M月d日";

    public static final SimpleDateFormat FORMAT_DATE_yyyyMMdd = new SimpleDateFormat(
            DATE_FORMAT_yyyyMMdd);

    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd");
    public static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat FORMAT_YYYYMMDDHHMMSS = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    private DateUtil() {
    }

    /**
     * 当前日期，默认yyyy-MM-dd
     */
    public static String getCurrentDate() {
        return FORMAT_DATE.format(new Date());
    }

    /**
     * 当前时间，默认格式yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime() {
        return FORMAT_DATE_TIME.format(new Date());
    }

    /**
     * 当前日期，按指定的格式
     *
     * @param format 指定的日期格式
     * @return 格式后后的当前日期
     */
    public static String getCurrent(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 当前日期，按指定的格式
     *
     * @param format 指定的日期格式
     * @return 格式后后的当前日期
     */
    public static String getCurrent(SimpleDateFormat format) {
        return format.format(new Date());
    }

    /**
     * 获取指定日期的年份
     *
     * @param date 日期
     * @return 年份
     */
    public static String getYearString(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        return Integer.toString(cal.get(Calendar.YEAR));
    }

    /**
     * 获取指定日期的月份
     *
     * @param date 日期
     * @return 月份
     */
    public static String getMonthString(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        return StringUtil.zeroPad(cal.get(Calendar.MONTH) + 1, 2);
    }

    /**
     * 获取指定日期的日
     *
     * @param date 日期
     * @return 月份中的日
     */
    public static String getDayString(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        return StringUtil.zeroPad(cal.get(Calendar.DAY_OF_MONTH), 2);
    }

    /**
     * 解析成日期
     *
     * @param strDate 字符串.
     * @param format  转换格式如:"yyyy-MM-dd mm:ss"
     * @return 字符串包含的日期
     */
    public static Date parseString(String strDate, SimpleDateFormat format) {
        try {
            return format.parse(strDate);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }

        return new Date();
    }

    /**
     * 获取相隔几个月的日期
     *
     * @param date   用于处理的日期
     * @param gap    相隔的月份数
     * @param format 目标时间的格式,传null的话默认格式为yyyy-MM-dd
     * @return 相隔的日期字符串
     */
    public static String getTimePassedMonth(Date date, int gap,
                                            SimpleDateFormat format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, gap);

        if (format == null) {
            format = FORMAT_DATE;
        }
        return format.format(cal.getTime());
    }

    /**
     * 获取时间字符串中的时间部分 注意: 这个方法需要参数是"yyyy-mm-dd"的格式
     *
     * @param src 时间字符串
     * @return 字符串中的日期部分
     */
    public static String getDate(String src) {
        if (src == null || src.length() < 10) {
            return "";
        }
        return src.substring(0, 10);
    }

    /**
     * 把时间字符串,按照某种格式进行转换
     *
     * @param dateStr    原时间字符串
     * @param srcformat  原格式
     * @param destformat 目标格式
     * @return 使用目标格式进行格式化的时间字符串, 转换失败的话返回空字符串
     */
    public static String convertDateString(String dateStr,
                                           SimpleDateFormat srcformat, SimpleDateFormat destformat) {
        try {
            Date date = srcformat.parse(dateStr);
            return destformat.format(date);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    /**
     * 比较两个日期的大小 注意: 需要两个日期的格式都是{@link #FORMAT_DATE_TIME}
     *
     * @param srcDate  第一个日期
     * @param destDate 第二个日期
     * @return 如果第一个日期比第二个日期小, 返回true, 否则返回false.转换失败也返回false
     */
    public static boolean isBefore(String srcDate, String destDate) {
        try {
            Date d1 = FORMAT_DATE_TIME.parse(srcDate);
            Date d2 = FORMAT_DATE_TIME.parse(destDate);
            return d1.before(d2);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    /**
     * 比较两个日期的大小 注意: 需要两个日期的格式都是foamt的格式
     *
     * @param srcDate  第一个日期
     * @param destDate 第二个日期
     * @param foamt    日期格式
     * @return 如果第一个日期比第二个日期小, 返回true, 否则返回false.转换失败也返回false
     */
    public static boolean isBefore(String srcDate, String destDate, String foamt) {
        SimpleDateFormat sdf = new SimpleDateFormat(foamt);
        try {
            Date d1 = sdf.parse(srcDate);
            Date d2 = sdf.parse(destDate);
            return d1.before(d2);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 显示时间格式为今天、昨天、yyyy/MM/dd hh:mm
     *
     * @param context 上下文
     * @param when    待处理的时间
     * @return 格式化后的时间
     */
    public static String formatTimeString(Context context, long when) {
        Time then = new Time();
        then.set(when);
        Time now = new Time();
        now.setToNow();

        if (then.year == now.year && then.yearDay == now.yearDay) {
            return context.getString(R.string.date_today);
        }
        if ((then.year == now.year) && ((now.yearDay - then.yearDay) == 1)) {
            return context.getString(R.string.date_yesterday);
        }

        String formatStr;
        if (then.year != now.year) {
            formatStr = "yyyy/MM/dd";
        } else if (then.yearDay != now.yearDay) {
            // If it is from a different day than today, show only the date.
            formatStr = "MM/dd";
        } else {
            // Otherwise, if the message is from today, show the time.
            formatStr = "HH:MM";
        }

        String temp = new SimpleDateFormat(formatStr).format(when);
        if (temp != null && temp.length() == 5
                && temp.substring(0, 1).equals("0")) {
            temp = temp.substring(1);
        }
        return temp;
    }

    /**
     * 获取当前的日期时间格式 date格式yyyy-MM-DD HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime(String date) {
        Calendar current = Calendar.getInstance();

        String myDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
        Date currentDate = parseString(date, sdf);
        if (current.get(Calendar.DAY_OF_MONTH) == currentDate.getDay()) {
            myDate = current.get(Calendar.HOUR_OF_DAY) + ":"
                    + current.get(Calendar.MINUTE);
        } else {
            myDate = date.substring(0, 10);
        }

        return myDate;
    }

    /**
     * 格式化日期为字符串函数.
     *
     * @param date   日期.
     * @param format 转换格式."yyyy-MM-dd mm:ss"
     * @return 日期转化来的字符串.
     */
    public static String formatDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 从FORMAT_DATE_TIME格式的日期中获取出月份和日期
     *
     * @param date
     * @return 月/日期
     */
    public static String getMonthAndDay(String date) {

        Calendar currCal = Calendar.getInstance();// 当前日历类
        Calendar appoCal = Calendar.getInstance();// 指定日历类

        Date newDate = new Date();
        Date mDate = DateUtil.parseString(date, DateUtil.FORMAT_DATE_TIME);

        currCal.setTime(newDate);
        appoCal.setTime(mDate);

        int currMonth = currCal.get(Calendar.MONTH) + 1;
        int appoMonth = appoCal.get(Calendar.MONTH) + 1;

        int currDay = currCal.get(Calendar.DAY_OF_MONTH);
        int appoDay = appoCal.get(Calendar.DAY_OF_MONTH);

        if (currDay == appoDay && currMonth == appoMonth) {
            return "今天";
        }
        String strMonth = null;
        if (appoMonth < 10) {
            strMonth = "0" + appoMonth;
        } else {
            strMonth = appoMonth + "";
        }
        String strDay = null;
        if (appoDay < 10) {
            strDay = "0" + appoDay;
        } else {
            strDay = appoDay + "";
        }
        String strTime = strMonth + "/" + strDay;
        return strTime;
    }

    /**
     * 截取分时秒 从yy：mm：dd hh：mm：ss格式将分时秒截取出来
     *
     * @param date
     * @return
     */
    public static String getTimeFromHour(String date) {
        if (StringUtil.isEmpty(date)) {
            return "";
        }
        Calendar appoCal = Calendar.getInstance(Locale.CHINA);// 指定日历类
        Date mDate = DateUtil.parseString(date, DateUtil.FORMAT_DATE_TIME);

        appoCal.setTime(mDate);

        int appoHour = appoCal.get(Calendar.HOUR_OF_DAY);
        int appoMin = appoCal.get(Calendar.MINUTE);
        int appoSec = appoCal.get(Calendar.SECOND);

        String strHour = null;
        if (appoHour < 10) {
            strHour = "0" + appoHour;
        } else {
            strHour = appoHour + "";
        }
        String strTime = strHour + ":" + appoMin + ":" + appoSec;
        return strTime;
    }

    /**
     * 计算两个时间点之间的时间差
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static String getTimeBetween(String beginTime, String endTime) {
        if (StringUtil.isEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime();
        }
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        Date end = null;
        long between = 0;
        long day = 0;
        long hour = 0;
        long minute = 0;
//        long second = 0;
        try {
            begin = dfs.parse(beginTime);
            end = dfs.parse(endTime);
            between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
            day = between / (24 * 3600);
            hour = between % (24 * 3600) / 3600;
            hour = 24 * day + hour;
            minute = between % 3600 / 60;
//            second = between  % 60 % 60;
        } catch (ParseException e) {
            Log.e("getTimeBetween error", e.toString());
        }
        return hour + "小时" + minute + "分";
    }

    public static boolean isDateAfter(String startDate, String endDate, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format);
            Date mDate1 = df.parse(startDate);
            Date mDate2 = df.parse(endDate);
            return mDate2.after(mDate1);
        } catch (ParseException e) {
            Log.e("DateUtil", "ParseException");
            return false;
        }
    }
}
