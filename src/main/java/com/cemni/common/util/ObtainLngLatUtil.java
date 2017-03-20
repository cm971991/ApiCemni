package com.cemni.common.util;

import com.cemni.common.bean.StoreBean;
import com.cemni.common.bean.lnglat.LngLatBean;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by chenyu on 2017/3/9.
 */
public class ObtainLngLatUtil
{
    private static final Logger LOG = Logger.getLogger(ObtainLngLatUtil.class.getName());

    public static void generateStoreLngAndLat(StoreBean storeBean) throws IOException
    {
        String url = "http://api.map.baidu.com/geocoder/v2/?address=" + storeBean.getAddr() + "&output=json&ak=cEsmFkPVSXtzgw1d4VTC6h7s";
        String json = getLngAndLatFromApi(url);

        LngLatBean lngLatBean = JsonUtil.stringToObject(json, LngLatBean.class);
        if (lngLatBean.getStatus().equals(0))
        {
            String lngValue = String.valueOf(lngLatBean.getResult().getLocation().getLng());
            String latValue = String.valueOf(lngLatBean.getResult().getLocation().getLat());

            double lng = Double.parseDouble(lngValue.substring(0, lngValue.lastIndexOf(".") + 7));
            double lat = Double.parseDouble(latValue.substring(0, latValue.lastIndexOf(".") + 7));

            storeBean.setLng(lng);
            storeBean.setLat(lat);
        }
        else
        {
            LOG.error("Generate Store Lng And Lat Error. " + json);
        }
    }

    public static String getLngAndLatFromApi(String url)
    {
        StringBuffer json = new StringBuffer();
        BufferedReader in = null;
        try
        {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                json.append(inputLine);
            }
        }
        catch (Exception e)
        {
            LOG.error("Get Lng And Lat From Api Error. ", e);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    LOG.error("Close BufferedReader Error. ", e);
                }
            }
        }
        return json.toString();
    }
}
