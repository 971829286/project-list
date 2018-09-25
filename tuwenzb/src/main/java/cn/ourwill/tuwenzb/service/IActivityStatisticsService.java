package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.ActivityStatistics;

/**
 * 　ClassName:IUserService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:51
 */

public interface IActivityStatisticsService extends IBaseService<ActivityStatistics>{
    //直播点赞
    public Integer addLikeNumber(Integer activityId);
    //增加评论数
    public Integer addCommentNumber(Integer activityId);
    //减少评论数
    public Integer minusCommentNumber(Integer activityId);
    //增加参与人数
    public Integer addParticipantsNumber(Integer activityId);
    //根据ActivityId查找
    public ActivityStatistics getByActivityId(Integer activityId);
    //按活动id删除
    public Integer deleteByActivityId(Integer activityId);

    //直播点赞(redis)
    public Long addLikeNumberRedis(Integer activityId,Integer number);
    //增加评论数(redis)
    public Long addCommentNumberRedis(Integer activityId);
    //减少评论数(redis)
    public Long minusCommentNumberRedis(Integer activityId);
    //刷新评论数
    public void refushCommentNumberRedis(Integer activityId);
    //增加参与人数(redis)
    public Long addParticipantsNumberRedis(Integer activityId,Integer number);
    //增加在线数(redis)
    public Long addOnlineNumberRedis(Integer activityId);
    //减少在线数(redis)
    public Long minusOnlineNumberRedis(Integer activityId);

    public void updateStatistics(Integer activityId,String type,Integer num);

    public void syncRedisToMySQL();

    public void initializeOnlineNum();

    void deleteRedis();
    //增加分享数
    public Long addShareNumberRedis(Integer activityId);


    //增加真实分享数
    public Long addRealShareNumberRedis(Integer activityId);

    //直播真实点赞(redis)
    public Long addRealLikeNumberRedis(Integer activityId);

    //增加真实评论数(redis)
    public Long addRealCommentNumberRedis(Integer activityId);

    //增加真实参与人数(redis)
    public Long addRealParticipantsNumberRedis(Integer activityId);

    public Long minusRealCommentNumberRedis(Integer activityId);

    public Long addTempLikeNumberRedis(Integer activityId,Integer number);
    public Long addTempParticipantsNumberRedis(Integer activityId,Integer number);

    public Long getTempLikeNumberRedis(Integer activityId);

    public Long getTempParticipantsNumberRedis(Integer activityId);


}
