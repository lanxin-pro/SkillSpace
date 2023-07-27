package cn.iocoder.educate.framework.common.util.date;

import java.time.LocalDateTime;

/**
 * 时间工具类，用于 {@link java.time.LocalDateTime}
 *
 * @Author: j-sentinel
 * @Date: 2023/7/27 15:15
 */
public class LocalDateTimeUtils {

    /**
     * 创建指定时间
     *
     * @param year  年
     * @param mouth 月
     * @param day   日
     * @return 指定时间
     */
    public static LocalDateTime buildTime(int year, int mouth, int day) {
        return LocalDateTime.of(year, mouth, day, 0, 0, 0);
    }

}
