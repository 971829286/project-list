package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.aop.UnPermissionException;
import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityAlbum;
import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.*;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2017/11/3 0003 11:37
 * @Description 照片直播相册
 */

@Component
@Path("/activityAlbum")
public class ActivityAlbumController {
    @Autowired
    private IActivityAlbumService activityAlbumService;
    @Autowired
    private IActivityPhotoService activityPhotoService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IActivityService activityService;
    @Autowired
    private IActivityImpowerService activityImpowerService;

    private static final Logger log = LogManager.getLogger(ActivityAlbumController.class);

    /**
     * 保存
     *
     * @param activityAlbum
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map save(@Context HttpServletRequest request, ActivityAlbum activityAlbum) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Activity activity = activityService.getById(activityAlbum.getActivityId());
            if (userLevel.equals(0) && (activity == null || !activity.getUserId().equals(userId)) && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId)) {
                throw new UnPermissionException();
            }
            activityAlbum.setDefaultFlag(0);
            activityAlbum.setCTime(new Date());
            activityAlbum.setUserId(userId);
            if (activityAlbumService.save(activityAlbum) > 0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.successResult(ReturnType.ADD_ERROR);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityAlbumController.save", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 修改信息
     *
     * @param request
     * @param id
     * @param activityAlbum
     * @return
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map update(@Context HttpServletRequest request, @PathParam("id") Integer id, ActivityAlbum activityAlbum) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            ActivityAlbum activityAlbumOld = activityAlbumService.getById(id);
            if (activityAlbumOld != null) {
                Activity activity = activityService.getById(activityAlbumOld.getActivityId());
                if (userLevel.equals(0) && (activity == null || !activity.getUserId().equals(userId)) && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId)) {
                    throw new UnPermissionException();
                }
                activityAlbum.setId(id);
                if (activityAlbumService.update(activityAlbum) > 0) {
                    return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
                }
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityAlbumController.update", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/{albumId}/photos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map selectPhotoByAlbum(@Context HttpServletRequest request, @PathParam("albumId") Integer albumId, @QueryParam("page") Integer page,
                                  @QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
                                  @QueryParam("orderBy") Integer orderBy) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            List<ActivityPhoto> activityPhotoList = new ArrayList<>();
            List<ActivityPhoto> reList = new ArrayList<>();
            if (page != null && page.equals(1)) {//分页
                PageHelper.startPage(pageNum, pageSize);
                activityPhotoList = activityPhotoService.selectByAlbumId(albumId, 1, orderBy);
                PageInfo<ActivityPhoto> pageInfo = new PageInfo<ActivityPhoto>(activityPhotoList);
                if (userId != null) {
                    reList = activityPhotoList.stream().map(photo -> {
                        photo.setLikeNum(activityPhotoService.getLikeNum(photo.getId()));
                        photo.setLiked(activityPhotoService.isLiked(photo.getId(), userId));
                        return photo;
                    }).collect(Collectors.toList());
                    //重置list
                    pageInfo.setList(reList);
                }else{
                    reList = activityPhotoList.stream().map(photo -> {
                        photo.setLikeNum(activityPhotoService.getLikeNum(photo.getId()));
                        return photo;
                    }).collect(Collectors.toList());
                    //重置list
                    pageInfo.setList(reList);
                }
                return ReturnResult.successResult("data", pageInfo, ReturnType.GET_SUCCESS);
            } else {//不分页
                activityPhotoList = activityPhotoService.selectByAlbumId(albumId, 1, orderBy);
                if (userId != null) {
                    reList = activityPhotoList.stream().map(photo -> {
                        photo.setLikeNum(activityPhotoService.getLikeNum(photo.getId()));
                        photo.setLiked(activityPhotoService.isLiked(photo.getId(), userId));
                        return photo;
                    }).collect(Collectors.toList());
                    return ReturnResult.successResult("data", reList, ReturnType.GET_SUCCESS);
                }else{
                    reList = activityPhotoList.stream().map(photo -> {
                        photo.setLikeNum(activityPhotoService.getLikeNum(photo.getId()));
                        return photo;
                    }).collect(Collectors.toList());
                    return ReturnResult.successResult("data", reList, ReturnType.GET_SUCCESS);
                }
            }
        } catch (Exception e) {
            log.error("ActivityAlbumController.selectPhotoByAlbum", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/{albumId}/photos/pc")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map selectPhotoByAlbumPc(@Context HttpServletRequest request, @PathParam("albumId") Integer albumId, @QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
                                    @QueryParam("photoStatus") Integer photoStatus,
                                    @QueryParam("orderBy") Integer orderBy) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<ActivityPhoto> photos = activityPhotoService.selectByAlbumId(albumId, photoStatus, orderBy);
            PageInfo<ActivityPhoto> pages = new PageInfo<>(photos);
            return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityAlbumController.selectPhotoByAlbum", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{albumId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map deleteById(@Context HttpServletRequest request, @PathParam("albumId") Integer albumId) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Activity activity = activityService.selectByAlbumId(albumId);
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }
            if (!activity.getUserId().equals(userId) && !userLevel.equals(1) && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId)) {
                throw new UnPermissionException();
            }
            List<ActivityPhoto> photos = activityPhotoService.selectByAlbumId(albumId, null, null);
            if (photos.size() > 0) {
                return ReturnResult.errorResult("该分区包含照片，无法删除！");
            }
            if (activityAlbumService.delete(albumId) > 0) {
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityAlbumController.deleteById", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 批量 修改信息  ，带添加，删除
     *
     * @param request
     * @param activityId
     * @param activityAlbums
     * @return
     */
    @PUT
    @Path("/batchUpdate/{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map batchUpdate(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId,
                           List<ActivityAlbum> activityAlbums) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            if (!activityService.checkOwner(request, activityId) || !userLevel.equals(1) || !activityImpowerService.isPhotoAdminImpower(activityId, userId)) {
                throw new UnPermissionException();
            }
            Activity activity = activityService.getById(activityId);
            if (activity == null || activity.getUserId() == null) {  // 根据会议创建者 更新 相册创建者
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
            int resultCode = activityAlbumService.batchUpdate(activityId, activity.getUserId(), activityAlbums);
            if (resultCode < 0) {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }

            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityAlbumController.batchUpdate", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 批量 保存信息
     *
     * @param request
     * @param activityId
     * @param activityAlbums
     * @return
     */
    @POST
    @Path("/batchSave/{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map batchSave(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId,
                         List<ActivityAlbum> activityAlbums) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            if (!activityService.checkOwner(request, activityId) || !userLevel.equals(1) || !activityImpowerService.isPhotoAdminImpower(activityId, userId)) {
                throw new UnPermissionException();
            }
            Activity activity = activityService.getById(activityId);
            if (activity == null || activity.getUserId() == null) {  // 根据会议创建者 更新 相册创建者
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            int resultCode = activityAlbumService.batchSave(activityId, activity.getUserId(), activityAlbums);
            if (resultCode < 0) {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityAlbumController.batchSave", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
