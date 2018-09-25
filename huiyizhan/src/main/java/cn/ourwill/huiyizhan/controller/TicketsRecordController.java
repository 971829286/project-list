package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.baseEnum.EmailType;
import cn.ourwill.huiyizhan.baseEnum.TicketStatus;
import cn.ourwill.huiyizhan.config.RabbitMqConfig;
import cn.ourwill.huiyizhan.entity.*;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityService;
import cn.ourwill.huiyizhan.service.ITicketsRecordService;
import cn.ourwill.huiyizhan.service.ITrxOrderService;
import cn.ourwill.huiyizhan.service.IUserService;
import cn.ourwill.huiyizhan.service.impl.GenerateTicketService;
import cn.ourwill.huiyizhan.utils.*;
import cn.ourwill.huiyizhan.weChat.Utils.WeixinPushMassage;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.entity.ContentBufferEntity;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static cn.ourwill.huiyizhan.entity.Config.systemDomain;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/29 15:11
 * @Description
 */
@RestController
@RequestMapping("/api/ticketsRecord")
@Slf4j
public class TicketsRecordController {
    @Autowired
    private ITicketsRecordService ticketsRecordService;
    @Autowired
    private IActivityService      activityService;
    @Autowired
    private ITrxOrderService      trxOrderService;

    @Autowired
    private GenerateTicketService generateTicketService;

    @Value("${weixin.ticket.detail.url}")
    private String ticketDetailUrl;
    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    org.springframework.amqp.core.Queue queue;

    @Autowired
    TopicExchange exchange;
    @Autowired
    private IUserService userService;

    @Value("${ssr.domain}")
    private String SSR_DOMAIN;

    @Value("${EmailAndPdfPath}")
    private String PATH;

