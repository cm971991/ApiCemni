package com.cemni.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenyu on 2017/3/11.
 */
public class VerificationCodeCache
{
    private static final Map<String, String> VERIFICATION_CODE_CACHE = new ConcurrentHashMap<String, String>();

    private static final Map<String, Long> VERIFICATION_CODE_TIME_CACHE = new ConcurrentHashMap<String, Long>();

    public static void setCode(String key, String value)
    {
        VERIFICATION_CODE_CACHE.put(key, value);
    }

    public static String getCode(String key)
    {
        return VERIFICATION_CODE_CACHE.get(key);
    }

    public static void delCode(String key)
    {
        VERIFICATION_CODE_CACHE.remove(key);
    }

    public static void setTime(String key, Long value)
    {
        VERIFICATION_CODE_TIME_CACHE.put(key, value);
    }

    public static Long getTime(String key)
    {
        return VERIFICATION_CODE_TIME_CACHE.get(key);
    }

    public static void delTime(String key)
    {
        VERIFICATION_CODE_TIME_CACHE.remove(key);
    }

    public static Map<String, Long> getTimeMap()
    {
        return VERIFICATION_CODE_TIME_CACHE;
    }
}
