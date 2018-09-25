package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.UserBlack;

import java.util.List;

public interface IUserBlackService extends IBaseService<UserBlack> {

    Integer editBlack(UserBlack userBlack);

    Integer delete(List<Integer> ids);

    List<UserBlack> getBlacks(String userName);
}
