package com.cemni.core.service;

import com.cemni.common.bean.ConfigBean;
import com.cemni.common.util.HttpUtil;
import com.cemni.common.util.JsonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenyu on 2017/3/9.
 */
@Service("smsService")
public class SmsService
{
    private static final Logger LOG = Logger.getLogger(SmsService.class.getName());

    private HttpUtil httpUtil = HttpUtil.getInstance();

    @Autowired
    @Qualifier("configBean")
    private ConfigBean configBean;

    /**
     * 单条短信发送,智能匹配短信模板
     *
     * @param apiKey 成功注册后登录云片官网,进入后台可查看
     * @param text   需要使用已审核通过的模板或者默认模板
     * @param mobile 接收的手机号,仅支持单号码发送
     * @return json格式字符串
     */
    public boolean singleSend(String apiKey, String text, String mobile)
    {
        boolean result = false;
        try
        {
            Map<String, String> params = new HashMap<String, String>();//请求参数集合
            params.put("apikey", apiKey);
            params.put("text", text);
            params.put("mobile", mobile);
            // 请自行使用post方式请求,可使用Apache HttpClient
            String response = httpUtil.sendHttpPost(configBean.getSmsRequestUrl(), params);

            Map<String, String> responseMap = JsonUtil.stringToObject(response, Map.class);
            if ("0".equals(String.valueOf(responseMap.get("code"))))
            {
                result = true;
            }
        }
        catch (Exception e)
        {
            LOG.error("Send Sms Error. ", e);
        }
        return result;
    }
}
