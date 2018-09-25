package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.UserStatistics;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-04-02 19:15
 **/
public interface IUserStatisticsService extends IBaseService<UserStatistics> {

    /**
     * 人气  + 1 (浏览量)
     */
    Long addPopularity(Integer userId);


    /**
     * 粉丝数目 +1
     */
    Long addFansCount(Integer userId);

    /**
     * 粉丝数目 -1
     */
    Long reduceFansCount(Integer userId);


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
    void deleteByUserIdFromRedis(Integer id);

    /**
     * 通过用户UI的获取其统计信息
     */
    UserStatistics getByUserId(int userId);

    /**
     * 根据用户ID从redis 获取其统计信息
     */
    UserStatistics getUserStatisticsFromRedis(int userId);


    /******************************************把加一减一操作换成 同步数据***********************************/
    void syncActivityCount(Integer userId);

    void syncFansCount(Integer userId);

    //同步用户统计到redis
    void syncUserStatisticsToRedis();

}
