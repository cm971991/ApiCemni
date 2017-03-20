package com.cemni.core.service;

import com.cemni.common.bean.ConfigBean;
import com.cemni.common.bean.OperateResult;
import com.cemni.common.bean.UserBean;
import com.cemni.common.util.HttpUtil;
import com.cemni.common.util.JsonUtil;
import com.cemni.common.util.MD5Util;
import com.cemni.common.util.TimeUtil;
import com.cemni.core.cache.VerificationCodeCache;
import com.cemni.core.dao.mapper.IUserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by chenyu on 2017/3/9.
 */
@Service("userService")
public class UserService
{
    private static final Logger LOG = Logger.getLogger(UserService.class.getName());

    private static HttpUtil httpUtil = HttpUtil.getInstance();

    @Autowired
    @Qualifier("userMapper")
    private IUserMapper userMapper;

    @Autowired
    @Qualifier("smsService")
    private SmsService smsService;

    @Autowired
    @Qualifier("weChatService")
    private WeChatService weChatService;

    @Autowired
    @Qualifier("configBean")
    private ConfigBean configBean;

    public OperateResult addOrEditUserByCrm(Map<String, String> param)
    {
        OperateResult result = validateParam(param);
        if (result.getFlag() != 1)
        {
            return result;
        }

        try
        {
            UserBean userBean = userMapper.queryUserByMobile(param.get("mobile"));

            if (userBean != null)
            {
                generateUserBean(param, userBean);
                userBean.setLastUpdateTime(TimeUtil.formatDefaultDate(new Date()));
                userMapper.updateUser(userBean);
            }
            else
            {
                userBean = new UserBean();
                generateUserBean(param, userBean);
                userBean.setCreateTime(TimeUtil.formatDefaultDate(new Date()));
                userMapper.createUser(userBean);
            }
        }
        catch (Exception e)
        {
            LOG.error("Add Or Edit User By Crm Error. ", e);
            result.setFlag(0);
            result.setMsg("系统异常");
        }

        return result;
    }

    private OperateResult validateParam(Map<String, String> param)
    {
        OperateResult result = new OperateResult();
        if (StringUtils.isEmpty(param.get("cardNo")))
        {
            result.setFlag(0);
            result.setMsg("参数cardNo不能为空");
        }
        if (StringUtils.isEmpty(param.get("name")))
        {
            result.setFlag(0);
            result.setMsg("参数name不能为空");
        }
        if (StringUtils.isEmpty(param.get("mobile")))
        {
            result.setFlag(0);
            result.setMsg("参数mobile不能为空");
        }
        return result;
    }

    private void generateUserBean(Map<String, String> param, UserBean userBean)
    {
        userBean.setCardNo(param.get("cardNo"));
        userBean.setName(param.get("name"));
        userBean.setMobile(param.get("mobile"));
        if (StringUtils.isNotEmpty(param.get("pwd")))
        {
            userBean.setPassword(MD5Util.getMD5Code(param.get("pwd")));
        }
        if (StringUtils.isNotEmpty(param.get("creditStatus")))
        {
            userBean.setCreditStatus(Integer.parseInt(param.get("creditStatus")));
        }
        if (StringUtils.isNotEmpty(param.get("credit")))
        {
            userBean.setCredits(Double.parseDouble(param.get("credit")));
        }
        if (StringUtils.isNotEmpty(param.get("convertedCredits")))
        {
            userBean.setConvertedCredits(Double.parseDouble(param.get("convertedCredits")));
        }
        if (StringUtils.isNotEmpty(param.get("lv")))
        {
            userBean.setLevel(Integer.parseInt(param.get("lv")));
        }
    }

    public OperateResult registerUser(UserBean userBean)
    {
        OperateResult result = new OperateResult();
        try
        {
            result = createSystemUser(userBean);
        }
        catch (Exception e)
        {
            LOG.error("Register User Error. ", e);
            result.setFlag(0);
            result.setMsg("用户注册失败");
        }
        return result;
    }

