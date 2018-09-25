package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.ActivityDynamic;
import cn.ourwill.huiyizhan.entity.WatchList;

import java.util.List;

/**
 * 　ClassName:IUserService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:51
 */

public interface IWatchListService extends IBaseService<WatchList> {
    public List<Activity> getWatchList(Integer userId, Integer status);

    public Boolean checkWatchStatus(Integer activityId, Integer userId);


    public int addWatch(Integer activityId, Integer userId);

    int cancelWatchById(Integer activityId, Integer userId);

    List<ActivityDynamic> getActivityDynamic(Integer userId);

    Integer getCollectCountByActivityId(Integer activityId);
}
