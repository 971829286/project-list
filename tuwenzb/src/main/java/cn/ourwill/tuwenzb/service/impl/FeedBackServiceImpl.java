package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.Feedback;
import cn.ourwill.tuwenzb.mapper.FeedBackMapper;
import cn.ourwill.tuwenzb.service.IFeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-06-05 15:25
 **/
@Service
public class FeedBackServiceImpl implements IFeedBackService {

    @Autowired
    private FeedBackMapper feedBackMapper;

    //保存
    public Integer save(Feedback feedback) {
        return feedBackMapper.save(feedback);
    }
}
