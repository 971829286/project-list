package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.PurchaseApplication;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.entity.WatchList;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IPurchaseApplicationService;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import cn.ourwill.tuwenzb.weixin.pojo.UserInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jdk.nashorn.internal.runtime.ECMAException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 　ClassName:PurchaseApplicationController
 * Description：
 * User:hasee
 * CreatedDate:2017/7/3 17:41
 */

@Component
@Path("/user")
public class PurchaseApplicationController {
    @Autowired
    private IPurchaseApplicationService purchaseApplicationService;
    @Autowired
    private IUserService userService;

    private static final Logger log = LogManager.getLogger(PurchaseApplicationController.class);

    //用于购买直播信息提交
    @Path("/buyLicense")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map buyLicense(@Context HttpServletRequest request, @Context HttpServletResponse response, @FormParam("userType")Integer userType,@FormParam("licenseType") Integer licenseType){
        Map<String,Object> map=new HashMap();
        Integer userId = GlobalUtils.getUserId(request);
        if(userId!=null) {
            //更改用户类型（（0:个人 1:企业））
            userService.updateUserType(userType,userId);
            //提交购买信息
            PurchaseApplication purchaseApplication=new PurchaseApplication();
            //购买授权类型（1:包年  2:包时长  9:永久）
            purchaseApplication.setLicenseType(licenseType);
            //设置创建时间为当前时间
            purchaseApplication.setCTime(new Date());
            //设置购买用户id
            purchaseApplication.setUserId(userId);
            //申请状态(0:未回复 1:购买成功 2:购买失败)
            purchaseApplication.setStatus(0);
            //保存购买信息
            Integer id=purchaseApplicationService.save(purchaseApplication);

            map.put("status","200");
            map.put("msg","success");
            return map;
        }
        map.put("status","500");
        map.put("msg","buy error");
        return map;
    }
    //联系购买信息提交
    @Path("/buyContact")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map buyContact(@Context HttpServletRequest request, @Context HttpServletResponse response,
                          PurchaseApplication purchaseApplication){
        Map<String,Object> map=new HashMap();
        Integer userId = GlobalUtils.getUserId(request);
        if(userId!=null) {
            //用户id
            purchaseApplication.setUserId(userId);
            purchaseApplication.setCTime(new Date());
            purchaseApplication.setStatus(0);
            purchaseApplicationService.save(purchaseApplication);
//            map.put("status","200");
//            map.put("msg","success");
            return ReturnResult.successResult(null,null, ReturnType.ADD_SUCCESS);
        }
//        map.put("status","500");
//        map.put("msg","submit error");
        return ReturnResult.errorResult("获取用户信息失败！");
    }

    @POST
    @Path("/getApplicationsList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map selectByParams(@Context HttpServletRequest request,@QueryParam("pageNum") @DefaultValue("1") Integer pageNum,
                              @QueryParam("pageSize")@DefaultValue("10") Integer pageSize,
                              Map params){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            User user = userService.getById(userId);
            if(params==null) params = new HashMap();
            PageHelper.startPage(pageNum, pageSize);
//            PageHelper.orderBy(" c_time desc");
            PageInfo<PurchaseApplication> pageInfo = new PageInfo<>(purchaseApplicationService.selectByParams(params));
            return ReturnResult.successResult("data", pageInfo, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.info("PurchaseApplicationController.selectByParams",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
