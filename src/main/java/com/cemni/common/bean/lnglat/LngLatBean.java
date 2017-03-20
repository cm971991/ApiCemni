package com.cemni.common.bean.lnglat;

/**
 * Created by chenyu on 2017/3/9.
 */
public class LngLatBean
{
    private Integer status;

    private Result result;

    private String msg;

    private Object results;

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Result getResult()
    {
        return result;
    }

    public void setResult(Result result)
    {
        this.result = result;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Object getResults()
    {
        return results;
    }

    public void setResults(Object results)
    {
        this.results = results;
    }
}