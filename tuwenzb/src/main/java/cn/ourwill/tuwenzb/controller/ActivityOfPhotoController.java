package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.aop.UnPermissionException;
import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityStatistics;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.*;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import cn.ourwill.tuwenzb.weixin.Utils.WeixinPushMassage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2017/11/9 0009 19:17
 * @Description 照片直播类
 */
@Component
@Path("/photoActivity")
public class ActivityOfPhotoController {

    @Autowired
    private IActivityService activityService;
    @Autowired
    private IWatchListService watchListService;
    @Autowired
    private IActivityStatisticsService activityStatisticsService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IActivityImpowerService activityImpowerService;
    @Autowired
    private IActivityContentService activityContentService;
    @Autowired
    private IActivityAlbumService activityAlbumService;
    @Autowired
    private IQiniuService qiniuService;
    @Value("${upload.photo.bucketDomain}")
    private String bucketDomain;

    @Autowired
    private IActivityPhotoService activityPhotoService;

    private static final Logger log = LogManager.getLogger(ActivityOfPhotoController.class);

    /**
     * 保存
     *
     * @param activity
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map save(@Context HttpServletRequest request, Activity activity) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            if (userId == null) {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            Integer days = GlobalUtils.daysOfTwo(activity.getStartTime(), activity.getEndTime());
//            if(days==null||days<1){
//                return ReturnResult.errorResult("活动天数不正确！");
//            }
//            activity.setEndTime(this.getEndDate(activity.getStartTime(),days));
            User user = userService.getById(userId);
            if (user.getPhotoLicenseType() == 0) {
                return ReturnResult.errorResult("未授权，不能创建活动！");
            } else if (user.getPhotoLicenseType() == 1 && user.getPhotoDueDate().before(new Date())) {
                return ReturnResult.errorResult("授权过期，不能创建活动！");
            } else if (user.getPhotoLicenseType() == 2) {
                if (user.getPhotoRemainingDays() <= 0) {
                    return ReturnResult.errorResult("授权过期，不能创建活动！");
                } else {
                    if (days > user.getPhotoRemainingDays()) {
                        return ReturnResult.errorResult("授权日期不足，不能创建活动！");
                    }
                }
            }
            activity.setPhotoLive(1);
            activity.setUserId(userId);
            activity.setCTime(new Date());
            if (user.getPhotoLicenseType().equals(1)) {
                if (activity.getStartTime().after(user.getPhotoDueDate()) || activity.getEndTime().after(user.getPhotoDueDate())) {
                    return ReturnResult.errorResult("活动时间不在用户授权时间内！");
                }
            }
            //试用账户数据
//            if(user.getUserType()!=null&&user.getUserType().equals(2)){
//                activity.setStatus(4);
//            }
            if (activity.getInputPwd() != null) activity.setPassword(activity.getInputPwd());
            if (activityService.save(activity) > 0) {
                ActivityStatistics statistics = new ActivityStatistics();
                statistics.setActivityId(activity.getId());
                statistics.setParticipantsNum(0);
                statistics.setCommentNum(0);
                statistics.setLikeNum(0);
                if (activityStatisticsService.save(statistics) > 0) {
                    //添加redis
                    int participantsNum = 0;
                    int likeNum = 0;
                    if (activity.getParticipantsNum() != null) {
                        participantsNum = activity.getParticipantsNum();
                    }
                    if (activity.getLikeNum() != null) {
                        likeNum = activity.getLikeNum();
                    }
                    RedisUtils.addActivityHash(activity.getId(), statistics.getId(), participantsNum, likeNum);
                    //扣除相应天数
                    userService.photoConsumeRemain(days, user);
                    try {
                        //创建活动成功模板信息
                        SimpleDateFormat dbf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        WeixinPushMassage.wxCreateSuccess(activity.getTitle(), activity.getSite(), dbf.format(activity.getStartTime()), "点击更多查看活动详情！", user.getWechatNum(), activity.getActivityUrl());
                    } catch (Exception e) {
                        log.error("WeixinPushMassage.wxCreateSuccess", e);
                    }
                    return ReturnResult.successResult("data", activity, ReturnType.ADD_SUCCESS);
                }
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        } catch (Exception e) {
            log.error("ActivityOfPhotoController.save", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新
     *
     * @param id
     * @param activity
     * @return
     */
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map update(@Context HttpServletRequest request, @PathParam("id") Integer id, Activity activity) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            if (!activityService.checkOwner(request, id) && !activityImpowerService.isPhotoAdminImpower(id, userId)) {
                throw new UnPermissionException();
            }
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            activity.setId(id);
            activity.setUTime(new Date());
            activity.setPhotoLive(1);
//            String end = sdf.format(activity.getEndTime());
//            activity.setEndTime(sdf.parse(end.replace("00:00:00","23:59:59")));
            //oldActivity
            Activity oldActivity = activityService.getById(id);
            if (oldActivity == null) return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            activity.setUserId(oldActivity.getUserId());
            if (oldActivity.getStatus().equals(4)) {
                User ownerUser = userService.getById(oldActivity.getUserId());
                if (!ownerUser.getUserType().equals(2)) {
                    return ReturnResult.errorResult("示例活动无法修改！");
                }
            }

            User user = userService.getById(oldActivity.getUserId());
            if (!userLevel.equals(1)) {
                int oldDays = GlobalUtils.daysOfTwo(oldActivity.getStartTime(), oldActivity.getEndTime());
                int newDays = GlobalUtils.daysOfTwo(activity.getStartTime(), activity.getEndTime());
//            int newDays = activity.getLiveDays();
                int dif_days = newDays - oldDays;
                //授权校验
                if (oldActivity.getStartTime().before(new Date()) && oldActivity.getStartTime().compareTo(activity.getStartTime()) != 0) {
                    return ReturnResult.errorResult("活动已开始，无法修改开始时间！");
                }
                if (dif_days < 0 && oldActivity.getStartTime().before(new Date())) {
                    return ReturnResult.errorResult("活动已开始，无法缩短时长！");
                }
                if (dif_days != 0 && oldActivity.getEndTime().before(new Date())) {
                    return ReturnResult.errorResult("活动已结束，无法修改时间！");
                }
                if (user.getPhotoLicenseType().equals(1)) {
                    if (activity.getStartTime().after(user.getPhotoDueDate()) || activity.getEndTime().after(user.getPhotoDueDate())) {
                        return ReturnResult.errorResult("活动时间不在用户授权时间内！");
                    }
                    if (user.getPhotoPackYearsDays() - dif_days < 0) {
                        return ReturnResult.errorResult("剩余授权场次不足，不能延长活动时间！");
                    }
                } else if (user.getPhotoLicenseType().equals(2)) {
                    if (user.getPhotoRemainingDays() - dif_days < 0) {
                        return ReturnResult.errorResult("剩余授权场次不足，不能延长活动时间！");
                    }
                }
                //扣除相应天数
                userService.photoConsumeRemain(dif_days, user);
            }
            if (activity.getInputPwd() != null) activity.setPassword(activity.getInputPwd());
            int count = activityService.update(activity);
            if (count > 0) {
                return ReturnResult.successResult("data", activity, ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityOfPhotoController.update", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @Path("/autoPublish/{activityId}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map updateAutoPublish(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId, @QueryParam("status") Integer status) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            if (!activityService.checkOwner(request, activityId) && !activityImpowerService.isPhotoAdminImpower(activityId, userId)) {
                throw new UnPermissionException();
            }
            if (activityService.updateAutoPublish(activityId, status) > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_ERROR);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityOfPhotoController.updateAutoPublish", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }



}
