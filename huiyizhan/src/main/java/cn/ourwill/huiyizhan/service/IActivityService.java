package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.ActivityType;
import cn.ourwill.huiyizhan.entity.User;

import java.util.HashMap;
import java.util.List;

/**
 * 会议站
 * 会议服务
 *
 * @author uusao
 * @create 2018-03-21 10:36
 **/
public interface IActivityService extends IBaseService<Activity> {

    boolean checkOwnerOrAdmin(Integer activityId, User user);

    /**
     * 获取 会议列表
     *
     * @return
     */
    List<Activity> getActivityList();


    /**
     * 获取 会议详情 （包含 联系人 、嘉宾、合作伙伴、日程、车票）
     *
     * @param id
     * @return
     */
    Activity getDetailById(Integer id, User loginUser);


    /**
     * @param id
     * @return
     */
    List<Activity> getByUserId(Integer id);

    /**
     * 获取我参与的会议
     *
     * @param user
     * @return
     */
    List<Activity> getActivityJoin(User user, boolean isValid);

    /**
     * 根据用户id ,获取其发布的 所有会议id
     */
    List<Integer> getAllIdByUserId(Integer id);

    /**
     * 设置是否热门
     */
    void setIsHot(int id, int isHot);

    /**
     * 设置是否最近
     */
    void setIsRecent(int id, int isRecent);

    /**
     * 获取 热门列表的会议
     */
    List<Activity> getHotList();

    /**
     * 更新热门列表
     */
    Integer updateHotSort(List<Integer> activityIds);


    /**
     * 获取 热门列表的会议
     */
    List<Activity> getRecentList();

    /**
     * 更新热门列表
     */
    Integer updateRecentSort(List<Integer> activityIds);

    /**
     * 发布---更新状态
     */
    Integer updateIssueStatus(int id, int userId);

    /**
     * 获取进行中的活动
     */
    List<Activity> getINGActivity(int userId);

    /**
     * 根据会议发布状态(1是：0否) 获取会议
     */
    List<Activity> getActivityByStatus(int userId, int status);

    /**
     * 根据会议发布状态(1是：0否) 获取会议
     */
    List<Activity> getActivityOver(int userId);

    /**
     * 未结束会议
     * @param userId
     * @return
     */
    List<Activity> getAllActivityNotOver(int userId);


    /**
     * 条件查询
     *
     * @param param
     * @return
     */
    List<Activity> selectByParam(HashMap param);

    List<ActivityType> getActivityTypeList();

    /**
     * 按状态获取全部会议
     *
     * @param issueStatus
     * @return
     */
    List<Activity> getAllActivityByStatus(int issueStatus);

    /**
     * 获取所有已经结束的会议
     *
     * @return
     */
    List<Activity> getAllActivityOver();

    /**
     * 更新bannerId
     *
     * @param activityId
     * @param bannerId
     * @return
     */
    Integer updateBannerId(Integer activityId, Integer bannerId);

    /**
     * 根据用户id 获取当前用户 发布的会议数量
     *
     * @param userId
     * @return
     */
    Integer getIssueActivityCount(int userId);

    /**
     * 获取所有 删除状态不为1 的 会议数量
     *
     */
    Integer getActivityCountWithOutDelete(Integer userId);

    Activity getByCustomUrl(String customUrl);

    boolean checkCustomUrl(String customUrl, Integer activityId);
}
