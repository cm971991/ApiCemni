package com.cemni.common.bean;

/**
 * Created by chenyu on 2017/3/8.
 */
public class OperateResult
{
    private int flag;

    private String msg;

    public OperateResult()
    {
        flag = 1;
        msg = "操作成功";
    }

    public int getFlag()
    {
        return flag;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
