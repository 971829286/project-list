package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.aop.UnPermissionException;
import cn.ourwill.tuwenzb.entity.*;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.*;
import cn.ourwill.tuwenzb.utils.*;
import cn.ourwill.tuwenzb.weixin.Utils.WeixinPushMassage;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 　ClassName:ActivityController
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 16:10
 */
@Component
@Path("/activity")
public class ActivityController {

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
    private IVoteService voteService;
    @Autowired
    private IActivityPhotoService activityPhotoService;
    @Value("${upload.bucketDomain}")
    private String bucketDomain;

    @Autowired
    RedisTemplate redisTemplate;

    private static final Logger log = LogManager.getLogger(ActivityController.class);
    //@Path请求路径
    //@Path("参数名称：正则表达式")
    //如：@Path("{from:\\d+}={to:\\d+}")
    //如：@Path("{beginMonth:\\d+},{beginYear:\\d+}")-{endMonth:\\d+},{endYear:\\d+}
    //@QueryParam：解析参数
    //@PathParam：解析路径参数
    //@FormParam：定义表单参数
    //@Encoded:禁用自动解码
    //@DefaultValue:默认值
    //@CookieParamm注解：匹配Cookie的键值对
    // @Consumes：客户端请求的媒体类型
    //@Produces:服务器端响应的媒体类型
    //@PUT:put请求
    //@GET:get请求
    //@DELETE：删除请求

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
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String end = sdf.format(activity.getEndTime());
//            int days = 0;//活动天数
            Integer days = activity.getLiveDays();
            if (days == null || days < 1) {
                return ReturnResult.errorResult("活动天数不正确！");
            }
            activity.setEndTime(this.getEndDate(activity.getStartTime(), days));
            User user = userService.getById(userId);
            if (user.getLicenseType() == 0) {
                return ReturnResult.errorResult("未授权，不能创建活动！");
            } else if (user.getLicenseType() == 1 && user.getDueDate().before(new Date())) {
                return ReturnResult.errorResult("授权过期，不能创建活动！");
            } else if (user.getLicenseType() == 2) {
                if (user.getRemainingDays() <= 0) {
                    return ReturnResult.errorResult("授权过期，不能创建活动！");
                } else {
//                    days = days+GlobalUtils.daysOfTwo(activity.getStartTime(),activity.getEndTime());
                    if (days > user.getRemainingDays()) {
                        return ReturnResult.errorResult("授权日期不足，不能创建活动！");
                    }
                }
            }
            activity.setUserId(userId);
            activity.setCTime(new Date());
            if (user.getLicenseType().equals(1)) {
                if (activity.getStartTime().after(user.getDueDate()) || activity.getEndTime().after(user.getDueDate())) {
                    return ReturnResult.errorResult("活动时间不在用户授权时间内！");
                }
            }
            //试用账户数据
            if (user.getUserType() != null && user.getUserType().equals(2)) {
                activity.setStatus(4);
            }
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
                    userService.consumeRemain(days, user);
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
            log.error("ActivityController.save", e);
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
            if (!activityService.checkOwner(request, id)) {
                throw new UnPermissionException();
            }
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            activity.setId(id);
            activity.setUTime(new Date());
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
            int oldDays = GlobalUtils.daysOfTwo(oldActivity.getStartTime(), oldActivity.getEndTime());
//            int newDays = GlobalUtils.daysOfTwo(activity.getStartTime(),activity.getEndTime());
            int newDays = activity.getLiveDays();
            int dif_days = newDays - oldDays;
            activity.setEndTime(this.getEndDate(activity.getStartTime(), newDays));
            //授权校验
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            if (!userLevel.equals(1)) {
                if (oldActivity.getStartTime().before(new Date()) && oldActivity.getStartTime().compareTo(activity.getStartTime()) != 0) {
                    return ReturnResult.errorResult("活动已开始，无法修改开始时间！");
                }
                if (dif_days < 0 && oldActivity.getStartTime().before(new Date())) {
                    return ReturnResult.errorResult("活动已开始，无法缩短时长！");
                }
                if (dif_days != 0 && oldActivity.getEndTime().before(new Date())) {
                    return ReturnResult.errorResult("活动已结束，无法修改时间！");
                }
                if (user.getLicenseType().equals(1)) {
                    if (activity.getStartTime().after(user.getDueDate()) || activity.getEndTime().after(user.getDueDate())) {
                        return ReturnResult.errorResult("活动时间不在用户授权时间内！");
                    }
                    if (user.getPackYearsDays() - dif_days < 0) {
                        return ReturnResult.errorResult("剩余授权场次不足，不能延长活动时间！");
                    }
                } else if (user.getLicenseType().equals(2)) {
                    if (user.getRemainingDays() - dif_days < 0) {
                        return ReturnResult.errorResult("剩余授权场次不足，不能延长活动时间！");
                    }
                }
                //扣除相应天数
                userService.consumeRemain(dif_days, user);
            }
            if (activity.getInputPwd() != null) activity.setPassword(activity.getInputPwd());
            int count = activityService.update(activity);
            if (count > 0) {
                if (!oldActivity.getCheckType().equals(activity.getCheckType())) {
                    activityStatisticsService.refushCommentNumberRedis(oldActivity.getId());
                }
                return ReturnResult.successResult("data", activity, ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityController.update", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 按id获取
     *
     * @param id
     * @return
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map getById(@Context HttpServletRequest request, @PathParam("id") Integer id, @QueryParam("isOld") Integer isOld, @QueryParam("password") String password) throws RuntimeException {
        try {
            if (isOld != null && isOld == 1) {
                return ReturnResult.successResult("data", activityService.getByIdWithOld(id), ReturnType.GET_SUCCESS);
            }
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Activity activity = activityService.getById(id);
            if (activity == null) {
                return ReturnResult.errorResult("直播不存在！");
            }
            Integer impower = activity.getIsImpower();
            if (activity.getPhotoLive().equals(1))
                impower = 1;
            //加密校验
            if (activity.getSwitchPassword().equals(1)) {
                if (userId != null) {
                    if (userLevel.equals(0) && !activity.getUserId().equals(userId) && RedisUtils.get("actPwd:" + id + "_" + userId) == null
                            && !activityImpowerService.isImpower(id, userId, impower)) {
                        if (password != null && password.equals(activity.getPassword())) {
                            RedisUtils.set("actPwd:" + id + "_" + userId, System.currentTimeMillis(), 10, TimeUnit.DAYS);
                        } else if (StringUtils.isEmpty(password)) {
                            return ReturnResult.customResult(-2, "请输入密码！");
                        } else if (!password.equals(activity.getPassword())) {
                            return ReturnResult.customResult(-2, "密码错误，请重新输入！");
                        }
                    }
                } else {
                    if (StringUtils.isEmpty(password) || !password.equals(activity.getPassword())) {
                        return ReturnResult.customResult(-2, "请输入密码！");
                    }
                }
            }
            User user = userService.getById(userId);
            if (activity.getPhotoLive().equals(1)) {
                List<ActivityAlbum> list = activityAlbumService.selectByActivity(id);
                activity.setActivityAlbums(list);
            }
            activity.setContentNumber(activityContentService.getContentCount(id));
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            }
            if (activity.getStatus() == 0) {
                if (userId == null || !userId.equals(activity.getUserId())) {
                    return ReturnResult.errorResult("该直播已关闭！");
                }
            }
            //参与人数统计
            ArrayList activitys = (ArrayList) GlobalUtils.getSessionValue(request, "activitys");
            if (activitys == null) activitys = new ArrayList();
            if (!activitys.contains(id)) {
                activityStatisticsService.addParticipantsNumberRedis(id,1);
                activityStatisticsService.addRealParticipantsNumberRedis(id);
                activitys.add(id);
                GlobalUtils.setSessionValue(request, "activitys", activitys);
            }
            //在线人数统计
            Integer activityId = (Integer) GlobalUtils.getSessionValue(request, "lastActivityId");
            if (activityId == null) {
                activityStatisticsService.addOnlineNumberRedis(id);
                GlobalUtils.setSessionValue(request, "lastActivityId", id);
            } else if (!activityId.equals(id)) {
                activityStatisticsService.minusOnlineNumberRedis(activityId);
                activityStatisticsService.addOnlineNumberRedis(id);
                GlobalUtils.setSessionValue(request, "lastActivityId", id);
            }
//            if(!activitys.contains(id)){
//                activityStatisticsService.addParticipantsNumberRedis(id);
//                activitys.add(id);
//                GlobalUtils.setSessionValue(request,"activitys",activitys);
//            }
            String markImgUrl = activity.getWatermark();
            //获取当前用户id
//            Integer userId = GlobalUtils.getUserId(request);
            if (userId != null) {
                if (watchListService.checkWatchStatus(id, userId)) {
                    activity.setIsAttention(true);
                } else if (user.getBoundId() != null && watchListService.checkWatchStatus(id, user.getBoundId())) {
                    activity.setIsAttention(true);
                } else {
                    activity.setIsAttention(false);
                }
                if (activity.getUserId().equals(userId) || user.getLevel().equals(1) || activityImpowerService.isImpower(activity.getId(), userId, activity.getIsImpower())) {
//                    log.info("++++++++++++userid"+activity.getUserId().equals(userId));
//                    log.info("++++++++++++level"+user.getLevel().equals(1));
                    activity.setModifyPermission(true);
                } else {
                    activity.setModifyPermission(false);
                }
            }
            if (StringUtils.isNotEmpty(markImgUrl)) {
                markImgUrl = bucketDomain + markImgUrl;
                String waterMark = ImgUtil.getWaterMark(markImgUrl);
                activity.setWaterMarkUrl(waterMark);
            }
            //添加创建者信息
            User createUser = userService.getById(activity.getUserId());
            activity.setUsername(createUser.getUsername());
            activity.setNickname(createUser.getNickname());
            activity.setAvatarUrl(createUser.getAvatarUrl());
            //添加授权人信息
            List<ActivityImpower> adminUser = activityImpowerService.getAdminByActivityId(id);
            activity.setAdminUser(adminUser);
            //是否有投票
            if (voteService.getVoteCount(activity.getId()) > 0) {
                activity.setIsVote(1);
            } else {
                activity.setIsVote(0);
            }
            return ReturnResult.successResult("data", activity, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.getById", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @Path("/{id}/statistics")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map getStatisticsById(@Context HttpServletRequest request, @PathParam("id") Integer id,@QueryParam("time") Long time) {
        try {
            ActivityStatistics statistics = RedisUtils.getByActivityId(id);
            if (statistics.getActivityId() == null) return ReturnResult.errorResult(ReturnType.GET_ERROR);
            if (time != null){
                Date date = new Date(time);
                Integer num = activityContentService.getNewContentNum(id,date);
                statistics.setNewContentNum(num);
            }
            return ReturnResult.successResult("data", statistics, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.getStatisticsById", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Path("/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map delete(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        try {
            if (!activityService.checkOwner(request, id)) {
                throw new UnPermissionException();
            }
            int count = activityService.delete(id);
//            activityStatisticsService.deleteByActivityId(id);
            if (count > 0) {
//                RedisUtils.deleteActivityHash(id);
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityController.delete", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 关闭
     *
     * @param id
     * @return
     */
    @Path("/close/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map close(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        try {
            if (!activityService.checkOwner(request, id)) {
                throw new UnPermissionException();
            }
            int count = activityService.close(id);
//            activityStatisticsService.deleteByActivityId(id);
            if (count > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityController.close", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 打开
     *
     * @param id
     * @return
     */
    @Path("/open/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map open(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        try {
            if (!activityService.checkOwner(request, id)) {
                throw new UnPermissionException();
            }
            int count = activityService.open(id);
//            activityStatisticsService.deleteByActivityId(id);
            if (count > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityController.open", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取所有
     *
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map findAll(@QueryParam("page") Integer page, @QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
                       @QueryParam("orderBy") Integer orderBy, @Context HttpServletRequest request,
                       @DefaultValue("0") @QueryParam("photoLive") Integer photoLive,@QueryParam("userName") String userName) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Map param = new HashMap();
            param.put("status", 1);
            if (photoLive.equals(1)) {
                param.put("photoLive", 1);
            } else {
                param.put("photoLive", 0);
            }
            if (request.getParameter("type") != null && !request.getParameter("type").trim().equals("0")) {
                param.put("type", request.getParameter("type"));
            }
            if(userName != null){
                System.out.println(userName);
                param.put("userName",userName);
            }
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy(" c_time desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy(" c_time asc");
            } else {
                PageHelper.orderBy(" priority desc,statuss desc,c_time desc");
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                List<Activity> lists = activityService.selectByParams(param);
//                addIsPwd(lists,userId,userLevel);
                PageInfo<Activity> pages = new PageInfo<>(lists);
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<Activity> lists = activityService.selectByParams(param);
//            addIsPwd(lists,userId,userLevel);
            return ReturnResult.successResult("data", lists, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.findAll", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/{activityId}/pwd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getActivityPwd(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Activity activity = activityService.getById(activityId);
            if (userLevel.equals(1) || userId.equals(activity.getUserId())) {
                return ReturnResult.successResult("data", activity.getPassword() == null ? "" : activity.getPassword(), ReturnType.GET_SUCCESS);
            } else {
                throw new UnPermissionException();
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityController.getActivityPwd", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

//    /**
//     * 判断是否输入密码
//     * @param list
//     * @param userId
//     */
//    private void addIsPwd(List<Activity> list,Integer userId,Integer userLevel){
//        if(userId!=null) {
//            for (Activity activity : list) {
//                if(userLevel.equals(1)||activity.getUserId().equals(userId)) {
//                    activity.setIsPassword(0);//管理员可见
//                    continue;
//                }
//                if(activity.getSwitchPassword().equals(1))
//                    if(RedisUtils.get("actPwd:" + activity.getId() + "_" + userId)!=null)
//                        activity.setIsPassword(0);
//                    else
//                        activity.setIsPassword(1);
//                else
//                    activity.setIsPassword(0);
//            }
//        }else{
//            for (Activity activity : list) {
//                if(activity.getSwitchPassword().equals(1))
//                    activity.setIsPassword(1);
//                else
//                    activity.setIsPassword(0);
//            }
//        }
//    }

    /**
     * 获取预告
     *
     * @return
     */
    @GET
    @Path("/advance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map findAll(@Context HttpServletRequest request) {
        try {
            List<Activity> lists = activityService.selectAdvance();
            return ReturnResult.successResult("data", lists, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.findAll", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取经典案例
     *
     * @return
     */
    @GET
    @Path("/classical")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map findClassical(@QueryParam("page") Integer page, @QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
                             @QueryParam("orderBy") Integer orderBy, @Context HttpServletRequest request) {
        try {
            Map param = new HashMap();
            param.put("status", 1);
            param.put("classical", 1);
            param.put("photoLive", 0);
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy(" c_time desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy(" c_time asc");
            } else {
                PageHelper.orderBy(" priority desc,c_time desc");
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                List<Activity> lists = activityService.selectByParams(param);
                PageInfo<Activity> pages = new PageInfo<>(lists);
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<Activity> lists = activityService.selectByParams(param);
            return ReturnResult.successResult("data", lists, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.findClassical", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取所有
     *
     * @return
     */
    @GET
    @Path("/withOld")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map findWithOld(@QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
                           @QueryParam("orderBy") Integer orderBy, @Context HttpServletRequest request) {
        try {
            Map param = new HashMap();
            param.put("status", 1);
            param.put("photoLive", 0);
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy(" activity_union.c_time desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy(" activity_union.c_time asc");
            } else {
                PageHelper.orderBy(" activity_union.priority desc");
            }
            PageHelper.startPage(pageNum, pageSize);
            List<Activity> lists = activityService.selectWithOld(param);
            PageInfo<Activity> pages = new PageInfo<>(lists);
            List<Activity> relist = lists.stream().map(activity -> {
                if (activity.getStartTime() == null) activity.setIsOld(1);
                else activity.setIsOld(0);
                return activity;
            }).collect(Collectors.toList());
            pages.setList(relist);
            return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.findAll", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 添加关注
     */
    @GET
    @Path("/{activityId}/attention")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map attention(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId) {
        try {
            //获取当前用户id
            Integer userId = GlobalUtils.getUserId(request);
            if (userId != null) {
                if (watchListService.checkWatchStatus(activityId, userId)) {
                    return ReturnResult.errorResult("该用户已关注");
                }
                WatchList watchList = new WatchList();
                watchList.setActivityId(activityId);
                watchList.setUserId(userId);
                watchListService.save(watchList);

                //发送模板信息，用户关注成功
                User user = userService.getById(userId);
                Activity activity = activityService.getById(activityId);
                try {
                    String remark = "";
                    if (activity.getTitle() != null)
                        remark = "活动名称：" + activity.getTitle();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    WeixinPushMassage.wxFollowSuccess(user.getNickname(),
                            sdf.format(new Date()), remark,
                            user.getWechatNum(), activity.getActivityUrl());
                } catch (Exception e) {
                    log.error("WeixinPushMassage.wxFollowSuccess", e);
                }
                return ReturnResult.successResult("关注成功！");
            }
            return ReturnResult.errorResult("用户未登录！");
        } catch (Exception e) {
            log.error("ActivityController.attention", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 取消关注
     */
    @DELETE
    @Path("/{activityId}/attention")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map unattention(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId) {
        try {
            //获取当前用户id
            Integer userId = GlobalUtils.getUserId(request);
            User user = userService.getById(userId);
            Integer boundId = user.getBoundId();
            if (userId != null) {
                if (watchListService.checkWatchStatus(activityId, userId)) {
                    watchListService.deleteByActivityAndUser(activityId, userId);
                    return ReturnResult.successResult("取消关注成功！");
                }
                if (boundId != null && watchListService.checkWatchStatus(activityId, boundId)) {
                    watchListService.deleteByActivityAndUser(activityId, boundId);
                    return ReturnResult.successResult("取消关注成功！");
                }
                return ReturnResult.errorResult("未关注，无法取消！");
            }
            return ReturnResult.errorResult("用户未登录");
        } catch (Exception e) {
            log.error("ActivityController.unattention", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新授权状态
     */
    @POST
    @Path("/{activityId}/updateImpower/{isImpower}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map updataImpower(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId, @PathParam("isImpower") Integer isImpower) {
        try {
            //获取当前用户id
//            Integer userId = GlobalUtils.getUserId(request);
//            User user = userService.getById(userId);
            Activity activity = activityService.getById(activityId);
            if (activity == null) ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            if (activity.getEndTime().before(new Date())) {
                return ReturnResult.errorResult("活动已结束！");
            }
            activityService.updataImpower(activityId, isImpower);
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.updataImpower", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/getAllType")
    @Produces(MediaType.APPLICATION_JSON)
    public Map getAllType() {
        try {
            List<ActivityType> activityTypes = activityService.getAllType();
            return ReturnResult.successResult("data", activityTypes, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.getAllType", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/{activityId}/alldate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getAllDate(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId) {
        try {
            List<String> dates = activityService.getAllDate(activityId);
            return ReturnResult.successResult("data", dates, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.getAllDate", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/{activityId}/activityAlbum")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map selectAlbumByActivity(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId) {
        try {
            List<ActivityAlbum> list = activityAlbumService.selectByActivity(activityId);
            return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.selectAlbumByActivity", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }

    @GET
    @Path("/{activityId}/activityAlbum/pc")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map selectAlbumByActivityImpowerPc(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            List<ActivityAlbum> list = new ArrayList<>();
            if (activityService.checkOwner(request, activityId) || userLevel.equals(1) || activityImpowerService.isPhotoAdminImpower(activityId, userId)) {
                list = activityAlbumService.selectByActivity(activityId);
            } else {
                list = activityAlbumService.selectByActivity(activityId, userId, 1);
            }
            return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.selectAlbumByActivityImpowerPc", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }

    @GET
    @Path("/{activityId}/activityAlbum/app")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map selectAlbumByActivityImpower(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            List<ActivityAlbum> list = new ArrayList<>();
            if (activityService.checkOwner(request, activityId) || userLevel.equals(1) || activityImpowerService.isPhotoAdminImpower(activityId, userId)) {
                list = activityAlbumService.selectByActivity(activityId);
            } else {
                list = activityAlbumService.selectByActivity(activityId, userId, 0);
            }
            return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityController.selectAlbumByActivityImpower", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }

    @GET
    @Path("/{activityId}/photos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map selectPhotoByAlbum(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId, @QueryParam("page") Integer page,
                                  @QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
                                  @QueryParam("orderBy") Integer orderBy) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            List<ActivityPhoto> activityPhotoList = new ArrayList<>();
            List<ActivityPhoto> reList = new ArrayList<>();
            if (page != null && page.equals(1)) {//分页
                PageHelper.startPage(pageNum, pageSize);
                activityPhotoList = activityPhotoService.selectByActivityId(activityId, 1, orderBy);
                PageInfo<ActivityPhoto> pageInfo = new PageInfo<ActivityPhoto>(activityPhotoList);
                if (userId != null) {
                    reList = activityPhotoList.stream().map(photo -> {
                        photo.setLikeNum(activityPhotoService.getLikeNum(photo.getId()));
                        photo.setLiked(activityPhotoService.isLiked(photo.getId(), userId));
                        return photo;
                    }).collect(Collectors.toList());
                    //重置list
                    pageInfo.setList(reList);
                }
                return ReturnResult.successResult("data", pageInfo, ReturnType.GET_SUCCESS);
            } else {//不分页
                activityPhotoList = activityPhotoService.selectByActivityId(activityId, 1, orderBy);
                if (userId != null) {
                    reList = activityPhotoList.stream().map(photo -> {
                        photo.setLikeNum(activityPhotoService.getLikeNum(photo.getId()));
                        photo.setLiked(activityPhotoService.isLiked(photo.getId(), userId));
                        return photo;
                    }).collect(Collectors.toList());
                    return ReturnResult.successResult("data", reList, ReturnType.GET_SUCCESS);
                }
                return ReturnResult.successResult("data", activityPhotoList, ReturnType.GET_SUCCESS);
            }
        } catch (Exception e) {
            log.error("ActivityAlbumController.selectPhotoByAlbum", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 更改该会议 展示的分区（就是相册）
     *
     * @param request
     * @param displayAlbumId
     * @return
     */
    @PUT
    @Path("/updateDisplayAlbum/{displayAlbumId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map upDateDisplayAlbum(@Context HttpServletRequest request,
                                  @PathParam("displayAlbumId") Integer displayAlbumId) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");

            ActivityAlbum album = activityAlbumService.getById(displayAlbumId);
            Integer activityId = album.getActivityId();
            if (activityId == null) {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
            if (!activityService.checkOwner(request, activityId) && !userLevel.equals(1) && !activityImpowerService.isPhotoAdminImpower(activityId, userId)) {
                throw new UnPermissionException();
            }
            Integer resultCode = activityService.upDateDisplayAlbum(activityId, userId, displayAlbumId);
            if (resultCode < 0) {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityController.upDateDisplayAlbum", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    private Date getEndDate(Date start, int days) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_YEAR, days - 1);
        String end = sdf.format(calendar.getTime());
        return sdf.parse(end.substring(0, 11).concat("23:59:59"));
    }

    /**
     * 设置活动人脸识别,
     * @param request
     * @return
     */
    @POST
    @Path("/setFaceSearch/{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map setFaceSearch(@Context HttpServletRequest request,@PathParam("activityId") Integer activityId,@DefaultValue("0")@QueryParam("faceLeve") Integer faceLeve){
        Activity activity = activityService.getById(activityId);
        if(activity != null){
            Integer flag = activityService.setActivityFaceSearch(faceLeve, activityId);
            if(flag > 0){
                return ReturnResult.successResult("修改成功");
            }
        }
        return ReturnResult.errorResult("修改失败");
    }

    @Path("/setLikeNum")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map setActivityLikeNum(@Context HttpServletRequest request, Activity activity){
        if(activity == null || activity.getId() == null) return ReturnResult.errorResult("传参错误");
        Integer update = activityService.update(activity);
        if(update > 0){
            return ReturnResult.successResult("修改成功");
        }else{
            return ReturnResult.errorResult("修改失败");
        }
    }
}
