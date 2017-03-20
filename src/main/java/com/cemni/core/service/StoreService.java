package com.cemni.core.service;

import com.cemni.common.bean.OperateResult;
import com.cemni.common.bean.StoreBean;
import com.cemni.common.util.JsonUtil;
import com.cemni.common.util.ObtainLngLatUtil;
import com.cemni.common.util.TimeUtil;
import com.cemni.core.dao.mapper.IStoreMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by chenyu on 2017/3/8.
 */
@Service("storeService")
public class StoreService
{
    private static final Logger LOG = Logger.getLogger(StoreService.class.getName());

    @Autowired
    @Qualifier("storeMapper")
    private IStoreMapper storeMapper;

    public List<StoreBean> queryAllStores()
    {
        return storeMapper.queryAllStore();
    }

    public OperateResult addOrEditStore(String json)
    {
        LOG.info("Add Or Edit Store Json : " + json);
        OperateResult result = new OperateResult();

        try
        {
            StoreBean storeBean = JsonUtil.stringToObject(json, StoreBean.class);
            ObtainLngLatUtil.generateStoreLngAndLat(storeBean);
            storeBean.setLastUpdateTime(TimeUtil.formatDefaultDate(new Date()));
            if (storeMapper.queryStoreByStoreNo(storeBean.getStoreNo()) != null)
            {
                storeMapper.updateStore(storeBean);
            }
            else
            {
                storeMapper.createStore(storeBean);
            }
        }
        catch (Exception e)
        {
            LOG.error("Create Store Error. ", e);
            result.setFlag(0);
            result.setMsg("系统异常");
        }

        return result;
    }
}
