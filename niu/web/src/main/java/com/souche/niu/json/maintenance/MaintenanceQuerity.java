package com.souche.niu.json.maintenance;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.souche.coupon.model.dto.CouponAvailableDTO;
import com.souche.coupon.model.dto.CouponListDTO;
import com.souche.niu.api.CmsService;
import com.souche.niu.constant.Constants;
import com.souche.niu.manager.carmaintenance.MaintenanceOrderManager;
import com.souche.niu.manager.carmaintenance.MaintenanceRecordManager;
import com.souche.niu.manager.cms.CmsManager;
import com.souche.niu.manager.msgpush.WeiBaoMsgPushManager;
import com.souche.niu.model.MaintenanceConfigDO;
import com.souche.niu.model.em.VendorCodeEnum;
import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.niu.model.maintenance.respone.CarMaintenance;
import com.souche.niu.result.ResultWeb;
import com.souche.niu.spi.OrderCenterSPI;
import com.souche.niu.spi.ShopSPI;
import com.souche.niu.spi.VINParserSPI;
import com.souche.niu.spi.WeiBaoMsgPushSPI;
import com.souche.niu.util.DateUtils;
import com.souche.niu.util.HttpUtil;
import com.souche.niu.util.VinUtil;
import com.souche.niu.vo.CouponBaseVO;
import com.souche.niu.vo.MaintenanceRecordVO;
import com.souche.optimus.cache.CacheService;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.core.annotation.Param;
import com.souche.optimus.core.annotation.Rest;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.web.OptimusRequestMethod;
import com.souche.optimus.core.web.Result;
import com.souche.order.model.OrderBizDataQO;
import com.souche.order.model.OrderDTO;
import com.souche.order.model.PaymentDTO;
import com.souche.shop.model.Shop;
import com.souche.vinquery.api.bean.ParseModel;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.souche.sso.client2.AuthNHolder;

/**
 * Created by sid on 2018/9/4.
 */
