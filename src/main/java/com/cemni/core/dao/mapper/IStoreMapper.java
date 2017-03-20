package com.cemni.core.dao.mapper;

import com.cemni.common.bean.StoreBean;
import com.cemni.core.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenyu on 2017/3/8.
 */
@Service("storeMapper")
public interface IStoreMapper extends IMyBatisMapper
{
    List<StoreBean> queryAllStore();

    StoreBean queryStoreByStoreNo(@Param(value = "storeNo") String storeNo);

    int createStore(@Param(value = "info") StoreBean storeBean);

    int updateStore(@Param(value = "info") StoreBean storeBean);
}
