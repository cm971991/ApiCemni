package com.cemni.web.controller;

import com.cemni.common.bean.OperateResult;
import com.cemni.common.bean.StoreBean;
import com.cemni.core.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by chenyu on 2017/3/8.
 */
@Controller("storeController")
public class StoreController
{
    @Autowired
    @Qualifier("storeService")
    private StoreService storeService;

    @RequestMapping(value = "generateStoreInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public OperateResult generateStoreInfo(@RequestBody String json)
    {
        return storeService.addOrEditStore(json);
    }

    @RequestMapping(value = "queryAllStores", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public List<StoreBean> queryAllStores()
    {
        return storeService.queryAllStores();
    }
}