@View
@Api(value = "maintenanceQuerity", description = "查保养")
public class MaintenanceQuerity {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceQuerity.class);

    @Autowired
    private OrderCenterSPI orderCenterSPI;

    @Autowired
    private MaintenanceOrderManager maintenanceOrderManager;

    @Autowired
    private MaintenanceRecordManager maintenanceRecordManager;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private VINParserSPI vinParserSPI;

    @Autowired
    private ShopSPI shopSPI;

    @Autowired
    private CmsService cmsService;

    @Autowired
    private WeiBaoMsgPushSPI weiBaoMsgPushSPI;


    @ApiOperation("维保回调接口")
    public String wbNotify(HttpServletRequest request) {
        logger.info("维保回调");
        try {
            String result = HttpUtil.getRequestPostStr(request);
            // result = "{\"code\":\"200\",\"data\":{\"accidentNumbers\":0,\"authId\":\"2018090801\",\"clientCode\":\"IOS\",\"enginestatus\":\"0\",\"enginestatusDesc\":\"未知\",\"finalVendorCode\":\"CACHEWB\",\"gasbagStatus\":\"0\",\"gasbagStatusDesc\":\"未知\",\"importantComponentsStatus\":\"1\",\"importantComponentsStatusDesc\":\"正常\",\"kmValue\":66687,\"kmValueStatus\":\"1\",\"kmValueStatusDesc\":\"正常\",\"lasttimeToShop\":\"2016/09/14\",\"outOrderNo\":\"15601159011\",\"reportSketch\":\"2013-03-25 更换发动机机油和滤清器                  ; 保养; \\n2013-06-13 保修; 保修服务; \\n2014-02-10 前杠,左前翼板喷漆; 钣金,拆装; 更换发动机机油; 油喷; \\n2014-02-25 钣金,拆装; 油喷; \\n2014-02-26 保修; 更换活性炭罐                            ; 熄火后部异响; \\n2014-10-13 车身五合一维护; 清洁节气门段的进气岐管                  ; 更换燃油滤清器                          ; 更换发动机空气滤清器                    ; 更换发动机机油和滤清器                  ; 保养; 下次保养提示:2015年10月13日前,36000公里前,以哪个先到为准;; \\n2015-07-20 清洗进气系统                   ; 10 000公里间隔的基本保养(预约客户)                ; 更换发动机机油和滤清器                  ; 更换后刹车片                ; 保养; 下次保养提示:2015年10月13日前,36000公里前,以哪个先到为准;\u0590¤; \\n2016-03-18 更换乘客室空气滤清器                    ; 10 000公里间隔的基本保养                ; 更换发动机机油和滤清器                  ; 更换发动机空气滤清器                    ; 保养; 下次保养提示:2015年10月13日前,36000公里前,以哪个先到为准;; \\n2016-09-14 清洗进气系统                ; 更换皮带                       ; 更换防冻液; 更换助力油                          ; 更换变速箱油                                                                                       ; 更换刹车油                                                                                     ; 10 000公里间隔的基本保养                ; 更换发动机机油和滤清器                  ; 更换燃油滤清器                          ; 更换发动机空气滤清器                    ; 保养; \\n2018-06-30 清舱行动,闪耀如新; 车辆进厂; \\n\",\"reportTime\":\"2018-09-08 14:37:46\",\"serviceNo\":\"200120180908143746135795\",\"showCode\":\"FA761255DE2D7F6010423B951EE3E4E51D5BB50A345B951A688226743CF689BB\",\"showReportUrl\":\"https://thirdservice.test.cheyipai.com/view/h5/weibaoReport.html?showcode=FA761255DE2D7F6010423B951EE3E4E51D5BB50A345B951A688226743CF689BB\",\"status\":\"SUCCESS\",\"statusDesc\":\"查询成功\",\"structurePartStatus\":\"1\",\"structurePartStatusDesc\":\"正常\",\"vin\":\"YV1FW63CXD1090561\",\"wbReportList\":[{\"kmValue\":6049,\"repairContent\":\"更换发动机机油和滤清器                  ; 保养; \",\"repairDate\":\"2013/03/25\",\"repairType\":\"一般\"},{\"kmValue\":8355,\"repairContent\":\"保修; 保修服务; \",\"repairDate\":\"2013/06/13\",\"repairType\":\"保修\"},{\"kmValue\":15484,\"repairContent\":\"前杠,左前翼板喷漆; 钣金,拆装; 更换发动机机油; 油喷; \",\"repairDate\":\"2014/02/10\",\"repairType\":\"一般\"},{\"kmValue\":15904,\"repairContent\":\"钣金,拆装; 油喷; \",\"repairDate\":\"2014/02/25\",\"repairType\":\"一般\"},{\"kmValue\":15907,\"repairContent\":\"保修; 更换活性炭罐                            ; 熄火后部异响; \",\"repairDate\":\"2014/02/26\",\"repairType\":\"保修\"},{\"kmValue\":26229,\"repairContent\":\"车身五合一维护; 清洁节气门段的进气岐管                  ; 更换燃油滤清器                          ; 更换发动机空气滤清器                    ; 更换发动机机油和滤清器                  ; 保养; 下次保养提示:2015年10月13日前,36000公里前,以哪个先到为准;; \",\"repairDate\":\"2014/10/13\",\"repairType\":\"一般\"},{\"kmValue\":35408,\"repairContent\":\"清洗进气系统                   ; 10 000公里间隔的基本保养(预约客户)                ; 更换发动机机油和滤清器                  ; 更换后刹车片                ; 保养; 下次保养提示:2015年10月13日前,36000公里前,以哪个先到为准;\u0590¤; \",\"repairDate\":\"2015/07/20\",\"repairType\":\"一般\"},{\"kmValue\":46066,\"repairContent\":\"更换乘客室空气滤清器                    ; 10 000公里间隔的基本保养                ; 更换发动机机油和滤清器                  ; 更换发动机空气滤清器                    ; 保养; 下次保养提示:2015年10月13日前,36000公里前,以哪个先到为准;; \",\"repairDate\":\"2016/03/18\",\"repairType\":\"一般\"},{\"kmValue\":57474,\"repairContent\":\"清洗进气系统                ; 更换皮带                       ; 更换防冻液; 更换助力油                          ; 更换变速箱油                                                                                       ; 更换刹车油                                                                                     ; 10 000公里间隔的基本保养                ; 更换发动机机油和滤清器                  ; 更换燃油滤清器                          ; 更换发动机空气滤清器                    ; 保养; \",\"repairDate\":\"2016/09/14\",\"repairType\":\"一般\"},{\"kmValue\":65847,\"repairContent\":\"清舱行动,闪耀如新; 车辆进厂; \",\"repairDate\":\"2018/06/30\",\"repairType\":\"一般\"}]},\"msg\":\"维保服务单处理成功\",\"success\":true}";
            logger.info("维保回调 收到回调的结果为:{}", result);
            JSONObject jsonObject = JSON.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            String orderId = data.getString("outOrderNo");//获取订单号
            String reason = data.getString("statusDesc");
            String serviceNo = data.getString("serviceNo");
            MaintenanceRecordDO maintenanceRecordDO = maintenanceRecordManager.getCarMaintenance(orderId);
            //如果是成功
            if (StringUtils.equals(data.getString("status"), "FAIL")) {
                //退款
                maintenanceOrderManager.refund(orderId);
                //设置为失败的结果
                maintenanceRecordManager.updateCarMaintenance(new MaintenanceRecordDO(orderId, 5, "2", reason, serviceNo, null));
                weiBaoMsgPushSPI.MsgPush("FAIL", orderId);
                return "SUCCESS";
            }
            String vendorCode = data.getString("finalVendorCode");//获取订单号
            String showReportUrl = data.getString("showReportUrl");//获取通知地址
            CarMaintenance carMaintenance = JsonUtils.fromJson(data.toJSONString(), CarMaintenance.class);
            MaintenanceRecordDO maintenance = new MaintenanceRecordDO(orderId, 4, "0", "报告已出", serviceNo, showReportUrl);
            VendorCodeEnum v = VendorCodeEnum.getByCode(vendorCode);
            if (v != null) {
                maintenance.setOrderType(v.getIndex());
            }
            //更新记录以及查询记过
            maintenanceRecordManager.updateCarMaintenance(maintenance);
            //是否有优惠券
            if(StringUtils.isEmpty(maintenanceRecordDO.getCouponCode())){
                //核销卷
                orderCenterSPI.finishCoupon(maintenanceRecordDO);
            }
            maintenanceRecordManager.saveMaintenanceDetail(carMaintenance, maintenanceRecordDO);
            weiBaoMsgPushSPI.MsgPush("SUCCESS", orderId);
            return "SUCCESS";
        } catch (Exception e) {
            logger.error("维保回调 保存维保查询结果失败", e);
            return "FAIL";
        }
    }

    @Rest(value = "v2", desc = "认证信息，图片，认证状态", method = OptimusRequestMethod.GET)
    public Result<Map<String, Object>> authentication(@ApiParam(value = "token", required = true)
                                                      @Param(value = "token", required = true) String token) {
        logger.info("认证信息 token={}", token);
        Result<Map<String, Object>> result = new Result();
        Map<String, Object> resultMap = new HashMap<>();
        logger.info("认证信息 准备获取图片 token={}", token);
        //图片
        MaintenanceConfigDO maintenanceConfigDO = cmsService.queryMaintenance();
        String bannerImg = maintenanceConfigDO.getBannerImg();
        logger.info("认证信息 获取到的图片 bannerImg={}", bannerImg);
        String shopCode = AuthNHolder.shopCode();
        //店铺信息
        logger.info("认证信息 准备获取店铺信息 shopCode={}，token={}", shopCode, token);
        Shop shop = shopSPI.loadShopByCode(shopCode);
        logger.info("认证信息 获取到的店铺信息 shop={}, shopCode={}，token={}", shop, shopCode, token);
        boolean authentication = false;
        if (StringUtils.isNotEmpty(shopCode)) {
            authentication = shopSPI.checkShopAuthentication(shopCode);
            logger.info("认证信息 是否认证 authentication={}", authentication);
        }
        String priceYuan = "";
        //获取价格
        logger.info("认证信息 准备获取价格");
        if (authentication) {
            //已经企业认证的一个价格
            priceYuan = maintenanceConfigDO.getVerifiedPrice().toString();
        } else {
            //企业未认证的一个价格
            priceYuan = maintenanceConfigDO.getUnverifiedPrice().toString();
        }
        logger.info("认证信息 获取到的价格（转换之后）priceYuan={}", priceYuan);
        //返回店铺信息
        resultMap.put("shopInfo", shop);
        Map<String, Object> certifStatus = new HashMap<>();
        certifStatus.put("authPicture", "");
        certifStatus.put("reviewReason", "");
        int reviewStatus = -1;
        if (authentication) {
            reviewStatus = 1;
        }
        certifStatus.put("reviewStatus", reviewStatus);
        certifStatus.put("shopCode", shopCode);
        certifStatus.put("is_auth", authentication);
        certifStatus.put("priceYuan", priceYuan);
        //返回是否认证
        resultMap.put("certifStatus", certifStatus);
        //返回图片
        resultMap.put("bannerImg", bannerImg);
        resultMap.put("dfcCertifStatus", -1);
        result.setSuccess(true);
        result.setCode("200");
        result.setData(resultMap);
        result.setMsg("OK");
        return result;
    }

    @Rest(value = "query_car_by_vin", desc = "创建订单", method = OptimusRequestMethod.POST)
    public Result<Map<String, Object>> queryCarByVin(@ApiParam(value = "token", required = true)
                                                     @Param(value = "token", required = true) String token,
                                                     @ApiParam(value = "vin", required = true)
                                                     @Param(value = "vin", required = true) String vin,
                                                     @ApiParam(value = "行驶证", required = false)
                                                     @Param(value = "license_plate", required = false) String license_plate,
                                                     @ApiParam(value = "优惠券", required = false)
                                                     @Param(value = "coupon_codes", required = false) String coupon_codes,
                                                     @ApiParam(value = "引擎盖vin编号", required = false)
                                                     @Param(value = "engine", required = false) String engine) {
        Result<Map<String, Object>> result = new Result<>();
        String s = AuthNHolder.userId();
        if (StringUtils.isEmpty(s)) {
            result.setSuccess(false);
            result.setCode("99");
            result.setMsg("用户未登录");
            return result;
        }
        //创建订单
        OrderDTO order;
        try {
            order = maintenanceOrderManager.createOrder(vin, coupon_codes, engine, license_plate);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("99");
            result.setMsg("创建订单失败");
            return result;
        }
        Map<String, Object> resultMap = new HashMap<>();
        //返回参数
        resultMap.put("order_id", order.getOrderCode());
        resultMap.put("should_pay_count", order.getBuyerShouldPayAmountYuan());
        String phone = AuthNHolder.userPhone();
        resultMap.put("mobile_phone", phone);
        result.setSuccess(true);
        result.setCode("200");
        result.setData(resultMap);
        result.setMsg("OK");
        return result;
    }


    @Rest(value = "get_detail", desc = "报告详情", method = OptimusRequestMethod.POST)
    public Result<Map<String, Object>> getDetail(@ApiParam(value = "订单Code", required = true)
                                                 @Param(value = "order_id", required = true) String order_id) {
        Result<Map<String, Object>> result = new Result<>();
        //查出从维保查好的记录
        Map<String, Object> resultMap = maintenanceRecordManager.getMaintenanceQueryDetail(order_id);
        //再加上一个查询结果url
        MaintenanceRecordDO carMaintenance = maintenanceRecordManager.getCarMaintenance(order_id);
        String notifyUrl = carMaintenance.getNotifyUrl();
        logger.info("报告详情 维保查询结果URL notifyUrl={}", notifyUrl);
        //查询订单详情
        result.setSuccess(true);
        result.setCode("200");
        result.setData(resultMap);
        result.setMsg("OK");
        return result;
    }

    /**
     * 获取预支付信息
     *
     * @author ZhangHui
     * @since 2018-09-06
     */
    @Rest(value = "prepare_pay", desc = "获取预支付信息", method = OptimusRequestMethod.POST)
    public Result<PaymentDTO> preparePay(@ApiParam(value = "order_code", required = true) @Param(value = "order_code", required = true) String order_code,
                                         @ApiParam(value = "amount") @Param(value = "amount") Double amount,
                                         @ApiParam(value = "payer_id") @Param(value = "payer_id") String payer_id,
                                         @ApiParam(value = "payer_type") @Param(value = "payer_type") String payer_type) {
        if (StringUtils.isEmpty(payer_id)) {
            payer_id = AuthNHolder.userId();
        }
        if (StringUtils.isEmpty(payer_type)) {
            payer_type = "cheniu_user";
        }
        logger.info("获取预支付信息入参 amount={}, orderCode={}, payer_id={}, payer_type={}", amount, order_code, payer_id, payer_type);
        Result<PaymentDTO> result = new Result<>();
        //从库中取出应该支付金额
        MaintenanceRecordDO carMaintenance = maintenanceRecordManager.getCarMaintenance(order_code);
        BigDecimal payPrice = carMaintenance.getPayPrice();
        BigDecimal payPricemultiply = payPrice.multiply(new BigDecimal(100).setScale(0, BigDecimal.ROUND_DOWN));
        long payPriceFans = payPricemultiply.longValue();
        logger.info("获取预支付信息 payPrice = {}, payPricemultiply = {}, payPriceFans = {}", payPrice.toString(), payPricemultiply.toString(), payPriceFans);
        PaymentDTO paymentDTO = orderCenterSPI.preparePay(order_code, payPricemultiply.longValue(), payer_id, payer_type);
        logger.info("获取预支付信息 返回结果 resultMap={}", JsonUtils.toJson(paymentDTO));
        result.setSuccess(true);
        result.setCode("10000");
        result.setMsg("OK");
        result.setData(paymentDTO);
        return result;
    }

    /**
     * 校验vin,获取价格等动作
     *
     * @author ZhangHui
     * @since 2018-09-06
     */
    @Rest(value = "check_band_by_vin", desc = "校验vin", method = OptimusRequestMethod.POST)
    public Result<Map<String, Object>> checkBandByVin(@ApiParam(value = "token", required = true)
                                                      @Param(value = "token", required = true) String token,
                                                      @ApiParam(value = "vin", required = false)
                                                      @Param(value = "vin", required = false) String vin) {
        logger.info("校验VIN码token={},vin={}", token, vin);
        String shopCode = AuthNHolder.shopCode();
        Result<Map<String, Object>> result = new Result<>();
        //win码校验
        boolean checkvin = VinUtil.checkVIN(vin);
        if (!checkvin) {
            result.setCode("200");
            result.setMsg("VIN码输入错误");
            result.setSuccess(false);
            Map<String, Object> resultmapFail = new HashMap<>();
            resultmapFail.put("canQuery", false);
            resultmapFail.put("message", "VIN码输入错误");
            result.setData(resultmapFail);
            return result;
        }
        //判断是否是认证用户
        boolean authentication = false;
        if (StringUtils.isNotEmpty(shopCode)) {
            authentication = shopSPI.checkShopAuthentication(shopCode);
        }
        logger.info("校验VIN码，是否是认证用户authentication={},价格price={}", authentication);
        String brand = null;
        try {
            List<ParseModel> list = vinParserSPI.getCarModelInfo(vin);
            if (CollectionUtils.isNotEmpty(list)) {
                brand = list.get(0).getBrandName();
                cacheService.setObject(Constants.CAR_PARSEMODEL_PRE + vin, list.get(0), 60 * 60);
            }
        } catch (Exception e) {
            logger.warn("根据vin码查询车辆信息失败");
        }

        result.setCode("200");
        result.setMsg("VIN码校验成功");
        result.setSuccess(true);
        Map<String, Object> resultMap = new HashMap<>();
        //返回是否是认证用户
        resultMap.put("authentication", authentication);
        //返回价格分为单位
        resultMap.put("brand", brand);
        resultMap.put("canQuery", true);
        resultMap.put("orderType", 0);
        resultMap.put("needLicensePlate", false);
        resultMap.put("needEngine", false);
        result.setData(resultMap);
        return result;
    }


    /**
     * 取消订单
     *
     * @author ZhangHui
     * @since 2018-09-06
     */
    @Rest(value = "cancel_order", desc = "取消订单", method = OptimusRequestMethod.POST)
    public Result<Void> cancelOrder(@ApiParam(value = "token", required = true)
                                    @Param(value = "token", required = true) String token,
                                    @ApiParam(value = "订单Code", required = true)
                                    @Param(value = "order_id", required = true) String order_id) {
        Result<Void> result = new Result<>();
        //查询流水状态
        MaintenanceRecordDO carMaintenance = maintenanceRecordManager.getCarMaintenance(order_id);
        int status = carMaintenance.getStatus();
        logger.info("关闭订单 查询流水状态结果 orderCode={}, status={}", order_id, status);
        if (status != 0) {
            logger.info("关闭订单 订单不是生成状态，关闭订单失败。orderCode={}, status={}", order_id, status);
            result.setSuccess(false);
            result.setCode("99");
            result.setMsg("订单不是生成状态，关闭订单失败。");
            return result;
        }
        OrderBizDataQO orderBizDataQO = new OrderBizDataQO();
        orderBizDataQO.setOrderCode(order_id);
        //修改订单状态。关闭订单。
        String orderMsg = orderCenterSPI.closeOrder(order_id);
        if ("error".equals(orderMsg)) {
            //订单服务异常
            result.setSuccess(true);
            result.setCode("99");
            result.setMsg("订单服务异常。");
            return result;
        }
        if ("".equals(orderMsg)) {
            //订单关闭失败
            result.setSuccess(true);
            result.setCode("99");
            result.setMsg("订单服务异常。");
            return result;
        }
        //修改流水的状态为  订单关闭
        MaintenanceRecordDO maintenanceRecordDO = new MaintenanceRecordDO();
        maintenanceRecordDO.setOrderId(order_id);
        maintenanceRecordDO.setStatus(-1);
        maintenanceRecordManager.updateCarMaintenance(maintenanceRecordDO);
        //查询订单详情
        result.setSuccess(true);
        result.setCode("200");
        result.setMsg("订单是生成状态，关闭订单成功");
        return result;
    }

    /**
     * 订单列表
     *
     * @author ZhangHui
     * @since 2018-09-06
     */
    @Rest(value = "get_list", desc = "订单列表", method = OptimusRequestMethod.POST)
    public Result<Map<String, Object>> getList(@ApiParam(value = "token", required = true)
                                               @Param(value = "token", required = true) String token,
                                               @ApiParam(value = "每页条数", required = true)
                                               @Param(value = "page_size", required = true) Integer pageSize,
                                               @ApiParam(value = "当前页", required = true)
                                               @Param(value = "page_no", required = true) Integer currentPage) {
        Result<Map<String, Object>> result = new Result<>();
        String phone = AuthNHolder.userPhone();
        if (currentPage < 1) {
            result.setSuccess(true);
            result.setCode("200");
            result.setData(null);
            result.setMsg("当前页page不正确");
            return result;
        }
        List<MaintenanceRecordDO> recordList = maintenanceRecordManager.getRecordList(phone, pageSize, currentPage);
        List<MaintenanceRecordVO> resultList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date toDayDate = new Date();
        for (MaintenanceRecordDO maintenanceRecordDO : recordList) {
            MaintenanceRecordVO maintenanceRecordVO = new MaintenanceRecordVO();
            maintenanceRecordVO.setRemark(maintenanceRecordDO.getRemarks());
            maintenanceRecordVO.setVin_code(maintenanceRecordDO.getVinNumber());
            maintenanceRecordVO.setPrice(maintenanceRecordDO.getPrice());
            Date date_create = maintenanceRecordDO.getDate_create();
            String format = simpleDateFormat.format(date_create);
            maintenanceRecordVO.setTime(format);
            maintenanceRecordVO.setOrder_id(maintenanceRecordDO.getOrderId());
            maintenanceRecordVO.setApp_name("cheniu_user");
            maintenanceRecordVO.setCurrent_pay_price(maintenanceRecordDO.getPrice());
            maintenanceRecordVO.setStatus(maintenanceRecordDO.getStatus());
            String detailUrl = maintenanceRecordDO.getDetailUrl();
            maintenanceRecordVO.setDetailUrl(detailUrl);
            if (StringUtils.isEmpty(detailUrl)) {
                maintenanceRecordVO.setDetailUrl("");
            }
            Date date = DateUtils.addDays(date_create, 1);
            String format1 = simpleDateFormat2.format(date);
            int status = maintenanceRecordDO.getStatus();
            //跟当前时间相比不超过一天，订单状态是初始状态的显示支付提示信息
            if (toDayDate.compareTo(date) < 0 && status == 0) {
                maintenanceRecordVO.setExtend_info("请在" + format1 + "之前付款, 否则订单自动取消");
            }
            //取消订单。
            resultList.add(maintenanceRecordVO);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", resultList);
        resultMap.put("phone", phone);
        result.setSuccess(true);
        result.setCode("200");
        result.setData(resultMap);
        result.setMsg("成功");
        return result;
    }

    /**
     * 优惠券信息
     *
     * @author ZhangHui
     * @since 2018-09-11
     */
    @Rest(value = "coupon_count", desc = "优惠券信息", method = OptimusRequestMethod.GET)
    public Result<Map<String, Object>> couponCount(@ApiParam(value = "token", required = true)
                                                   @Param(value = "token", required = true) String token) {
        Result<Map<String, Object>> result = new Result<>();
        Integer couponCount = 0;
        try {
            Map<String, Integer> stringIntegerMap = orderCenterSPI.couponCount();
            couponCount = stringIntegerMap.get("count");
        } catch (Exception e) {
            logger.error("查询优惠券失败", e);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("count", couponCount);
        result.setSuccess(true);
        result.setCode("200");
        result.setData(resultMap);
        result.setMsg("成功");
        return result;
    }

    /**
     * 用户卷列表
     *
     * @author ZhangHui
     * @since 2018-09-11
     */
    @Rest(value = "pay_coupons", desc = "查询优惠券列表", method = OptimusRequestMethod.GET)
    public ResultWeb<Map<String, Object>> payCoupons(@ApiParam(value = "token", required = true)
                                                     @Param(value = "token", required = true) String token,
                                                     @ApiParam(value = "biz_name")
                                                     @Param(value = "biz_name") String biz_name) {
        ResultWeb<Map<String, Object>> result = new ResultWeb<>();
        List<CouponBaseVO> couponListDTOSResult = null;
        logger.info("用户卷列表");
        try {
            List<CouponAvailableDTO> couponAvailableDTOS = orderCenterSPI.userCoupons();
            couponListDTOSResult = new ArrayList<>();
            for (CouponAvailableDTO couponListDTO : couponAvailableDTOS) {
                CouponBaseVO couponBaseVO = new CouponBaseVO();
                couponBaseVO.setCoupon_id(couponListDTO.getCouponId());
                couponBaseVO.setCoupon_batch_id(couponListDTO.getCouponBatchId());
                couponBaseVO.setStatus(couponListDTO.getStatus());
                couponBaseVO.setCoupon_code(couponListDTO.getCouponCode());
                couponBaseVO.setOrder_code(couponListDTO.getOrderCode());
                couponBaseVO.setAmount(couponListDTO.getAmount());
                couponBaseVO.setBiz_code(couponListDTO.getBizCode());
                couponBaseVO.setExpiry_date_text(couponListDTO.getExpiryDateText());
                couponBaseVO.setCoupon_name(couponListDTO.getCouponName());
                couponBaseVO.setPlatform(couponListDTO.getPlatform());
                couponBaseVO.setWill_expired_icon_url(couponListDTO.getWillExpiredIconUrl());
                couponBaseVO.setList_cover_url(couponListDTO.getListCoverUrl());
                couponBaseVO.setWill_expired(couponListDTO.getWillExpired());
                couponListDTOSResult.add(couponBaseVO);
            }
        } catch (Exception e) {
            logger.info("用户卷列表 异常 e={}", JsonUtils.toJson(e));
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("coupons", couponListDTOSResult);
        result.setSuccess(true);
        result.setCode("10000");
        result.setData(resultMap);
        result.setMsg("success");
        result.setStatus(200);
        return result;
    }

    @Rest(value = "refund", desc = "退款", method = OptimusRequestMethod.GET)
    public Result<Boolean> refund(String orderCode) {
        MaintenanceRecordDO maintenanceRecordDO = maintenanceRecordManager.getCarMaintenance(orderCode);
//        if (maintenanceRecordDO.getStatus() == 3 || maintenanceRecordDO.getStatus() == 4) {
//            return Result.fail("500", "状态不允许");
//        }
        boolean bol = maintenanceOrderManager.refund(orderCode);
        return Result.success(true);
    }
}
