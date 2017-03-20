package com.cemni.common.main;

import com.cemni.common.util.JsonUtil;
import com.cemni.common.util.ObtainLngLatUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by chenyu on 2017/3/14.
 */
public class HttpTestMain
{
    public static void main(String[] args) throws Exception
    {
        String json = "{\"birthday\":\"2017-03-16\",\"gender\":1,\"mobile\":13776628520,\"name\":\"&#38472;&#26126;\",\"wechat\":\"&#36831;&#21040;&#21315;&#24180;&#12289;\",\"wechatId\":\"oOJUJjxDwhgypfM5G53ow4vKcs\",\"pwd\":\"16ca9b467d388d2163077c47e5a1137f\",\"cardNo\":\"\"}";

        String str = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ind=\"http://individCust.inter.huiju.com/\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<ind:yw2crm>" +
                "<message>" +
                json +
                "</message>" +
                "</ind:yw2crm>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        String result = "{\"cardNo\":\"C800500115\",\"flag\":1,\"msg\":\"鎿嶄綔鎴愬姛\"}";
        Map<String, Object> map = JsonUtil.stringToObject(result, Map.class);
        System.exit(0);

        String address = "http://10.0.0.101:8080/IndividCustWsBeanService/IndividCustWsBean?wechar2crm";
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
        outputStream.writeBytes(str);
        outputStream.flush();
        outputStream.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String line;
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null)
        {
            buffer.append(line);
        }
        reader.close();
        http.disconnect();
        System.out.println(buffer.toString());

        String response = buffer.toString();
        String message = response.substring(response.indexOf("<message>") + 9, response.indexOf("</message>"));
        System.out.println(message);
    }
}
