package com.cemni.common.constant;

import com.cemni.common.bean.lnglat.WeiXinVerification;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/10.
 */
public class WeChatConstant
{
    private static final Map<String, WeiXinVerification> map = new HashMap<String, WeiXinVerification>();

    public static void setWeiXinVerification(WeiXinVerification weiXinVerification)
    {
        synchronized (map)
        {
            map.put("weiXinVerification", weiXinVerification);
        }
    }

    public static void getWeiXinVerification()
    {
        synchronized (map)
        {
            map.get("weiXinVerification");
        }
    }
}
