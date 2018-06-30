package jack.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * * 本类最主要的是获取时间以及对时间格式的转换
 * 1. DateUtil.getDate()               获取日期时间
 * 2. DateUtil.getCurrentTimeMills()   获取当前时间毫秒值
 * 3. DateUtil.getCurrentTimeSeconds() 获取当前时间秒值
 * 4. DateUtil.getDate()               获取指定格式日期,eg:2014-11-07 09:41
 * 5. DateUtil.getDate2()              获取指定格式日期,eg:2014年11月07日
 * 6. DateUtil.getDate3()              获取指定格式日期,eg:20141107
 * 7. DateUtil.GetDateResult()         得到最终显示值。eg:凌晨，中午，昨天，11月7日，2014年8月23日
 * 8. DateUtil.getDaojiTime()          得到倒计时的秒值
 * 9. DateUtil.getOpen()               判断是否可以开奖
 * 10.DateUtil.getDifference(long,long)比较两个时间点的差值 getDifference(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
 * 11.DateUtil.getDifference(long)     毫秒值转换成 年月日时分秒 getDifference(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
 * 12.DateUtil.getGeShiDate(long)      通过秒数换算出 分 秒
 * 13.DateUtil.getStandardDate(long)   时间转换成多少分钟前
 * <h3>Description</h3>
 * TODO
 * <h3>Author</h3> （你的姓名）
 * <h3>Date</h3> 2016/3/28 16:40
 * <h3>Copyright</h3> Copyright (c)2016 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class DateUtil {
    private static SimpleDateFormat formatBuilder;

    /**
     * 获取日期时间
     *
     * @param format String
     * @return String
     */
    public static String getDate(String format) {
        formatBuilder = new SimpleDateFormat(format);
        return formatBuilder.format(new Date());
    }

    /**
     * 获取当前时间毫秒值
     *
     * @return long
     */
    public static long getCurrentTimeMills() {
        return new Date().getTime();
    }

    /**
     * 获取当前时间秒值
     *
     * @return long
     */
    public static long getCurrentTimeSeconds() {
        return new Date().getTime() / 1000;
    }

    /**
     * 获取指定格式日期,eg:2014-11-07 09:41
     *
     * @return String
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd HH:mm");
    }

    /**
     * 获取指定格式日期,eg:2014年11月07日
     *
     * @return String
     */
    public static String getDate2() {
        return getDate("yyyy年MM月dd日");
    }

    /**
     * 获取指定格式日期,eg:20141107094253
     *
     * @return String
     */
    public static String getDate3() {
        return getDate("yyyyMMddHHMMSS");
    }

    public static String getDate4() {
        return getDate("yyyy-MM-dd");
    }


    /**
     * 得到最终显示值。eg:凌晨，中午，昨天，11月7日，2014年8月23日
     *
     * @param time String
     * @return str
     */
   /* public static String GetDateResult(Context context, String time) {
        if (time != null && !time.equals("")) {
            // 2011 11 21 12 12 12yyyyMMddhhmmss //2013 03 12 12 50 01
            int year = Integer.valueOf((String) time.subSequence(0, 4));
            int month = Integer.valueOf((String) time.subSequence(4, 6));
            int date = Integer.valueOf((String) time.subSequence(6, 8));
            int hour = Integer.valueOf((String) time.subSequence(8, 10));
            int minute = Integer.valueOf((String) time.subSequence(10, 12));
            // int second=Integer.valueOf((String) time.subSequence(12, 14));
            Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
            int year1 = c.get(Calendar.YEAR);
            // int month1 = c.get(Calendar.MONTH)+1;
            // int date1 = c.get(Calendar.DATE);
            int data11 = c.get(Calendar.DAY_OF_YEAR);
            // int hour1 = c.get(Calendar.HOUR_OF_DAY);
            // int minute1 = c.get(Calendar.MINUTE);
            // int second1 = c.get(Calendar.SECOND);
            String dayofyear = year + "/" + month + "/" + date;
            String str = null;
            int dataM = Math.abs(data11 - GetDayOfYear(dayofyear));
            int yearM = Math.abs(year1 - year);
            if (dataM == 0) {// 今天
                if (hour >= 0 && hour < 5) {// 凌晨
                    str = "凌晨";
                } else if (hour >= 5 && hour < 9) {// 早晨
                    str = "早晨";
                } else if (hour >= 9 && hour < 13) {// 上午
                    str = "上午";
                } else if (hour >= 13 && hour < 19) {// 下午
                    str = "下午";
                } else if (hour >= 18 && hour < 21) {// 晚上
                    str = "晚上";
                } else if (hour >= 21 && hour <= 24) {// 深夜
                    str = "深夜";
                }
            } else if (dataM == 1) { // 昨天
                str = "昨天";
            } else if (dataM == 2) {// 周几
                str = Week(context, time, "yyyyMMddhhmmss");
            } else {// 年月日
                if (yearM == 0) { // 月 日
                    str = month + "月" + date + "日";
                } else {// 年月日
                    str = year + "年" + month + "月" + date + "日";
                }
            }
            return str + "  " + NumDouble(hour) + ":" + NumDouble(minute);
        } else
            return "";
    }
*/
    /**
     * 将时间转换成 24时格式
     *
     * @param timea 十二小时格式
     * @return 返回值
     */
    private static String NumDouble(int timea) {
        DecimalFormat df = new DecimalFormat("00");
        return df.format(timea);
    }

/*
    public static String Week(Context context, String time, String format) {
        final String dayNames[] = {context.getString(R.string.sunday), context.getString(R.string.monday), context.getString(R.string.tuesday), context.getString(R.string.wednesday), context.getString(R.string.thursday), context.getString(R.string.friday), context.getString(R.string.saturday)};
        SimpleDateFormat sdfInput = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }*/

    private static int GetDayOfYear(String time) {
        @SuppressWarnings("deprecation")
        Date date = new Date(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 得到当前值
     *
     * @return str 和服务端发送数据一致
     */
    public static String GetNow() {
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH) + 1;
        int date1 = c.get(Calendar.DATE);
        int hour1 = c.get(Calendar.HOUR_OF_DAY);
        int minute1 = c.get(Calendar.MINUTE);
        int second1 = c.get(Calendar.SECOND);
        return year1 + "" + NumDouble(month1) + "" + NumDouble(date1) + "" + NumDouble(hour1) + "" + NumDouble(minute1) + "" + NumDouble(second1);
    }

    /**
     * 得到当前时间 yy-mm-dd HH:mm:ss
     *
     * @return str 和服务端发送数据一致
     */
    public static String Getnowtime() {
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH) + 1;
        int date1 = c.get(Calendar.DATE);
        int hour1 = c.get(Calendar.HOUR_OF_DAY);
        int minute1 = c.get(Calendar.MINUTE);
        int second1 = c.get(Calendar.SECOND);
        return year1 + "-" + NumDouble(month1) + "-" + NumDouble(date1) + " " + NumDouble(hour1) + ":" + NumDouble(minute1) + ":" + NumDouble(second1);
    }

    public static String strToDate(String strDate) throws ParseException {
        int a;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date strtodate = formatter.parse(strDate);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(strtodate);
        a = cal1.get(Calendar.DAY_OF_WEEK);
        return a + "";
    }

    /**
     * 通过参数传入1-7 返回周几
     *
     * @param sdate String
     * @return String
     */
    public static String getWeekStr(String sdate) {
        String str;
        str = sdate;
        if ("1".equals(str)) {
            str = "周日";
        } else if ("2".equals(str)) {
            str = "周一";
        } else if ("3".equals(str)) {
            str = "周二";
        } else if ("4".equals(str)) {
            str = "周三";
        } else if ("5".equals(str)) {
            str = "周四";
        } else if ("6".equals(str)) {
            str = "周五";
        } else if ("7".equals(str)) {
            str = "周六";
        }
        return str;
    }


    /**
     * 得到闹钟的时间
     *
     * @param sd 时间 毫秒
     * @return String 日期
     */
    public static String getDateToString(long sd) {
        // long sd=1345185923140L;
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(gc.getTime());
    }


    // 查分毫秒值用的常量
    private final static long yearLevelValue = 365 * 24 * 60 * 60 * 1000l;
    private final static long monthLevelValue = 30 * 24 * 60 * 60 * 1000l;
    private final static long dayLevelValue = 24 * 60 * 60 * 1000l;
    private final static long hourLevelValue = 60 * 60 * 1000l;
    private final static long minuteLevelValue = 60 * 1000l;
    private final static long secondLevelValue = 1000l;

    /**
     * 比较两个时间点的差值 getDifference(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
     *
     * @param nowTime    long
     * @param targetTime long
     * @return String
     */
    public static String getDifference(long nowTime, long targetTime) {// 目标时间与当前时间差
        long period = targetTime - nowTime;
        return getDifference(period);
    }

    /**
     * 毫秒值转换成 年月日时分秒 getDifference(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
     *
     * @param period long
     * @return String
     */
    private static String getDifference(long period) {
        String result;

        /******* 计算出时间差中的年、月、日、天、时、分、秒 *******/
        int year = getYear(period);
        int month = getMonth(period - year * yearLevelValue);
        int day = getDay(period - year * yearLevelValue - month * monthLevelValue);
        int hour = getHour(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue);
        int minute = getMinute(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue - hour * hourLevelValue);
        int second = getSecond(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue - hour * hourLevelValue - minute * minuteLevelValue);

        result = year + "年" + month + "月" + day + "天" + hour + "时" + minute + "分" + second + "秒";
        return result;
    }

    /**
     * 通过秒数换算出 分 秒
     *
     * @param miao long
     * @return String
     */
    public static String getGeShiDate(long miao) {
        long minute = miao / 60;
        long scond = miao - minute * 60;
        return minute + "分" + scond + "秒";
    }

    public static int getYear(long period) {
        return (int) (period / yearLevelValue);
    }

    public static int getMonth(long period) {
        return (int) (period / monthLevelValue);
    }

    public static int getDay(long period) {
        return (int) (period / dayLevelValue);
    }

    public static int getHour(long period) {
        return (int) (period / hourLevelValue);
    }

    public static int getMinute(long period) {
        return (int) (period / minuteLevelValue);
    }

    public static int getSecond(long period) {
        return (int) (period / secondLevelValue);
    }

    /**
     * 获取当毫秒数
     *
     * @param date 需要转换的时间（格式yyyy/MM/dd HH:mm:ss）
     * @return 返回long
     */
    public static long getTimeMillis(String date) {
        long timeMillis = 0;
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date));
            timeMillis = calendar.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMillis;
    }

    public static String gettime() {
        return null;

    }

    /**
     * 掉此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String dataOne(String time, String format) {
        SimpleDateFormat sdr = new SimpleDateFormat(format,
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
            Log.i("time",times);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * @param date   时间
     * @param format 需要格式化的格式模板
     */
    public static long getTimeMillis(String date, String format) {
        long timeMillis = 0;
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(new SimpleDateFormat(format).parse(date));
            timeMillis = calendar.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMillis;
    }


    public static String getDateStringByTimeMillis(long timeMillis) {
        if ((timeMillis + "").length() == 10) {
            timeMillis = timeMillis * 1000;
        }
        Date dat = new Date(timeMillis);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(gc.getTime());
    }

    public static String getDateString4TimeMillis(long timeMillis) {
        Date dat = new Date(timeMillis);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(gc.getTime());
    }

    /**
     * 时间转换成多少分钟之前
     *
     * @param timeStr 时间
     * @return String
     */
    public static String getStandardDate(String timeStr) {
        StringBuilder sb = new StringBuilder();
        long t = Long.parseLong(timeStr);
        // long time = System.currentTimeMillis() - (t * 1000);
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);// 秒前
        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前
        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前
        if (day - 1 > 0) {
            sb.append(day).append("天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour).append("小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute).append("分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill).append("秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    public static long timeDateFormat(String time, String chooseTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long diff = 0;
        try {
            Date d1 = df.parse(time);
            Date d2 = df.parse(chooseTime);
            diff = d1.getTime() - d2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diff;
    }

    public static Date getDate(String sDate, String dateFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        ParsePosition pos = new ParsePosition(0);
        return fmt.parse(sDate, pos);
    }

    public static Date getDateFromString(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date resDate = null;
        try {
            resDate = sdf.parse(dateStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resDate;
    }

    /**
     * 获取手机时区
     *
     * @return
     */
    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getDisplayName(false, TimeZone.SHORT);
    }

    /**
     * 根据传入的时间格式 返回当前日期时间
     *
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;
    }

    /**
     * 根据传入的时间进行格式化，返回日期加周几
     *
     * @param format
     * @return
     */
 /*   public static String getCurrentDate(Context context, Date date, String format) {
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            c.setTime(date);
            curDateTime = mSimpleDateFormat.format(c.getTime()) + " " + Week(context, mSimpleDateFormat.format(c.getTime()), format);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;
    }
*/

//    /**
//     * 根据传入的时间进行格式化，返回日期加周几
//     *
//     * @param format
//     * @return
//     */
//    public static int compareCurrentDate(Date date) {
//        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
//        Calendar c1 = new GregorianCalendar();
//        c1.setTime(new Date());
//        c
//        Calendar c2 = new GregorianCalendar();
//        c2.setTime(date);
//        return c1.compareTo(c2);
//    }

    /**
     * 根据传入的时间进行格式化，返回日期加周几
     *
     * @param
     * @return
     */


    public static String getTimestampString(Date data) {
        String time = "";
        long l = data.getTime();
        Calendar var4 = GregorianCalendar.getInstance();
        var4.setTime(data);
        int var5 = var4.get(Calendar.HOUR_OF_DAY);
        int var6 = var4.get(Calendar.SECOND);
        if (var5 < 10 && var5 > 0) {
            time += "0" + var5;
        } else {
            time = "" + var5;
        }
        time += ":";

        if (var6 < 10 && var5 > 0) {
            time += "0" + var6;
        } else {
            time += "" + var6;
        }
        return time;
    }
}
