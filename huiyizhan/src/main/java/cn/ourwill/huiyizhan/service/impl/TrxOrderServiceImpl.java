package cn.ourwill.huiyizhan.service.impl;


import cn.ourwill.huiyizhan.baseEnum.EmailType;
import cn.ourwill.huiyizhan.baseEnum.TicketStatus;
import cn.ourwill.huiyizhan.config.RabbitMqConfig;
import cn.ourwill.huiyizhan.entity.*;
import cn.ourwill.huiyizhan.mapper.*;
import cn.ourwill.huiyizhan.service.ITrxOrderService;
import cn.ourwill.huiyizhan.utils.*;
import cn.ourwill.huiyizhan.weChat.Utils.PageAuthorizeUtils;
import cn.ourwill.huiyizhan.weChat.WXpay.*;
import cn.ourwill.huiyizhan.weChat.pojo.QRCodeParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static cn.ourwill.huiyizhan.entity.Config.systemDomain;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/11 0011 15:20
 * @Version1.0
 */
@Service
@Slf4j
public class TrxOrderServiceImpl extends BaseServiceImpl<TrxOrder> implements ITrxOrderService{
    @Autowired
    private TrxOrderMapper trxOrderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LicenseRecordMapper licenseRecordMapper;

    @Autowired
    private PriceTypeMapper priceTypeMapper;

    @Autowired
    private TicketsRecordMapper ticketsRecordMapper;

    @Autowired
    private ActivityTicketsMapper activityTicketsMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    RedisTemplate redisTemplate;


    @Autowired
    private SysSequenceMapper sequenceMapper;

    @Autowired
    AmqpTemplate                        amqpTemplate;
    @Autowired
    org.springframework.amqp.core.Queue queue;

    @Autowired
    TopicExchange exchange;

    @Value("${weixin.expire.seconds}")
    private Integer expire_seconds;

    //    @Autowired
//    private SystemConfigMapper systemConfigMapper;
    @Value("${weixin.pay.useSandbox}")
    private boolean useSandbox;
    @Value("${weixin.pay.callBackUrl}")
    private String callBackUrl;
    @Value("${weixin.pay.certPath}")
    private String certPath;
    @Value("${scheduled.switch}")
    private Boolean scheduledSwitch;

    private static WXPay wxpay;

    private WXPayConfigImpl config;

    private WXPay getWXPay() throws Exception {
        config = WXPayConfigImpl.getInstance(certPath);
        if(wxpay==null){
            wxpay = new WXPay(config, WXPayConstants.SignType.MD5, useSandbox);
        }
        return wxpay;
    }


    /**
     * 生成订单号
     * @return
     */
    public String generateOrderNo(String type){
        StringBuffer str = new StringBuffer(type);
        str.append(new SimpleDateFormat("yyMMdd").format(new Date()));
        str.append(String.valueOf(System.currentTimeMillis()).substring(6,12));
        str.append(String.format("%06d", sequenceMapper.nextVal("trxorder")));
        str.append(GlobalUtils.getRandomString(4, 1));
        return str.toString();
    }

