package com.cemni.common.bean.lnglat;

/**
 * Created by chenyu on 2017/3/9.
 */
public class Result
{
    private Location location;

    private Integer precise;

    private Integer confidence;

    private String level;

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public Integer getPrecise()
    {
        return precise;
    }

    public void setPrecise(Integer precise)
    {
        this.precise = precise;
    }

    public Integer getConfidence()
    {
        return confidence;
    }

    public void setConfidence(Integer confidence)
    {
        this.confidence = confidence;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }
}
