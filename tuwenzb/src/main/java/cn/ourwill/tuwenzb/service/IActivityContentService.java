package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.ActivityContent;

import java.util.Date;
import java.util.List;

/**
 * 　ClassName:IUserService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:51
 */

public interface IActivityContentService extends IBaseService<ActivityContent>{
    //获取最新直播内容
    public List<ActivityContent> getRecentActivity(Integer activityId,String date,String timeOrder);

    Integer getContentCount(Integer activityId);
    //获取全部直播图片
    List getAllImgByActivityId(Integer activityId);

    String exportWord(Integer activityId);

    Integer getCountNum(Integer activityId);

    Integer stickActivityContent(Integer id, Integer stickSign );

    Integer getStickNum(Integer id);

    Integer getNewContentNum (Integer activityId, Date date);

    List<ActivityContent>  getRecentConcentByTime (Integer activityId, Date date, Integer timeOrder,Integer contentNum);
    String getMaxTime(Integer activityId);
}