    /**
     * 门票下单
     * @param trxOrder
     * @return
     */
    @Override
    @Transactional
    public Map addTicketOrder(TrxOrder trxOrder) throws Exception {
        List<TicketsRecord> ticketList = trxOrder.getTicketsRecordList();

        trxOrder.setNumber(ticketList.size());
        trxOrder.setPrice(0);
        trxOrder.setIsCheckView(1);
        //门票信息处理
        //查询会议活动的所有门票模版
        List<ActivityTickets> activityTickets = activityTicketsMapper.selectByActivityId(trxOrder.getActivityId());
        //将模版记录列表按模版id分组:Map(modelId:model)
        Map<Integer,ActivityTickets> activityTicketsHashMap = new HashMap<>();
        activityTickets.stream().forEach(e->activityTicketsHashMap.put(e.getId(),e));
        //将订单门票记录按门票模版id分组：Map(modelId:recordList)
        Date now = new Date();
        Map<Integer,List<TicketsRecord>> ticksMap = this.groupByTicketsId(ticketList);
        //循环检查，更新门票模版剩余库存
        for(Integer ticketsId : ticksMap.keySet()){
            List<TicketsRecord> eachTicketList = ticksMap.get(ticketsId);
            ActivityTickets ticketModel = activityTicketsHashMap.get(ticketsId);
            if(ticketModel==null) return ReturnResult.errorResult("下单失败，存在无效票！");
            //更新库存
            if(ticketModel.getTotalNumber()>0 && activityTicketsMapper.updateStockNumber(ticketsId, eachTicketList.size())<1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ReturnResult.errorResult("库存不足！下单失败");
            }
//            if(ticketModel.getStartTime())
            for(TicketsRecord ticket:eachTicketList){
                ticket.setTicketsName(ticketModel.getTicketName());
                ticket.setTicketsPrice(ticketModel.getTicketPrice());
                ticket.setActivityId(ticketModel.getActivityId());
                if(ticketModel.getIsCheck().equals(1)){
                    //如果需要审核 则为待审核
                    ticket.setTicketStatus(3);
                }else{
                    //不需要审核 则为待签到
                    ticket.setTicketStatus(1);
                    trxOrder.setIsCheckView(0);
                }
                //产生签到号和校验码
                {
                    int signCode = 0;
                    boolean valid = false;
                    do{
                        signCode = GlobalUtils.generateSignCode();
                        valid = this.addSignCode(trxOrder.getActivityId(), signCode);
                    }while(!valid);
                    ticket.setSignCode(signCode);
                    ticket.setAuthCode(GlobalUtils.generateUUID());
                }
                ticket.setUserId(trxOrder.getUserId());
                ticket.setCTime(now);
            }
        }
//        if()
        //将二维码url和二维码过期时间保存到订单表中
        //获取access token
        String accessToken = PageAuthorizeUtils.getAccess_token(false);

        //封装二维码参数
        Activity activity = activityMapper.getById(trxOrder.getActivityId());
        QRCodeParam qrCodeParam = new QRCodeParam();
        qrCodeParam.setAccess_token(accessToken);
        qrCodeParam.setExpire_seconds(expire_seconds);
        Date overtime=new Date(trxOrder.getCreateTime().getTime()+qrCodeParam.getExpire_seconds().longValue()*1000);
        trxOrder.setOverTime(overtime);
        qrCodeParam.setAction_name("QR_SCENE");
        //TR_开头的参数，为订单号
        qrCodeParam.setScene_str("TR_"+trxOrder.getOrderNo());
        String QRCodeTicketUrl = PageAuthorizeUtils.getQRCodeTicket(qrCodeParam);
        if(StringUtils.isNotEmpty(QRCodeTicketUrl)){
            trxOrder.setQRCodeTicketUrl(QRCodeTicketUrl);
        }
        //保存订单
        int count = trxOrderMapper.insertSelective(trxOrder);

        if (count > 0) {
            ticketList.stream().forEach(entity->entity.setOrderId(trxOrder.getId()));
            //保存门票记录
            ticketsRecordMapper.batchSave(trxOrder.getTicketsRecordList());

            //-----------------------MQ方式
            //遍历获取票的相关情况,订单邮件
            List<TicketsRecord> ticketsRecordList = trxOrder.getTicketsRecordList();
            Integer check = 0;
            Integer checkNot = 0;
            for(TicketsRecord ticketsRecord : ticketsRecordList){
                if(ticketsRecord.getTicketStatus() == 3) checkNot ++;
                else check ++;
            }
            EmailBean emailBean = new EmailBean();
            HashMap map = emailBean.getMap();
            User user = userMapper.selectByPrimaryKey(activity.getUserId());

            String address;
            if(1 == activity.getIsOnline()){
                address =  systemDomain+"web/activity/"+activity.getId();
            }else{
                address = JsonUtil.fromJson(activity.getActivityAddress(), Address.class).toString();
            }

            map.put("trxOrder",trxOrder);
            map.put("user",user);
            map.put("activity",activity);
            map.put("address", address);
            map.put("checkNot",checkNot);
            map.put("check",check);
            map.put("QRCode",trxOrder.getQRCodeTicketUrl());
            map.put("ticketsRecords",ticketsRecordList);
            //通过审核的票数为0则无附件
            emailBean.setAttach(check == 0 ? false : true);
            emailBean.setTicketsRecords(check == 0 ? null : ticketsRecordList);
            emailBean.setEmailSubject("订单确认函");
            emailBean.setEmailTo(trxOrder.getBuyerEmail());
            emailBean.setEmailType(EmailType.TICKET);
            emailBean.setMap(map);
            //写入MQ
            amqpTemplate.convertAndSend(exchange.getName(), RabbitMqConfig.ROUTE_KEY,JsonUtil.toJson(emailBean));

            //通知邮件开始
            List<TicketsRecord> records = trxOrder.getTicketsRecordList().stream().filter(ticketsRecord -> ticketsRecord.getTicketStatus() == 1).collect(Collectors.toList());
            records.stream().forEach(record->{
                EmailBean bean = new EmailBean();
                HashMap hashMap = bean.getMap();
                hashMap.put("trxOrder",trxOrder);
                hashMap.put("activity",activity);
                hashMap.put("user",user);
                hashMap.put("address", address);
                hashMap.put("check",1);
                hashMap.put("checkNot",0);
                bean.setAttach(true);
                bean.setEmailSubject("您申请的【 " + activity.getActivityTitle() + " 】已经通过组织者审核");
                bean.setEmailTo(record.getConfereeEmail());
                bean.setEmailType(EmailType.INFORM);
                bean.setMap(hashMap);
                bean.setTicketsRecords(Arrays.asList(record));
                amqpTemplate.convertAndSend(exchange.getName(), RabbitMqConfig.ROUTE_KEY,JsonUtil.toJson(bean));
            });
            return ReturnResult.successResult("data",trxOrder,"下单成功！");
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return ReturnResult.errorResult("下单失败，请稍后再试！");
    }

    @Override
    public TrxOrder getByNo(String orderNo) {
        return trxOrderMapper.getByOrderNo(orderNo);
    }

    @Override
    public Integer updateOrderOpenIdByNO(TrxOrder order) {
        return trxOrderMapper.updateOrderOpenIdByNO(order);
    }

    @Override
    public List<TrxOrder> selectByOpenId(String fromUserName) {
        return trxOrderMapper.selectByOpenId(fromUserName);
    }


    private boolean addSignCode(Integer activityId,Integer code) {
        return redisTemplate.opsForZSet().add("signCode:"+activityId,code,System.currentTimeMillis());
    }


    private boolean deleteSignCode(Integer activityId,Integer code) {
        Long result = redisTemplate.opsForZSet().remove("signCode:"+activityId,code);
        if(result>0){
            return true;
        }
        return false;
    }


    private boolean isExitSignCode(Integer activityId,Integer code) {
        Double result = redisTemplate.opsForZSet().score("signCode:"+activityId,code);
        if(result==null){
            return false;
        }
        return true;
    }

    private Map<Integer,List<TicketsRecord>> groupByTicketsId(List<TicketsRecord> ticketsRecords){
        Map<Integer,List<TicketsRecord>> ticketsIdMap = new HashMap<>();
        for (TicketsRecord ticketsRecord : ticketsRecords) {
            List<TicketsRecord> tempList = ticketsIdMap.get(ticketsRecord.getTicketsId());
            if(tempList == null){
                tempList = new ArrayList<>();
                tempList.add(ticketsRecord);
                ticketsIdMap.put(ticketsRecord.getTicketsId(), tempList);
            }else{
                tempList.add(ticketsRecord);
            }
        }
        return ticketsIdMap;
    }

//    @Override
//    public Integer getLikeNum(Integer commentId) {
//        return redisTemplate.opsForZSet().zCard("com:"+commentId).intValue();
//    }

    /**
     * 统一下单
     * @param trxOrder
     * @return
     */
    @Override
    public Map addOrder(TrxOrder trxOrder) {
        try {
            HashMap<String, String> data = new HashMap<String, String>();
            PriceType priceType = priceTypeMapper.getByType(trxOrder.getType());
            if(priceType==null){
                return ReturnResult.errorResult("套餐类型错误！");
            }
            data.put("body",priceType.getDescription());
            //价格计算 begin
//            SystemConfig systemConfig = systemConfigMapper.getConfig();
//            if(systemConfig.getDiscountSwitch().equals(0)) {
//                if (trxOrder.getType().equals(3)) {
//                    Integer number = trxOrder.getNumber();
//                    Integer price = priceType.getPrice();
//                    trxOrder.setAmount(number * price);
//                } else {
//                    trxOrder.setNumber(priceType.getNumber());
//                    trxOrder.setAmount(priceType.getPrice());
//                }
//            }
            Contract contract = null;
            if(priceType.getPhotoLive().equals(0)) {
                contract = getContract(trxOrder.getNumber(), priceType, trxOrder.getUserId());
            }else{
                contract = getPhotoContract(trxOrder.getNumber(), priceType, trxOrder.getUserId());
            }
            trxOrder.setNumber(contract.getActivityNum());
            trxOrder.setAmount(contract.getActivityAmount());
            trxOrder.setPrice(priceType.getPrice());
            String tradeType = trxOrder.getTradeType();//支付类型
            data.put("out_trade_no", trxOrder.getOrderNo());
            data.put("device_info", "WEB");
            data.put("total_fee", String.valueOf(trxOrder.getAmount()));
            data.put("spbill_create_ip", trxOrder.getCreateIp());
            data.put("notify_url", callBackUrl);
            data.put("trade_type", tradeType);
            if(tradeType.equals("JSAPI")) {
                data.put("openid", trxOrder.getOpenId());
            }else if(tradeType.equals("NATIVE")){
                data.put("product_id",trxOrder.getOrderNo());
            }else{
                return ReturnResult.errorResult("支付类型错误！");
            }
            log.info("统一下单："+data.toString());
            Map<String,String> reMap = getWXPay().unifiedOrder(data);
            log.info("统一下单返回："+reMap.toString());
            //会写订单信息
            if(reMap.get("return_code").equals(WXPayConstants.SUCCESS)){
                if(reMap.get("result_code").equals(WXPayConstants.SUCCESS)) {
                    trxOrder.setPrepayId(reMap.get("prepay_id"));
                    if(tradeType.equals("NATIVE")); trxOrder.setCodeUrl(reMap.get("code_url"));
                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.clear();
                    calendar.setTime(date);
                    calendar.add(Calendar.MINUTE, 110);
                    trxOrder.setPrepayIdDueTime(calendar.getTime());
                    trxOrder.setTransactionStatus(0);
                    trxOrder.setOrderStatus(0);
                    if (trxOrderMapper.insertSelective(trxOrder) > 0) {
                        HashMap map = new HashMap();
                        HashMap redata = new HashMap();
                        if(tradeType.equals("JSAPI")) {
                            redata.put("appId", config.getAppID());
                            redata.put("timeStamp", String.valueOf(GlobalUtils.getTimestamp()));
                            redata.put("nonceStr", WXPayUtil.generateNonceStr());
                            redata.put("package", "prepay_id=" + trxOrder.getPrepayId());
                            redata.put("signType", "MD5");
                            log.info("+++++++++++mchId:" + config.getMchID());
                            log.info("+++++++++++key:" + config.getKey());
                            log.info("+++++++++++redata:" + redata.toString());
                            String sign = WXPayUtil.generateSignature(redata, config.getKey(), WXPayConstants.SignType.MD5);
                            log.info("+++++++++++sign:" + sign);
                            redata.put("paySign", sign);
                            log.info("+++++++++++redataXml:" + WXPayUtil.mapToXml(redata));
                        }else if(tradeType.equals("NATIVE")){
                            redata.put("codeUrl",trxOrder.getCodeUrl());
                        }
                        map.put("trxOrder",trxOrder);
                        map.put("jsData",redata);
                        return ReturnResult.successResult("data", map, ReturnType.ADD_SUCCESS);
                    }
                    return ReturnResult.errorResult(ReturnType.ADD_ERROR);
                }
                return ReturnResult.errorResult(reMap.get("err_code_des"));
            }
            return ReturnResult.errorResult(reMap.get("return_msg"));
        } catch (Exception e) {
            log.info("TrxOrderServiceImpl.addOrder",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 页面获取价格列表
     * @return
     */
    @Override
    public List<Contract> getContractList(Integer userId) {
        List<PriceType> priceTypes = priceTypeMapper.findAll(0);
        List<Contract> contracts = new ArrayList<>();
        for (PriceType priceType : priceTypes) {
            if(!priceType.getType().equals(4))
                contracts.add(getContract(1,priceType,userId));
        }
        return contracts;
    }

    /**
     * 页面获取价格列表(照片直播)
     * @return
     */
    @Override
    public List<Contract> getPhotoContractList(Integer userId) {
        List<PriceType> priceTypes = priceTypeMapper.findAll(1);
        List<Contract> contracts = new ArrayList<>();
        for (PriceType priceType : priceTypes) {
            contracts.add(getPhotoContract(1,priceType,userId));
        }
        return contracts;
    }

    /**
     * 页面获取价格
     * @param activityNum
     * @return
     */
    public Contract getContract(Integer activityNum,Integer userId){
        PriceType priceType = priceTypeMapper.getByType(3);
        return getContract(activityNum,priceType,userId);
    }

    /**
     * 页面获取价格(照片直播)
     * @param activityNum
     * @return
     */
    public Contract getPhotoContract(Integer activityNum,Integer userId){
        PriceType priceType = priceTypeMapper.getByType(7);
        return getPhotoContract(activityNum,priceType,userId);
    }

    /**
     * 获取金额
     * @param activityNum
     * @param priceType
     * @return
     */
    @Override
    public Contract getContract(Integer activityNum,PriceType priceType,Integer userId){
        //价格计算 begin
        Contract contract = new Contract();
//        SystemConfig systemConfig = systemConfigMapper.getConfig(0);
//        if(systemConfig.getDiscountSwitch().equals(1)&&systemConfig.getDiscountStart().before(new Date())&&systemConfig.getDiscountEnd().after(new Date())) {
//            contract = getDiscountContract2(activityNum,priceType,userId);
//        }else{
        contract = getDiscountContract(activityNum,priceType,userId);
//            if (priceType.getType().equals(3)) {
//                Integer number = activityNum;
//                Integer price = priceType.getPrice();
//                contract.setPriceType(3);
//                contract.setActivityNum(activityNum);
//                contract.setActivityDays(activityNum);
//                contract.setActivityValid(0);
//                contract.setActivityAmount(number * price);
//            } else if(priceType.getType().equals(2)){
//                contract.setPriceType(2);
//                contract.setActivityNum(priceType.getNumber());
//                contract.setActivityDays(priceType.getNumber());
//                contract.setActivityValid(0);
//                contract.setActivityAmount(priceType.getPrice());
//            } else{
//                contract.setPriceType(1);
//                contract.setActivityNum(priceType.getNumber());
//                contract.setActivityDays(priceType.getNumber());
//                contract.setActivityValid(365);
//                contract.setActivityAmount(priceType.getPrice());
//            }
//        }
        return contract;
    }

    /**
     * 获取优惠金额(根据实际活动修改)
     * 双11优惠
     * @param activityNum
     * @param priceType
     * @return
     */
    private Contract getDiscountContract2(Integer activityNum, PriceType priceType, Integer userId) {
        Contract contract = new Contract();
        if (priceType.getType().equals(3)) {
            Integer number = activityNum;
            Integer price = priceType.getPrice();
            contract.setPriceType(3);
            contract.setActivityNum(activityNum);
            contract.setActivityDays(activityNum);
            contract.setActivityValid(0);
            contract.setActivityOrignAmount(number * price);
            contract.setActivityAmount(number * (price - 20000));
        } else if(priceType.getType().equals(2)){
            contract.setPriceType(2);
            contract.setActivityNum(priceType.getNumber()+2);
            contract.setActivityDays(priceType.getNumber()+2);
            contract.setActivityValid(0);
            contract.setActivityOrignAmount(priceType.getPrice());
            contract.setActivityAmount(priceType.getPrice()-150900);
        } else {
            contract.setPriceType(1);
            contract.setActivityNum(priceType.getNumber());
            contract.setActivityDays(priceType.getNumber());
            contract.setActivityValid(365);
            contract.setActivityOrignAmount(priceType.getPrice());
            contract.setActivityAmount(priceType.getPrice()-450000);
        }
        return contract;
    }

    /**
     * 获取优惠金额(根据实际活动修改)
     * 首次购买优惠
     * @param activityNum
     * @param priceType
     * @return
     */
    private Contract getDiscountContract(Integer activityNum, PriceType priceType, Integer userId) {
        Contract contract = new Contract();
        if (priceType.getType().equals(3)) {
            //校验用户是否符合优惠
            boolean discount = true;
            if(userId!=null) {
                List<LicenseRecord> licenseRecords = licenseRecordMapper.selectByUserId(userId,priceType.getPhotoLive());
                if(licenseRecords!=null&&licenseRecords.size()>0){
                    discount = false;
                }
            }else{
                discount = false;
            }
            Integer number = activityNum;
            Integer price = priceType.getPrice();
            contract.setPriceType(3);
            contract.setActivityNum(activityNum);
            contract.setActivityDays(activityNum);
            contract.setActivityValid(0);
            if(discount) {
                contract.setActivityOrignAmount(number * price);
                contract.setActivityAmount(number * price - 20000);
            }else{
                contract.setActivityAmount(number * price);
            }
        } else if(priceType.getType().equals(2)){
            contract.setPriceType(2);
            contract.setActivityNum(priceType.getNumber());
            contract.setActivityDays(priceType.getNumber());
            contract.setActivityValid(0);
            contract.setActivityAmount(priceType.getPrice());
        } else {
            contract.setPriceType(1);
            contract.setActivityNum(priceType.getNumber());
            contract.setActivityDays(priceType.getNumber());
            contract.setActivityValid(365);
            contract.setActivityAmount(priceType.getPrice());
        }
        return contract;
    }

    /**
     * 获取优惠金额(根据实际活动修改)(照片直播)
     * 首次购买优惠
     * @param activityNum
     * @param priceType
     * @return
     */
    private Contract getPhotoDiscountContract(Integer activityNum, PriceType priceType, Integer userId) {
        return null;
    }
    /**
     * 获取金额（照片直播）
     * @param activityNum
     * @param priceType
     * @return
     */
    @Override
    public Contract getPhotoContract(Integer activityNum,PriceType priceType,Integer userId){
        //价格计算 begin
        Contract contract = new Contract();
//        SystemConfig systemConfig = systemConfigMapper.getConfig(1);
//        if(systemConfig.getDiscountSwitch().equals(1)&&systemConfig.getDiscountStart().before(new Date())&&systemConfig.getDiscountEnd().after(new Date())) {
//            contract = getPhotoDiscountContract(activityNum,priceType,userId);
//        }else{
//            contract = getDiscountContract(activityNum,priceType,userId);
        if (priceType.getType().equals(7)) {
            Integer number = activityNum;
            Integer price = priceType.getPrice();
            contract.setPriceType(7);
            contract.setActivityNum(activityNum);
            contract.setActivityDays(activityNum);
            contract.setActivityValid(0);
            contract.setActivityAmount(number * price);
        } else if(priceType.getType().equals(6)){
            contract.setPriceType(6);
            contract.setActivityNum(priceType.getNumber());
            contract.setActivityDays(priceType.getNumber());
            contract.setActivityValid(0);
            contract.setActivityAmount(priceType.getPrice());
        } else{
            contract.setPriceType(5);
            contract.setActivityNum(priceType.getNumber());
            contract.setActivityDays(priceType.getNumber());
            contract.setActivityValid(365);
            contract.setActivityAmount(priceType.getPrice());
        }
//        }
        return contract;
    }


    /**
     * 查询订单
     * @param orderNo
     * @return
     */
    @Transactional
    @Override
    public Map queryOrder(String orderNo) {
        try {
            SimpleDateFormat sdf  = new SimpleDateFormat("yyyyMMddHHmmss");
            //本地数据查询,如果数据未接收通知，则查询订单
            TrxOrder trxOrder = trxOrderMapper.getByOrderNoWithClock(orderNo);
            if(trxOrder==null){
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            }
            if(!trxOrder.getOrderStatus().equals(0)){
                return ReturnResult.successResult("data",trxOrder,ReturnType.GET_SUCCESS);
            }
//            if(trxOrder.getOrderStatus().equals(2)){
//                if(!trxOrder.getTransactionStatus().equals(0)&&!trxOrder.getTransactionStatus().equals(6)){
//                    return ReturnResult.successResult("data",trxOrder,ReturnType.GET_SUCCESS);
//                }
//            }
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no",orderNo);
            Map<String,String> reMap = getWXPay().orderQuery(data);
//            log.info("订单查询："+reMap.toString());
//            if(reMap.get("return_code").equals(WXPayConstants.SUCCESS)) {
//                if(reMap.get("result_code").equals(WXPayConstants.SUCCESS)) {
//                    trxOrder.setTransactionId(reMap.get("transaction_id"));
//                    trxOrder.setTransactionStatus(getTransactionStatus(reMap.get("trade_state")));
//                    if(!trxOrder.getTransactionStatus().equals(0)&&!trxOrder.getTransactionStatus().equals(6)) {
//                        trxOrder.setOrderStatus(2);//已查询
//                        trxOrder.setBankType(reMap.get("bank_type"));
//                        trxOrder.setFeeType(reMap.get("fee_type"));
//                        String finishTimeStr = reMap.get("time_end");
//                        trxOrder.setFinishTime(sdf.parse(finishTimeStr));
//                        if (trxOrderMapper.update(trxOrder) > 0) {
//                            log.info("更新订单成功！");
//                        }
//                    }
//                    //支付成功 则授权
//                    if(trxOrder.getTransactionStatus().equals(1)){
//                        LicenseRecord licenseRecord = new LicenseRecord();
//                        User user = userMapper.getById(trxOrder.getUserId());
//                        licenseRecord.setSessionsTotal(trxOrder.getNumber());
//                        licenseRecord.setAmount(String.valueOf(trxOrder.getAmount()/100D));
//                        //创建时间
//                        licenseRecord.setCTime(new Date());
//                        //交易时间
//                        licenseRecord.setTransactionDate(trxOrder.getFinishTime());
//                        licenseRecord.setUserId(user.getId());
//                        licenseRecord.setUsername(user.getUsername());
//                        licenseRecord.setPaymentType("微信支付");
//                        licenseRecord.setOrderNo(trxOrder.getOrderNo());
//                        if(trxOrder.getType().equals(1)) {//包年
//                            licenseRecord.setLicenseType(1);
//                            user.setLicenseType(1);
//                            Calendar c = Calendar.getInstance();
//                            c.add(Calendar.YEAR, 1);
//                            //授权截止日期
//                            licenseRecord.setDueDate(c.getTime());
//                            user.setDueDate(c.getTime());
//                            user.setPackYearsDays(trxOrder.getNumber());
//                        }else if(trxOrder.getType().equals(2)||trxOrder.getType().equals(3)){//包场
//                            licenseRecord.setLicenseType(2);
//                            user.setLicenseType(2);
//                            Integer remainingDays = user.getRemainingDays()==null?0:user.getRemainingDays();
//                            user.setRemainingDays(remainingDays+trxOrder.getNumber());
//                        } else if(trxOrder.getType().equals(5)){
//                            licenseRecord.setLicenseType(1);
//                            user.setPhotoLicenseType(1);
//                            Calendar c = Calendar.getInstance();
//                            c.add(Calendar.YEAR, 1);
//                            //授权截止日期
//                            licenseRecord.setDueDate(c.getTime());
//                            user.setPhotoDueDate(c.getTime());
//                            user.setPhotoPackYearsDays(100);
//                        } else{
//                            licenseRecord.setLicenseType(2);
//                            user.setPhotoLicenseType(2);
//                            Integer remainingDays = user.getPhotoRemainingDays() == null ? 0 : user.getPhotoRemainingDays();
//                            user.setPhotoRemainingDays(remainingDays + trxOrder.getNumber());
//                        }
//                        userMapper.updateAuthorization(user);
//                        licenseRecordMapper.save(licenseRecord);
//
////                        try {
////                            String remark = "";
////                            if (trxOrder.getOrderNo() != null)
////                                remark = "订单号：" + trxOrder.getOrderNo();
////                            //发送购买成功通知模板信息
////                            PriceType priceType = priceTypeMapper.getByType(trxOrder.getType());
////                            SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                            WeixinPushMassage.wxPurchaseSuccess(priceType.getDescription(), licenseRecord.getAmount(), sdft.format(licenseRecord.getTransactionDate()), remark, user.getWechatNum(), "");
////                        }catch (Exception e){
////                            log.error("WeixinPushMassage.wxPurchaseSuccess",e);
////                        }
//                    }
//                    return ReturnResult.successResult("data", trxOrder, ReturnType.GET_SUCCESS);
//                }
//                return ReturnResult.errorResult(reMap.get("err_code_des"));
//            }
            return ReturnResult.errorResult(reMap.get("return_msg"));
        } catch (Exception e) {
            log.info("TrxOrderServiceImpl.queryOrder",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 关闭订单
     * @param orderNo
     * @return
     */
    public Map closeOrder(String orderNo){
        try {
            TrxOrder trxOrder = trxOrderMapper.getByOrderNo(orderNo);
            if(trxOrder.getTransactionStatus().equals(4)) {
                return ReturnResult.errorResult("当前订单已关闭，无法重复执行！");
            }
            HashMap<String,String> data = new HashMap<>();
            data.put("out_trade_no",orderNo);
            Map<String,String> reMap = getWXPay().closeOrder(data);
            if(reMap.get("return_code").equals(WXPayConstants.SUCCESS)){
                if(reMap.get("result_code").equals(WXPayConstants.SUCCESS)){
                    trxOrder.setTransactionStatus(4);
                    trxOrderMapper.updateByPrimaryKeySelective(trxOrder);
                    return ReturnResult.successResult("订单已关闭！");
                }
                return ReturnResult.errorResult(reMap.get("err_code_des"));
            }
            return ReturnResult.errorResult(reMap.get("return_msg"));
        } catch (Exception e) {
            log.info("TrxOrderServiceImpl.closeOrder",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
    /**
     * 通知回调
     * @param strXml
     * @return
     */
    @Transactional
    @Override
    public Map<String, String> callback(String strXml) {
        try {
            log.info("通知回调：strXml:"+strXml);
            Map<String,String> originMap = WXPayUtil.xmlToMap(strXml);
            Map<String,String> reMap = new HashMap<>();
            SimpleDateFormat sdf  = new SimpleDateFormat("yyyyMMddHHmmss");
            WXPay wxPay = getWXPay();
            if(!wxPay.isPayResultNotifySignatureValid(originMap)){
                log.info("签名失败！");
                reMap.put("returnCode",WXPayConstants.FAIL);
                reMap.put("returnMsg","签名失败");
                return reMap;
            }
            String returnCode = originMap.get("return_code");//通信状态
            String resultCode = originMap.get("result_code");//交易状态
            if(returnCode.equals(WXPayConstants.SUCCESS)) {
                TrxOrder trxOrder = trxOrderMapper.getByOrderNoWithClock(originMap.get("out_trade_no"));
                if (trxOrder != null && trxOrder.getOrderStatus().equals(0) && trxOrder.getAmount().equals(Integer.valueOf(originMap.get("total_fee")))) {
                    log.info("通知回调有效，更新订单数据！");
                    if (resultCode.equals(WXPayConstants.SUCCESS)) {
                        log.info("callback,支付成功");
                        trxOrder.setTransactionStatus(1);
                        trxOrder.setOrderStatus(1);
                        trxOrder.setTransactionId(originMap.get("transaction_id"));
                        trxOrder.setFeeType(originMap.get("fee_type"));
                        trxOrder.setBankType(originMap.get("bank_type"));
                        String finishTimeStr = originMap.get("time_end");
                        trxOrder.setFinishTime(sdf.parse(finishTimeStr));
                    } else {
                        log.info("callback,支付失败");
                        trxOrder.setTransactionStatus(2);
                    }
                    if (trxOrderMapper.updateByPrimaryKeySelective(trxOrder) > 0) {
                        log.info("更新订单成功！");
                    }
//                    //支付成功 则授权
//                    if (trxOrder.getTransactionStatus().equals(1)) {
//                        LicenseRecord licenseRecord = new LicenseRecord();
//                        User user = userMapper.getById(trxOrder.getUserId());
//                        licenseRecord.setSessionsTotal(trxOrder.getNumber());
//                        licenseRecord.setAmount(String.valueOf(trxOrder.getAmount() / 100D));
//                        //创建时间
//                        licenseRecord.setCTime(new Date());
//                        //交易时间
//                        licenseRecord.setTransactionDate(trxOrder.getFinishTime());
//                        licenseRecord.setUserId(user.getId());
//                        licenseRecord.setUsername(user.getUsername());
//                        licenseRecord.setPaymentType("微信支付");
//                        licenseRecord.setOrderNo(trxOrder.getOrderNo());
//                        licenseRecord.setPhotoLive(trxOrder.getPhotoLive());
//                        if (trxOrder.getType().equals(1)) {//包年
//                            licenseRecord.setLicenseType(1);
//                            user.setLicenseType(1);
//                            Calendar c = Calendar.getInstance();
//                            c.add(Calendar.YEAR, 1);
//                            //授权截止日期
//                            licenseRecord.setDueDate(c.getTime());
//                            user.setDueDate(c.getTime());
//                            user.setPackYearsDays(100);
//                        } else if(trxOrder.getType().equals(2)||trxOrder.getType().equals(3)){//包场
//                            licenseRecord.setLicenseType(2);
//                            user.setLicenseType(2);
//                            Integer remainingDays = user.getRemainingDays() == null ? 0 : user.getRemainingDays();
//                            user.setRemainingDays(remainingDays + trxOrder.getNumber());
//                        } else if(trxOrder.getType().equals(5)){
//                            licenseRecord.setLicenseType(1);
//                            user.setPhotoLicenseType(1);
//                            Calendar c = Calendar.getInstance();
//                            c.add(Calendar.YEAR, 1);
//                            //授权截止日期
//                            licenseRecord.setDueDate(c.getTime());
//                            user.setPhotoDueDate(c.getTime());
//                            user.setPhotoPackYearsDays(100);
//                        } else{
//                            licenseRecord.setLicenseType(2);
//                            user.setPhotoLicenseType(2);
//                            Integer remainingDays = user.getPhotoRemainingDays() == null ? 0 : user.getPhotoRemainingDays();
//                            user.setPhotoRemainingDays(remainingDays + trxOrder.getNumber());
//                        }
//                        userMapper.updateAuthorization(user);
//                        licenseRecordMapper.save(licenseRecord);
//                        try{
//                            String remark = "";
//                            if(trxOrder.getOrderNo()!=null)
//                                remark = "订单号："+trxOrder.getOrderNo();
//                            PriceType priceType = priceTypeMapper.getByType(trxOrder.getType());
//                            //发送购买成功通知模板信息
//                            SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            WeixinPushMassage.wxPurchaseSuccess(priceType.getDescription(), licenseRecord.getAmount(), sdft.format(licenseRecord.getTransactionDate()), remark, user.getWechatNum(),"");
//                        }catch (Exception e){
//                            log.error("WeixinPushMassage.wxPurchaseSuccess",e);
//                        }
//                    }
                }
            }
            reMap.put("returnCode",WXPayConstants.SUCCESS);
            reMap.put("returnMsg","OK");
            return reMap;
        } catch (Exception e) {
            log.info("TrxOrderServiceImpl.callback",e);
            return null;
        }
    }

    private Integer getTransactionStatus(String state){
        int transactionStatus = 0;
        switch (state){
            case "SUCCESS":
                transactionStatus = 1;
                break;
            case "PAYERROR":
                transactionStatus = 2;
                break;
            case "REFUND":
                transactionStatus = 3;
                break;
            case "NOTPAY":
                transactionStatus = 0;
                break;
            case "CLOSED":
                transactionStatus = 4;
                break;
            case "REVOKED":
                transactionStatus = 5;
                break;
            case "USERPAYING":
                transactionStatus = 6;
                break;
            default:
                transactionStatus = 0;
        }
        return transactionStatus;
    }

    /**
     * 统一下单测试
     * @param trxOrder
     * @return
     */
    @Override
    public Map addOrderTest(TrxOrder trxOrder) {
        try {
            HashMap<String, String> data = new HashMap<String, String>();
            PriceType priceType = priceTypeMapper.getByType(trxOrder.getType());
            if(priceType==null){
                return ReturnResult.errorResult("套餐类型错误！");
            }
            data.put("body",priceType.getDescription());
            trxOrder.setPrice(priceType.getPrice());
            trxOrder.setNumber(priceType.getNumber());
            trxOrder.setAmount(priceType.getPrice());

            String tradeType = trxOrder.getTradeType();//支付类型
            data.put("out_trade_no", trxOrder.getOrderNo());
            data.put("device_info", "WEB");
            data.put("total_fee", String.valueOf(trxOrder.getAmount()));
            data.put("spbill_create_ip", trxOrder.getCreateIp());
            data.put("notify_url", callBackUrl);
            data.put("trade_type", tradeType);
            if(tradeType.equals("JSAPI")) {
                data.put("openid", trxOrder.getOpenId());
            }else if(tradeType.equals("NATIVE")){
                data.put("product_id",trxOrder.getOrderNo());
            }else{
                return ReturnResult.errorResult("支付类型错误！");
            }
            log.info("统一下单："+data.toString());
            Map<String,String> reMap = getWXPay().unifiedOrder(data);
            log.info("统一下单返回："+reMap.toString());
            //会写订单信息
            if(reMap.get("return_code").equals(WXPayConstants.SUCCESS)){
                if(reMap.get("result_code").equals(WXPayConstants.SUCCESS)) {
                    trxOrder.setPrepayId(reMap.get("prepay_id"));
                    if(tradeType.equals("NATIVE")); trxOrder.setCodeUrl(reMap.get("code_url"));
                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.clear();
                    calendar.setTime(date);
                    calendar.add(Calendar.MINUTE, 110);
                    trxOrder.setPrepayIdDueTime(calendar.getTime());
                    trxOrder.setTransactionStatus(0);
                    trxOrder.setOrderStatus(0);
                    if (trxOrderMapper.insertSelective(trxOrder) > 0) {
                        HashMap map = new HashMap();
                        HashMap redata = new HashMap();
                        if(tradeType.equals("JSAPI")) {
                            redata.put("appId", config.getAppID());
                            redata.put("timeStamp", String.valueOf(GlobalUtils.getTimestamp()));
                            redata.put("nonceStr", WXPayUtil.generateNonceStr());
                            redata.put("package", "prepay_id=" + trxOrder.getPrepayId());
                            redata.put("signType", "MD5");
                            log.info("+++++++++++mchId:" + config.getMchID());
                            log.info("+++++++++++key:" + config.getKey());
                            log.info("+++++++++++redata:" + redata.toString());
                            String sign = WXPayUtil.generateSignature(redata, config.getKey(), WXPayConstants.SignType.MD5);
                            log.info("+++++++++++sign:" + sign);
                            redata.put("paySign", sign);
                            log.info("+++++++++++redataXml:" + WXPayUtil.mapToXml(redata));
                        }else if(tradeType.equals("NATIVE")){
                            redata.put("codeUrl",trxOrder.getCodeUrl());
                        }
                        map.put("trxOrder",trxOrder);
                        map.put("jsData",redata);
                        return ReturnResult.successResult("data", map, ReturnType.ADD_SUCCESS);
                    }
                    return ReturnResult.errorResult(ReturnType.ADD_ERROR);
                }
                return ReturnResult.errorResult(reMap.get("err_code_des"));
            }
            return ReturnResult.errorResult(reMap.get("return_msg"));
        } catch (Exception e) {
            log.info("TrxOrderServiceImpl.addOrder",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @Override
    public List<TrxOrder> selectByParams(Map params) {
        return trxOrderMapper.selectByParams(params);
    }

    @Override
    public List<TrxOrder> selectByParamsWithTicket(Map params) {
        return trxOrderMapper.selectByParamsWithTicket(params);
    }

    @Override
    public TrxOrder selectById(Integer id) {
        return trxOrderMapper.selectByPrimaryKey(id);
    }


//    @Scheduled(cron = "${trxorder.status.refresh}")
//    public void refreshStatus(){
//        if(scheduledSwitch) {
//            log.info("===================================refresh trxorder start======================================");
//            int count = trxOrderMapper.refreshStatus();
//            log.info("===================================refresh trxorder end======================================");
//        }
//    }
}
