package cn.ourwill.huiyizhan.service.search;

import cn.ourwill.huiyizhan.entity.SearchBean;

import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-07 11:12
 **/
public interface IElasticSearchService {
    //delete&importAll
    boolean importAll();

    //insert
    boolean insert(SearchBean searchBean);
    //inserts
    boolean inserts(List<SearchBean> searchBeans);
    //query&delete
    boolean deleteByActivityId(Integer activityId);

    boolean deleteByUserId(Integer userId);

//    boolean updateByActivityId(Integer activityId);
//
//    boolean updateByUserId(Integer userId);

    boolean deleteAll();
}
