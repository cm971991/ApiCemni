package com.cemni.common.bean;

/**
 * Created by Administrator on 2017/3/8.
 */
public class WeiXinUrl
{
    public static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}";

    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    public static final String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi";

    public static final String OPEN_ID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";
}
