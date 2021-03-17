package com.ruoyi.common.utils;

import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }


    /**
     * 计算两个时间段之间每月的时间段
     */
    public static List<Map> getDateBetween(Date startDate, Date endDate, List<Map> times){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        // 计算月尾时间
        calendar.add(Calendar.MONTH, 1);

        Calendar end_calendar = Calendar.getInstance();
        end_calendar.setTime(endDate);
        end_calendar.add(Calendar.DATE, 1);

        if (calendar.getTime().getTime() <= end_calendar.getTime().getTime()) {
            while(true){
                Map<String, Object> timeMap = Maps.newHashMap();
                timeMap.put("startDate", DateUtils.dateTime(startDate));
                startDate =calendar.getTime();
                calendar.add(Calendar.DATE, -1);
                // 设置月末时间
                timeMap.put("endDate", DateUtils.dateTime(calendar.getTime()));
                // 首期账单要交押金的标识
                if (times.size() == 0) {
                    timeMap.put("isDeposit", true);
                }
                else {
                    timeMap.put("isDeposit", false);
                }
                // 标记当前为账单完整的月计
                timeMap.put("type", "M");
                times.add(timeMap);
              return getDateBetween(startDate, endDate, times);
            }
        } else {
            Map<String, Object> timeMap0 = Maps.newHashMap();
            // 如果当前增加一个月时间大于结束时间，直接取当前结束时间
            timeMap0.put("startDate", DateUtils.dateTime(startDate));
            timeMap0.put("endDate", DateUtils.dateTime(endDate));
            // 首期账单要交押金的标识
            if (times.size() == 0) {
                timeMap0.put("isDeposit", true);
            }
            else {
                timeMap0.put("isDeposit", false);
            }
            // 标记当前为账单日计
            timeMap0.put("type", "D");
            times.add(timeMap0);
        }
        return times;
    }

    /**
     * 计算两日期间的月份数
     * @param startDate
     * @param endDate
     * @return
     */
    public static Integer getDifMonth(Date startDate, Date endDate){
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        int result = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int month = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result) + 1;
    }

}
