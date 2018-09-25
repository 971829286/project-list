package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.entity.WatchList;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IWatchListService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import cn.ourwill.tuwenzb.weixin.pojo.UserInfo;
import cn.ourwill.tuwenzb.weixin.pojo.UserInfoReturn;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 　ClassName:WatchListContrller
 * Description：
 * User:hasee
 * CreatedDate:2017/7/3 16:45
 */
@Component
@Path("/user")
public class WatchListContrller {

    @Autowired
    private IWatchListService watchListService;

    //用于获取用户的关注直播列表
    @Path("/getWatchList")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getWatchList(@Context HttpServletRequest request, @Context HttpServletResponse response,
                            @QueryParam("page") Integer page, @QueryParam("pageNum")@DefaultValue("1") Integer pageNum,
                            @QueryParam("pageSize")@DefaultValue("10") Integer pageSize, @QueryParam("orderBy") Integer orderBy,
                            @QueryParam("photoLive")@DefaultValue("0") Integer photoLive){
        try {
            Map<String, Object> map = new HashMap();
            Integer userId = GlobalUtils.getUserId(request);
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy(" id desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy(" id asc");
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<WatchList> pages = new PageInfo<>(watchListService.getWatchList(userId,photoLive));
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<WatchList> watchLists = watchListService.getWatchList(userId,photoLive);
            return ReturnResult.successResult("data", watchLists, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

}
