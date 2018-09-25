package cn.ourwill.tuwenzb.controller;


import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.BannerHome;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IBannerHomeService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import cn.ourwill.tuwenzb.weixin.pojo.UserInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/6 0006 13:56
 * 首页banner图
 * @Version1.0
 */
@Component
@Path("/banner")
public class BannerController {
    @Autowired
    private IBannerHomeService bannerHomeService;

    private static final Logger log = LogManager.getLogger(BannerController.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map create(@Context HttpServletRequest request, BannerHome bannerHome){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            if (userId != null) {
                bannerHome.setCUser(userId);
                bannerHome.setCTime(new Date());
                bannerHomeService.save(bannerHome);
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        }catch (Exception e){
            log.info("BannerController.create",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map delete(@Context HttpServletRequest request,@PathParam("id") Integer id){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            if (userId != null) {
                if (bannerHomeService.delete(id) > 0) {
                    return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
                }
            }
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        }catch (Exception e){
            log.info("BannerController.delete",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map update(@Context HttpServletRequest request,@PathParam("id") Integer id,BannerHome bannerHome){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            if (userId != null) {
                bannerHome.setUUser(userId);
                bannerHome.setId(id);
                bannerHome.setUTime(new Date());
                if (bannerHomeService.update(bannerHome) > 0) {
                    return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
                }
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.info("BannerController.update",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getById(@Context HttpServletRequest request,@PathParam("id") Integer id){
        try {
            BannerHome bannerHome = bannerHomeService.getById(id);
            if(bannerHome!=null)
                return ReturnResult.successResult("data", bannerHome, ReturnType.GET_SUCCESS);
            else
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
        }catch (Exception e){
            log.info("BannerController.selectAll",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map selectAll(@QueryParam("pageNum") Integer pageNum, @QueryParam("pageSize") Integer pageSize,@QueryParam("page") Integer page,@QueryParam("photoLive") Integer photoLive){
        try {
            if(pageNum==null){
                pageNum=1;
            }
            if(pageSize==null){
                pageSize=10;
            }
            if(photoLive==null)
                photoLive = 0;
            PageHelper.startPage(pageNum,pageSize);
            PageHelper.orderBy("priority asc");
            List<BannerHome> list = bannerHomeService.findAll(photoLive);
            PageInfo<BannerHome> pages = new PageInfo<>(list);
            if(page!=null&&page==1) {
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.info("BannerController.selectAll",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @POST
    @Path("/updatePriority")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map updatePriority(@Context HttpServletRequest request,List<Integer> ids){
        try {
            if(ids==null&&ids.size()<1){
                return ReturnResult.errorResult("参数为空！");
            }
            for (int i=0;i<=ids.size()-1;i++) {
                bannerHomeService.updatePriorityById(ids.get(i),i);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        }catch (Exception e){
            log.info("BannerController.selectAll",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
