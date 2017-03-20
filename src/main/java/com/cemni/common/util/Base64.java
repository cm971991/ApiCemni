package com.cemni.common.util;

import com.cemni.common.bean.BaiDuLocationInfo;
import com.cemni.core.service.WeChatService;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/3/13.
 */
public class Base64
{
    public static String encodeBase64(String str)
    {
        byte[] b = null;
        String s = null;
        try
        {
            b = str.getBytes("utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        if (b != null)
        {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    public static String decodeBase64(String s)
    {
        byte[] b = null;
        String result = null;
        if (s != null)
        {
            BASE64Decoder decoder = new BASE64Decoder();
            try
            {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }
}