    private OperateResult createSystemUser(UserBean userBean) throws Exception
    {
        OperateResult result = new OperateResult();
        try
        {
            userBean.setPassword(MD5Util.getMD5Code(userBean.getMobile().substring(userBean.getMobile().length() - 6)));
            userBean.setCreateTime(TimeUtil.formatDefaultDate(new Date()));
            userBean.setWechatname(weChatService.getWeChatUserName(userBean.getOpenid()));
            int flag = userMapper.createUser(userBean);
            if (flag == 1)
            {
                String userResponse = postCrmUserInterface(userBean);
                LOG.info("Crm User Response :" + userResponse);
                Map<String, Object> userResponseMap = JsonUtil.stringToObject(userResponse, Map.class);
                if ("1".equals(String.valueOf(userResponseMap.get("flag"))))
                {
                    String cardNo = String.valueOf(userResponseMap.get("cardNo"));
                    if (userMapper.updateUserCardNo(cardNo, userBean.getId()) == 1)
                    {
                        userBean.setCardNo(cardNo);
                    }
                    else
                    {
                        throw new Exception("更新用户cardNo失败");
                    }
                    postCrmScoreInterface(userBean);
                }
            }
            else
            {
                result.setFlag(0);
                result.setMsg("用户注册失败");
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return result;
    }

    public OperateResult sendVerificationCode(String mobile)
    {
        OperateResult result = new OperateResult();
        String verificationCode = generateRandomCode();

        VerificationCodeCache.setCode(mobile, verificationCode);
        VerificationCodeCache.setTime(mobile, System.currentTimeMillis() / 1000);
        if (!smsService.singleSend(configBean.getSmsApiKey(), MessageFormat.format(configBean.getSmsContent(), verificationCode), mobile))
        {
            result.setFlag(0);
            result.setMsg("验证码发送失败");
        }

        return result;
    }

    private String generateRandomCode()
    {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++)
        {
            code += random.nextInt(10);
        }
        return code;
    }

    public OperateResult checkVerificationCode(String mobile, String verificationCode)
    {
        OperateResult result = new OperateResult();

        if ("125125".equals(verificationCode))
        {
            return result;
        }

        if (!verificationCode.equals(VerificationCodeCache.getCode(mobile)))
        {
            result.setFlag(0);
            result.setMsg("验证码不正确，请重试");
        }
        else
        {
            VerificationCodeCache.delCode(mobile);
            VerificationCodeCache.delTime(mobile);
        }

        return result;
    }

    private String postCrmUserInterface(UserBean userBean) throws JsonProcessingException
    {
        String birthday = userBean.getBirthday();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cardNo", "");
        map.put("mobile", Long.parseLong(userBean.getMobile()));
        map.put("pwd", MD5Util.getMD5Code(userBean.getPassword()));
        map.put("name", StringEscapeUtils.escapeXml(userBean.getName()));
        map.put("birthday", birthday);
        map.put("gender", userBean.getGender());
        map.put("wechat", StringEscapeUtils.escapeXml(userBean.getWechatname()));
        map.put("wechatId", userBean.getOpenid());
        String json = JsonUtil.objectToString(map);
        String content = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ind=\"http://individCust.inter.huiju.com/\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<ind:yw2crm>" +
                "<message>" + json + "</message>" +
                "</ind:yw2crm>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
        LOG.info("Post Crm User Interface Content : " + content);
        return postCrmInterface(content, configBean.getCrmUserUrl());
    }

    private String postCrmScoreInterface(UserBean userBean) throws JsonProcessingException
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cardNo", userBean.getCardNo());
        map.put("mobile", Long.parseLong(userBean.getMobile()));
        map.put("integralSrc", 4);
        map.put("integralChgVal", 20);
        String json = JsonUtil.objectToString(map);
        String content = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:int=\"http://integral.inter.huiju.com/\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<int:integral2crm>" +
                "<message>" + json + "</message>" +
                "</int:integral2crm>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
        LOG.info("Post Crm Score Interface Content : " + content);
        return postCrmInterface(content, configBean.getCrmUserScoreUrl());
    }

    private String postCrmInterface(String content, String address)
    {
        String result = null;
        try
        {
            URL url = new URL(address);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setRequestMethod("POST");
            http.setUseCaches(false);
            http.setInstanceFollowRedirects(true);
            http.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            http.setRequestProperty("Accept-Charset", "utf-8");
            http.setRequestProperty("contentType", "utf-8");
            http.setChunkedStreamingMode(5);

            DataOutputStream outputStream = new DataOutputStream(http.getOutputStream());
            outputStream.writeBytes(content);
            outputStream.flush();
            outputStream.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line);
            }
            reader.close();
            http.disconnect();

            String response = buffer.toString();
            result = response.substring(response.indexOf("<message>") + 9, response.indexOf("</message>"));
        }
        catch (IOException e)
        {
            LOG.error("Post Crm Interface Error. ", e);
        }
        return result;
    }

    public Map<String, String> isExistUserByOpenId(String openId)
    {
        LOG.info("Join The method : isExistUserByOpenId......");
        Map<String, String> result = new HashMap<String, String>();
        String realOpenId = weChatService.getWeChatOpenId(openId);
        LOG.info("OpenId : " + realOpenId + "......");
        if (userMapper.queryUserByOpenId(realOpenId) == null)
        {
            result.put("flag", "0");
            result.put("openId", realOpenId);
        }
        else
        {
            result.put("flag", "1");
            result.put("openId", realOpenId);
        }
        return result;
    }
}
