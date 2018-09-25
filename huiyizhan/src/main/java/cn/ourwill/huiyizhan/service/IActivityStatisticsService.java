package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.ActivityStatistics;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-04-02 19:15
 **/
public interface IActivityStatisticsService extends IBaseService<ActivityStatistics> {

    /**
     * 添加收藏 收藏数目+1
     */
    Long addCollectNumber(Integer activityId);

    /**
     * 取消收藏 取消数目-110
     */
    Long reduceCollectNumber(Integer activityId);


    /**
     * 会议
     * 浏览量+ 1
     */
    Long addWatch(Integer activityId);


    /**
     * 留言+ 1
     */
    Long addLeaveMessage(Integer activityId);


    /**
     * 同步到mysql
     */
    void syncRedisToMySQL();


    /**
     * 删除redis 中记录此会议的统计信息
     */
    void deleteAllFromRedis();

    /**
     * 删除redis 中会议的所有统计信息
     */
    void deleteByActivityIdFromRedis(Integer id);

    /**
     * 根据会议ID从redis 获取其统计信息
     */
    public ActivityStatistics getActivityStatisticsFromRedis(Integer activityId);

    /**
     * @param statistics
     */
    public void updateActivityHash(ActivityStatistics statistics);

    /**
     * 同步收藏数量
     * @param id
     */
    public void syncCollectCount(Integer id);
}
