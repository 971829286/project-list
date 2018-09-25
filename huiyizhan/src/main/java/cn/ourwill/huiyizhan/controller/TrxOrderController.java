package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.entity.Contract;
import cn.ourwill.huiyizhan.entity.TicketsRecord;
import cn.ourwill.huiyizhan.entity.TrxOrder;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityService;
import cn.ourwill.huiyizhan.service.ILicenseRecordService;
import cn.ourwill.huiyizhan.service.ITrxOrderService;
import cn.ourwill.huiyizhan.service.IUserService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import cn.ourwill.huiyizhan.weChat.WXpay.WXPayConstants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2018/4/2 0011 15:14
 * @Version1.0
 */
@RestController
@RequestMapping("/api/trxOrder")
@Slf4j
public class TrxOrderController {

    @Autowired
    private ITrxOrderService trxOrderService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ILicenseRecordService licenseRecordService;
    @Autowired
    private IActivityService activityService;

    /**
     *
     * @param request
     * @param trxOrder
     * @return
     */
    @PostMapping("/addOrderTicket/{activityId}")
    @ResponseBody
    public Map addOrderTicket(HttpServletRequest request, @RequestBody TrxOrder trxOrder, @PathVariable("activityId") Integer activityId){
        try {
            User user = GlobalUtils.getLoginUser(request);
            if(user!=null&&user.getId()!=null) {
                trxOrder.setUserId(user.getId());
            }
            List<TicketsRecord> ticketList = trxOrder.getTicketsRecordList();
            if(ticketList==null||ticketList.size()<1) return ReturnResult.errorResult("请填写购票信息！");

            trxOrder.setActivityId(activityId);
            trxOrder.setCreateIp(GlobalUtils.getIp(request));
            trxOrder.setOrderNo(trxOrderService.generateOrderNo("T"));
            trxOrder.setCreateTime(new Date());
            trxOrder.setFinishTime(new Date());
            trxOrder.setType(0);
            trxOrder.setSysType(1);
            trxOrder.setOrderStatus(2);
            trxOrder.setTransactionStatus(1);
            ticketList.stream().forEach(entity -> {
                entity.setOrderNo(trxOrder.getOrderNo());
            });
//            log.info(trxOrder.toString()+"==========");
            return trxOrderService.addTicketOrder(trxOrder);
        } catch (Exception e) {
            log.info("TrxOrderController.addOrder",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

//    @PostMapping("/addOrderTest")
//    @ResponseBody
//    public Map addOrderTest(HttpServletRequest request,TrxOrder trxOrder){
//        Integer userId = GlobalUtils.getUserId(request);
//        User user = userService.getById(userId);
//        trxOrder.setUserId(userId);
//        if(user.getWechatNum()!=null) {
//            trxOrder.setOpenId(user.getWechatNum());
//        }
//        trxOrder.setCreateIp(GlobalUtils.getIp(request));
////        trxOrder.setType(0);
//        trxOrder.setOrderNo(GlobalUtils.generateUUID());
//        trxOrder.setCreateTime(new Date());
//        return trxOrderService.addOrderTest(trxOrder);
//    }
//
    @PostMapping("/getOrder")
    @ResponseBody
    @Access
    public Map getOrder(HttpServletRequest request,@RequestBody Map params){
//        Integer userId = GlobalUtils.getUserId(request);
//        User user = userService.getById(userId);
        String orderNo = (String) params.get("orderNo");
        return trxOrderService.queryOrder(orderNo);
    }

    @PostMapping("/getTicketOrder/{activityId}")
    @ResponseBody
    @Access
    public Map getTicketOrder(HttpServletRequest request,@RequestBody(required = false) Map params,@PathVariable("activityId") Integer activityId,
                              @RequestParam(value="pageNum",defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            if(params==null) params = new HashedMap();
            params.put("sysType", 1);
            params.put("activityId", activityId);
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<TrxOrder> pages = new PageInfo<>(trxOrderService.selectByParamsWithTicket(params));
            return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
        }catch (UnPermissionException e){
            throw e;
        }catch (Exception e){
            log.info("TrxOrderController.getTicketOrder",e );
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @PutMapping("{trxOrderId}/updateTicketOrder/{activityId}")
    @ResponseBody
    @Access
    public Map updateTicketOrder(HttpServletRequest request,@RequestBody Map params,@PathVariable("trxOrderId") Integer trxOrderId,@PathVariable("activityId") Integer activityId){
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            HashMap map = new HashMap();
            map.put("buyer",params.get("buyer"));
            map.put("buyerPhone",params.get("buyerPhone"));
            map.put("buyerEmail",params.get("buyerEmail"));
            TrxOrder trxOrder = GlobalUtils.toBean(map, TrxOrder.class);
            trxOrder.setId(trxOrderId);
            int count = trxOrderService.update(trxOrder);
            if(count>0)
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (UnPermissionException e){
            throw e;
        }catch (Exception e){
            log.info("TrxOrderController.updateTicketOrder",e );
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
//
//    @POST
//    @Path("/closeOrder")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Access
//    public Map closeOrder(@Context HttpServletRequest request,Map params){
//        String orderNo = (String) params.get("orderNo");
//        return trxOrderService.closeOrder(orderNo);
//    }
//
//    @POST
//    @Path("/callback")
//    @Produces(MediaType.TEXT_XML)
//    public String callback(@Context HttpServletRequest request){
//        InputStream in = null;
//        try {
//            in = request.getInputStream();
//            String strXml = IOUtils.toString(in,"UTF-8");
//            String returnCode = WXPayConstants.FAIL;
//            String returnMsg = "系统异常";
//            Map<String,String> reMap = trxOrderService.callback(strXml);
//            if(reMap!=null) {
//                returnCode = reMap.get("returnCode");
//                returnMsg = reMap.get("returnMsg");
//                log.info("+++++callback返回："+reMap.toString());
//            }
//            String reXml = "<xml> \n" +
//                            " <return_code><![CDATA["+returnCode+"]]></return_code>\n" +
//                            " <return_msg><![CDATA["+returnMsg+"]]></return_msg>\n" +
//                            " </xml> \n";
//            log.info("通知回调返回："+reXml);
//            return reXml;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    @POST
//    @Path("/callbacktest")
//    @Produces(MediaType.TEXT_XML)
//    public String callbackTest(@Context HttpServletRequest request) throws Exception {
//        Enumeration e  = request.getHeaderNames();
//        while (e.hasMoreElements()) {
//            String name = (String) e.nextElement();
//            String value = request.getHeader(name);
//            log.info("+++++++++++++++++++++++"+name+":"+value);
//        }
//        log.info("method:"+request.getMethod());
//
//        InputStream is = request.getInputStream();
//        String contentStr= IOUtils.toString(is, "utf-8");
////        Map<String,String> reMap = WXPayUtil.xmlToMap(contentStr);
//        log.info("body:"+contentStr);
//
//        String reXml = "<xml> \n" +
//                " <return_code><![CDATA[SUCCESS]]></return_code>\n" +
//                " <return_msg><![CDATA[OK]]></return_msg>\n" +
//                " </xml> \n";
//        log.info("通知回调返回："+reXml);
//        return reXml;
//    }
//
    @PostMapping("/selectByParams")
    @ResponseBody
    @Access(level = 1)
    public Map selectByParams(HttpServletRequest request,@RequestParam(value="pageNum",defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,@RequestBody Map params){
        try {
            PageHelper.startPage(pageNum,pageSize);
            if(params==null) params = new HashMap();
            params.put("orderBy",1);
            PageInfo pageInfo = new PageInfo(trxOrderService.selectByParams(params));
            return ReturnResult.successResult("data", pageInfo, ReturnType.GET_SUCCESS);
        }catch(Exception e){
            log.info("TrxOrderController.selectByParams",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
//
//    @GET
//    @Path("/getPriceList")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Map getPriceList(@Context HttpServletRequest request){
//        try {
//            Integer userId = GlobalUtils.getUserId(request);
//            List<Contract> list = trxOrderService.getContractList(userId);
//            return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
//        }catch(Exception e){
//            log.info("TrxOrderController.getPriceList",e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }
//
//    @GET
//    @Path("/getPrice/{number}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Map getPrice(@Context HttpServletRequest request,@PathParam("number") Integer activityNum){
//        try {
//            Integer userId = GlobalUtils.getUserId(request);
//            Contract contract = trxOrderService.getContract(activityNum,userId);
//            return ReturnResult.successResult("data", contract, ReturnType.GET_SUCCESS);
//        }catch(Exception e){
//            log.info("TrxOrderController.getPriceList",e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }
//
//    @GET
//    @Path("/photo/getPriceList")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Map getPhotoPriceList(@Context HttpServletRequest request){
//        try {
//            Integer userId = GlobalUtils.getUserId(request);
//            List<Contract> list = trxOrderService.getPhotoContractList(userId);
//            return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
//        }catch(Exception e){
//            log.info("TrxOrderController.getPhotoPriceList",e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }
//
//    @GET
//    @Path("/photo/getPrice/{number}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Map getPhotoPrice(@Context HttpServletRequest request,@PathParam("number") Integer activityNum){
//        try {
//            Integer userId = GlobalUtils.getUserId(request);
//            Contract contract = trxOrderService.getPhotoContract(activityNum,userId);
//            return ReturnResult.successResult("data", contract, ReturnType.GET_SUCCESS);
//        }catch(Exception e){
//            log.info("TrxOrderController.getPhotoPrice",e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }
}
