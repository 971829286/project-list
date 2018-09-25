package com.souche.niu.manager.carmaintenance.impl;

import com.souche.niu.constant.Constants;
import com.souche.niu.manager.carmaintenance.MaintenanceOrderManager;
import com.souche.niu.manager.carmaintenance.MaintenanceRecordManager;
import com.souche.niu.manager.cms.CmsManager;
import com.souche.niu.model.MaintenanceConfigDO;
import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.niu.model.maintenance.request.QueryMaintenanceRequest;
import com.souche.niu.spi.OrderCenterSPI;
import com.souche.niu.spi.ShopSPI;
import com.souche.niu.spi.SpaySPI;
import com.souche.niu.spi.WeiBaoSPI;
import com.souche.optimus.cache.CacheService;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.order.model.OrderDTO;
import com.souche.order.model.OrderRegisterQO;
import com.souche.sso.client2.AuthNHolder;
import com.souche.vinquery.api.bean.ParseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 订单操作服务
 * Created by sid on 2018/9/5.
 */
@Service
public class MaintenanceOrderManagerImpl implements MaintenanceOrderManager {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceOrderManagerImpl.class);

    @Autowired
    private OrderCenterSPI orderCenterSPI;

    @Autowired
    private CmsManager cmsManager;

    @Autowired
    private ShopSPI shopSPI;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private MaintenanceRecordManager maintenanceRecordManager;

    @Autowired
    private SpaySPI spaySPI;

    @Autowired
    private WeiBaoSPI weiBaoSPI;

    private String notifyUrl = OptimusConfig.getValue("domain") + "/maintenance/maintenanceQuerity/wbNotify.json";

    @Override
    public OrderDTO createOrder(String vin, String coupon_codes, String engine, String license_plate) {
        logger.info("创建订单，车vin码vin={},优惠券coupon_codes={},引擎盖vin编号engine={},牌照license_plate={}", vin, coupon_codes, engine, license_plate);
        String userId = AuthNHolder.userId();
        String shopCode = AuthNHolder.shopCode();
        OrderRegisterQO orderRegisterQO = new OrderRegisterQO();
        orderRegisterQO.setBusinessType(Constants.BUSSIZE_TYPE);
        orderRegisterQO.setCouponCode(coupon_codes);
        orderRegisterQO.setBuyerId(userId);
        orderRegisterQO.setBuyerType("cheniu_user");
        orderRegisterQO.setSellerId("000000");
        orderRegisterQO.setSellerType("fengche_shop");
        orderRegisterQO.setTradeInfo("车牛维保个人支付订单");
        orderRegisterQO.setTradeDesc("车牛维保个人支付订单");
        orderRegisterQO.setTradeWay("车牛维保个人支付订单");
        orderRegisterQO.setItemId("0");
        orderRegisterQO.setItemType("service");
        MaintenanceConfigDO m = cmsManager.queryMaintenance();
        boolean isCert = shopSPI.checkShopAuthentication(shopCode);
        Long price = 0L;
        if (isCert) {
            //已经企业认证的一个价格
            price = (m.getVerifiedPrice().multiply(new BigDecimal(100))).longValue();
        } else {
            //企业未认证的一个价格
            price = (m.getUnverifiedPrice().multiply(new BigDecimal(100))).longValue();
        }
        orderRegisterQO.setAmountFens(price);
        OrderDTO orderDTO = null;
        try {
            orderDTO = orderCenterSPI.createOrder(orderRegisterQO);
        } catch (Exception e) {
            return null;
        }
        logger.info("创建订单之后，订单中心返回orderDTO={}", orderDTO);
        //创建记录
        createRecord(vin, engine, license_plate, orderRegisterQO, orderDTO);
        return orderDTO;
    }

    /**
     * 只是请求.具体结果等mq通知
     *
     * @param orderId
     */
    @Override
    public boolean refund(String orderId) {
        logger.info("订单:{},开始执行退款操作", orderId);
        boolean isSuccess = false;
        try {
            MaintenanceRecordDO maintenanceRecordDO = maintenanceRecordManager.getCarMaintenance(orderId);
            BigDecimal price = maintenanceRecordDO.getCurrentPrice();
            int refundFree = price.multiply(new BigDecimal(100)).intValue();
            isSuccess = spaySPI.refund(orderId, refundFree);
            int refundStatus = 1;
            //更改退款状态
            if (!isSuccess) {
                refundStatus = 2;
                logger.error("退款出错:{}", orderId);
            } else {
                logger.info("订单:{},退款操作请求完成", orderId);
            }
            maintenanceRecordDO.setStatus(2);
            maintenanceRecordDO.setRefundStatus(refundStatus);
            maintenanceRecordManager.updateCarMaintenance(maintenanceRecordDO);
            logger.info("更新维保查询记录结束:{}", JsonUtils.toJson(maintenanceRecordDO));
        } catch (Exception e) {
            logger.error("退款 向订单中心请求退款异常", e);
        }
        return isSuccess;
    }

    @Override
    public void queryWbRequest(String orderCode) {
        //获取需要查询的vin码
        logger.info("维保查询 orderCode={}", orderCode);
        MaintenanceRecordDO maintenanceRecordDO = maintenanceRecordManager.getCarMaintenance(orderCode);
        QueryMaintenanceRequest queryMaintenanceRequest = new QueryMaintenanceRequest(maintenanceRecordDO.getVinNumber(), orderCode, notifyUrl);
        Map<String, Object> resMap = weiBaoSPI.thirdServiceQuery(queryMaintenanceRequest);
        boolean isSuccess = (Boolean) resMap.get("success");
        String reason = (String) resMap.get("msg");
        MaintenanceRecordDO carMaintenance = maintenanceRecordManager.getCarMaintenance(orderCode);
        int status = carMaintenance.getStatus();
        if (isSuccess) {
            //如果查询成功,结束就好
            logger.info("维保查询 维保查询成功 准备更改流水表中状态 order_code={}", orderCode);
            if (status < 3) {
                carMaintenance = maintenanceRecordManager.updateCarMaintenance(new MaintenanceRecordDO(orderCode, 3, 0));
                logger.info("维保查询 更新流水后 maintenanceRecordDO={}", JsonUtils.toJson(carMaintenance));
            }
            return;
        }
        //维保查询失败。
        //修改流水状态
        logger.info("维保查询 维保查询失败，准备修改流水状态,orderCode:{}", orderCode);
        if (status < 2) {
            carMaintenance = new MaintenanceRecordDO(orderCode, 2, "1", reason, null, "");
            logger.info("维保查询 更新流水后 maintenanceRecordDO={}", JsonUtils.toJson(carMaintenance));
            //支付消息记录
            //执行退款
           refund(carMaintenance);
        }
    }

    private void refund(MaintenanceRecordDO recordDO){
        logger.info("订单:{},开始执行退款操作", recordDO);
        try {
            MaintenanceRecordDO maintenanceRecordDO = maintenanceRecordManager.getCarMaintenance(recordDO.getOrderId());
            BigDecimal price = maintenanceRecordDO.getCurrentPrice();
            int refundFree = price.multiply(new BigDecimal(100)).intValue();
            boolean isSuccess = spaySPI.refund(recordDO.getOrderId(), refundFree);
            int refundStatus = 1;
            //更改退款状态
            if (!isSuccess) {
                refundStatus = 2;
                logger.error("退款出错:{}", recordDO.getOrderId());
            } else {
                logger.info("订单:{},退款操作请求完成", recordDO.getOrderId());
            }
            recordDO.setRefundStatus(refundStatus);
            maintenanceRecordManager.updateCarMaintenance(recordDO);
            logger.info("更新维保查询记录结束:{}", JsonUtils.toJson(recordDO));
        } catch (Exception e) {
            logger.error("退款 向订单中心请求退款异常", e);
        }
    }


    //创建记录
    private void createRecord(String vin, String engine, String license_plate, OrderRegisterQO orderRegisterQO, OrderDTO orderDTO) {
        logger.info("创建订单中的创建记录，vin={}", vin);
        String phone = AuthNHolder.userPhone();
        MaintenanceRecordDO maintenanceRecordDO = new MaintenanceRecordDO();
        maintenanceRecordDO.setVinNumber(vin);
        maintenanceRecordDO.setOrderId(orderDTO.getOrderCode());
        maintenanceRecordDO.setPhone(phone);
        maintenanceRecordDO.setCouponCode(orderRegisterQO.getCouponCode());
        maintenanceRecordDO.setAppName(orderRegisterQO.getBuyerType());
        //订单总金额
        maintenanceRecordDO.setPrice(orderDTO.getOrderTotalAmount());
        //实际支付金额
        maintenanceRecordDO.setCurrentPrice(orderDTO.getBuyerShouldPayAmountYuan());
        maintenanceRecordDO.setRefundStatus(0);//未发生退款动作.
        maintenanceRecordDO.setEngine(engine);
        maintenanceRecordDO.setLicensePlate(license_plate);
        maintenanceRecordDO.setStatus(0);
        maintenanceRecordDO.setPayPrice(new BigDecimal(0));
        ParseModel parseModel = cacheService.getObject(Constants.CAR_PARSEMODEL_PRE + vin, ParseModel.class);
        if (parseModel != null) {
            maintenanceRecordDO.setBrand(parseModel.getBrandName());
        }
        maintenanceRecordManager.createMaintenanceRecord(maintenanceRecordDO);
        logger.info("创建维保记录完成:{}", JsonUtils.toJson(maintenanceRecordDO));
    }
}
