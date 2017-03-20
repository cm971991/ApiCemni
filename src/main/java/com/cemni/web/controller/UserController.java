package com.cemni.web.controller;

import com.cemni.common.bean.OperateResult;
import com.cemni.common.bean.UserBean;
import com.cemni.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by chenyu on 2017/3/8.
 */
@Controller("userController")
public class UserController
{
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @RequestMapping(value = "crmUserSynchronous", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public OperateResult crmUserSynchronous(@RequestBody Map<String, String> param)
    {
        return userService.addOrEditUserByCrm(param);
    }

    @RequestMapping(value = "registerUser", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public OperateResult registerUser(@RequestBody UserBean userBean)
    {
        return userService.registerUser(userBean);
    }

    @RequestMapping(value = "sendVerificationCode", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public OperateResult sendVerificationCode(@RequestBody Map<String, String> param)
    {
        String mobile = param.get("telephone");
        return userService.sendVerificationCode(mobile);
    }

    @RequestMapping(value = "checkVerificationCode", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public OperateResult checkVerificationCode(@RequestBody Map<String, String> param)
    {
        String mobile = param.get("telephone");
        String verificationCode = param.get("verificationCode");
        return userService.checkVerificationCode(mobile, verificationCode);
    }

    @RequestMapping(value = "isExistUserByOpenId", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Map<String, String> isExistUserByOpenId(@RequestBody Map<String, String> param)
    {
        String openId = param.get("openId");
        return userService.isExistUserByOpenId(openId);
    }
}
