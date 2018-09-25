package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.UserBlack;
import cn.ourwill.huiyizhan.mapper.UserBlackMapper;
import cn.ourwill.huiyizhan.service.IUserBlackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-04-10 10:29
 **/
@Service
public class UserBlackServiceImpl extends BaseServiceImpl<UserBlack> implements IUserBlackService {

    @Autowired
    private UserBlackMapper userBlackMapper;

    @Override
    public Integer editBlack(UserBlack userBlack) {
        return userBlackMapper.updateByUserId(userBlack);

    }

    @Override
    public Integer delete(List<Integer> ids) {
        return userBlackMapper.deleteByUserIds(ids);
    }

    @Override
    public List<UserBlack> getBlacks(String userName) {

        if (StringUtils.isEmpty(userName)) {
            return userBlackMapper.findAll();
        } else {
            return userBlackMapper.findByUserId(userName);
        }
    }
}
