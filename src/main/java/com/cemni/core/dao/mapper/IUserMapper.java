package com.cemni.core.dao.mapper;

import com.cemni.common.bean.UserBean;
import com.cemni.core.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenyu on 2017/3/9.
 */
@Service("userMapper")
public interface IUserMapper extends IMyBatisMapper
{
    List<UserBean> queryAllUsers();

    UserBean queryUserByMobile(@Param(value = "mobile") String mobile);

    UserBean queryUserByOpenId(@Param(value = "openId") String openId);

    int createUser(@Param(value = "info") UserBean userBean);

    int updateUser(@Param(value = "info") UserBean userBean);

    int updateUserCardNo(@Param(value = "cardNo") String cardNo, @Param(value = "id") int id);
}
