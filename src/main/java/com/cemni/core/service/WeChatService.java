package com.cemni.core.service;

import com.cemni.common.bean.*;
import com.cemni.common.bean.lnglat.WeiXinVerification;
import com.cemni.common.util.Base64;
import com.cemni.common.util.HttpUtil;
import com.cemni.common.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by chenyu on 2017/3/10.
 */
@Service("weChatService")
public class WeChatService
{
    private static final Logger LOG = Logger.getLogger(WeChatService.class.getName());

    private HttpUtil httpUtil = new HttpUtil();

    public String getWeChatOpenId(String openId)
    {
        String result = openId;
        if (StringUtils.isNotEmpty(openId))
        {
            String requestUrl = MessageFormat.format(WeiXinUrl.OPEN_ID_URL, WeiXinConfig.APPID, WeiXinConfig.SECRET, openId);
            LOG.info("Get OpenId Url : " + requestUrl);
            String response = httpUtil.sendHttpGet(requestUrl);
            LOG.info("Get OpenId Response : " + response);
            try
            {
                Map<String, String> userInfo = JsonUtil.stringToObject(response, Map.class);
                result = userInfo.get("openid");
            }
            catch (Exception e)
            {
                LOG.error("Get WeChat Open Id Error. ", e);
            }
        }
        return result;
    }

    public String getWeChatUserName(String openId)
    {
        String result = null;
        if (StringUtils.isNotEmpty(openId))
        {
            String requestUrl = MessageFormat.format(WeiXinUrl.USER_INFO_URL, getAccessToken(), openId);
            String response = httpUtil.sendHttpGet(requestUrl);
            try
            {
                Map<String, Object> userInfo = JsonUtil.stringToObject(response, Map.class);
                result = String.valueOf(userInfo.get("nickname"));
            }
            catch (Exception e)
            {
                LOG.error("Get WeChat User Name Error. ", e);
            }
        }
        return result;
    }

    private String getAccessToken()
    {
        String result = null;
        String requestUrl = MessageFormat.format(WeiXinUrl.ACCESS_TOKEN_URL, WeiXinConfig.APPID, WeiXinConfig.SECRET);
        String response = httpUtil.sendHttpGet(requestUrl);
        try
        {
            Token accessToken = JsonUtil.stringToObject(response, Token.class);
            result = accessToken.getAccess_token();
        }
        catch (Exception e)
        {
            LOG.error("Get Access Token Error. ", e);
        }
        return result;
    }

    private String getTicket()
    {
        String result = null;
        String requestUrl = MessageFormat.format(WeiXinUrl.TICKET_URL, getAccessToken());
        String response = httpUtil.sendHttpGet(requestUrl);
        try
        {
            Ticket ticket = JsonUtil.stringToObject(response, Ticket.class);
            result = ticket.getTicket();
        }
        catch (Exception e)
        {
            LOG.error("Get Ticket Error. ", e);
        }
        return result;
    }

    /**
     * @param url
     * @return
     */
    public WeiXinVerification getSign(String url)
    {
        long currentMills = System.currentTimeMillis() / 1000;
        String timestamp = Long.toString(currentMills);
        String nonceStr = Long.toString(currentMills);

        String ticket = getTicket();
        LOG.info("Get Sign Ticket : " + ticket);

        String str = "jsapi_ticket=" + ticket +
                "&noncestr=" + nonceStr +
                "&timestamp=" + timestamp +
                "&url=" + url;
        LOG.info("Get Sign Str : " + str);

        String signature = SHA1(str);
        LOG.info("Get Sign Signature : " + signature);

        WeiXinVerification weiXinVerification = new WeiXinVerification();
        weiXinVerification.setTimestamp(timestamp);
        weiXinVerification.setSignature(signature);
        weiXinVerification.setNonceStr(nonceStr);
        weiXinVerification.setTicket(ticket);
        return weiXinVerification;
    }

    /**
     * @return
     */
    public BaiDuLocationInfo getLocation(String lng, String lat)
    {
        LOG.info("getLocation in  lng:  " + lng + "     lat:  " + lat);
        String requestUrl = MessageFormat.format(BaiDuUrl.CONVERT_URL, lng, lat);
        String response = httpUtil.sendHttpGet(requestUrl);
        try
        {
            BaiDuLocation convert = JsonUtil.stringToObject(response, BaiDuLocation.class);
            if (convert.getError() == 0)
            {
                String x = Base64.decodeBase64(convert.getX());
                String y = Base64.decodeBase64(convert.getY());
                LOG.info("getLocation x:" + x);
                LOG.info("getLocation y:" + y);
                BaiDuLocationInfo baiDuAddress = getAddress(x, y);
                if (baiDuAddress != null)
                {
                    return baiDuAddress;
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Get getLocation Error. ", e);
        }
        return null;
    }

    /**
     * @param lng
     * @param lat
     */
    public BaiDuLocationInfo getAddress(String lng, String lat)
    {
        String requestUrl = MessageFormat.format(BaiDuUrl.ADDRESS_URL, lat, lng);
        String response = httpUtil.sendHttpGet(requestUrl);
        try
        {
            Map<String, Object> address = JsonUtil.stringToObject(response, Map.class);
            Map<String, Object> result = (Map<String, Object>) address.get("result");

            Map<String, Object> location = (Map<String, Object>) result.get("location");
            String lngValue = String.valueOf(location.get("lng"));
            String latValue = String.valueOf(location.get("lat"));

            String formattedAddress = String.valueOf(result.get("formatted_address"));

            Map<String, Object> addressComponent = (Map<String, Object>) result.get("addressComponent");
            String city = String.valueOf(addressComponent.get("city"));

            BaiDuLocationInfo baiDuLocationInfo = new BaiDuLocationInfo();
            baiDuLocationInfo.setLat(latValue);
            baiDuLocationInfo.setLng(lngValue);
            baiDuLocationInfo.setFormatted_address(formattedAddress);
            baiDuLocationInfo.setCity(city);

            return baiDuLocationInfo;
        }
        catch (Exception e)
        {
            LOG.error("Get getAddress Error. ", e);
        }
        return null;
    }

    /**
     * @param str
     * @return
     */
    private String SHA1(String str)
    {
        try
        {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1"); //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++)
            {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2)
                {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();

        }
        catch (Exception e)
        {
            LOG.error("SHA1 Error. ", e);
        }
        return null;
    }
}
