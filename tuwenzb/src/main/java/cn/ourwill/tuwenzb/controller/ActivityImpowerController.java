package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.aop.UnPermissionException;
import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityAlbum;
import cn.ourwill.tuwenzb.entity.ActivityImpower;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IActivityAlbumService;
import cn.ourwill.tuwenzb.service.IActivityImpowerService;
import cn.ourwill.tuwenzb.service.IActivityService;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/23 0023 16:05
 * @Version1.0
 */
@Component
@Path("/activityImpower")
public class ActivityImpowerController {
    @Autowired
    IActivityImpowerService activityImpowerService;
    @Autowired
    IActivityService activityService;
    @Autowired
    IActivityAlbumService activityAlbumService;
    @Autowired
    IUserService userService;

    private static final Logger log = LogManager.getLogger(ActivityImpowerController.class);

    /**
     * 活动增加授权
     * @param request
     * @param activityId
     * @param params
     * @return
     */
    @POST
    @Path("/{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map save(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId,Map params){
        try{
            String impowerCode = (String) params.get("impowerCode");
            Activity activity = activityService.getById(activityId);
            if (activity.getIsImpower().equals(0)){
                return ReturnResult.errorResult("本活动对外授权已关闭！");
            }
            String orgImpowerCode = (String) RedisUtils.get("ImpowerCode:" + activityId);
            if(StringUtils.isNotEmpty(impowerCode)&&StringUtils.isNotEmpty(orgImpowerCode)&&impowerCode.equals(orgImpowerCode)) {
                Integer userId = GlobalUtils.getUserId(request);
                ActivityImpower activityImpower = new ActivityImpower();
                activityImpower.setActivityId(activityId);
                activityImpower.setUserId(userId);
                activityImpower.setCTime(new Date());
                activityImpower.setStatus(1);
                return activityImpowerService.save(activityImpower);
            }else{
                return ReturnResult.errorResult("授权码错误！");
            }
        }catch(Exception e){
            log.info("ActivityImpowerController.save",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 照片分区增加授权
     * @param request
     * @param activityId
     * @param albumId
     * @param param
     * @return
     */
    @POST
    @Path("/{activityId}/photo/{albumId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map photoSave(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId,@PathParam("albumId") Integer albumId,
                         Map param){
        try{
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            if(param.get("mobPhone")==null||param.get("type")==null){
                return ReturnResult.errorResult("参数无效！");
            }
            String mobPhone = (String) param.get("mobPhone");
            Integer type = (Integer) param.get("type");
            ActivityAlbum album = activityAlbumService.getById(albumId);
            if(album!=null&&album.getActivityId().equals(activityId)) {
                Activity activity = activityService.getById(album.getActivityId());
                if(type.equals(2)) {
                    if (!activity.getUserId().equals(userId) && userLevel.equals(0)) {
                        throw new UnPermissionException();
                    }
                }else{
                    if (!activityImpowerService.isPhotoAdminImpower(activityId, userId) && !activity.getUserId().equals(userId) && userLevel.equals(0)) {
                        throw new UnPermissionException();
                    }
                }
                User user = userService.selectByUsernameOrPhone(mobPhone);
                if (user != null) {
                    ActivityImpower activityImpower = new ActivityImpower();
                    activityImpower.setActivityId(activityId);
                    activityImpower.setAlbumId(albumId);
                    activityImpower.setUserId(user.getId());
                    activityImpower.setType(type);
                    activityImpower.setCTime(new Date());
                    activityImpower.setStatus(1);
                    return activityImpowerService.saveForPhoto(activityImpower);
                } else {
                    return ReturnResult.errorResult("无此用户！");
                }
            }else{
                return ReturnResult.errorResult("参数无效！");
            }
        }catch (UnPermissionException e){
            throw e;
        }catch(Exception e){
            log.info("ActivityImpowerController.photoSave",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 照片增加管理授权
     * @param request
     * @param activityId
     * @param param
     * @return
     */
    @POST
    @Path("/{activityId}/photo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map photoSaveAdmin(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId,
                         Map param){
        try{
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            if(param.get("mobPhone")==null){
                return ReturnResult.errorResult("参数无效！");
            }
            String mobPhone = (String) param.get("mobPhone");
            Activity activity = activityService.getById(activityId);
            if (!activity.getUserId().equals(userId) && userLevel.equals(0)) {
                throw new UnPermissionException();
            }
            User user = userService.selectByUsernameOrPhone(mobPhone);
            if (user != null) {
                ActivityImpower activityImpower = new ActivityImpower();
                activityImpower.setActivityId(activityId);
                activityImpower.setUserId(user.getId());
                activityImpower.setType(2);
                activityImpower.setCTime(new Date());
                activityImpower.setStatus(1);
                return activityImpowerService.saveForPhotoAdmin(activityImpower);
            } else {
                return ReturnResult.errorResult("无此用户！");
            }
        }catch (UnPermissionException e){
            throw e;
        }catch(Exception e){
            log.info("ActivityImpowerController.photoSaveAdmin",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取活动授权信息
     * @param request
     * @param activityId
     * @return
     */
    @GET
    @Path("/{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map selectByActivityId(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId){
        try{
            List<ActivityImpower> list = activityImpowerService.selectByActivityId(activityId);
            return ReturnResult.successResult("data",list,ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("ActivityImpowerController.selectByActivityId",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取授权码
     * @param request
     * @param activityId
     * @param isRefresh
     * @return
     */
    @GET
    @Path("/{activityId}/getImpowerCode")
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getImpowerCode(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId, @QueryParam("isRefresh") boolean isRefresh){
        try {
            Activity activity = activityService.getById(activityId);
            Integer userId = GlobalUtils.getUserId(request);
            if (activity == null) {
                return ReturnResult.errorResult("活动不存在！");
            }
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request,"userLevel");
            if (!activity.getUserId().equals(userId)&&userLevel.equals(0)) {
                throw new UnPermissionException();
            }
            if(activity.getEndTime().before(new Date())){
                return ReturnResult.errorResult("活动已结束！");
            }
            if (activity.getIsImpower().equals(0)){
                return ReturnResult.errorResult("本活动对外授权已关闭！");
            }
            if (!isRefresh) {
                String orgImpowerCode = (String) RedisUtils.get("ImpowerCode:" + activityId);
                if (StringUtils.isNotEmpty(orgImpowerCode)) {
                    return ReturnResult.successResult("data", orgImpowerCode, ReturnType.GET_SUCCESS);
                }
            }
            String impowerCode = GlobalUtils.getRandomString(6);
            log.info("ImpowerCode:" + impowerCode);
            int valiDays = GlobalUtils.daysOfTwo(new Date(),activity.getEndTime())+1;
            RedisUtils.set("ImpowerCode:" + activityId, impowerCode,valiDays,TimeUnit.DAYS);
            return ReturnResult.successResult("data", impowerCode, ReturnType.GET_SUCCESS);
        }catch (UnPermissionException e){
            throw e;
        }catch(Exception e){
            log.info("ActivityImpowerController.getImpowerCode",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 照片直播删除授权记录
     * @param request
     * @param id
     * @return
     */
    @DELETE
    @Path("/{id}/photo")
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map deleteForPhoto(@Context HttpServletRequest request, @PathParam("id") Integer id){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            ActivityImpower impower = activityImpowerService.getById(id);
            if (impower != null) {
                Activity activity = activityService.getById(impower.getActivityId());
                if (impower.getType().equals(2)) {
                    if (!activity.getUserId().equals(userId) && userLevel.equals(0)) {
                        throw new UnPermissionException();
                    }
                } else {
                    if (!activity.getUserId().equals(userId) && userLevel.equals(0) && !activityImpowerService.isPhotoAdminImpower(impower.getActivityId(), userId)) {
                        throw new UnPermissionException();
                    }
                }
                if (activityImpowerService.deleteById(id) > 0) {
                    return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
                }
            }
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        }catch (UnPermissionException e){
            throw e;
        }catch(Exception e){
            log.info("ActivityImpowerController.delete",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 删除授权记录
     * @param request
     * @param id
     * @return
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map delete(@Context HttpServletRequest request, @PathParam("id") Integer id){
        try{
//            activityImpowerService.getById(id);
            if(activityImpowerService.deleteById(id)>0){
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        }catch(Exception e){
            log.info("ActivityImpowerController.delete",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取分区授权信息
     * @param request
     * @param activityId
     * @param albumId
     * @return
     */
    @GET
    @Path("/{activityId}/photo/{albumId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getImpowerByAlbumId(@Context HttpServletRequest request,@PathParam("activityId") Integer activityId,@PathParam("albumId") Integer albumId){
        try{
            List<ActivityImpower> reList = activityImpowerService.getByAlbumIdAndActivityId(activityId,albumId);
            return ReturnResult.successResult("data",reList,ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("ActivityImpowerController.getImpowerByAlbumId",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
