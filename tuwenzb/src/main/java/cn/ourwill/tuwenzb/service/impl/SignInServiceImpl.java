package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.SignIn;
import cn.ourwill.tuwenzb.mapper.SignInMapper;
import cn.ourwill.tuwenzb.service.ISignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:42
 * @Description
 */
@Repository
public class SignInServiceImpl extends BaseServiceImpl<SignIn> implements ISignInService {

    @Autowired
    private SignInMapper signInMapper;

    @Override
    public List<SignIn> getByUserId(Integer userId,Integer activityId) {
        return signInMapper.getByUserId(userId,activityId);
    }

    @Override
    public List<SignIn> getByActivityId(Integer activityId,Date time) {
        return signInMapper.getByActivityId(activityId,time);
    }
}