    /**
     * 获取门票详细
     *
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    @Access
    public Map getById(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            TicketsRecord ticketsRecord = ticketsRecordService.getById(id);
            if (ticketsRecord == null)
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            if (!activityService.checkOwnerOrAdmin(ticketsRecord.getActivityId(), loginUser)) {
                throw new UnPermissionException();
            }
            ticketsRecord.setTrxOrder(trxOrderService.getById(ticketsRecord.getOrderId()));
            return ReturnResult.successResult("data", ticketsRecord, ReturnType.GET_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("TicketsRecordController.getById", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 修改门票参会信息
     *
     * @param request
     * @param id
     * @param ticketsRecord
     * @return
     */
    @PutMapping("/{id}")
    @ResponseBody
    @Access
    public Map update(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody TicketsRecord ticketsRecord) {
        try {
//            User user = GlobalUtils.getLoginUser(request);
//            TicketsRecord ori = ticketsRecordService.getById(id);
//            if(ori==null||ori.get)
            TicketsRecord origin = ticketsRecordService.getById(id);
            if (origin == null)
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            //判断允许修改时间
            Activity activity = activityService.getById(origin.getActivityId());
            if (activity == null)
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            Date now = new Date();
            if (activity.getTicketConfig() < 0) {
                return ReturnResult.errorResult("主办方设置不允许修改！");
            } else if (activity.getTicketConfig() > 0) {
                if (now.after(activity.getStartTime())) {
                    return ReturnResult.errorResult("活动已开始，不允许修改！");
                }
                int diffDays = diffDays(now, activity.getStartTime());
                if (diffDays < activity.getTicketConfig()) {
                    return ReturnResult.errorResult("主办方设置不允许修改！");
                }
            }
            if (origin.getTicketStatus().equals(TicketStatus.CHECK_NOT_PASS.getIndex())
                    || origin.getTicketStatus().equals(TicketStatus.SIGN_NOT.getIndex()))
                ticketsRecord.setTicketStatus(TicketStatus.CHECK_NOT.getIndex());
            ticketsRecord.setId(id);
            int count = ticketsRecordService.updateUserInfo(ticketsRecord);
            if (count > 0) {
                origin.setConfereeName(ticketsRecord.getConfereeName());
                origin.setConfereePhone(ticketsRecord.getConfereePhone());
                origin.setConfereeEmail(ticketsRecord.getConfereeEmail());
                origin.setTicketStatus(ticketsRecord.getTicketStatus());
                return ReturnResult.successResult("data", origin, ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        } catch (Exception e) {
            log.info("TicketsRecordController.update", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    private int diffDays(Date fDate, Date oDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String f = sdf.format(fDate);
        Date fDateN = sdf.parse(f);
        String o = sdf.format(oDate);
        Date oDateN = sdf.parse(o);
        long nd = 1000 * 24 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = oDateN.getTime() - fDateN.getTime();
        // 计算差多少天
        int day = (int) (diff / nd);
        return day;
    }

    /**
     * 按活动获取门票信息
     *
     * @param request
     * @param params
     * @param activityId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("/getTicketsRecords/{activityId}")
    @ResponseBody
    @Access
    public Map getTicketsRecords(HttpServletRequest request, @RequestBody Map params, @PathVariable("activityId") Integer activityId,
                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            params.put("activityId", activityId);
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<TicketsRecord> pages = new PageInfo<>(ticketsRecordService.selectByParamsWithOrder(params));
            return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("TicketsRecordController.getTicketsRecords", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 门票审核
     *
     * @param request
     * @param id
     * @param activityId
     * @return
     */
    @PostMapping("/{id}/checkTicket/{activityId}")
    @ResponseBody
    @Access
    public Map checkTicket(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("activityId") Integer activityId,
                           @RequestParam(value = "isPass", required = true) String isPass) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            //通过审核改为未签到
            int count = ticketsRecordService.checkTicket(id, activityId, isPass.equals("true") ? true : false);
            if (count > 0) {
                // 发送通知邮件开始
                TicketsRecord ticketsRecord = ticketsRecordService.getById(id);
                TrxOrder trxOrder = trxOrderService.selectById(ticketsRecord.getOrderId());
                Activity activity = activityService.getById(trxOrder.getActivityId());
                if (isPass.equals("true")) {
                    //状态为未签到,可以重新发送
                    //  发送邮件MQ
                    String subject = "您申请的【" + activity.getActivityTitle() + "】已经通过组织者审核";
                    String address;
                    if (1 == activity.getIsOnline()) {
                        address = systemDomain + "web/activity/" + activity.getId();
                    } else {
                        address = JsonUtil.fromJson(activity.getActivityAddress(), Address.class).toString();
                    }

                    EmailBean emailBean = new EmailBean();
                    HashMap map = emailBean.getMap();
                    map.put("trxOrder", trxOrder);
                    map.put("activity", activity);
                    map.put("address", address);
                    map.put("user", userService.getById(activity.getUserId()));
                    map.put("check", 1);
                    map.put("checkNot", 0);
                    emailBean.setAttach(true);
                    emailBean.setEmailSubject(subject);
                    emailBean.setEmailType(EmailType.INFORM);
                    emailBean.setEmailTo(ticketsRecord.getConfereeEmail());
                    emailBean.setMap(map);
                    emailBean.setTicketsRecords(Arrays.asList(ticketsRecord));
                    amqpTemplate.convertAndSend(exchange.getName(), RabbitMqConfig.ROUTE_KEY, JsonUtil.toJson(emailBean));

                }
                //微信通知--------参数
                //String activityTitle,
                //String confereeName,
                //String auditResult,
                //String auditTime,
                //String remark,
                //String openid,
                //String redirectUrl
                String openId = trxOrder.getOpenId();
                if (StringUtils.isNotEmpty(openId)) {
                    String auditResult = isPass.equals("true") ? "审核通过" : "审核不通过";
                    String remark = isPass.equals("true") ? "您申请的【" + activity.getActivityTitle() + "】已通过审核" :
                            "很遗憾,您申请的【" + activity.getActivityTitle() + "】被拒绝";
                    String redirectUrl = ticketDetailUrl + trxOrder.getActivityId() + "?openId=" + openId;
                    WeixinPushMassage.wxAuditSuccess(activity.getActivityTitle(),
                            ticketsRecord.getConfereeName(), auditResult,
                            TimeUtils.getCurrentTime(), remark, openId, redirectUrl
                    );
                }
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("TicketsRecordController.getTicketsRecords", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 门票批量审核
     *
     * @param request
     * @param ids
     * @param activityId
     * @return
     */
    @PostMapping("/batchCheckTicket/{activityId}")
    @ResponseBody
    @Access
    public Map checkTicket(HttpServletRequest request, @RequestBody List<Integer> ids, @PathVariable("activityId") Integer activityId,
                           @RequestParam(value = "isPass", required = true) String isPass) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            //通过审核改为未签到
            int count = ticketsRecordService.checkTicketBatch(ids, activityId, isPass.equals("true") ? true : false);
            if (count > 0 && ids != null && ids.size() > 0) {
                List<TicketsRecord> toBeSendList = new ArrayList<>();
                //组装ticketsRecord
                ids.stream().forEach(entity -> {
                    TicketsRecord record = ticketsRecordService.getById(entity);
                    if (record != null) {
                        toBeSendList.add(record);
                    }
                });
                Activity activity = activityService.getById(activityId);
                User user = userService.getById(activity.getUserId());
                // MQ
                if (isPass.equals("true")) {
                    toBeSendList.stream().forEach(entity -> {
                        TrxOrder trxOrder = trxOrderService.getById(entity.getOrderId());
                        if (trxOrder != null) {
                            List<TicketsRecord> ticketsRecords = new ArrayList<>();
                            ticketsRecords.add(entity);
                            String address;
                            if (1 == activity.getIsOnline()) {
                                address = systemDomain + "web/activity/" + activity.getId();
                            } else {
                                address = JsonUtil.fromJson(activity.getActivityAddress(), Address.class).toString();
                            }
                            EmailBean emailBean = new EmailBean();
                            HashMap map = emailBean.getMap();
                            map.put("trxOrder", trxOrder);
                            map.put("activity", activity);
                            map.put("address", address);
                            map.put("user", user);
                            map.put("check", 1);
                            map.put("checkNot", 0);
                            String subject = "您申请的【 " + activity.getActivityTitle() + " 】已经通过组织者审核";
                            emailBean.setAttach(true);
                            emailBean.setTicketsRecords(Arrays.asList(entity));
                            emailBean.setEmailSubject(subject);
                            emailBean.setEmailType(EmailType.INFORM);
                            emailBean.setEmailTo(entity.getConfereeEmail());
                            emailBean.setMap(map);
                            amqpTemplate.convertAndSend(exchange.getName(), RabbitMqConfig.ROUTE_KEY, JsonUtil.toJson(emailBean));
                        }
                    });
                }
                //微信通知--------参数
                //String activityTitle,
                //String confereeName,
                //String auditResult,
                //String auditTime,
                //String remark,
                //String openid,
                //String redirectUrl
                for (TicketsRecord ticketsRecord : toBeSendList) {
                    TrxOrder trxOrder = trxOrderService.getById(ticketsRecord.getOrderId());
                    //不能过滤重复订单 如果一张订单同时存在拒绝和通过的票会出错
                    String openId = trxOrder.getOpenId();
                    if (StringUtils.isNotEmpty(openId)) {
                        String auditResult = isPass.equals("true") ? "审核通过" : "审核不通过";
                        String redirectUrl = ticketDetailUrl + trxOrder.getActivityId() + "?openId=" + openId;
                        String remark = isPass.equals("true") ? "您申请的【" + activity.getActivityTitle() + "】已通过审核" :
                                "很遗憾,您申请的【" + activity.getActivityTitle() + "】被拒绝";
                        WeixinPushMassage.wxAuditSuccess(activity.getActivityTitle(),
                                ticketsRecord.getConfereeName(), auditResult,
                                TimeUtils.getCurrentTime(), remark, openId, redirectUrl
                        );
                    }

                }
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("TicketsRecordController.getTicketsRecords", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 退票
     *
     * @param request
     * @param id
     * @param activityId
     * @return
     */
    @PostMapping("/{id}/refundTicket/{activityId}")
    @ResponseBody
    @Access
    public Map refundTicket(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("activityId") Integer activityId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            int count = ticketsRecordService.refundTicket(id, activityId);
            if (count > 0)
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("TicketsRecordController.getTicketsRecords", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取会议签到成功的列表
     *
     * @param request
     * @return
     */
    @GetMapping("/signedRecord/{activityId}")
    @Access
    public Map getSignedRecord(HttpServletRequest request, @PathVariable("activityId") Integer activityId,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            PageHelper.startPage(pageNum, pageSize);
            PageHelper.orderBy(" sign_time desc");
            List<TicketsRecord> list = ticketsRecordService.selectSignedByActivityId(activityId);
            return ReturnResult.successResult("data", list, ReturnType.LIST_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("TicketsRecordController.getSignedRecord", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 根据签到码或校验码获取门票信息
     *
     * @param request
     * @param activityId
     * @param code
     * @return
     */
    @GetMapping("{activityId}/code/{code}")
    @Access
    public Map selectTicketByCode(HttpServletRequest request, @PathVariable("activityId") Integer activityId, @PathVariable("code") String code) {
        try {
            TicketsRecord ticketsRecord = ticketsRecordService.selectByAuthCodeOrSignCode(activityId, code);
            return ReturnResult.successResult("data", ticketsRecord, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("TicketsRecordController.selectTicketByCode", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 会议签到
     */
    @PostMapping("{id}/signIn/{activityId}")
    @Access
    public Map signIn(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("activityId") Integer activityId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
//            TicketsRecord ticketsRecord = new TicketsRecord();
//            ticketsRecord.setId(id);
//            ticketsRecord.setTicketStatus(TicketStatus.SIGN.getIndex());
            if (ticketsRecordService.updateSignStatus(id, activityId, TicketStatus.SIGN.getIndex()) > 0) {
                return ReturnResult.successResult("签到成功！");
            }
            return ReturnResult.errorResult("签到失败，门票不存在或已失效！");
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("TicketsRecordController.getSignedRecord", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 会议取消签到
     */
    @PostMapping("{id}/signOut/{activityId}")
    @Access
    public Map signOut(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("activityId") Integer activityId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
//            TicketsRecord ticketsRecord = new TicketsRecord();
//            ticketsRecord.setId(id);
//            ticketsRecord.setTicketStatus(TicketStatus.SIGN.getIndex());
            if (ticketsRecordService.updateSignStatus(id, activityId, TicketStatus.SIGN_NOT.getIndex()) > 0) {
                return ReturnResult.successResult("取消签到成功！");
            }
            return ReturnResult.errorResult("取消失败，请稍后再试！");
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("TicketsRecordController.getSignedRecord", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * <pre>
     *     参与
     *     根据票是否到期
     * 获取当前登录人 参与的活动 ，
     *
     * </pre>
     */
    @GetMapping("/participation/{status}")
    // @Access
    public Map getParticipation(@PathVariable("status") Integer status, Integer userId, HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize) {
        try {
            if (userId == null) { // 没传id ,说明是后台查询
                User loginUser = GlobalUtils.getLoginUser(request);
                if (loginUser == null) {
                    return ReturnResult.errorResult(ReturnType.GET_ERROR);
                }
                userId = loginUser.getId();
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<TicketsRecord> pages = new PageInfo<TicketsRecord>(ticketsRecordService.getParticipation(userId, status));
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<TicketsRecord> ticketsRecords = ticketsRecordService.getParticipation(userId, status);
            return ReturnResult.successResult("data", ticketsRecords, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("TicketsRecordController.getParticipation", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 下载门票
     * * @param id
     *
     * @param response
     * @return
     */
//    @GetMapping("/getPDFTicket/{id}")
//    @ResponseBody
    public Map getPDFTicket(@PathVariable("id") Integer id,
                            @RequestParam(value = "type", defaultValue = "PDF") String type,
                            HttpServletResponse response) {
        TicketsRecord ticketsRecord = ticketsRecordService.getById(id);
        if (ticketsRecord == null) {
            return ReturnResult.errorResult("该条记录不存在");
        }
        if (ticketsRecord.getTicketStatus() == 0 ||
                ticketsRecord.getTicketStatus() == 4 ||
                ticketsRecord.getTicketStatus() == 9 ||
                ticketsRecord.getTicketStatus() == 3) {
            return ReturnResult.errorResult("门票已经过期或者尚未通过审核");
        }
        String fileName = "";
        if (type.toUpperCase().equals("PDF")) { //Type=1生成PDF的票
            //如果没有票的记录,就生成一张票
            List<TicketsRecord> ticketsRecords = new ArrayList<>();
            ticketsRecords.add(ticketsRecord);
            if (StringUtils.isEmpty(ticketsRecord.getTicketLink())) {
                fileName = generateTicketService.getTicketPDF(ticketsRecords, true);
            } else {
                fileName = ticketsRecord.getTicketLink();
            }
            if (StringUtils.isEmpty(fileName)) {
                return ReturnResult.errorResult("生成票据出错");
            }
            //此处filName 取的是数据库的URL
            //fileName = "/email/2018-05-03/611500_1782410056_signal.html.pdf"
            // fileName = "H:/"+fileName;
            File file = new File(fileName);

            //票据被误删,重新生成新的票
            if (!file.exists()) {
                fileName = generateTicketService.getTicketPDF(ticketsRecords, true);
            }
        } else {
            fileName = generateTicketService.generateTicketImg(ticketsRecord);
            if (StringUtils.isEmpty(fileName)) {
                return ReturnResult.errorResult("生成票据出错");
            }
        }
        //设置相应头
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        try {
            //读写流进行下载
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            IOUtils.write(bytes, response.getOutputStream());

        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return ReturnResult.errorResult(e.getMessage());
        }
        return ReturnResult.successResult("data", ticketsRecord, ReturnType.GET_SUCCESS);
    }


    /**
     * 下载门票
     * * @param id
     *
     * @return
     */
    @GetMapping("/getPDFTicket/{id}")
    @ResponseBody
    public Map test1111(@PathVariable("id") Integer id,
                        @RequestParam(value = "type", defaultValue = "PDF") String type,
                        HttpServletResponse response) {
            TicketsRecord ticketsRecord = ticketsRecordService.getById(id);
            if (ticketsRecord == null) {
                return ReturnResult.errorResult("该条记录不存在");
            }
            if (ticketsRecord.getTicketStatus() == 0 ||
                    ticketsRecord.getTicketStatus() == 4 ||
                    ticketsRecord.getTicketStatus() == 9 ||
                    ticketsRecord.getTicketStatus() == 3) {
                return ReturnResult.errorResult("门票已经过期或者尚未通过审核");
            }
            String address;
            Activity activity = activityService.getById(ticketsRecord.getActivityId());
            if (1 == activity.getIsOnline()) {
                address = "线上活动";
            } else {
                address = JsonUtil.fromJson(activity.getActivityAddress(), Address.class).toString();
            }

            List<TicketsRecord> ticketsRecords = new ArrayList<>();
            ticketsRecords.add(ticketsRecord);
            Map map = new HashMap();
            map.put("address", address);
            map.put("activity", activity);
            map.put("ticketsRecords", ticketsRecords);

        CopyOnWriteArrayList arrayList = new CopyOnWriteArrayList();
        return ReturnResult.errorResult("生成票据出错");
    }


    /**
     * 重新发送通知邮件
     *
     * @param id
     * @return
     */
    @GetMapping("/reSendEmail/{id}")
    @ResponseBody
    @Access
    public Map reSendEmail(@PathVariable("id") Integer id) {
        TicketsRecord ticketsRecord = ticketsRecordService.getById(id);
        if (ticketsRecord == null) {
            return ReturnResult.errorResult("该记录不存在");
        }
        TrxOrder trxOrder = trxOrderService.selectById(ticketsRecord.getOrderId());
        Activity activity = activityService.getById(trxOrder.getActivityId());
        if (trxOrder == null || activity == null) {
            return ReturnResult.errorResult("该活动或者订单记录不完整");
        } else {
            //（0:未生成，1：未签到，2：已签到，3：待审核，4:审核未通过，9：已退票）
            if (ticketsRecord.getTicketStatus() == 1) {

                //状态为未签到,可以重新发送
                // MQ
                String subejct = "您申请的【" + activity.getActivityTitle() + "】已经通过组织者审核";


                String address;
                if (1 == activity.getIsOnline()) {
                    address = systemDomain + "web/activity/" + activity.getId();
                } else {
                    address = JsonUtil.fromJson(activity.getActivityAddress(), Address.class).toString();
                }
                EmailBean emailBean = new EmailBean();
                HashMap map = emailBean.getMap();
                map.put("trxOrder", trxOrder);
                map.put("activity", activity);
                map.put("address", address);
                map.put("user", userService.getById(activity.getUserId()));
                map.put("check", 1);
                map.put("checkNot", 0);
                emailBean.setAttach(true);
                emailBean.setEmailSubject(subejct);
                emailBean.setEmailType(EmailType.INFORM);
                emailBean.setEmailTo(ticketsRecord.getConfereeEmail());
                emailBean.setMap(map);
                emailBean.setTicketsRecords(Arrays.asList(ticketsRecord));
                amqpTemplate.convertAndSend(exchange.getName(), RabbitMqConfig.ROUTE_KEY, JsonUtil.toJson(emailBean));
            } else {
                //否则不能进行重新发送
                return ReturnResult.errorResult("无法重新发送,仅有通过审核才可以发送邮件");
            }
        }
        return ReturnResult.successResult("邮件发送成功");
    }

    @GetMapping("/statisticsMyTicket")
    @ResponseBody
    @Access
    public Map statisticsMyTicket(@RequestParam(value = "userId", required = false) Integer userId, HttpServletRequest request) {
        Integer resId;
        if (userId == null) {
            resId = GlobalUtils.getLoginUser(request).getId();
        } else {
            resId = userId;
        }
        Integer valid = ticketsRecordService.statisticsMyTicket(resId, true);
        Integer invalid = ticketsRecordService.statisticsMyTicket(resId, false);
        HashMap<String, Integer> res = new HashMap();
        res.put("valid", valid);
        res.put("invalid", invalid);
        return ReturnResult.successResult("data", res, ReturnType.GET_SUCCESS);
    }
}
