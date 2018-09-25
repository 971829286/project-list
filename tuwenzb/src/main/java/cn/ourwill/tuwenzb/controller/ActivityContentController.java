package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.aop.UnPermissionException;
import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityContent;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IActivityContentService;
import cn.ourwill.tuwenzb.service.IActivityImpowerService;
import cn.ourwill.tuwenzb.service.IActivityService;
import cn.ourwill.tuwenzb.service.IUserService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 　ClassName:ActivityController
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 16:10
 */
@Component
@Path("/content")
public class ActivityContentController {
    @Autowired
    private IActivityContentService activityContentService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IActivityImpowerService activityImpowerService;

    private static final Logger log = LogManager.getLogger(ActivityContentController.class);
    //@Path请求路径
    //@Path("参数名称：正则表达式")
    //如：@Path("{from:\\d+}-{to:\\d+}")
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

    //获取最新直播内容
    @Path("/getRecentActivity/{activityId}/{pageNum}/{pageSize}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map getRecentActivity(@PathParam("activityId") Integer activityId,
                                 @DefaultValue("1")@PathParam("pageNum") Integer pageNum,
                                 @DefaultValue("10")@PathParam("pageSize") Integer pageSize,
                                 @DefaultValue("0")@QueryParam("timeOrder")Integer timeOrder,
                                 @QueryParam("date") String date){
        //timeOrder：是否以时间排序，0为倒叙，1为正序
        String tOrder = "DESC";
        if(timeOrder == 1)
            tOrder = "ASC";
        try {
            //开始分页,pageNum:页码   pageSize:每页显示数量
            PageHelper.startPage(pageNum, pageSize);
            List<ActivityContent> list = activityContentService.getRecentActivity(activityId,date,tOrder);
            PageInfo<ActivityContent> pages = new PageInfo<>(list);
            return ReturnResult.successResult("data",pages, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("ActivityContentController.getRecentActivity",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    //新建直播内容
    @Path("/add/{activityId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map save(@Context HttpServletRequest request, @PathParam("activityId") Integer activityId,ActivityContent activityContent){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request,"userLevel");
            Activity activity = activityService.getById(activityId);
            if (!activity.getUserId().equals(userId)&&!userLevel.equals(1)&&!activityImpowerService.isImpower(activityId,userId,activity.getIsImpower())) {
                throw new UnPermissionException();
            }
            if(activity.getStatus().equals(4)){
                User ownerUser = userService.getById(activity.getUserId());
                if(ownerUser.getUserType()==null||!ownerUser.getUserType().equals(2)) {
                    Integer count = activityContentService.getContentCount(activityId);
                    if (count >= 10) {
                        return ReturnResult.errorResult("示例活动内容不能超过10条！");
                    }
                }
            }
            Date now = new Date();
            if(!userLevel.equals(1)&&activity.getStartTime()!=null&&activity.getEndTime()!=null&&(activity.getStartTime().after(now)||now.after(activity.getEndTime()))){
                return ReturnResult.errorResult("不在直播时间段内，无法创建内容！");
            }
            activityContent.setActivityId(activityId);
            //设置创建日期
            activityContent.setCTime(new Date());
            if (activityContentService.save(activityContent) > 0) {
                return ReturnResult.successResult(null, null, ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        }catch (UnPermissionException e){
//            log.error("ActivityContentController.save",e);
            throw e;
        }catch (Exception e){
            log.error("ActivityContentController.save",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    //删除直播内容
    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map delete(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request,"userLevel");
//            User user = userService.getById(userId);
            ActivityContent content = activityContentService.getById(id);
            if (content != null) {
                Activity activity = activityService.getById(content.getActivityId());
                if (!userLevel.equals(1)&&!activity.getUserId().equals(userId)&&!activityImpowerService.isImpower(activity.getId(),userId,activity.getIsImpower())) {
                    throw new UnPermissionException();
                }
                int count = activityContentService.delete(id);
                if (count > 0) {
                    return ReturnResult.successResult(null, null, ReturnType.DELETE_SUCCESS);
                }
            }
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        }catch(UnPermissionException e){
            log.error("ActivityContentController.delete",e);
            throw e;
        }catch (Exception e){
            log.error("ActivityContentController.delete",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    //修改直播内容
    @Path("/update/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map update(@Context HttpServletRequest request,@PathParam("id") Integer id,ActivityContent activityContent){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request,"userLevel");
//            User user = userService.getById(userId);
            ActivityContent content = activityContentService.getById(id);
            if(content != null) {
                Activity activity = activityService.getById(content.getActivityId());
                if (!userLevel.equals(1)&&!activity.getUserId().equals(userId)&&!activityImpowerService.isImpower(activity.getId(),userId,activity.getIsImpower())) {
                    throw new UnPermissionException();
                }
                //设置直播id
                activityContent.setId(id);
                //设置创建日期
                activityContent.setUTime(new Date());
                activityContent.setStickSign(content.getStickSign());
                if (activityContentService.update(activityContent) > 0) {
                    return ReturnResult.successResult("data", activityContent, ReturnType.UPDATE_SUCCESS);
                }
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch(UnPermissionException e){
            log.error("ActivityContentController.update",e);
            throw e;
        }catch (Exception e){
            log.error("ActivityContentController.update",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    //获取所有直播图片
    @Path("/{id}/allImgUrls")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map selectAllImgs(@Context HttpServletRequest request,@PathParam("id")Integer activityId){
        return ReturnResult.successResult("data",activityContentService.getAllImgByActivityId(activityId),ReturnType.GET_SUCCESS);
    }

    /**
     * 导出word
     */
    @GET
    @Path("/{activityId}/exportWord")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map exportWord(@Context HttpServletRequest request,@PathParam("activityId") Integer activityId){
        try {
            //获取当前用户id
            Integer userId = GlobalUtils.getUserId(request);
            String key = activityContentService.exportWord(activityId);
//            User user = userService.getById(userId);
//            activityService.updataImpower(activityId,isImpower);
            if(key!=null) {
                return ReturnResult.successResult("data",key,ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult("生成失败！请重试");
        }catch(Exception e){
            log.error("ActivityController.exportWord",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 置顶and取消置顶
     * @param id
     * @return
     */
    @POST
    @Path("/stick/{id}/{stickSign}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map stickActivityContent(@PathParam("id") Integer id,@PathParam("stickSign")Integer stickSign){

        try{
            if(stickSign == 0){
                activityContentService.stickActivityContent(id, stickSign);
                return ReturnResult.successResult("取消置顶成功！" );
            }else {
                Integer stickNum = activityContentService.getStickNum(id);
                if (stickNum < 3){
                    activityContentService.stickActivityContent(id, stickSign);
                    return ReturnResult.successResult("置顶成功！" );
                }else {
                    return ReturnResult.errorResult("置顶数超过3个，请先取消置顶");
                }
            }
        }catch (Exception e){
            log.error("ActivityController.exportWord",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取当前时间的前/后几条数据
     * @param activityId 活动id
     * @param time 时间
     * @param timeOrder 时间排序，0为倒叙，1为正序
     * @param contentNum 获取的数量
     * @return
     */
    @Path("/getNewContentList/{activityId}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getNewContentList(@PathParam("activityId") Integer activityId,@QueryParam("time") Long time,@DefaultValue("0")@QueryParam("timeOrder")Integer timeOrder,@QueryParam("contentNum")Integer contentNum){

        try{
            Map remap = new HashMap();
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            if(time != null){
                date = new Date(time);
            }
            List<ActivityContent> list = activityContentService.getRecentConcentByTime(activityId,date,timeOrder,contentNum);
            String maxTime = activityContentService.getMaxTime(activityId);
            if (!"".equals(maxTime) && null != maxTime){
                Date date1 = format.parse(maxTime);
                Long s = date1.getTime();
                remap.put("maxTime",s);
            }else {
                remap.put("maxTime",maxTime);
            }
            remap.put("data",list);
            return ReturnResult.successResult("data" ,remap, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("ActivityController.getNewContentList",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }

}
