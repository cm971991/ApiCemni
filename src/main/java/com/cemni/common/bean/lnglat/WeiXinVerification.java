package com.cemni.common.bean.lnglat;

/**
 * Created by Administrator on 2017/3/10.
 */
public class WeiXinVerification
{
    private String signature;
    private String timestamp;
    private String nonceStr;
    private String ticket;

    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getNonceStr()
    {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr)
    {
        this.nonceStr = nonceStr;
    }

    public String getTicket() {return ticket;}

    public void setTicket(String ticket) {this.ticket = ticket;}
}
