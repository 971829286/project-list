package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.WatchList;

import java.util.List;

/**
 * 　ClassName:IUserService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:51
 */

public interface IWatchListService extends IBaseService<WatchList> {
    public List<WatchList> getWatchList(Integer userId,Integer photoLive);
    public Boolean checkWatchStatus(Integer activityId,Integer userId);
    public int deleteByActivityAndUser(Integer activityId,Integer userId);
}
