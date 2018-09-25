package cn.ourwill.huiyizhan.service.search;

import cn.ourwill.huiyizhan.entity.SearchBean;
import cn.ourwill.huiyizhan.mapper.search.SearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-07 09:56
 **/
@Service
public class SearchService implements ISearchService {
    @Autowired
    SearchMapper searchMapper;

    @Override
    public SearchBean getSearchBean(Integer activityId) {
        return searchMapper.getSearchBean(activityId);
    }

    @Override
    public List<SearchBean> getSearchBeanByUserId(Integer userId) {
        return searchMapper.getSearchBeanByUserId(userId);
    }

    @Override
    public List<SearchBean> getAllSearchBean() {
        return searchMapper.getAllSearchBean();
    }
}
