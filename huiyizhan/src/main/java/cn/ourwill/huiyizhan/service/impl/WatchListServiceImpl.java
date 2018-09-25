package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.ActivityDynamic;
import cn.ourwill.huiyizhan.entity.WatchList;
import cn.ourwill.huiyizhan.mapper.WatchListMapper;
import cn.ourwill.huiyizhan.service.IActivityStatisticsService;
import cn.ourwill.huiyizhan.service.IWatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 会议关注 接口
 */
@Service
public class WatchListServiceImpl extends BaseServiceImpl<WatchList> implements IWatchListService {

    @Autowired
    private WatchListMapper watchListMapper;

    @Autowired
    private IActivityStatisticsService activityStatisticsService;

    @Override
    public List<Activity> getWatchList(Integer userId, Integer status) {
        List<Activity> activities = null;
        if (status - 2 == 0)
            activities = watchListMapper.getWatchActivityOver(userId, new Date());
        else
            activities = watchListMapper.getWatchActivityListByIssue(userId, status, new Date());
        activities.stream().forEach(entity -> {
            entity.setActivityStatistics(activityStatisticsService.getActivityStatisticsFromRedis(entity.getId()));
        });
        return activities;

    }

    @Override
    public Boolean checkWatchStatus(Integer activityId, Integer userId) {
        List<WatchList> watchLists = watchListMapper.selectByActivityAndUser(activityId, userId);
        if (watchLists != null && watchLists.size() > 0) {
            return true;//已关注
        }
        return false;//未关注
    }

    @Override
    public int addWatch(Integer activityId, Integer userId) {
        WatchList watchList = new WatchList();
        watchList.setActivityId(activityId);
        watchList.setUserId(userId);
        watchList.setWatchDate(new Date());
        return watchListMapper.save(watchList);
    }

    @Override
    public int cancelWatchById(Integer activityId, Integer userId) {
        WatchList watchList = new WatchList();
        watchList.setActivityId(activityId);
        watchList.setUserId(userId);
        return watchListMapper.delete(watchList);
    }

    @Override
    public Integer getCollectCountByActivityId(Integer activityId) {
        return watchListMapper.selectCountByActivity(activityId);
    }

    @Override
    public List<ActivityDynamic> getActivityDynamic(Integer userId) {
        List<ActivityDynamic> activityDynamics = watchListMapper.getActivityDynamic(userId);
        activityDynamics.stream().forEach(entity -> {
            if (userId.equals(entity.getUserId())) {
                entity.setStatus(0);
            } else {
                entity.setStatus(1);
            }
        });
        return activityDynamics;
    }


}
