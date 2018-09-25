package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.aop.UnauthenticatedException;
import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.Comment;
import cn.ourwill.tuwenzb.entity.CommentWithActivity;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.*;
import cn.ourwill.tuwenzb.utils.*;
import cn.ourwill.tuwenzb.weixin.pojo.UserInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/4 0004 15:25
 * @Version1.0
 */

@Component
@Path("/comment")
public class CommentController {

    @Autowired
    ICommentService commentService;

    @Autowired
    IUserService userService;

    @Autowired
    IActivityService activityService;

    @Autowired
    IActivityStatisticsService activityStatisticsService;

    @Autowired
    IBlackListService blackListService;

    @Autowired
    IActivityPhotoService activityPhotoService;

    @Value("${comment.defaultAvator}")
    private String avator;

    //敏感词汇过滤开关
    @Value("${wordfilter.switch}")
    private Boolean wordfilter;

    @Value("${upload.local.path}")
    private String FILE_PATH;

    private static final Logger log = LogManager.getLogger(CommentController.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map create(@Context HttpServletRequest request, Comment comment){

        //图形验证码使用一次后，无论正确与否，从session中清除
        String verifyCodeSession = (String) GlobalUtils.getSessionValue(request, "verifyCode");
        if(StringUtils.isNotEmpty(verifyCodeSession)){
            GlobalUtils.removeSessionValue(request, "verifyCode");
        }
        try {
            Integer userId = GlobalUtils.getUserId(request);
            if (blackListService.getOperantByUserId(userId) > 0) {
                return ReturnResult.errorResult("你已被禁止评论！");
            }
            String ip = GlobalUtils.getIp(request);
            if (StringUtils.isEmpty(comment.getContent())) {
                return ReturnResult.errorResult("评论内容不能为空！");
            }
            if (userId == null) {
                if (comment.getVerifyCode() == null || !comment.getVerifyCode().trim().toUpperCase().equals(verifyCodeSession)) {
                    return ReturnResult.errorResult("验证码错误！");
                }
            }
            //敏感词汇过滤
            if(wordfilter){
                if(!WordFilter.doFilter(comment.getContent().toString()))
                return ReturnResult.errorResult("评论内容包含违禁词，发布失败！");
            }
            if (userId != null) {
                User user = userService.getById(userId);
                comment.setUserId(userId);
                comment.setNickname(user.getNickname());
                comment.setAvatar(user.getAvatar());
            }
            if (StringUtils.isEmpty(comment.getNickname())) {
                comment.setNickname(GlobalUtils.getAddressByIP(ip));
            }
            if (StringUtils.isEmpty(comment.getAvatar())) {
                comment.setAvatar(avator);
            }
            //默认审核状态为0
            comment.setCheck(0);
            comment.setIp(ip);
            comment.setCTime(new Date());
            Activity activity = activityService.getById(comment.getActivityId());
            if(activity==null)
                return ReturnResult.errorResult("评论失败");
            if(commentService.save(comment,activity.getCheckType())>0) {
                if(activity.getCheckType().equals(1)){
                    return ReturnResult.successResult("评论成功");
                }else{
                    return ReturnResult.successResult("评论已提交,审核后显示");
                }
            } else {
                return ReturnResult.errorResult("评论失败");
            }
        }catch (Exception e){
            log.info("CommentController.create",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map update(@PathParam("id") Integer id,Comment comment){
        try {
            comment.setId(id);
            if (commentService.update(comment) > 0) {
                return ReturnResult.successResult("comment", comment, ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        }catch (Exception e){
            log.info("CommentController.update",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @Path("/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map delete(@PathParam("id") Integer id){
        try{
            Comment comment = commentService.getById(id);
            if(comment==null) return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            if(commentService.delete(id)>0){
                activityStatisticsService.refushCommentNumberRedis(comment.getActivityId());
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }else{
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }
        }catch (Exception e){
            log.info("CommentController.delete",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 按id获取
     * @param id
     * @return
     */
    @Path("/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getById(@PathParam("id")Integer id){
        try {
            return ReturnResult.successResult("comment", commentService.getById(id), ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("CommentController.getById",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 按活动id获取
     * @param activityId
     * @return
     */
    @GET
    @Path("/activity/{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getByActivityId(@Context HttpServletRequest request,@PathParam("activityId") Integer activityId,
                               @QueryParam("pageNum") Integer pageNum, @QueryParam("pageSize") Integer pageSize){
        try {
            if(pageNum==null){
                pageNum=1;
            }
            if(pageSize==null){
                pageSize=10;
            }
            Map param = new HashMap();
            param.put("activityId",activityId);
            Activity activity = activityService.getById(activityId);
            if(activity==null)
                return ReturnResult.errorResult("活动不存在！");
            param.put("checkType",activity.getCheckType());
            param.put("orderBy",1);
            PageHelper.startPage(pageNum,pageSize);
            List<Comment> list = commentService.getByParamWithBack(param);
            Integer userId = GlobalUtils.getUserId(request);
            PageInfo<Comment> page = new PageInfo<>(list);
            if(userId!=null) {
                List<Comment> reList = list.stream().map(comment -> {
                    comment.setLikeNum(commentService.getLikeNum(comment.getId()));
                    comment.setLiked(commentService.isLiked(comment.getId(),userId));
                    return comment;
                }).collect(Collectors.toList());
                //重置list
                page.setList(reList);
                return ReturnResult.successResult("comment",page, ReturnType.GET_SUCCESS);
            }
            return ReturnResult.successResult("comment",page, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("CommentController.getByActivityId",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 按条件获取获取pc
     * @param
     * @return
     */
    @POST
    @Path("/selectByParams")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map selectByParams(@QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize")@DefaultValue("10") Integer pageSize,
                               Map params){
        try {
            PageHelper.startPage(pageNum,pageSize);
            if(params==null) params = new HashMap();
            PageInfo pageInfo = new PageInfo(commentService.getByParam(params));
            return ReturnResult.successResult("comment", pageInfo, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("CommentController.selectByParams",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 按活动id获取pc
     * @param activityId
     * @return
     */
    @POST
    @Path("/activity/{activityId}/pc")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getByActivityId(@PathParam("activityId") Integer activityId,
                                    @QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize")@DefaultValue("10") Integer pageSize,
                              Map params){
        try {
//            if(pageNum==null){
//                pageNum=1;
//            }
//            if(pageSize==null){
//                pageSize=10;
//            }
            PageHelper.startPage(pageNum,pageSize);
            if(params==null) params = new HashMap();
            params.put("activityId",activityId);
//            param.put("nickname",nickname);
//            param.put("ip",ip);
//            param.put("startTime",starTime);
//            param.put("endTime",endTime);
//            param.put("orderBy",orderBy);
//            param.put("check",check);
            PageInfo pageInfo = new PageInfo(commentService.getByParam(params));
            return ReturnResult.successResult("comment", pageInfo, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("CommentController.getByActivityId",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 按用户id获取评论
     * @param
     * @return
     */
    @GET
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getByUserId(@Context HttpServletRequest request,@QueryParam("pageNum") Integer pageNum, @QueryParam("pageSize") Integer pageSize,
                           @QueryParam("page") Integer page,@QueryParam("photoLive")@DefaultValue("0") Integer photoLive){
        try {
            if(pageNum==null){
                pageNum=1;
            }
            if(pageSize==null){
                pageSize=10;
            }
            Integer userId= GlobalUtils.getUserId(request);
            if(userId==null){
                return ReturnResult.errorResult("无法获取用户信息！");
            }
            PageHelper.startPage(pageNum,pageSize);
            List<CommentWithActivity> list = commentService.getByUserId(userId,photoLive);
            PageInfo<CommentWithActivity> pageInfo = new PageInfo(list);
            if(page!=null&&page==1){
                return ReturnResult.successResult("comment", pageInfo, ReturnType.GET_SUCCESS);
            }
            return ReturnResult.successResult("comment", list, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("CommentController.getByUserId",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 按用户id获取回复
     * @param
     * @return
     */
    @GET
    @Path("/user/back")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getBackByUserId(@Context HttpServletRequest request,@QueryParam("pageNum") Integer pageNum, @QueryParam("pageSize") Integer pageSize,
                           @QueryParam("page") Integer page,@QueryParam("photoLive")@DefaultValue("0") Integer photoLive){
        try {
            if(pageNum==null){
                pageNum=1;
            }
            if(pageSize==null){
                pageSize=10;
            }
            Integer userId= GlobalUtils.getUserId(request);
            if(userId==null){
                return ReturnResult.errorResult("无法获取用户信息！");
            }
            PageHelper.startPage(pageNum,pageSize);
            List<CommentWithActivity> list = commentService.getBackByUserId(userId,photoLive);
            PageInfo<CommentWithActivity> pageInfo = new PageInfo(list);
            if(page!=null&&page==1){
                return ReturnResult.successResult("comment", pageInfo, ReturnType.GET_SUCCESS);
            }
            return ReturnResult.successResult("comment", list, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("CommentController.getByUserId",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 按照片id获取
     * @param photoId
     * @return
     */
    @GET
    @Path("/photo/{photoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getByPhotoId(@Context HttpServletRequest request,@PathParam("photoId") Integer photoId,@QueryParam("pageNum")@DefaultValue("1") Integer pageNum, @QueryParam("pageSize")@DefaultValue("10") Integer pageSize){
        try {
            Activity activity = activityService.getByPhotoId(photoId);
//            activityService.
            if(activity==null)
                return ReturnResult.errorResult("无此活动！");
            PageHelper.startPage(pageNum,pageSize);
            List<Comment> list = commentService.getByPhotoId(photoId,activity.getCheckType());
            return ReturnResult.successResult("comment", list, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.info("CommentController.getByPhotoId",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 审核批量通过
     *
     */
    @POST
    @Path("/checkPass")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Response checkPassBatch(@Context HttpServletRequest request, Map param){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request,"userLevel");
            List<Integer> ids = (List<Integer>) param.get("ids");
            List<Integer> activityIds = (List<Integer>) param.get("activityIds");
            if(ids==null||ids.size()<=0){
                return Response.ok(ReturnResult.errorResult("没有提交数据！")).build();
            }
            if(activityIds==null||activityIds.size()<=0){
                return Response.ok(ReturnResult.errorResult("没有提交数据！")).build();
            }
            if(userLevel.equals(0)){
                if(activityIds.size()<1){
                    return Response.ok(ReturnResult.errorResult("没有选择数据！")).build();
                }else{
                    Activity activity = activityService.getById(activityIds.get(0));
                    if(!activity.getUserId().equals(userId)){
                        return Response.ok(ReturnResult.errorResult("权限不足！")).build();
                    }
                }
            }
            int count = commentService.updateCheckBatch(ids,2);
            if(count>0) {
                //评论数更新
                activityIds = activityIds.stream().distinct().collect(Collectors.toList());
                for (Integer activityId : activityIds) {
                    activityStatisticsService.refushCommentNumberRedis(activityId);
                }
                return Response.ok(ReturnResult.successResult("审核成功")).build();
            }else{
                return Response.ok(ReturnResult.errorResult("审核失败！")).build();
            }
        }catch(Exception e){
            log.error("CommentController.checkPassBatch",e);
            return Response.ok(ReturnResult.errorResult(ReturnType.SERVER_ERROR)).build();
        }
    }

    /**
     * 审核批量删除
     *
     */
    @POST
    @Path("/checkDelete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Response checkDeleteBatch(@Context HttpServletRequest request, Map param){
        try {
            List<Integer> ids = (List<Integer>) param.get("ids");
            List<Integer> activityIds = (List<Integer>) param.get("activityIds");
            if(ids==null||ids.size()<=0){
                return Response.ok(ReturnResult.errorResult("没有提交数据！")).build();
            }
            if(activityIds==null||activityIds.size()<=0){
                return Response.ok(ReturnResult.errorResult("没有提交数据！")).build();
            }
            int count = commentService.updateCheckBatch(ids,1);
            if(count>0) {
                //评论数更新
                activityIds = activityIds.stream().distinct().collect(Collectors.toList());
                for (Integer activityId : activityIds) {
                    activityStatisticsService.refushCommentNumberRedis(activityId);
                }
                return Response.ok(ReturnResult.successResult("删除成功")).build();
            }else{
                return Response.ok(ReturnResult.errorResult("删除失败！")).build();
            }
        }catch(Exception e){
            log.error("CommentController.checkDeleteBatch",e);
            return Response.ok(ReturnResult.errorResult(ReturnType.SERVER_ERROR)).build();
        }
    }

    /**
     *  多条评论添加
     *  wanghao
     */
    @POST
    @Path("/insertMultiple")
    @Consumes(MediaType.MULTIPART_FORM_DATA+";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON)
    public Map insertMultipleComment(@FormDataParam("file") InputStream fileInputStream,
                               @FormDataParam("file") FormDataContentDisposition disposition,
                                     @FormDataParam("activityId")Integer activityId) throws IOException{
        String fileName = Calendar.getInstance().getTimeInMillis() + disposition.getFileName();
        String file_url = FILE_PATH + fileName;
        File file = new File(file_url);
        try {
            FileUtils.copyInputStreamToFile(fileInputStream, file);
        } catch (IOException ex) {
            Logger.getLogger(CommentController.class.getName()).trace("CommentController.insertMultipleComment",ex);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }finally {
            if(fileInputStream!=null)
                fileInputStream.close();
        }
        List<Comment> comments = ExcelUtil.readExcel(file_url,fileName,activityId);
        if(comments!=null){
            if(commentService.volumeIncrease(comments)>0)
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
        }
        return ReturnResult.errorResult(ReturnType.ADD_ERROR);
    }

    /**
     * 评论点赞
     *
     */
    @POST
    @Path("/addLike/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map addLike(@Context HttpServletRequest request,@PathParam("commentId") Integer commentId) {
        try{
            Integer userId = GlobalUtils.getUserId(request);
            Comment comment = commentService.getById(commentId);
            if(comment==null){
                return ReturnResult.errorResult("无此评论！");
            }
            if(commentService.addLike(commentId,userId)) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult("重复点赞！");
        }catch (Exception e){
            log.error("CommentController.addLike",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 评论取消点赞
     *
     */
    @POST
    @Path("/cancelLike/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map cancelLike(@Context HttpServletRequest request,@PathParam("commentId") Integer commentId) {
        try{
            Integer userId = GlobalUtils.getUserId(request);
            if(commentService.cancelLike(commentId,userId)) {
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }
            return ReturnResult.errorResult("没有点赞，无法取消");
        }catch (Exception e){
            log.error("CommentController.cancelLike",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}