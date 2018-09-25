package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.*;
import cn.ourwill.tuwenzb.mapper.*;
import cn.ourwill.tuwenzb.service.ICommentService;
import cn.ourwill.tuwenzb.service.ITrxOrderService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import cn.ourwill.tuwenzb.weixin.Utils.WeixinPushMassage;
import cn.ourwill.tuwenzb.weixin.WXpay.WXPay;
import cn.ourwill.tuwenzb.weixin.WXpay.WXPayConfigImpl;
import cn.ourwill.tuwenzb.weixin.WXpay.WXPayConstants;
import cn.ourwill.tuwenzb.weixin.WXpay.WXPayUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/11 0011 15:20
 * @Version1.0
 */
@Service
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
    private FundDetailMapper fundDetailMapper;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private WithdrawalOrderMapper withdrawalOrderMapper;

    @Autowired
    private FundStatisticsMapper fundStatisticsMapper;

    @Autowired
    private ICommentService commentService;

    @Value("${weixin.pay.useSandbox}")
    private boolean useSandbox;
    @Value("${weixin.pay.callBackUrl}")
    private String callBackUrl;
    @Value("${weixin.pay.rewardCallBackUrl}")
    private String rewardCallBackUrl;
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

    private static final Logger log = LogManager.getLogger(TrxOrderServiceImpl.class);

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
                    if (trxOrderMapper.save(trxOrder) > 0) {
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
            if(priceType.getIsDisplay().equals(1))
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
            if(priceType.getIsDisplay().equals(1))
                contracts.add(getPhotoContract(1,priceType,userId));
        }
        return contracts;
    }

    /**
     * 页面获取价格
     * @param activityNum
     * @return
     */
    @Override
    public Contract getContract(Integer activityNum,Integer type,Integer userId){
        PriceType priceType = priceTypeMapper.getByType(type);
        if(priceType==null) return null;
        return getContract(activityNum,priceType,userId);
    }

    /**
     * 页面获取价格(照片直播)
     * @param activityNum
     * @return
     */
    @Override
    public Contract getPhotoContract(Integer activityNum,Integer type,Integer userId){
        PriceType priceType = priceTypeMapper.getByType(type);
        if(priceType==null) return null;
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
        SystemConfig systemConfig = systemConfigMapper.getConfig(0);
        if(systemConfig.getDiscountSwitch().equals(1)&&systemConfig.getDiscountStart().before(new Date())&&systemConfig.getDiscountEnd().after(new Date())) {
            contract = getDiscountContract2(activityNum,priceType,userId);
        }else{
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
        }
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
        } else if(priceType.getType().equals(9)){
            PriceType originPriceType = priceTypeMapper.getByType(3);
            contract.setPriceType(9);
            contract.setActivityNum(activityNum*5);
            contract.setActivityDays(activityNum*5);
            contract.setActivityOrignAmount(originPriceType.getPrice()*activityNum*5);
            contract.setActivityValid(0);
            contract.setActivityAmount(priceType.getPrice()*activityNum);
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
        } else if(priceType.getType().equals(1)){
            contract.setPriceType(1);
            contract.setActivityNum(priceType.getNumber());
            contract.setActivityDays(priceType.getNumber());
            contract.setActivityValid(365);
            contract.setActivityAmount(priceType.getPrice());
        } else {
            contract.setPriceType(priceType.getType());
            contract.setActivityNum(priceType.getNumber());
            contract.setActivityDays(priceType.getNumber());
            contract.setActivityValid(0);
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
        Contract contract = new Contract();
        if (priceType.getType().equals(7)) {
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
            contract.setPriceType(7);
            contract.setActivityNum(activityNum);
            contract.setActivityDays(activityNum);
            contract.setActivityValid(0);
            if(discount) {
                PriceType disPriceType = priceTypeMapper.getByType(8);
                contract.setActivityOrignAmount(number * price);
                contract.setActivityAmount(disPriceType.getPrice() + (number-1)*price);
            }else{
                contract.setActivityAmount(number * price);
            }
        } else if(priceType.getType().equals(6)){
            contract.setPriceType(6);
            contract.setActivityNum(priceType.getNumber());
            contract.setActivityDays(priceType.getNumber());
            contract.setActivityValid(0);
            contract.setActivityAmount(priceType.getPrice());
        } else if(priceType.getType().equals(10)){
            PriceType originPriceType = priceTypeMapper.getByType(7);
            contract.setPriceType(priceType.getType());
            contract.setActivityNum(activityNum*5);
            contract.setActivityDays(activityNum*5);
            contract.setActivityValid(0);
            contract.setActivityAmount(priceType.getPrice()*activityNum);
            contract.setActivityOrignAmount(originPriceType.getPrice()*activityNum*5);
        } else{
            contract.setPriceType(5);
            contract.setActivityNum(priceType.getNumber());
            contract.setActivityDays(priceType.getNumber());
            contract.setActivityValid(365);
            contract.setActivityAmount(priceType.getPrice());
        }
        return contract;
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
        SystemConfig systemConfig = systemConfigMapper.getConfig(1);
        if(systemConfig.getDiscountSwitch().equals(1)&&systemConfig.getDiscountStart().before(new Date())&&systemConfig.getDiscountEnd().after(new Date())) {
            contract = getPhotoDiscountContract(activityNum,priceType,userId);
        }else{
//            contract = getDiscountContract(activityNum,priceType,userId);
            if (priceType.getType().equals(7)) {
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
                contract.setPriceType(7);
                contract.setActivityNum(activityNum);
                contract.setActivityDays(activityNum);
                contract.setActivityValid(0);
                if(discount) {
                    PriceType disPriceType = priceTypeMapper.getByType(8);
                    contract.setActivityOrignAmount(number * price);
                    contract.setActivityAmount(disPriceType.getPrice() + (number-1)*price);
                }else{
                    contract.setActivityAmount(number * price);
                }
//                Integer number = activityNum;
//                Integer price = priceType.getPrice();
//                contract.setPriceType(7);
//                contract.setActivityNum(activityNum);
//                contract.setActivityDays(activityNum);
//                contract.setActivityValid(0);
//                contract.setActivityAmount(number * price);
            } else if(priceType.getType().equals(6)){
                contract.setPriceType(6);
                contract.setActivityNum(priceType.getNumber());
                contract.setActivityDays(priceType.getNumber());
                contract.setActivityValid(0);
                contract.setActivityAmount(priceType.getPrice());
            } else if(priceType.getType().equals(5)){
                contract.setPriceType(5);
                contract.setActivityNum(priceType.getNumber());
                contract.setActivityDays(priceType.getNumber());
                contract.setActivityValid(365);
                contract.setActivityAmount(priceType.getPrice());
            } else {
                contract.setPriceType(priceType.getType());
                contract.setActivityNum(priceType.getNumber());
                contract.setActivityDays(priceType.getNumber());
                contract.setActivityValid(0);
                contract.setActivityAmount(priceType.getPrice());
            }
        }
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
            log.info("订单查询："+reMap.toString());
            if(reMap.get("return_code").equals(WXPayConstants.SUCCESS)) {
                if(reMap.get("result_code").equals(WXPayConstants.SUCCESS)) {
                    trxOrder.setTransactionId(reMap.get("transaction_id"));
                    trxOrder.setTransactionStatus(getTransactionStatus(reMap.get("trade_state")));
                    if(!trxOrder.getTransactionStatus().equals(0)&&!trxOrder.getTransactionStatus().equals(6)) {
                        trxOrder.setOrderStatus(2);//已查询
                        trxOrder.setBankType(reMap.get("bank_type"));
                        trxOrder.setFeeType(reMap.get("fee_type"));
                        String finishTimeStr = reMap.get("time_end");
                        trxOrder.setFinishTime(sdf.parse(finishTimeStr));
                        if (trxOrderMapper.update(trxOrder) > 0) {
                            log.info("更新订单成功！");
                        }
                    }
                    //支付成功 则授权
                    if(trxOrder.getTransactionStatus().equals(1)){
                        //红包订单，区分开
                        if(trxOrder.getDealType()!=null&&trxOrder.getDealType()==1){
                            log.info("更新打赏红包订单数据！");
                            rewardSuccess(trxOrder);
                        }else {
                            LicenseRecord licenseRecord = new LicenseRecord();
                            User user = userMapper.getById(trxOrder.getUserId());
                            licenseRecord.setSessionsTotal(trxOrder.getNumber());
                            licenseRecord.setAmount(String.valueOf(trxOrder.getAmount()/100D));
                            //创建时间
                            licenseRecord.setCTime(new Date());
                            //交易时间
                            licenseRecord.setTransactionDate(trxOrder.getFinishTime());
                            licenseRecord.setUserId(user.getId());
                            licenseRecord.setUsername(user.getUsername());
                            licenseRecord.setPaymentType("微信支付");
                            licenseRecord.setOrderNo(trxOrder.getOrderNo());
                            if(trxOrder.getType().equals(1)) {//包年
                                licenseRecord.setLicenseType(1);
                                user.setLicenseType(1);
                                Calendar c = Calendar.getInstance();
                                c.add(Calendar.YEAR, 1);
                                //授权截止日期
                                licenseRecord.setDueDate(c.getTime());
                                user.setDueDate(c.getTime());
                                user.setPackYearsDays(trxOrder.getNumber());
                            }else if(trxOrder.getType().equals(2)||trxOrder.getType().equals(3)){//包场
                                licenseRecord.setLicenseType(2);
                                user.setLicenseType(2);
                                Integer remainingDays = user.getRemainingDays()==null?0:user.getRemainingDays();
                                user.setRemainingDays(remainingDays+trxOrder.getNumber());
                            } else if(trxOrder.getType().equals(5)){
                                licenseRecord.setLicenseType(1);
                                user.setPhotoLicenseType(1);
                                Calendar c = Calendar.getInstance();
                                c.add(Calendar.YEAR, 1);
                                //授权截止日期
                                licenseRecord.setDueDate(c.getTime());
                                user.setPhotoDueDate(c.getTime());
                                user.setPhotoPackYearsDays(100);
                            } else{
                                licenseRecord.setLicenseType(2);
                                user.setPhotoLicenseType(2);
                                Integer remainingDays = user.getPhotoRemainingDays() == null ? 0 : user.getPhotoRemainingDays();
                                user.setPhotoRemainingDays(remainingDays + trxOrder.getNumber());
                            }
                            userMapper.updateAuthorization(user);
                            licenseRecordMapper.save(licenseRecord);
                            try {
                                String remark = "";
                                if (trxOrder.getOrderNo() != null)
                                    remark = "订单号：" + trxOrder.getOrderNo();
                                //发送购买成功通知模板信息
                                PriceType priceType = priceTypeMapper.getByType(trxOrder.getType());
                                SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                WeixinPushMassage.wxPurchaseSuccess(priceType.getDescription(), licenseRecord.getAmount(), sdft.format(licenseRecord.getTransactionDate()), remark, user.getWechatNum(), "");
                            }catch (Exception e){
                                log.error("WeixinPushMassage.wxPurchaseSuccess",e);
                            }
                        }
                    }
                    return ReturnResult.successResult("data", trxOrder, ReturnType.GET_SUCCESS);
                }
                return ReturnResult.errorResult(reMap.get("err_code_des"));
            }
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
    @Override
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
                    trxOrderMapper.update(trxOrder);
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
                    if (trxOrderMapper.update(trxOrder) > 0) {
                        log.info("更新订单成功！");
                    }
                    //支付成功 则授权
                    if (trxOrder.getTransactionStatus().equals(1)) {
                        LicenseRecord licenseRecord = new LicenseRecord();
                        User user = userMapper.getById(trxOrder.getUserId());
                        licenseRecord.setSessionsTotal(trxOrder.getNumber());
                        licenseRecord.setAmount(String.valueOf(trxOrder.getAmount() / 100D));
                        //创建时间
                        licenseRecord.setCTime(new Date());
                        //交易时间
                        licenseRecord.setTransactionDate(trxOrder.getFinishTime());
                        licenseRecord.setUserId(user.getId());
                        licenseRecord.setUsername(user.getUsername());
                        licenseRecord.setPaymentType("微信支付");
                        licenseRecord.setOrderNo(trxOrder.getOrderNo());
                        licenseRecord.setPhotoLive(trxOrder.getPhotoLive());
                        if (trxOrder.getType().equals(1)) {//包年
                            licenseRecord.setLicenseType(1);
                            user.setLicenseType(1);
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.YEAR, 1);
                            //授权截止日期
                            licenseRecord.setDueDate(c.getTime());
                            user.setDueDate(c.getTime());
                            user.setPackYearsDays(100);
                        } else if(trxOrder.getType().equals(2)||trxOrder.getType().equals(3) || trxOrder.getType().equals(9)){//包场
                            licenseRecord.setLicenseType(2);
                            user.setLicenseType(2);
                            Integer remainingDays = user.getRemainingDays() == null ? 0 : user.getRemainingDays();
                            user.setRemainingDays(remainingDays + trxOrder.getNumber());
                        } else if(trxOrder.getType().equals(5)){
                            licenseRecord.setLicenseType(1);
                            user.setPhotoLicenseType(1);
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.YEAR, 1);
                            //授权截止日期
                            licenseRecord.setDueDate(c.getTime());
                            user.setPhotoDueDate(c.getTime());
                            user.setPhotoPackYearsDays(100);
                        } else {
                            licenseRecord.setLicenseType(2);
                            user.setPhotoLicenseType(2);
                            Integer remainingDays = user.getPhotoRemainingDays() == null ? 0 : user.getPhotoRemainingDays();
                            user.setPhotoRemainingDays(remainingDays + trxOrder.getNumber());
                        }
                        userMapper.updateAuthorization(user);
                        licenseRecordMapper.save(licenseRecord);
                        try{
                            String remark = "";
                            if(trxOrder.getOrderNo()!=null)
                                remark = "订单号："+trxOrder.getOrderNo();
                            PriceType priceType = priceTypeMapper.getByType(trxOrder.getType());
                            //发送购买成功通知模板信息
                            SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            WeixinPushMassage.wxPurchaseSuccess(priceType.getDescription(), licenseRecord.getAmount(), sdft.format(licenseRecord.getTransactionDate()), remark, user.getWechatNum(),"");
                        }catch (Exception e){
                            log.error("WeixinPushMassage.wxPurchaseSuccess",e);
                        }
                    }
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
                    if (trxOrderMapper.save(trxOrder) > 0) {
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
    public TrxOrder selectById(Integer id) {
        return trxOrderMapper.getById(id);
    }

    @Scheduled(cron = "${trxorder.status.refresh}")
    @Override
    public void refreshStatus(){
        if(scheduledSwitch) {
            log.info("===================================refresh trxorder start======================================");
            int count = trxOrderMapper.refreshStatus();
            log.info("===================================refresh trxorder end======================================");
        }
    }

    /**
     * 打赏下单
     * @param trxOrder 订单
     * @param userId 被打赏者的id
     * @return
     */
    @Override
    public Map addReward(TrxOrder trxOrder,Integer userId,Integer activityId){
        try{
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("body","打赏-红包");
            String tradeType = trxOrder.getTradeType();//支付类型
            data.put("out_trade_no", trxOrder.getOrderNo());
            data.put("device_info", "WEB");
            data.put("total_fee", String.valueOf(trxOrder.getAmount()));
            data.put("spbill_create_ip", trxOrder.getCreateIp());
            data.put("notify_url", rewardCallBackUrl);
            data.put("trade_type", tradeType);
            if("JSAPI".equals(tradeType)) {
                data.put("openid", trxOrder.getOpenId());
            }else if("NATIVE".equals(tradeType)){
                data.put("product_id",trxOrder.getOrderNo());
            }else{
                return ReturnResult.errorResult("支付类型错误！");
            }
            Map<String,String> reMap = getWXPay().unifiedOrder(data);
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
                    if (trxOrderMapper.save(trxOrder) > 0) {
                        HashMap map = new HashMap();
                        HashMap redata = new HashMap();
                        if(tradeType.equals("JSAPI")) {
                            redata.put("appId", config.getAppID());
                            redata.put("timeStamp", String.valueOf(GlobalUtils.getTimestamp()));
                            redata.put("nonceStr", WXPayUtil.generateNonceStr());
                            redata.put("package", "prepay_id=" + trxOrder.getPrepayId());
                            redata.put("signType", "MD5");
                            String sign = WXPayUtil.generateSignature(redata, config.getKey(), WXPayConstants.SignType.MD5);
                            redata.put("paySign", sign);
                        }else if(tradeType.equals("NATIVE")){
                            redata.put("codeUrl",trxOrder.getCodeUrl());
                        }
                        //存收益明细（资金流水），状态未支付
                        FundDetail fund = new FundDetail();
                        fund.setAmount(trxOrder.getAmount());
                        fund.setOrderNo(trxOrder.getOrderNo());
                        fund.setType(0);
                        fund.setFund_status(0);
                        fund.setUserId(userId);
                        fund.setCreateTime(new Date());
                        fund.setPayUser(trxOrder.getUserId());
                        fund.setActivityId(activityId);
                        fund.setPayWeChatNum(trxOrder.getOpenId());
                        Integer count = fundDetailMapper.save(fund);
                        if(count > 0){
                            map.put("trxOrder",trxOrder);
                            map.put("jsData",redata);
                            return ReturnResult.successResult("data", map, ReturnType.ADD_SUCCESS);
                        }
                        return ReturnResult.errorResult(ReturnType.ADD_ERROR);
                    }
                    return ReturnResult.errorResult(ReturnType.ADD_ERROR);
                }
                return ReturnResult.errorResult(reMap.get("err_code_des"));
            }
            return ReturnResult.errorResult(reMap.get("return_msg"));
        }catch (Exception e){
            log.info("TrxOrderServiceImpl.addReward",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 打赏回调
     * @param strXml
     * @return
     */
    @Transactional
    @Override
    public Map<String, String> callbackReward(String strXml) {
        try {
            log.info("通知回调：strXml:"+strXml);
            Map<String,String> originMap = WXPayUtil.xmlToMap(strXml);
            Map<String,String> reMap = new HashMap<>();
            SimpleDateFormat sdf  = new SimpleDateFormat("yyyyMMddHHmmss");
            WXPay wxPay = getWXPay();
            if(!wxPay.isPayResultNotifySignatureValid(originMap)){
                reMap.put("returnCode",WXPayConstants.FAIL);
                reMap.put("returnMsg","签名失败");
                return reMap;
            }
            //通信状态
            String returnCode = originMap.get("return_code");
            //交易状态
            String resultCode = originMap.get("result_code");
            if(returnCode.equals(WXPayConstants.SUCCESS)) {
                TrxOrder trxOrder = trxOrderMapper.getByOrderNoWithClock(originMap.get("out_trade_no"));
                if (trxOrder != null && trxOrder.getOrderStatus().equals(0) && trxOrder.getAmount().equals(Integer.valueOf(originMap.get("total_fee")))) {
                    log.info("通知回调有效，更新订单数据！");
                    if (resultCode.equals(WXPayConstants.SUCCESS)) {
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
                    if (trxOrderMapper.update(trxOrder) > 0) {
                        log.info("更新订单成功！");
                    }
                    //支付成功 则更新收益明细已支付
                    if (trxOrder.getTransactionStatus().equals(1)) {
                        rewardSuccess(trxOrder);

                    }
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

    /**
     * 打赏成功调的方法
     * @param trxOrder
     */
    public void rewardSuccess (TrxOrder trxOrder){
        FundDetail fundDetail =  fundDetailMapper.getByOrderNo(trxOrder.getOrderNo());
        FundStatistics fundStatistics = fundStatisticsMapper.selectByUserId(fundDetail.getUserId());
        if (fundStatistics==null){
            //记录收益统计
            fundStatistics = new FundStatistics();
            fundStatistics.setInCome(trxOrder.getAmount());
            fundStatistics.setSurplus(trxOrder.getAmount());
            fundStatistics.setTodayIncome(trxOrder.getAmount());
            fundStatistics.setWithdraw(0);
            fundStatistics.setUserId(fundDetail.getUserId());
            fundStatisticsMapper.save(fundStatistics);
            //记录收益明细
            if (fundDetail != null && fundDetail.getFund_status().equals(0)&&fundDetail.getAmount().equals(trxOrder.getAmount())){
                fundDetail.setFund_status(1);
                fundDetail.setFinishTime(trxOrder.getFinishTime());
                fundDetail.setLastBalance(0);
                Integer balance = trxOrder.getAmount();
                fundDetail.setNowBalance(balance);
                fundDetailMapper.update(fundDetail);
            }
        }else {
            //记录收益明细
            if (fundDetail != null && fundDetail.getFund_status().equals(0)&&fundDetail.getAmount().equals(trxOrder.getAmount())){
                fundDetail.setFund_status(1);
                fundDetail.setFinishTime(trxOrder.getFinishTime());
                fundDetail.setLastBalance(fundStatistics.getSurplus());
                Integer balance = fundStatistics.getSurplus()+trxOrder.getAmount();
                fundDetail.setNowBalance(balance);
                fundDetailMapper.update(fundDetail);
            }
            //记录收益统计
            log.info("###################################################################################");
            log.info("记录收益统计"+fundStatistics.getUserId()+"收到"+trxOrder.getAmount());
            fundStatistics.setInCome(fundStatistics.getInCome()+trxOrder.getAmount());
            fundStatistics.setSurplus(fundStatistics.getSurplus()+trxOrder.getAmount());
            fundStatistics.setTodayIncome(fundStatistics.getTodayIncome()+trxOrder.getAmount());
            fundStatisticsMapper.update(fundStatistics);
        }
        try{
            User user = userMapper.getById(trxOrder.getUserId());
            User targetUser = userMapper.getById(fundDetail.getUserId());
            String remark = "";
            if(trxOrder.getOrderNo()!=null){
                remark = "订单号："+trxOrder.getOrderNo();
            }
            //发送购买成功通知模板信息
            SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DecimalFormat df   = new DecimalFormat("#######0.00");
            String amount = df.format(trxOrder.getAmount().doubleValue()/100);
            WeixinPushMassage.wxRemarkSuccess(targetUser.getNickname(), amount, sdft.format(trxOrder.getFinishTime()), remark, user.getWechatNum(),"");
            //评论
            Comment comment = new Comment();
            comment.setContent("<svg class=\"icon red\"  aria-hidden=\"true\"> <use xlink:href=\"#icon-hongbao\"></use></svg>打赏给主播"+"<span class='red'> "+amount+" 元红包</span>");
            comment.setActivityId(fundDetail.getActivityId());
            comment.setUserId(user.getId());
            comment.setNickname(user.getNickname());
            comment.setAvatar(user.getAvatar());
            comment.setCTime(new Date());
            comment.setCheck(2);
            comment.setParentUserId(fundDetail.getUserId());
            commentService.save(comment,1);
        }catch (Exception e){
            log.error("WeixinPushMassage.wxPurchaseSuccess",e);
        }
    }


    /**
     * 提现操作
     * @param withdrawalOrder
     * @return
     */
    @Override
    @Transactional
    public Map withdrawDeposit(WithdrawalOrder withdrawalOrder){
        Map<String,String> reMap = new HashMap<String,String>();
        try{
            if(!withdrawalSuccess(withdrawalOrder)){
                return ReturnResult.errorResult("提现账户有误！！！");
            }
            HashMap<String, String> data = new HashMap<String, String>();
            config = WXPayConfigImpl.getInstance(certPath);
            data.put("amount",withdrawalOrder.getPracticalAmount().toString());
            //校验用户姓名选项:NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
            data.put("check_name","NO_CHECK");
            data.put("desc",withdrawalOrder.getDescription());
            data.put("mch_appid",config.getAppID());
            data.put("mchid",config.getMchID());
            data.put("nonce_str",WXPayUtil.generateNonceStr());
            data.put("openid",withdrawalOrder.getOpenId());
            data.put("partner_trade_no",withdrawalOrder.getPartnerTradeNo());
            data.put("spbill_create_ip",withdrawalOrder.getCreateIp());
            String sign = WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5);
            data.put("sign", sign);
            //提现操作
            String strXml = getWXPay().transfers(data);
            reMap =  WXPayUtil.xmlToMap(strXml);
            if("SUCCESS".equals(reMap.get("return_code"))){
                if ("SUCCESS".equals(reMap.get("result_code"))){
                    //插入付款订单表
                    log.info(" save   withdrawal_order 表"+withdrawalOrder.getUserId());
                    withdrawalOrder.setPaymentNo(reMap.get("payment_no"));
                    SimpleDateFormat sdf =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                    withdrawalOrder.setPaymentTime(sdf.parse(reMap.get("payment_time")));
                    withdrawalOrder.setTransferStatus(1);
                    Integer num = withdrawalOrderMapper.save(withdrawalOrder);
                    log.info(" update FundDetail 表");
                    FundDetail fundDetail = fundDetailMapper.getByOrderNo(withdrawalOrder.getPartnerTradeNo());
                    fundDetail.setFinishTime(sdf.parse(reMap.get("payment_time")));
                    fundDetail.setFund_status(1);
                    fundDetailMapper.update(fundDetail);
                    //发送提现成功通知模板信息
                    log.info(" 发送提现成功通知模板信息");
                    String remark = "订单号："+withdrawalOrder.getPartnerTradeNo();
                    SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    DecimalFormat df   = new DecimalFormat("#######0.00");
                    String requestAmount = df.format(withdrawalOrder.getRequestAmount().doubleValue()/100);
                    String practicalAmount = df.format(withdrawalOrder.getPracticalAmount().doubleValue()/100);
                    String procedureAmount = df.format(((withdrawalOrder.getRequestAmount().doubleValue()-withdrawalOrder.getPracticalAmount().doubleValue())/100));
                    WeixinPushMassage.wxTransfersSuccess(sdft.format(withdrawalOrder.getTransferTime()),"转账到零钱",requestAmount,
                            procedureAmount,practicalAmount, remark, withdrawalOrder.getOpenId(),"");
                    return ReturnResult.successResult("data",withdrawalOrder,reMap.get("result_code"));
                }else if ("SYSTEMERROR".equals(reMap.get("err_code"))){
                    //通过查询接口确认此次付款的结果
                    HashMap<String, String> errData = new HashMap<String, String>();
                    errData.put("nonce_str",WXPayUtil.generateNonceStr());
                    errData.put("mch_appid",config.getAppID());
                    errData.put("mchid",config.getMchID());
                    errData.put("partner_trade_no",withdrawalOrder.getPartnerTradeNo());
                    String errSign = WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5);
                    errData.put("sign", errSign);
                    Map<String,String> errMap = getWXPay().getTransferInfo(errData);
                    //SUCCESS:转账成功;FAILED:转账失败;PROCESSING:处理中
                    if("SUCCESS".equals(reMap.get("return_code"))&&"SUCCESS".equals(reMap.get("result_code"))){
                        String status = errMap.get("status");
                        while ("PROCESSING".equals(status)){
                            errMap = getWXPay().getTransferInfo(errData);
                            status = errMap.get("status");
                        }
                        //转账成功
                        if ("SUCCESS".equals(errMap.get("status"))){
                            //更新付款订单表
                            withdrawalOrder.setPaymentTime(new Date());
                            withdrawalOrder.setPaymentNo(errMap.get("detail_id "));
                            SimpleDateFormat sdf =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                            withdrawalOrder.setTransferTime(sdf.parse(errMap.get("transfer_time ")));
                            withdrawalOrder.setTransferStatus(1);
                            withdrawalOrder.setUserName(errMap.get("transfer_name "));
                            log.info(" 查询接口确认结果 save   withdrawal_order 表"+withdrawalOrder.getUserId());
                            withdrawalOrderMapper.save(withdrawalOrder);
                            FundDetail fundDetail = fundDetailMapper.getByOrderNo(withdrawalOrder.getPartnerTradeNo());
                            fundDetail.setFinishTime(sdf.parse(reMap.get("payment_time")));
                            fundDetail.setFund_status(1);
                            fundDetailMapper.update(fundDetail);
                            //发送提现成功通知模板信息
                            log.info(" 发送提现成功通知模板信息");
                            String remark = "订单号："+errMap.get("partner_trade_no ");
                            //SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            DecimalFormat df   = new DecimalFormat("#######0.00");
                            String requestAmount = df.format(withdrawalOrder.getRequestAmount().doubleValue()/100);
                            String practicalAmount = df.format(withdrawalOrder.getPracticalAmount().doubleValue()/100);
                            String procedureAmount = df.format(((withdrawalOrder.getRequestAmount().doubleValue()-withdrawalOrder.getPracticalAmount().doubleValue())/100));
                            WeixinPushMassage.wxTransfersSuccess(errMap.get("transfer_name "),"转账到零钱",requestAmount,
                                    procedureAmount,practicalAmount, remark, withdrawalOrder.getOpenId(),"");
                            return ReturnResult.successResult("data",withdrawalOrder,errMap.get("return_code"));
                        }else {//转账失败
                            log.info("退款失败1");
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            //表示退款失败,存储失败提现的记录咯
                            log.info(" 提现失败 save   withdrawal_order 表"+withdrawalOrder.getUserId());
                            withdrawalOrder.setTransferStatus(0);
                            withdrawalOrder.setFailedReason(errMap.get("reason"));
                            withdrawalOrderMapper.save(withdrawalOrder);
                            //提现失败发通知模板信息
                            String remark = "订单号："+withdrawalOrder.getPartnerTradeNo();
                            DecimalFormat df   = new DecimalFormat("#######0.00");
                            String requestAmount = df.format(withdrawalOrder.getRequestAmount().doubleValue()/100);
                            WeixinPushMassage.wxTransfersFailing(requestAmount, errMap.get("transfer_time"), errMap.get("err_code_des"),remark, errMap.get("openid"),"");
                            return ReturnResult.errorResult(errMap.get("err_code_des"));
                        }
                    }
                } else {
                    //表示退款失败,存储失败提现的记录咯
                    log.info("退款失败2");
                    withdrawalOrder.setTransferStatus(0);
                    withdrawalOrder.setFailedReason(reMap.get("err_code"));
                    withdrawalOrderMapper.save(withdrawalOrder);
                    //提现失败发通知模板信息
                    String remark = "订单号："+withdrawalOrder.getPartnerTradeNo();
                    SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    DecimalFormat df   = new DecimalFormat("#######0.00");
                    String requestAmount = df.format(withdrawalOrder.getRequestAmount().doubleValue()/100);
                    WeixinPushMassage.wxTransfersFailing(requestAmount, sdft.format(withdrawalOrder.getTransferTime()), reMap.get("err_code_des"),remark, withdrawalOrder.getOpenId(),"");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ReturnResult.errorResult(reMap.get("err_code_des"));
                }
                log.info("退款失败3");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            }
            log.info("退款失败4");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnResult.errorResult("提现失败！！！");
        }catch (Exception e){
            log.info("TrxOrderServiceImpl.withdrawDeposit",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 提现成功调的方法（主要是更新fund_detail，fund_statistics表，为防止出错，先调后支付，出错可以回滚）
     * @param withdrawalOrder
     */
    public Boolean withdrawalSuccess (WithdrawalOrder withdrawalOrder) {
        //资金流水明细
        log.info("主要是更新fund_detail，fund_statistics表");
        FundDetail fund = new FundDetail();
        fund.setAmount(-1 * withdrawalOrder.getRequestAmount());
        fund.setOrderNo(withdrawalOrder.getPartnerTradeNo());
        fund.setType(1);
        fund.setUserId(withdrawalOrder.getUserId());
        fund.setCreateTime(new Date());
        FundStatistics fundStatistics = fundStatisticsMapper.selectByUserId(fund.getUserId());
        if (fundStatistics != null) {
            //存收益明细（资金流水)
            fund.setFund_status(1);
            fund.setLastBalance(fundStatistics.getSurplus());
            Integer balance = fundStatistics.getSurplus() - withdrawalOrder.getRequestAmount();
            log.info("save fund_detail表，last余额为" + fundStatistics.getSurplus() + "  now余额为" + balance);
            fund.setNowBalance(balance);
            fundDetailMapper.save(fund);
            //记录收益统计
            log.info("更新fund_statistics表，余额为" + balance + "  提现金额为" + fundStatistics.getWithdraw());
            if(fundStatistics.getWithdraw()==null){
                fundStatistics.setWithdraw(0);
            }
            Integer withdraw = fundStatistics.getWithdraw() + withdrawalOrder.getPracticalAmount();
            fundStatistics.setSurplus(balance);
            fundStatistics.setWithdraw(withdraw);
            fundStatisticsMapper.update(fundStatistics);
            log.info("withdrawalSuccess执行成功！！！！！");
            return true;
        }else {
            return false;
        }
    }



    /**
     * 个人收益
     * @param userId
     * @return
     */
    @Override
    public FundStatistics getTotalAmount (Integer userId){
        FundStatistics fundStatistics = fundStatisticsMapper.selectByUserId(userId);
        Integer todayIncome = fundDetailMapper.selectTodayIncome(userId);
        if(fundStatistics==null){
            fundStatistics = new FundStatistics();
            fundStatistics.setWithdraw(0);
            fundStatistics.setInCome(0);
            fundStatistics.setTodayIncome(0);
            fundStatistics.setSurplus(0);
            fundStatistics.setUserId(userId);
        }else {
            fundStatistics.setTodayIncome(todayIncome);
            fundStatistics.setUserId(userId);
            fundStatisticsMapper.update(fundStatistics);
        }
        Integer maxWithdraw = (int)Math.round(fundStatistics.getSurplus()/1.1);
        fundStatistics.setMaxWithdraw(maxWithdraw);
        return fundStatistics;
    }

    /**
     *个人收益明细
     * @param userId
     * @return
     */
    @Override
    public List<FundDetail> selectFundListById(Integer userId){
        return fundDetailMapper.selectFundListById(userId);
    }

    @Override
    public List<FundDetail> getRewardList(Integer activityId){
        return fundDetailMapper.getRewardList(activityId);
    }
}
