package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.aop.UnPermissionException;
import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityAlbum;
import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import cn.ourwill.tuwenzb.entity.ActivityPhotoToken;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.*;
import cn.ourwill.tuwenzb.utils.*;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2017/11/3 0003 11:37
 * @Description 照片直播内容
 */
@Component
@Path("/activityPhoto")
public class ActivityPhotoController {
    @Autowired
    private IActivityPhotoService   activityPhotoService;
    @Autowired
    private IActivityService        activityService;
    @Autowired
    private IActivityImpowerService activityImpowerService;
    @Autowired
    private IQiniuService           qiniuService;
    @Autowired
    private IActivityAlbumService   activityAlbumService;
    @Autowired
    private IFaceServer             faceServer;

    @Autowired
    private IActivityPhotoTokenService activityPhotoTokenService;
    @Autowired
    private IFacePlusServer            facePlusServer;
    @Value("${upload.photo.bucketDomain}")
    private String                     bucketDomain;

    private static final Logger log = LogManager.getLogger(ActivityPhotoController.class);

    /**
     * 保存(停用)
     *
     * @param activityPhoto
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map save(@Context HttpServletRequest request, ActivityPhoto activityPhoto) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Activity activity = activityService.selectByAlbumId(activityPhoto.getAlbumId());
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            if (!activity.getUserId().equals(userId) && !userLevel.equals(1) && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId) && !activityImpowerService.isPhotoImpower(activity.getId(), activityPhoto.getAlbumId(), userId)) {
                throw new UnPermissionException();
            }
            Date now = new Date();
            if (!userLevel.equals(1) && activity.getStartTime() != null && activity.getEndTime() != null && (activity.getStartTime().after(now) || now.after(activity.getEndTime()))) {
                return ReturnResult.errorResult("不在直播时间段内，无法发布照片！");
            }
            activityPhoto.setUserId(userId);
            qiniuService.redExif(activityPhoto);
            activityPhotoService.save(activityPhoto);
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityPhotoController.save", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 批量保存
     */
    @POST
    @Path("/batchSave/{albumId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map batchSave(@Context HttpServletRequest request, @PathParam("albumId") Integer albumId, List<ActivityPhoto> photoList) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Activity activity = activityService.selectByAlbumId(albumId);
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            if (!activity.getUserId().equals(userId) && !userLevel.equals(1) && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId) && !activityImpowerService.isPhotoImpower(activity.getId(), albumId, userId)) {
                throw new UnPermissionException();
            }
//            Date now = new Date();
//            if(!userLevel.equals(1)&&activity.getStartTime()!=null&&activity.getEndTime()!=null&&(activity.getStartTime().after(now)||now.after(activity.getEndTime()))){
//                return ReturnResult.errorResult("不在直播时间段内，无法发布照片！");
//            }
            if (activity.getStatus().equals(4)) {
                Integer count = activityPhotoService.getPhotoCount(activity.getId());
                if (count + photoList.size() >= 20) {
                    return ReturnResult.errorResult("示例活动照片不能超过20张！");
                }
            }
            Date now = new Date();
            Integer photoStatus = 0;
            //在直播期间并且自动发布打开，则自动发布照片
            if (activity.getStartTime() != null && activity.getEndTime() != null && activity.getStartTime().before(now) && now.before(activity.getEndTime()))
                photoStatus = 1;
            Map reMap = activityPhotoService.batchSave(userId, albumId, photoList, photoStatus);
            return ReturnResult.successResult("data", reMap, ReturnType.ADD_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityPhotoController.batchSave", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 批量保存(app端)
     */
    @POST
    @Path("/batchSaveByApp/{albumId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map batchSaveByApp(@Context HttpServletRequest request, @PathParam("albumId") Integer albumId, List<ActivityPhoto> photoList) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Activity activity = activityService.selectByAlbumId(albumId);
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            if (!activity.getUserId().equals(userId) && !userLevel.equals(1) && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId) && !activityImpowerService.isPhotoImpower(activity.getId(), albumId, userId)) {
                throw new UnPermissionException();
            }
            Date now = new Date();
            Integer isAutoPublish = activity.getIsAutoPublish();
            Integer photoStatus = 0;
            //在直播期间并且自动发布打开，则自动发布照片
            if (isAutoPublish == 1 && activity.getStartTime() != null && activity.getEndTime() != null && activity.getStartTime().before(now) && now.before(activity.getEndTime()))
                photoStatus = 1;
            Map reMap = activityPhotoService.batchSaveByApp(userId, albumId, photoList, photoStatus);
            return ReturnResult.successResult("data", reMap, ReturnType.ADD_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityPhotoController.batchSave", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 批量删除
     */
    @POST
    @Path("/batchDelete/{albumId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map batchDelete(@Context HttpServletRequest request, @PathParam("albumId") Integer albumId, List<Integer> photoIds) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            int success = photoIds.size();
            int failed = 0;
            Activity activity = activityService.selectByAlbumId(albumId);
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            if (!activity.getUserId().equals(userId) && !userLevel.equals(1) && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId) && !activityImpowerService.isPhotoImpower(activity.getId(), albumId, userId)) {
                throw new UnPermissionException();
            }
            int count = activityPhotoService.batchDelete(photoIds, albumId);
            if (count > 0) {
                failed = success - count;
                success = count;
                Map<String, Integer> reMap = new HashMap();
                reMap.put("success", success);
                reMap.put("failed", failed);
                return ReturnResult.successResult("data", reMap, ReturnType.DELETE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityPhotoController.batchDelete", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 批量发布
     */
    @POST
    @Path("/batchPublishOn/{albumId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map batchPublishOn(@Context HttpServletRequest request, @PathParam("albumId") Integer albumId, List<Integer> photoIds) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Activity activity = activityService.selectByAlbumId(albumId);
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            if (!activity.getUserId().equals(userId) && !userLevel.equals(1) && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId) && !activityImpowerService.isPhotoImpower(activity.getId(), albumId, userId)) {
                throw new UnPermissionException();
            }
            Date now = new Date();
            if (!userLevel.equals(1) && activity.getStartTime() != null && activity.getEndTime() != null && (activity.getStartTime().after(now) || now.after(activity.getEndTime()))) {
                return ReturnResult.errorResult("不在直播时间段内，无法发布照片！");
            }
            int count = activityPhotoService.batchPublish(photoIds, albumId, 1);
            if (count > 0) {
                //日志
                activityPhotoService.addPhotoLog(albumId, userId, count, 0);
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityPhotoController.batchPublishOn", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 批量取消发布
     */
    @POST
    @Path("/batchPublishOff/{albumId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map batchPublishOff(@Context HttpServletRequest request, @PathParam("albumId") Integer albumId, List<Integer> photoIds) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Activity activity = activityService.selectByAlbumId(albumId);
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            if (!activity.getUserId().equals(userId) && !userLevel.equals(1) && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId) && !activityImpowerService.isPhotoImpower(activity.getId(), albumId, userId)) {
                throw new UnPermissionException();
            }
            int count = activityPhotoService.batchPublish(photoIds, albumId, 0);
            if (count > 0) {
                //日志
                activityPhotoService.addPhotoLog(albumId, userId, count, 1);
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityPhotoController.batchPublishOff", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 照片点赞
     */
    @POST
    @Path("/addLike/{photoId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map addLike(@Context HttpServletRequest request, @PathParam("photoId") Integer photoId) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            ActivityPhoto activityPhoto = activityPhotoService.getById(photoId);
            if (activityPhoto == null) {
                return ReturnResult.errorResult("无此照片！");
            }
            if (activityPhotoService.addLike(photoId, userId)) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult("重复点赞！");
        } catch (Exception e) {
            log.error("ActivityPhotoController.addLike", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 照片取消点赞
     */
    @POST
    @Path("/cancelLike/{photoId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map cancelLike(@Context HttpServletRequest request, @PathParam("photoId") Integer photoId) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            if (activityPhotoService.cancelLike(photoId, userId)) {
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }
            return ReturnResult.errorResult("没有点赞，无法取消");
        } catch (Exception e) {
            log.error("ActivityPhotoController.cancelLike", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
//
//    /**
//     * 照片替换(停用)
//     * @param request
//     * @param photoId
//     * @param params
//     * @return
//     */
//    @POST
//    @Path("/replace/{photoId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Access
//    public Map replaceByPhotoId(@Context HttpServletRequest request,@PathParam("photoId") Integer photoId,Map params){
//        try{
//            Integer userId = GlobalUtils.getUserId(request);
//            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request,"userLevel");
//            String photoPath = (String) params.get("photoPath");
//            ActivityPhoto originPhoto = activityPhotoService.getById(photoId);
//            Activity activity = activityService.selectByAlbumId(originPhoto.getAlbumId());
//            if (!activity.getUserId().equals(userId)&&!userLevel.equals(1)&&!activityImpowerService.isImpower(activity.getId(),userId,activity.getIsImpower())) {
//                throw new UnPermissionException();
//            }
//            return activityPhotoService.replacePhoto(photoPath,originPhoto);
//        }catch (UnPermissionException e){
//            throw e;
//        }catch (Exception e){
//            log.error("ActivityPhotoController.replaceByPhotoId",e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }
//
//    /**
//     * 照片替换（停用）
//     * @param request
//     * @param albumId
//     * @param params
//     * @return
//     */
//    @POST
//    @Path("/replaceByName/{albumId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Access
//    public Map batchReplace(@Context HttpServletRequest request,@PathParam("albumId") Integer albumId,Map params){
//        try{
//            String photoPath = (String) params.get("photoPath");
//            Integer userId = GlobalUtils.getUserId(request);
//            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request,"userLevel");
//            Activity activity = activityService.selectByAlbumId(albumId);
//            if (!activity.getUserId().equals(userId)&&!userLevel.equals(1)&&!activityImpowerService.isImpower(activity.getId(),userId,activity.getIsImpower())) {
//                throw new UnPermissionException();
//            }
//            return activityPhotoService.replacePhotoByName(photoPath,activity.getId(),albumId);
//        }catch (UnPermissionException e){
//            throw e;
//        }catch (Exception e){
//            log.error("ActivityPhotoController.batchReplace",e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }

    /**
     * 照片移动
     *
     * @param request
     * @param activityId
     * @return
     */
    @POST
    @Path("/photoMove/{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map photoMove(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId, Map params) {
        try {
            Integer fAlbumId = (Integer) params.get("fAlbumId");
            Integer tAlbumId = (Integer) params.get("tAlbumId");
            List<Integer> photoIds = (List<Integer>) params.get("photoIds");
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
            Activity activity = activityService.getById(activityId);
            if (!activity.getUserId().equals(userId) && !userLevel.equals(1) && !activityImpowerService.isPhotoImpower(activity.getId(), fAlbumId, userId)
                    && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId) && !activityImpowerService.isPhotoImpower(activity.getId(), tAlbumId, userId)) {
                throw new UnPermissionException();
            }
            List<ActivityAlbum> albums = activityAlbumService.selectByActivity(activityId);
            long count = albums.stream().filter(album -> (album.getId().equals(fAlbumId) || album.getId().equals(tAlbumId))).count();
            if (count < 2) {
                return ReturnResult.errorResult("相册id错误！");
            }
            List<Integer> list = activityPhotoService.photoMove(activityId, fAlbumId, tAlbumId, photoIds);
            if (list != null && list.size() > 0) {
                activityPhotoService.addPhotoLog(fAlbumId, userId, list.size(), 3);
                activityPhotoService.addPhotoLog(tAlbumId, userId, list.size(), 4);

                return ReturnResult.successResult("data", list, "移动成功！");
            }
            return ReturnResult.errorResult("移动失败！");
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityPhotoController.photoMove", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新下载状态
     *
     * @param request
     * @param photoId
     * @return
     */
    @POST
    @Path("/downloadStatus/{photoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map updateDownLoadStatus(@Context HttpServletRequest request, @PathParam("photoId") Integer photoId) {
        try {
            activityPhotoService.updateDownLoadStatus(photoId);
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error("ActivityPhotoController.updateDownLoadStatus", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 照片替换
     *
     * @param request
     * @param form
     * @return
     */
    @POST
    @Path("/replace")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map doReplace(@Context HttpServletRequest request, FormDataMultiPart form) {
        String key = null;
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request, "userLevel");

            FormDataBodyPart p = form.getField("file");
            Integer albumId = Integer.valueOf(form.getField("albumId").getValue());
//            if(form.getField("photoId")!=null)
            Integer photoId = null;
            String photoPath = null;
            if (form.getField("photoId") != null)
                photoId = Integer.valueOf(form.getField("photoId").getValue());
            if (form.getField("photoPath") != null)
                photoPath = form.getField("photoPath").getValue();
            //校验权限
            Activity activity = activityService.selectByAlbumId(albumId);
            if (!activity.getUserId().equals(userId) && !userLevel.equals(1) && !activityImpowerService.isPhotoAdminImpower(activity.getId(), userId) && !activityImpowerService.isPhotoImpower(activity.getId(), albumId, userId)) {
                throw new UnPermissionException();
            }
            if (photoId == null || StringUtil.isEmpty(photoPath)) {
                FormDataContentDisposition detail = p.getFormDataContentDisposition();
                String photoName = new String(detail.getFileName().getBytes("ISO-8859-1"), "UTF-8");
                log.info("PhotoName:" + photoName);
                ActivityPhoto originPhoto = activityPhotoService.getByPhotoName(photoName, albumId);
                if (originPhoto != null) {
//                    if(originPhoto.getPhotoStatus().equals(1))
//                        return ReturnResult.errorResult("已发布的照片无法替换！");
                    photoPath = originPhoto.getPhotoPath();
                    photoId = originPhoto.getId();
                } else {
                    return ReturnResult.errorResult("无法匹配路径！");
                }
            }
            if (StringUtil.isEmpty(photoPath)) {
                return ReturnResult.errorResult("无法匹配路径！");
            }
            InputStream is = p.getValueAs(InputStream.class);
            String newPhotoPath = photoPath.replace(photoPath.substring(photoPath.lastIndexOf("_") + 1, photoPath.lastIndexOf(".") + 1), String.valueOf(System.currentTimeMillis()) + ".");
            key = qiniuService.upload(is, newPhotoPath, null, "application/octet-stream");
            //更新数据库状态
            ActivityPhoto activityPhoto = new ActivityPhoto();
            activityPhoto.setId(photoId);
            activityPhoto.setPhotoPath(key);
            activityPhoto.setReplaceStatus(1);
            activityPhoto.setPhotoStatus(1);
            int count = activityPhotoService.update(activityPhoto);
            if (count > 0) {
                //日志
                activityPhotoService.addPhotoLog(albumId, userId, count, 2);
                qiniuService.delete(photoPath);
            }
//            activityPhotoService.updateReplaceStatus(photoId);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception ex) {
            log.error("PhotoUploadController.doUploads", ex);
            return ReturnResult.errorResult("替换失败！");
        }
        return ReturnResult.successResult("data", bucketDomain + key, "替换成功！");
    }

    @Path("/updateStatus/{albumId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map getUpdateStatus(@Context HttpServletRequest request, @PathParam("albumId") Integer albumId, @QueryParam("photoStatus") Integer photoStauts, @QueryParam("time") Long time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (time == null)
                return null;
            Date date = new Date(time);
            Map reMap = activityPhotoService.selectCountAfterUTime(albumId, photoStauts, date);
            return reMap;
        } catch (Exception e) {
            log.error("PhotoUploadController.getUpdateStatus", e);
            return null;
        }
    }

    @Path("/photoMerge")
    @POST
    public void photoMerge(@Context HttpServletRequest request, @Context HttpServletResponse response, List<String> paths) {
        try {
//            String[] files = (String[])paths.toArray();
            InputStream[] inputStreams = ImgUtil.getImageByUrl(paths);
            ImgUtil.addImgWatermark(inputStreams, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Path("/searchByUrl/{activityId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Map searchByUrl(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("activityId") Integer activityId, @QueryParam("imageUrl") String imageUrl) {
        if (activityId == null || StringUtils.isEmpty(imageUrl)) {
            return ReturnResult.errorResult("参数不完整");
        }
        Activity activity = activityService.getById(activityId);
        if (activity.getIsOpenFaceSearch() == 0) {
            return ReturnResult.errorResult("该活动尚未开启人脸查找功能");
        }
        String faceSetToken = activity.getFaceSetToken();
        if (StringUtils.isEmpty(faceSetToken)) {
            faceSetToken = activityService.getFaceSetTokenByFacePlus(activityId);
        }
        if (StringUtils.isNotEmpty(faceSetToken) && StringUtils.isNotEmpty(imageUrl)) {
            List<String> faceTokens = facePlusServer.search(faceSetToken, imageUrl);
            if (faceTokens != null && faceTokens.size() > 0) {
                List<ActivityPhoto> resList = activityPhotoService.getByFaceTokenList(faceTokens, activityId);
                //过滤未发布和已删除的
                Predicate<ActivityPhoto> isOpen = e -> e.getPhotoStatus() != 1;
                resList.removeIf(isOpen);
                return ReturnResult.successResult("data", resList, ReturnType.GET_SUCCESS);
            }
        }
        return ReturnResult.errorResult("查询结果不存在");
    }

    @Path("/searchByFile/{activityId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Map searchByFile(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("activityId") Integer activityId, FormDataMultiPart form) throws IOException {
        FormDataBodyPart image = form.getField("image");
        if (image == null) {
            return ReturnResult.errorResult("参数不正确");
        }
        String imageType = image.getMediaType().getSubtype();
        Activity activity = activityService.getById(activityId);
        if (activity.getIsOpenFaceSearch() == 0) {
            return ReturnResult.errorResult("该活动尚未开启人脸查找功能");
        }
        if (imageType.equalsIgnoreCase("jpeg") || imageType.equalsIgnoreCase("jpg") || imageType.equalsIgnoreCase("png") || imageType.equalsIgnoreCase("webp")) {
            String faceSetToken = activity.getFaceSetToken();
            if (StringUtils.isEmpty(faceSetToken)) {
                faceSetToken = activityService.getFaceSetTokenByFacePlus(activityId);
            }
            InputStream inputStream = null;
            if (imageType.equalsIgnoreCase("webp")) {
                File file = ImgUtil.webpToPng(image.getValueAs(File.class));
                inputStream = new FileInputStream(file);
            } else {
                File file = image.getValueAs(File.class);
                if (file.length() > 2097152 && file.length() < 4194304) {
                    FileInputStream fis = new FileInputStream(file);
                    inputStream = ImgUtil.getCompressInputStream(fis, imageType);
                } else if (file.length() >= 4194304) {
                    return ReturnResult.errorResult("图片大于4M");
                } else {
                    inputStream = image.getValueAs(InputStream.class);
                }
            }
            InputStream resInputStream = ImgUtil.resetDirectory(inputStream, imageType);
            if (activity != null && activity.getIsOpenFaceSearch() == 1) {
                List<String> faceTokens = facePlusServer.searchByFile(faceSetToken, resInputStream, imageType);
                inputStream.close();
                Files.list(Paths.get("tmpFile")).forEach(e -> {
                    try {
                        Files.deleteIfExists(e);
                    } catch (Exception a) {
                        a.printStackTrace();
                    }
                });
                if (StringUtils.isNotEmpty(faceSetToken)) {
                    if (faceTokens != null && faceTokens.size() > 0) {
                        List<ActivityPhoto> resList = activityPhotoService.getByFaceTokenList(faceTokens, activityId);
                        //过滤未发布和已删除的
                        Predicate<ActivityPhoto> isOpen = e -> e.getPhotoStatus() != 1;
                        resList.removeIf(isOpen);
                        if (resList != null && resList.size() > 0) {
                            return ReturnResult.successResult("data", resList, ReturnType.GET_SUCCESS);
                        }
                    }
                    return ReturnResult.errorResult("未匹配到相关照片");
                }
            }
            if (activity != null && activity.getIsOpenFaceSearch() == 2) {
                List<String> faceTokens = facePlusServer.searchByFile(faceSetToken, resInputStream, imageType);
                inputStream.close();
                Files.list(Paths.get("tmpFile")).forEach(e -> {
                    try {
                        Files.deleteIfExists(e);
                    } catch (Exception a) {
                        a.printStackTrace();
                    }
                });
                List<Integer> ids = new ArrayList<>();
                if (faceTokens != null && faceTokens.size() > 0) {
                    for (String faceToken : faceTokens) {
                        ActivityPhotoToken tokenBean = activityPhotoTokenService.findByFaceToken(faceToken);
                        if (tokenBean != null && tokenBean.getSamePerson() != null) {
                            List<ActivityPhotoToken> tokens = activityPhotoTokenService.findBySamePerson(tokenBean.getSamePerson());
                            if (tokens != null && tokens.size() > 0) {
                                ids.addAll(tokens.stream().map(e -> e.getPhotoId()).collect(Collectors.toList()));
                            }
                        } else if (tokenBean != null && StringUtils.isEmpty(tokenBean.getSamePerson())) {
                            ids.add(tokenBean.getPhotoId());
                        }
                    }
                }
                List<Integer> collect = ids.stream().distinct().collect(Collectors.toList());
                List<ActivityPhoto> resList = activityPhotoService.getByIds(collect);
                Predicate<ActivityPhoto> isOpen = e -> e.getPhotoStatus() != 1;
                resList.removeIf(isOpen);
                if (resList != null && resList.size() > 0) {
                    return ReturnResult.successResult("data", resList, ReturnType.GET_SUCCESS);
                }
                return ReturnResult.errorResult("未匹配到相关照片");
            }
        }
        return ReturnResult.errorResult("仅支持png,jpg,jpeg样式的图片");
    }

    @Path("/package/{activityId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Map packagePics(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId,@RequestBody List<Integer> photoIds) {
        String downloadFileName = activityId+"/"+TimeUtils.getFileNameByDate();
        List<String> pictures = activityPhotoService.getUrlsByIds(photoIds);
        String resultId = qiniuService.packagePics(pictures, downloadFileName);

        if (StringUtils.isEmpty(resultId))
            return ReturnResult.errorResult("---打包失败---");
        else
            return ReturnResult.successResult("data", resultId, ReturnType.ADD_SUCCESS);

    }

    @Path("/packageStatus/{packageId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map packageStatus(@Context HttpServletRequest request, @PathParam("packageId") String packageId) {
        try {
            Map data = qiniuService.getPackageStatus(packageId);
            if (data == null)
                return ReturnResult.errorResult("---获取打包信息失败---");
            else
                return ReturnResult.successResult("data", data, ReturnType.ADD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ReturnResult.errorResult("---获取打包信息失败---");
    }
}