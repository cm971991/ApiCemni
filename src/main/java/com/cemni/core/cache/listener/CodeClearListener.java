package com.cemni.core.cache.listener;

import com.cemni.core.cache.VerificationCodeCache;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * Created by chenyu on 2017/3/11.
 */
public class CodeClearListener implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        // 服务启动、初始化时执行
        int minutes = 5;
        int second = 60;
        int timeDistance = 1000;
        // 使用定时类，每隔一段时间执行
        Timer timer = new Timer();
        timer.schedule(new ClearCodeTask(), new Date(), minutes * second * timeDistance);
    }

    private class ClearCodeTask extends TimerTask
    {
        public void run()
        {
            // 每隔一段时间执行
            clearRandomMap();
        }
    }

    public void clearRandomMap()
    {
        Long now = System.currentTimeMillis() / 1000;

        for (Map.Entry<String, Long> entry : VerificationCodeCache.getTimeMap().entrySet())
        {
            if (now - entry.getValue() > 5 * 60)
            {
                VerificationCodeCache.delCode(entry.getKey());
                VerificationCodeCache.delTime(entry.getKey());
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {

    }
}
