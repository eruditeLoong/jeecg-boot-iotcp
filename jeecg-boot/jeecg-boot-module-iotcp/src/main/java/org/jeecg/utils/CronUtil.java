package org.jeecg.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @program: jeecg-boot-module-iot
 * @description: cron工具类
 * @author: zhouwr
 * @create: 2020-06-02 12:42
 * @version：1.0
 **/

public class CronUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");

    /**
     *  功能描述：日期转换cron表达式
     * @param date
     * @return
     */
    public static String formatDateByPattern(Date date) {
        String formatTimeStr = null;
        if (Objects.nonNull(date)) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /***
     * convert Date to cron, eg "0 07 10 15 1 ? 2016"
     * @param date  : 时间点
     * @return
     */
    public static String getCron(Date date) {
        return formatDateByPattern(date);
    }

    public static void main(String[] args) {
        System.out.println(getCron(new Date(1)));

    }
}

