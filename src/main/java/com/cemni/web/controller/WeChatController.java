package com.cemni.web.controller;

import com.cemni.common.bean.BaiDuLocationInfo;
import com.cemni.common.bean.lnglat.WeiXinVerification;
import com.cemni.core.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by chenyu on 2017/3/11.
 */
@Controller("weChatController")
public class WeChatController
{
    @Autowired
    @Qualifier("weChatService")
    private WeChatService weChatService;

    /**
     * @param param
     * @return
     */
    @RequestMapping(value = "getSign", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public WeiXinVerification getSign(@RequestBody Map<String, String> param)
    {
        String url = param.get("url");
        return weChatService.getSign(url);
    }

    /**
     * @param param
     * @return
     */
    @RequestMapping(value = "getLocation", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public BaiDuLocationInfo getLocation(@RequestBody Map<String, String> param)
    {
        String longitude = param.get("longitude");
        String latitude = param.get("latitude");
        return weChatService.getLocation(longitude, latitude);
    }
}
