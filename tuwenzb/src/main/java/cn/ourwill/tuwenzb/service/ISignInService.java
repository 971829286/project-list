package cn.ourwill.tuwenzb.service;


import cn.ourwill.tuwenzb.entity.SignIn;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:41
 * @Description 签到
 */
public interface ISignInService extends IBaseService<SignIn>{
    List<SignIn> getByUserId(Integer userId, Integer activityId);

    List<SignIn> getByActivityId(Integer activityId, Date time);
}
