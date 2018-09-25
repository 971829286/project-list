package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.BlackList;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IBlackListService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/24 0024 16:48
 * @Version1.0
 */
@Component
@Path("/blackList")
public class BlackListController {
    @Autowired
    private IBlackListService blackListService;

    private static final Logger log = LogManager.getLogger(ActivityController.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map create(@Context HttpServletRequest request, BlackList blackList){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            int count = blackListService.getOperantByUserId(blackList.getUserId());
            if(count>0){
                return ReturnResult.errorResult("该用户已存在生效的黑名单！");
            }
            blackList.setCId(userId);
            if(blackListService.save(blackList)>0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        }catch (Exception e){
            log.info("BlackListController.create",e);
            return ReturnResult.errorResult("server error");
        }
    }

//    @DELETE
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Access(level = 1)
//    public Map delete(@Context HttpServletRequest request,@PathParam("id") Integer id){
//        try {
//            if (blackListService.delete(id) > 0) {
//                return ReturnResult.successResult( ReturnType.DELETE_SUCCESS);
//            }
//            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
//        }catch (Exception e){
//            e.printStackTrace();
//            return ReturnResult.errorResult("server error");
//        }
//    }

    @POST
    @Path("/unlock")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map unlock(@Context HttpServletRequest request,List<Integer> ids){
        try {
            if(ids==null||ids.size()<1){
                return ReturnResult.errorResult("参数为空！");
            }
            if (blackListService.unlock(ids) > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.info("BlackListController.unlock",e);
            return ReturnResult.errorResult("server error");
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map update(@Context HttpServletRequest request,@PathParam("id") Integer id,BlackList blackList){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            BlackList oldObj = blackListService.getById(id);
            if(oldObj==null) return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            BlackList newObj = new BlackList();
            newObj.setId(id);
            newObj.setStartDate(oldObj.getStartDate());
            newObj.setType(blackList.getType());
            newObj.setUId(userId);
            newObj.setReason(blackList.getReason());
            if (blackListService.update(newObj) > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.info("BlackListController.update",e);
            return ReturnResult.errorResult("server error");
        }
    }

    @POST
    @Path("/selectByParams")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map selectByParams(@QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
                         Map param){
        try {
            PageHelper.startPage(pageNum,pageSize);
            if(param==null) param = new HashMap();
            List<BlackList> list = blackListService.selectByParams(param);
            PageInfo<BlackList> pages = new PageInfo<>(list);
            return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.info("BlackListController.selectByParams",e);
            return ReturnResult.errorResult("server error");
        }
    }
}
