package com.cemni.common.bean;

/**
 * Created by chenyu on 2017/3/9.
 */
public class ConfigBean
{
    private String crmUserUrl;

    private String crmUserScoreUrl;

    private String smsRequestUrl;

    private String smsApiKey;

    private String smsContent;

    public String getCrmUserUrl()
    {
        return crmUserUrl;
    }

    public void setCrmUserUrl(String crmUserUrl)
    {
        this.crmUserUrl = crmUserUrl;
    }

    public String getCrmUserScoreUrl()
    {
        return crmUserScoreUrl;
    }

    public void setCrmUserScoreUrl(String crmUserScoreUrl)
    {
        this.crmUserScoreUrl = crmUserScoreUrl;
    }

    public String getSmsRequestUrl()
    {
        return smsRequestUrl;
    }

    public void setSmsRequestUrl(String smsRequestUrl)
    {
        this.smsRequestUrl = smsRequestUrl;
    }

    public String getSmsApiKey()
    {
        return smsApiKey;
    }

    public void setSmsApiKey(String smsApiKey)
    {
        this.smsApiKey = smsApiKey;
    }

    public String getSmsContent()
    {
        return smsContent;
    }

    public void setSmsContent(String smsContent)
    {
        this.smsContent = smsContent;
    }
}
