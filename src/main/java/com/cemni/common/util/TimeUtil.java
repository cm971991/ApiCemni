package com.cemni.common.util;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chenyu on 2016/2/2.
 */
public class TimeUtil
{
    private static final ThreadLocal<SimpleDateFormat> DEFAULT_DATE_TIME_FORMAT = new ThreadLocal<SimpleDateFormat>()
    {
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> YMD_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>()
    {
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * @param date
     * @return
     */
    public static String formatDefaultDate(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return DEFAULT_DATE_TIME_FORMAT.get().format(date);
    }

    /**
     * @param dataStr
     * @return
     * @throws ParseException
     */
    public static Date formatDefaultDate(String dataStr) throws ParseException
    {
        if (StringUtils.isEmpty(dataStr))
        {
            return null;
        }
        return DEFAULT_DATE_TIME_FORMAT.get().parse(dataStr);
    }

    /**
     * @param date
     * @return
     */
    public static String formatYmdDate(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return YMD_DATE_FORMAT.get().format(date);
    }

    /**
     * @param dateStr
     * @return
     */
    public static Date formatYmdDate(String dateStr) throws ParseException
    {
        if (StringUtils.isEmpty(dateStr))
        {
            return null;
        }
        return YMD_DATE_FORMAT.get().parse(dateStr);
    }
}
