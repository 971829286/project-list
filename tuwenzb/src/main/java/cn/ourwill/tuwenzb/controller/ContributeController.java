package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.Contribute;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IContributeService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

/**
 * 描述：投稿
 *
 * @author zhaoqing
 * @create 2018-06-20 14:46
 **/
//
@Component
@Slf4j
@Path("/contribute")
public class ContributeController {


    @Autowired
    private IContributeService contributeService;

    /**
     * 是否投稿
     * @param request
     * @return
     */
    @GET
    @Path("/isContribute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map isContribute(@Context HttpServletRequest request){

        try {
            Integer userId = GlobalUtils.getUserId(request);
            List<Contribute> cons = contributeService.selectByUserId(userId);
            if(cons.size()>0){
                return  ReturnResult.successResult("data",true,ReturnType.GET_SUCCESS);
            }else {
                return  ReturnResult.successResult("data",false,ReturnType.GET_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 投稿
     * @param request
     * @param contribute
     * @return
     */
    @POST
    @Path("/addContribute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map addContribute(@Context HttpServletRequest request,Contribute contribute){
        try {
            if(contribute==null){
                return ReturnResult.errorResult("参数不完整");
            }
            Integer userId = GlobalUtils.getUserId(request);
            List<Contribute> cons = contributeService.selectByUserId(userId);
            if(cons.size()>0){
                return  ReturnResult.errorResult("当前用户已投稿，无法重复投稿！");
            }
            String mobPhone = contribute.getMobPhone();
            Integer smsCode = Integer.valueOf((String) contribute.getSmsCode());
            Integer RealSmsCode = (Integer) RedisUtils.get("smsCode"+mobPhone);
            if(smsCode==null||RealSmsCode==null||!smsCode.equals(RealSmsCode)){
                return ReturnResult.errorResult("验证码错误！");
            }
            //图形验证码使用一次后，无论正确与否，从session中清除
//            String verifyCodeSession = (String) GlobalUtils.getSessionValue(request, "verifyCode");
//            if (contribute.getVerifyCode() == null || !contribute.getVerifyCode().trim().toUpperCase().equals(verifyCodeSession)) {
//                return ReturnResult.errorResult("验证码错误！");
//            }
//            if (contribute.getVerifyCode() == null || !contribute.getVerifyCode().trim().toUpperCase().equals(verifyCodeSession)) {
//                return ReturnResult.errorResult("验证码错误！");
//            }
            //RedisUtils.set("smsCode"+mobPhone,mobile_code,10, TimeUnit.MINUTES);
//  id,user_id,name,sex,mob_phone,email,address,pic_url,check_status
            contribute.setUserId(userId);
            contribute.setCheckStatus(1);
            contribute.setSubTime(new Date());
            contribute.setUpdateTime(new Date());
            List<String> pics = contribute.getPicList();
            if(pics != null && pics.size() > 3){
                return  ReturnResult.errorResult("仅支持三张图片！");
            }
            String picsStr = StringUtils.join(pics, ";");
//            for (int i =0;i<urls.size();i++){
//                contribute.setPicUrl(urls.get(i));
//
//                Integer count = contributeService.save(contribute);
//            }
            contribute.setPicUrl(picsStr);
            Integer count = contributeService.save(contribute);
            if(count>0) {
                return ReturnResult.successResult("data", contribute, ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);

        } catch (Exception e) {
            log.error("ContributeController.addContribute",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 查看审核状态
     * @param request
     * @return
     */
    @GET
    @Path("/viewStatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map viewCheckStatus(@Context HttpServletRequest request){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Contribute contribute = contributeService.selectOneByUserId(userId);
            return ReturnResult.successResult("date",contribute,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @POST
    @Path("/updateContribute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map updateContribute(@Context HttpServletRequest request,Contribute contribute){

        try {
            Integer userId = GlobalUtils.getUserId(request);
            Contribute con = contributeService.selectOneByUserId(userId);
            if(con==null){return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);}
            contribute.setId(con.getId());
            contribute.setUpdateTime(new Date());//加修改時間？？？？
            contribute.setCheckStatus(1);
            List<String> pics = contribute.getPicList();
            String picsStr = StringUtils.join(pics, ";");
            contribute.setPicUrl(picsStr);
            if(contributeService.update(contribute)>0) {
                return ReturnResult.successResult("data",contributeService.getById(con.getId()),ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 审核列表
     * @param request
     * @param status
     * @param pageNum
     * @param pageSize
     * @param orderByTime
     * @return
     */
    @GET
    @Path("/getList/{status}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getContributeList(@Context HttpServletRequest request,@PathParam("status")Integer status,
                                 @QueryParam("pageNum") @DefaultValue("1") Integer pageNum,
                                 @QueryParam("pageSize") @DefaultValue("5") Integer pageSize,
                                 @QueryParam("searchText")@DefaultValue("") String searchText,
//                                 @QueryParam("orderById")@DefaultValue("0") Integer orderById,
                                 @QueryParam("orderByTime") Integer orderByTime){


        try {
            if (orderByTime != null && orderByTime == 0) {
                PageHelper.orderBy(" update_time desc");
            } else if (orderByTime != null && orderByTime == 1) {
                PageHelper.orderBy(" update_time asc");
            }
            Map map = new HashMap();
            if(!StringUtils.isEmpty(searchText)){
                map.put("searchText",searchText);
            }
            if(status!=0){//查看全部
                map.put("checkStatus",status);
            }
            PageHelper.startPage(pageNum, pageSize);
            List<Contribute> list = contributeService.getContributeList(map);
            PageInfo<Contribute> pages = new PageInfo<>(list);
            return ReturnResult.successResult("data",pages, ReturnType.GET_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 点击图片审核
     * @param request
     * @param id
     * @return
     */
    @POST
    @Path("/picCheck/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map picCheck(@Context HttpServletRequest request,@PathParam("id")Integer id,Map params){
        try {
            Integer status = (Integer) params.get("status");
            String feedback = (String) params.get("feedback");
            if(status==null) return ReturnResult.successResult(ReturnType.UPDATE_ERROR);
            Integer count = contributeService.updateStatus(id,status,feedback);
            if (count>0){
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 批量审核
     * @param request
     * @param params
     * @return
     */

    @POST
    @Path("/batchCheck")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map batchCheck(@Context HttpServletRequest request,Map params){
        Integer checkStatus = (Integer) params.get("status");
        String feedback = (String) params.get("feedback");
        List<Integer> list = (List<Integer>) params.get("list");
        if(list == null || list.size() < 0) return ReturnResult.errorResult("id为空");
        if (checkStatus == null) {
            ReturnResult.successResult(ReturnType.UPDATE_ERROR);
        }
        Integer count = contributeService.batchCheck(checkStatus,list,feedback);
        if (count>0){
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        }else{
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }
    }

    @GET
    @Path("/promotionNum")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getPromotionNum(@Context HttpServletRequest request){
        try{
            Integer userId = GlobalUtils.getUserId(request);
            return ReturnResult.successResult("data",contributeService.getPromotionNum(userId),ReturnType.GET_SUCCESS);
        } catch (Exception e){
            log.error("ContributeController.promotionNum",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
