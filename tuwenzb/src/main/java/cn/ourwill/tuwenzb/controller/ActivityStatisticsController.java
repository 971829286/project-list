package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.ActivityStatistics;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IActivityStatisticsService;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * 　ClassName:ActivityController
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 16:10
 */
@Component
@Path("/activity")
public class ActivityStatisticsController {
    @Autowired
    private IActivityStatisticsService activityStatisticsService;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger log = LogManager.getLogger(ActivityStatisticsController.class);
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
    //@Consumes：客户端请求的媒体类型
    //@Produces:服务器端响应的媒体类型
    //@PUT:put请求
    //@GET:get请求
    //@DELETE：删除请求

    //直播点赞
    @Path("/addLike/{activityId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map addLikeNumber(@PathParam("activityId") Integer activityId){
        try {
            if (activityId != null) {
//                activityStatisticsService.addLikeNumber(activityId);
                activityStatisticsService.addLikeNumberRedis(activityId,1);
                activityStatisticsService.addRealLikeNumberRedis(activityId);
//                ActivityStatistics activityStatistics = activityStatisticsService.getByActivityId(activityId);
                ActivityStatistics activityStatistics = RedisUtils.getByActivityId(activityId);
                return ReturnResult.successResult("data",activityStatistics, ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.error("ActivityStatisticsController.addLikeNumber",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新redis数据
     */
    @GET
    @Path("/redisRefresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map redisRefresh(@Context HttpServletRequest request){
        try {
            List<ActivityStatistics> list = activityStatisticsService.findAll();
            if(list!=null&&list.size()>0){
                for (ActivityStatistics statistics : list) {
//                    if(!redisTemplate.hasKey(String.valueOf(statistics.getActivityId()))){
                        RedisUtils.updateActivityHash(statistics);
//                    }
                }
                return ReturnResult.successResult("更新成功！");
            }
            return ReturnResult.errorResult("暂无更新");
        }catch(Exception e){
            log.error("ActivityController.redisRefresh",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新redis数据
     */
    @GET
    @Path("/syncRedis")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map syncRedisToMySQL(@Context HttpServletRequest request){
        try {
            activityStatisticsService.syncRedisToMySQL();
            return ReturnResult.successResult("更新成功！");
        }catch(Exception e){
            log.error("ActivityController.redisRefresh",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/deleteRedis")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map syncRedis(@Context HttpServletRequest request){
        try {
            activityStatisticsService.deleteRedis();
            return ReturnResult.successResult("更新成功！");
        }catch(Exception e){
            log.error("ActivityController.redisRefresh",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 修改参与人数
     */
    @POST
    @Path("/{id}/updateStatistics")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map updateStatistics(@Context HttpServletRequest request,@PathParam("id") Integer activityId, Map params){
        try {
            Integer participantsNum = (Integer) params.get("participantsNum");
            Integer likeNum = (Integer) params.get("likeNum");
            if(participantsNum!=null)
                activityStatisticsService.updateStatistics(activityId,"participantsNum",participantsNum);
            if(likeNum!=null)
                activityStatisticsService.updateStatistics(activityId,"likeNum",likeNum);
            return ReturnResult.successResult("更新成功！");
        }catch(Exception e){
            log.error("ActivityController.updateStatistics",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 增加分享数量
     * @param activityId
     * @return
     */
    @Path("/addShare/{activityId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map addShareNumber (@PathParam("activityId") Integer activityId){
        try {
            if (activityId != null) {
                activityStatisticsService.addShareNumberRedis(activityId);
                activityStatisticsService.addRealShareNumberRedis(activityId);
                ActivityStatistics activityStatistics = RedisUtils.getByActivityId(activityId);
                return ReturnResult.successResult("data",activityStatistics, ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.error("ActivityStatisticsController.addLikeNumber",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

}
