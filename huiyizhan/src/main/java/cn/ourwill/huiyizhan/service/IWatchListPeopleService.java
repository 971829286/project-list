package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.PeopleDynamic;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.entity.UserBasicInfo;
import cn.ourwill.huiyizhan.entity.WatchListPeople;

import java.util.List;

/**
 * 描述： 关注人接口
 *
 * @author liupenghao
 * @create 2018-03-28 18:00
 **/
public interface IWatchListPeopleService extends IBaseService<WatchListPeople> {


    /**
     * <pre>
     *  查询 当前用户是否关注了此人
     *      1: yes
     *      0: no
     * </pre>
     */
    public Boolean checkWatchStatus(Integer watchedUserId, Integer userId);


    /**
     * 添加关注
     */
    public int addWatch(Integer watchedUserId, Integer userId);

    /**
     * 取消关注
     */
    int cancelWatchById(Integer watchedUserId, Integer userId);


    /**
     * 获取当前用户关注的人的信息
     */
    List<UserBasicInfo> getWatchPeopleInfo(Integer userId);

    /**
     * 获取当前用户粉丝的信息
     */
    List<UserBasicInfo> getFansInfo(Integer userId);

    /**
     * <prep>
     * 获取当前用户信息
     * 及关注的人的信息
     * 及粉丝的信息
     * </prep>
     */
    public User getWatchAll(Integer userId);


    /**
     * 是否互粉
     */
    public boolean isMutualfans(Integer firstId, Integer TwoId);


    /**
     * 根据当前登录用户 ,
     * 获取关注动态
     */
    List<PeopleDynamic> getPeopleDynamic(Integer userId);

    /**
     * 根据id 获取粉丝数量
     * @param userId
     * @return
     */
    Integer getFansCount(int userId);

    /**
     * 根据id 获取 关注了多少人
     * @param userId
     * @return
     */
    Integer getWatchCount(int userId);
}
