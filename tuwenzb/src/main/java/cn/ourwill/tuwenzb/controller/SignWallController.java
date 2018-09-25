package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.SignWall;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IBulletScreenService;
import cn.ourwill.tuwenzb.service.ISignWallService;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
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
@Path("/signWall")
public class SignWallController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ISignWallService signWallService;

    private static final Logger log = LogManager.getLogger(SignWallController.class);

    /**
     * 签到
     * @param request
     * @param roomId
     * @return
     */
    @POST
    @Path("/{roomId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map save(@Context HttpServletRequest request,@PathParam("roomId") Integer roomId){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            User user = userService.getById(userId);
            List<SignWall> signWallList = signWallService.getByUserId(userId,roomId);
            if(signWallList!=null&&signWallList.size()>0){
                SignWall reSignWall = signWallList.get(0);
                reSignWall.setRanking(signWallService.getRanking(roomId,reSignWall.getCTime()));
                return ReturnResult.successResult("data",reSignWall,"不能重复签到！");
            }
            SignWall signWall = new SignWall();
            signWall.setRoomId(roomId);
            signWall.setUserId(userId);
            signWall.setNickname(user.getNickname());
            signWall.setAvatar(user.getAvatar());
            signWall.setCTime(new Date());
            if (signWallService.save(signWall) > 0) {
                signWall.setRanking(signWallService.getRanking(roomId,signWall.getCTime()));
                return ReturnResult.successResult("data",signWall,"签到成功");
            }
            return ReturnResult.successResult("签到成功");
        }catch (Exception e){
            log.info("SignWallController.save",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取签到信息
     * @param request
     * @param roomId
     * @return
     */
    @GET
    @Path("/{roomId}/isSign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getIsSign(@Context HttpServletRequest request,@PathParam("roomId") Integer roomId){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            List<SignWall> signWallList = signWallService.getByUserId(userId,roomId);
            if(signWallList!=null&&signWallList.size()>0){
                return ReturnResult.successResult("data",true,ReturnType.GET_SUCCESS);
            }
            return ReturnResult.successResult("data",false,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.info("SignWallController.getIsSign",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }



    /**
     * 获取签到列表
     * @param request
     * @param roomId
     * @return
     */
    @GET
    @Path("/{roomId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map findByRoom(@Context HttpServletRequest request,@PathParam("roomId") Integer roomId,@QueryParam("time") Long time){
        try {
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            if(time!=null)
                date = new Date(time);
            List<SignWall> signWalls = signWallService.getByRoomId(roomId,date);
            return ReturnResult.successResult("data",signWalls,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.info("SignWallController.findByRoom",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
