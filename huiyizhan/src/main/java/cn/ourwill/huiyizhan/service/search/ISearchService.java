package cn.ourwill.huiyizhan.service.search;

import cn.ourwill.huiyizhan.entity.SearchBean;

import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-07 10:06
 **/
public interface ISearchService {
    SearchBean getSearchBean(Integer activityId);

    List<SearchBean> getSearchBeanByUserId(Integer userId);

    List<SearchBean> getAllSearchBean();
}
