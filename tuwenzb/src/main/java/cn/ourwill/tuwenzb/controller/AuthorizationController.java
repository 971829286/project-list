package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.LicenseRecord;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.ILicenseRecordService;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.ResultType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/22 0022 16:54
 * @Version1.0
 */
@Component
@Path("/authorize")
public class AuthorizationController {


    @Autowired
    IUserService userService;

    @Autowired
    ILicenseRecordService licenseRecordService;

    private static final Logger log = LogManager.getLogger(HelpDocumentController.class);

    /**
     * 根据Id查找
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get")
    @Access(level=1)
    public Map getById(@QueryParam("id") Integer id){
        try {
            LicenseRecord licenseRecord = licenseRecordService.getById(id);
            if (licenseRecord == null)
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            return ReturnResult.successResult("data", licenseRecord, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("AuthorizationController.getById",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 条件搜索，分页
     */
    @GET
    @Path("/getByParam")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Access(level=1)
    public Map selectByParam(@QueryParam("username") String username,@QueryParam("pageNum")Integer pageNum,@QueryParam("pageSize")Integer pageSize,@DefaultValue("0") @QueryParam("photoLive") Integer photoLive,@Context HttpServletRequest request) {
        try {
            //按照用户名查询用户ID
            Integer userId = new Integer(-1);
            if(username!=null) {
                User user = userService.selectByUsername(username.trim());
                if(user!=null)
                    userId = user.getId();
                else
                    return ReturnResult.errorResult(ReturnType.LIST_ERROR);
            }
            //查询出所有用户ID为userID的记录
            Map map = new HashMap();
            map.put("userId", userId);
            map.put("photoLive",photoLive);
            PageHelper.startPage(pageNum,pageSize);
            List<LicenseRecord> licenseRecords = licenseRecordService.getByParam(map);
            PageInfo<LicenseRecord> pages = new PageInfo<>(licenseRecords);
            System.out.println(pages.getSize());
            if (!licenseRecords.isEmpty() && licenseRecords.size() > 0)
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            return ReturnResult.errorResult(ReturnType.LIST_ERROR);

        }catch (Exception e){
            log.error("AuthorizationController.selectByParam",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 分页
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level=1)
    public Map getPage(@QueryParam("pageNum")Integer pageNum,@QueryParam("pageSize")Integer pageSize,@Context HttpServletRequest request,@DefaultValue("0") @QueryParam("photoLive") Integer photoLive){
        try {
            PageHelper.startPage(pageNum,pageSize);
            PageHelper.orderBy(" c_time desc");
            List<LicenseRecord> licenseRecords = licenseRecordService.findAll(photoLive);
            PageInfo<LicenseRecord> pages = new PageInfo<>(licenseRecords);
            if(!licenseRecords.isEmpty()&&licenseRecords.size()>0)
                return ReturnResult.successResult("data",pages,ReturnType.LIST_SUCCESS);
            return ReturnResult.errorResult(ReturnType.LIST_ERROR);
        }catch (Exception e){
            log.error("AuthorizationController.getPage",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 删除
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Access(level=1)
    public Map delete(@Context HttpServletRequest request, List<Integer> ids){
        try{
            if(licenseRecordService.deletePatch(ids)>0)
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        }catch (Exception e){
            log.error("AuthorizationController.delete",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 新增授权
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/add")
    @Access(level=1)
    public Map save(Map map, @Context HttpServletRequest request){

//        Integer userId = -1;
//        Integer count =0;
//        String username ="";
//        Integer licenseType = -1;
//        String amount = "";
//        String paymentType = "";
//        Integer photoLive = 0;
//
//        if(map.get("userId")!=null)
//            userId = Integer.parseInt(map.get("userId").toString());
//        if(map.get("count")!=null)
//            count = Integer.parseInt(map.get("count").toString());
//        if(map.get("username")!=null)
//            username = map.get("username").toString();
//        if(map.get("licenseType")!=null)
//            licenseType = Integer.parseInt(map.get("licenseType").toString());
//        if(map.get("amount")!=null)
//            amount = map.get("amount").toString();
//        if (map.get("paymentType")!=null)
//            paymentType = map.get("paymentType").toString();
//        if(map.get("photoLive")!=null&&map.get("photoLive").equals(1))
//            photoLive = 1;
//
//        //count参数为输入的  场数  或   年数
//        LicenseRecord licenseRecord = new LicenseRecord();
//        licenseRecord.setLicenseType(licenseType);
//        licenseRecord.setAmount(amount);
//        licenseRecord.setPaymentType(paymentType);
//        licenseRecord.setPhotoLive(photoLive);
//
//        //用于更新用户表
//        User u = new User();
//        try{
//            //传入LicenseRecord对象
//            //根据用户名查询出ID,设置用户ID
//            User user = userService.selectByUsername(username);
//            if(user == null)
//                return ReturnResult.errorResult("输入的用户不存在");
//            else {
//                userId = user.getId();
//                licenseRecord.setUserId(userId);
//                u.setId(userId);
//            }
//            if(user.getLicenseType().equals(1)){
//                return ReturnResult.errorResult("已有包年授权，无法再次授权！");
//            }
//
//            //判断授权类型 （1:包年  2:包时长  9:永久）
//            if(licenseRecord.getLicenseType() == 1){
//                //包年
//                licenseRecord.setSessionsTotal(100);
//                //计算
//                Calendar c = Calendar.getInstance();
//                c.add(Calendar.YEAR, count);
//                //授权截止日期
//                licenseRecord.setDueDate(c.getTime());
//                u.setDueDate(c.getTime());
//                u.setPackYearsDays(100);
//            }else if(licenseRecord.getLicenseType() == 2){
//                //包时长
//                licenseRecord.setSessionsTotal(count);
//                Integer remainingDays = user.getRemainingDays()==null?0:user.getRemainingDays();
//                u.setRemainingDays(remainingDays+count);
//            }else if(licenseRecord.getLicenseType() == 9){
//                //包永久，数据表不存
//            }else{
//                //授权类型传递错误
//                return ReturnResult.errorResult("授权类型不存在");
//            }
//            Integer managerId = GlobalUtils.getUserId(request);
//            //创建人ID
//            licenseRecord.setCId(managerId);
//            //创建时间
//            licenseRecord.setCTime(new Date());
//            //交易时间
//            licenseRecord.setTransactionDate(new Date());
//
//            u.setUId(managerId);
//            u.setUTime(new Date());
//            u.setLicenseType(licenseRecord.getLicenseType());
//
//            if(userService.updateAuthorization(u)<=0)
//                return ReturnResult.errorResult("提交失败");
//            //授权记录信息保存   保存记录
//            if(licenseRecordService.save(licenseRecord)>0)
//                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
//            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        try{
            Integer userId = GlobalUtils.getUserId(request);
            return licenseRecordService.addLicense(userId,map);
        }catch (Exception e){
            log.error("AuthorizationController.save",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    //根据用户ID获取购买记录分页信息
    @POST
    @Path("/getByUId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Access
    public Map selectByUId(@QueryParam("pageNum")Integer pageNum,@QueryParam("pageSize")Integer pageSize,@DefaultValue("0") @QueryParam("photoLive") Integer photoLive, @Context HttpServletRequest request,Map params) {
        try {
            Integer userId = GlobalUtils.getUserId(request);
            //查询出所有用户ID为userID的记录
//            Map map = new HashMap();
            params.put("userId", userId);
            params.put("photoLive",photoLive);
            PageHelper.startPage(pageNum,pageSize);
            PageHelper.orderBy(" l.c_time desc");
            List<LicenseRecord> licenseRecords = licenseRecordService.getByParam(params);
            PageInfo<LicenseRecord> pages = new PageInfo<>(licenseRecords);
            return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("AuthorizationController.selectByParam",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
