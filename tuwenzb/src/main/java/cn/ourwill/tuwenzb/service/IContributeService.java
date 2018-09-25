package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.Contribute;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IContributeService extends IBaseService<Contribute>{

    Integer save(Contribute contribute);

    Integer update(Contribute contribute);

    List<Contribute> findAll();

    List<Contribute>selectByUserId(Integer userId);

    Contribute selectOneByUserId(Integer userId);

    List<Contribute> getContributeList(Map<String,Object> param);

    Integer updateStatus(Integer id, Integer status, String feedback) throws Exception;

    Integer batchCheck(Integer checkStatus,List<Integer> list,String feedBack);

    Integer getPromotionNum(Integer userId);
}
