package com.cemni.common.bean;

/**
 * Created by Administrator on 2017/3/13.
 */
public class BaiDuLocationInfo
{
    private String lng;

    private String lat;

    private String formatted_address;

    private String city;

    public String getLng()
    {
        return lng;
    }

    public void setLng(String lng)
    {
        this.lng = lng;
    }

    public String getLat()
    {
        return lat;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getFormatted_address()
    {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address)
    {
        this.formatted_address = formatted_address;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }
}

