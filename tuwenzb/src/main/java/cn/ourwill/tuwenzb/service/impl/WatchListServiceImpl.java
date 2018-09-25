package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.WatchList;
import cn.ourwill.tuwenzb.mapper.WatchListMapper;
import cn.ourwill.tuwenzb.service.IWatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WatchListServiceImpl extends BaseServiceImpl<WatchList> implements IWatchListService {

    @Autowired
    public WatchListMapper watchListMapper;
    @Override
    public List<WatchList> getWatchList(Integer userId,Integer photoLive) {
        return watchListMapper.getWatchList(userId,photoLive);
    }

    @Override
    public Boolean checkWatchStatus(Integer activityId, Integer userId) {
        List<WatchList> watchLists = watchListMapper.selectByActivityAndUser(activityId,userId);
        if(watchLists!=null&&watchLists.size()>0){
            return true;//已关注
        }
        return false;//未关注
    }

    @Override
    public int deleteByActivityAndUser(Integer activityId, Integer userId) {
        Map param = new HashMap<>();
        param.put("activityId",activityId);
        param.put("userId",userId);
        return watchListMapper.delete(param);
    }
}
