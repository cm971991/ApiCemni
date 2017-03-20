package com.cemni.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by yangyang on 2016/3/10.
 */
public class JsonUtil
{
    private static ObjectMapper mapper = new ObjectMapper();

    public static String objectToString(Object object) throws JsonProcessingException
    {
        return mapper.writeValueAsString(object);
    }

    public static <T> T stringToObject(String value, TypeReference<T> valueTypeRef) throws IOException
    {
        return mapper.readValue(value, valueTypeRef);
    }

    public static <T> T stringToObject(String content, Class<T> beanClass) throws IOException
    {
        return mapper.readValue(content, beanClass);
    }
}
