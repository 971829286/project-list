package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.SignIn;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.ISignInService;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import com.github.pagehelper.PageHelper;
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
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:34
 * @Description
 */
@Component
@Path("/signIn")
public class SignInController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ISignInService signInService;

    private static final Logger log = LogManager.getLogger(SignInController.class);

    /**
     * 签到
     * @param request
     * @param activityId
     * @return
     */
    @POST
    @Path("/{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map save(@Context HttpServletRequest request,@PathParam("activityId") Integer activityId){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            User user = userService.getById(userId);
            List<SignIn> signInList = signInService.getByUserId(userId,activityId);
            if(signInList!=null&&signInList.size()>0){
                return ReturnResult.successResult("不能重复签到！");
            }
            SignIn signIn = new SignIn();
            signIn.setActivityId(activityId);
            signIn.setUserId(userId);
            if (signInService.save(signIn) > 0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.successResult(ReturnType.ADD_ERROR);
        }catch (Exception e){
            log.info("SignInController.save",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取签到列表
     * @param request
     * @param activityId
     * @return
     */
    @GET
    @Path("/{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map findByActivity(@Context HttpServletRequest request,@QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,@PathParam("activityId") Integer activityId,@QueryParam("time") Long time){
        try {
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            if(time!=null)
                date = new Date(time);
            PageHelper.startPage(pageNum,pageSize);
            PageHelper.orderBy("s.c_time asc");
            List<SignIn> signIns = signInService.getByActivityId(activityId,date);
            return ReturnResult.successResult("data",signIns,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.info("SignInController.findByActivity",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
