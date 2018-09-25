package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.BlackList;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/24 0024 16:46
 * @Version1.0
 */
public interface IBlackListService extends IBaseService<BlackList>{

    @Override
    Integer save(BlackList entity);

    @Override
    Integer update(BlackList entity);

    Integer getOperantByUserId(Integer userId);

    List<BlackList> selectByParams(Map param);

    Integer unlock(List<Integer> ids);
}
