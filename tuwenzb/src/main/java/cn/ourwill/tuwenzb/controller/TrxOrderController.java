package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.*;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.ILicenseRecordService;
import cn.ourwill.tuwenzb.service.ITrxOrderService;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import cn.ourwill.tuwenzb.weixin.WXpay.WXPayConstants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/11 0011 15:14
 * @Version1.0
 */
@Component
@Path("/trxorder")
public class TrxOrderController {

    @Autowired
    private ITrxOrderService trxOrderService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ILicenseRecordService licenseRecordService;

    @Autowired
    RedisTemplate redisTemplate;

    private static final Logger log = LogManager.getLogger(TrxOrderController.class);

    @POST
    @Path("/addOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map addOrder(@Context HttpServletRequest request,TrxOrder trxOrder,@DefaultValue("0") @QueryParam("photoLive") Integer photoLive){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            User user = userService.getById(userId);
            if(user.getUserfromType().equals(0)){
                return ReturnResult.errorResult("请注册授权账号！");
            }
            if(photoLive.equals(0)&&user.getLicenseType().equals(1)&&user.getDueDate().after(new Date())){
                return ReturnResult.errorResult("您已经是包年用户！");
            }
            if(photoLive.equals(1)&&user.getPhotoLicenseType().equals(1)&&user.getPhotoDueDate().after(new Date())){
                return ReturnResult.errorResult("您已经是包年用户！");
            }
//            if(trxOrder.getType().equals(4)) {
//                List<LicenseRecord> licenseRecords = licenseRecordService.selectByUserId(userId);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//                Date date = sdf.parse(discountDay);
//                if (licenseRecords != null && licenseRecords.size() > 0) {
//                    return ReturnResult.errorResult("不在优惠范围内！");
//                } else if (user.getCTime().before(date)) {
//                    return ReturnResult.errorResult("不在优惠范围内！");
//                }
//            }
            trxOrder.setPhotoLive(photoLive);
            trxOrder.setUserId(userId);
            if(user.getWechatNum()!=null) {
                trxOrder.setOpenId(user.getWechatNum());
            }
            trxOrder.setCreateIp(GlobalUtils.getIp(request));
    //        trxOrder.setType(0);
            trxOrder.setOrderNo(GlobalUtils.generateUUID());
            trxOrder.setCreateTime(new Date());
            return trxOrderService.addOrder(trxOrder);
        } catch (Exception e) {
            log.info("TrxOrderController.addOrder",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @POST
    @Path("/addOrderTest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map addOrderTest(@Context HttpServletRequest request,TrxOrder trxOrder){
        Integer userId = GlobalUtils.getUserId(request);
        User user = userService.getById(userId);
        trxOrder.setUserId(userId);
        if(user.getWechatNum()!=null) {
            trxOrder.setOpenId(user.getWechatNum());
        }
        trxOrder.setCreateIp(GlobalUtils.getIp(request));
//        trxOrder.setType(0);
        trxOrder.setOrderNo(GlobalUtils.generateUUID());
        trxOrder.setCreateTime(new Date());
        return trxOrderService.addOrderTest(trxOrder);
    }

    @POST
    @Path("/getOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getOrder(@Context HttpServletRequest request,Map params){
//        Integer userId = GlobalUtils.getUserId(request);
//        User user = userService.getById(userId);
        String orderNo = (String) params.get("orderNo");
        return trxOrderService.queryOrder(orderNo);
    }

    @POST
    @Path("/closeOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map closeOrder(@Context HttpServletRequest request,Map params){
        String orderNo = (String) params.get("orderNo");
        return trxOrderService.closeOrder(orderNo);
    }

    @POST
    @Path("/callback")
    @Produces(MediaType.TEXT_XML)
    public String callback(@Context HttpServletRequest request){
        InputStream in = null;
        try {
            in = request.getInputStream();
            String strXml = IOUtils.toString(in,"UTF-8");
            String returnCode = WXPayConstants.FAIL;
            String returnMsg = "系统异常";
            Map<String,String> reMap = trxOrderService.callback(strXml);
            if(reMap!=null) {
                returnCode = reMap.get("returnCode");
                returnMsg = reMap.get("returnMsg");
                log.info("+++++callback返回："+reMap.toString());
            }
            String reXml = "<xml> \n" +
                            " <return_code><![CDATA["+returnCode+"]]></return_code>\n" +
                            " <return_msg><![CDATA["+returnMsg+"]]></return_msg>\n" +
                            " </xml> \n";
            log.info("通知回调返回："+reXml);
            return reXml;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @POST
    @Path("/callbacktest")
    @Produces(MediaType.TEXT_XML)
    public String callbackTest(@Context HttpServletRequest request) throws Exception {
        Enumeration e  = request.getHeaderNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String value = request.getHeader(name);
            log.info("+++++++++++++++++++++++"+name+":"+value);
        }
        log.info("method:"+request.getMethod());

        InputStream is = request.getInputStream();
        String contentStr= IOUtils.toString(is, "utf-8");
//        Map<String,String> reMap = WXPayUtil.xmlToMap(contentStr);
        log.info("body:"+contentStr);

        String reXml = "<xml> \n" +
                " <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                " <return_msg><![CDATA[OK]]></return_msg>\n" +
                " </xml> \n";
        log.info("通知回调返回："+reXml);
        return reXml;
    }

    @POST
    @Path("/selectByParams")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map selectByParams(@Context HttpServletRequest request,@QueryParam("pageNum") @DefaultValue("1") Integer pageNum,
                              @QueryParam("pageSize")@DefaultValue("10") Integer pageSize,Map params){
        try {
            PageHelper.startPage(pageNum,pageSize);
            if(params==null) params = new HashMap();
            if(!params.containsKey("photoLive")){
                params.put("photoLive",0);
            }
            params.put("orderBy",1);
            PageInfo pageInfo = new PageInfo(trxOrderService.selectByParams(params));
            return ReturnResult.successResult("data", pageInfo, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("TrxOrderController.selectByParams",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/getPriceList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getPriceList(@Context HttpServletRequest request){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            List<Contract> list = trxOrderService.getContractList(userId);
            return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("TrxOrderController.getPriceList",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/getPrice/{number}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getPrice(@Context HttpServletRequest request,@PathParam("number") Integer activityNum,@NotNull@QueryParam("type") Integer type){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Contract contract = trxOrderService.getContract(activityNum,type,userId);
            return ReturnResult.successResult("data", contract, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("TrxOrderController.getPriceList",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/photo/getPriceList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getPhotoPriceList(@Context HttpServletRequest request){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            List<Contract> list = trxOrderService.getPhotoContractList(userId);
            return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("TrxOrderController.getPhotoPriceList",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/photo/getPrice/{number}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getPhotoPrice(@Context HttpServletRequest request,@PathParam("number") Integer activityNum,@NotNull @QueryParam("type") Integer type){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            Contract contract = trxOrderService.getPhotoContract(activityNum,type,userId);
            return ReturnResult.successResult("data", contract, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("TrxOrderController.getPhotoPrice",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 打赏
     * @param request
     * @param trxOrder
     * @return
     */
    @POST
    @Path("/addReward/{liveUser}/{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map addReward(@Context HttpServletRequest request,TrxOrder trxOrder,@DefaultValue("0") @QueryParam("photoLive") Integer photoLive,
                         @PathParam("liveUser") Integer liveUser,@PathParam("activityId") Integer activityId){
        try{
            if(liveUser == null || "".equals(activityId)){
                return ReturnResult.errorResult("打赏活动为空！");
            }
            if(liveUser == null || "".equals(liveUser)){
                return ReturnResult.errorResult("被打赏人为空");
            }
            User rewardUser = userService.getById(liveUser);
            if (rewardUser.getUserfromType()!=1){
                return ReturnResult.errorResult("被打赏人未绑定账户");
            }
            Integer userId = GlobalUtils.getUserId(request);
            User user = userService.getById(userId);
            trxOrder.setPhotoLive(photoLive);
            trxOrder.setUserId(userId);
            if(user.getWechatNum()!=null) {
                trxOrder.setOpenId(user.getWechatNum());
            }
            trxOrder.setCreateIp(GlobalUtils.getIp(request));
            trxOrder.setOrderNo(GlobalUtils.generateUUID());
            trxOrder.setCreateTime(new Date());
            trxOrder.setDealType(1);
            trxOrder.setTradeType("JSAPI");
            return trxOrderService.addReward(trxOrder,liveUser,activityId);
        }catch (Exception e){
            log.info("TrxOrderController.addReward",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 打赏回调
     * @param request
     * @return
     */

    @POST
    @Path("/callback/reward")
    @Produces(MediaType.TEXT_XML)
    public String callbackReward(@Context HttpServletRequest request){
        InputStream in = null;
        try {
            in = request.getInputStream();
            String strXml = IOUtils.toString(in,"UTF-8");
            String returnCode = WXPayConstants.FAIL;
            String returnMsg = "系统异常";
            Map<String,String> reMap = trxOrderService.callbackReward(strXml);
            if(reMap!=null) {
                returnCode = reMap.get("returnCode");
                returnMsg = reMap.get("returnMsg");
                log.info("+++++callback返回："+reMap.toString());
            }
            String reXml = "<xml> \n" +
                    " <return_code><![CDATA["+returnCode+"]]></return_code>\n" +
                    " <return_msg><![CDATA["+returnMsg+"]]></return_msg>\n" +
                    " </xml> \n";
            log.info("通知回调返回："+reXml);
            return reXml;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 提现
     * @return
     */
    @POST
    @Path("/withdrawDeposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map withdrawDeposit(@Context HttpServletRequest request,WithdrawalOrder withdrawalOrder){
        try{

            Integer userId = GlobalUtils.getUserId(request);
            //redis验证
            Calendar time = (Calendar)RedisUtils.get("withdrawal:"+userId.toString());
            Calendar calendar = Calendar.getInstance();
            if (time==null){
                calendar.clear();
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE,5);
                RedisUtils.set("withdrawal:"+userId.toString(),calendar);
            } else {
                if(time.getTimeInMillis()>calendar.getTimeInMillis()){
                    calendar.clear();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MINUTE,5);
                    RedisUtils.set(userId.toString(),calendar);
                    return ReturnResult.errorResult("操作过于频繁，请稍后再试！");
                }
                calendar.clear();
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE,5);
                RedisUtils.set(userId.toString(),calendar);
            }
            User user = userService.getById(userId);
            if(user.getWechatNum()!=null) {
                withdrawalOrder.setOpenId(user.getWechatNum());
            }else{
                return ReturnResult.errorResult("提现账户为空");
            }
            if (user.getUserfromType()!=1){
                return ReturnResult.errorResult("该用户没有绑定");
            }
            if (withdrawalOrder.getPracticalAmount()!=null&&withdrawalOrder.getServiceCharge()!=null){
                Integer deductAmount = withdrawalOrder.getPracticalAmount()+withdrawalOrder.getServiceCharge();
                withdrawalOrder.setRequestAmount(deductAmount);
            }else {
                return ReturnResult.errorResult("请输入提现金额！");
            }
            withdrawalOrder.setUserId(userId);
            withdrawalOrder.setDescription("提现");
            //withdrawalOrder.setUserName(user.getNickname());//
            withdrawalOrder.setCreateIp(GlobalUtils.getIp(request));
            withdrawalOrder.setPartnerTradeNo(GlobalUtils.generateUUID());
            withdrawalOrder.setTransferTime(new Date());
            Map<String,String> reMap = trxOrderService.withdrawDeposit(withdrawalOrder);
            return reMap;
        }catch (Exception e){
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     *我的打赏账单
     * @param request
     * @return
     */
    @GET
    @Path("/getFundList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map selectFundListById(@Context HttpServletRequest request,@QueryParam("pageNum")@DefaultValue("1") Integer pageNum,
                                               @QueryParam(value = "pageSize")@DefaultValue("2") Integer pageSize){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            PageHelper.startPage(pageNum, pageSize);
            List<FundDetail> lists = trxOrderService.selectFundListById(userId);
            PageInfo<FundDetail> pages = new PageInfo<>(lists);
            return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 我的打赏收益
     * @param request
     * @return
     */
    @GET
    @Path("/getTotalAmount")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getTotalAmount(@Context HttpServletRequest request){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            FundStatistics fundStatistics =  trxOrderService.getTotalAmount(userId);
            return ReturnResult.successResult("data", fundStatistics, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/getRewardList/{activityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getRewardList (@Context HttpServletRequest request,@PathParam("activityId") Integer activityId){
        try {
            List<FundDetail> relist= trxOrderService.getRewardList(activityId);
            return ReturnResult.successResult("data",relist,ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/serviceCharge")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getServiceCharge(@Context HttpServletRequest request,@QueryParam("withdrawAmount") Integer withdrawAmount){
        try {
            if (withdrawAmount==null){
                return ReturnResult.successResult("data",null,ReturnType.GET_SUCCESS);
            }
            Integer deductAmount = (int)Math.round(withdrawAmount*0.1);
            return ReturnResult.successResult("data",deductAmount,ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
