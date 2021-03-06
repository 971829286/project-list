package com.souche.niu.spi.adapter;

import com.souche.coupon.api.CouponApiService;
import com.souche.coupon.constant.BizCodeEnum;
import com.souche.coupon.constant.UserTypeEnum;
import com.souche.coupon.model.dto.CouponAvailableDTO;
import com.souche.coupon.model.dto.CouponListDTO;
import com.souche.coupon.model.qo.OperateCouponQO;
import com.souche.coupon.result.CommonResult;
import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.niu.spi.OrderCenterSPI;
import com.souche.niu.util.SignUtils;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.order.api.OrderApiService;
import com.souche.order.api.PaymentOrderApiService;
import com.souche.order.model.*;
import com.souche.sso.client2.AuthNHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sid on 2018/9/3.
 */
@Component
public class OrderCenterSPIAdapter implements OrderCenterSPI {

    private static final Logger logger = LoggerFactory.getLogger(OrderCenterSPIAdapter.class);

    private String finishCouponSalt = OptimusConfig.getValue("finishCoupon.salt");

    @Resource
    private OrderApiService orderApiService;

    @Resource
    private PaymentOrderApiService paymentOrderApiService;

    @Resource
    private CouponApiService couponApiService;

    @Override
    public PaymentDTO preparePay(String orderCode, Long amountFens, String payerId, String payType) {
        try {
            logger.info("获取支付信息 准备获取支付信息 orderCode = {}, amountFens = {}, payerId = {}, payType = {}", orderCode, amountFens, payerId, payType);
            OrderResult<PaymentDTO> orderResult = orderApiService.preparePay(orderCode, amountFens, payerId, payType);
            if (orderResult.isSuccess()){
                return orderResult.getData();
            }
        }catch (Exception e){
            logger.error("获取支付信息错误={}",e);
            return null;
        }
        return null;
    }

    @Override
    public Object refundPay() {
        return null;
    }

    /**
     * 创建订单
     *
     * @return
     */
    @Override
    public OrderDTO createOrder(OrderRegisterQO orderRegisterQO) {
        try {
            logger.info("创建订单 向订单中心请求创建订单 orderRegisterQO={}",orderRegisterQO);
            OrderResult<OrderDTO> result = orderApiService.register(orderRegisterQO);
            logger.info("创建订单 订单中心返回创建订单结果 result=[}",result);
            if (result.isSuccess() && result.getData() != null) {
                return result.getData();
            }
        } catch (Exception e) {
            logger.error("创建订单向订单中心请求创建订单异常",e);
            return null;
        }
        return null;
    }

    /**
     * 创建支付单
     *
     * @param paymentOrderQO
     * @return
     */
    @Override
    public PaymentOrderDTO createPaymentOrder(PaymentOrderQO paymentOrderQO) {
        try {
            OrderResult<PaymentOrderDTO> result = paymentOrderApiService.create(paymentOrderQO);
            if (result.isSuccess() && result.getData() != null) {
                return result.getData();
            }
        } catch (Exception e) {
            logger.error("向订单中心创建支付单时异常",e);
            return null;
        }
        return null;
    }

    /**
     * 修改订单
     *
     * @param orderBizDataQO
     * @return
     */
    @Override
    public OrderResult changeOrder(OrderBizDataQO orderBizDataQO) {
        try {
            OrderResult orderResult = orderApiService.bizData(orderBizDataQO);
            if (orderResult.isSuccess() && orderResult.getData() != null) {
                return orderResult;
            }
        } catch (Exception e) {
            logger.error("向订单中心修改订单时异常e={}",e);
            return null;
        }
        return null;
    }

    /**
     * 核销券
     *
     * @author ZhangHui
     * @since 2018-09-08
     */
    @Override
    public String finishCoupon(MaintenanceRecordDO maintenanceRecordDO) {
        try {
            logger.info("核销卷 maintenanceRecordDO={}",JsonUtils.toJson(maintenanceRecordDO));
            OperateCouponQO operateCouponQO = new OperateCouponQO();
            operateCouponQO.setCouponCode(maintenanceRecordDO.getCouponCode());
            logger.info("核销卷 准备加密Salt={},orderCode={}",finishCouponSalt, maintenanceRecordDO.getOrderId());
            String encrypt = SignUtils.encrypt(finishCouponSalt + maintenanceRecordDO.getOrderId());
            operateCouponQO.setSafeToken(encrypt);
            logger.info("核销卷 准备请求订单中心");
            CommonResult<String> result = couponApiService.finishCoupon(operateCouponQO);
            logger.info("核销卷 订单中心返回结果 result={}",result);
            if (result.isSuccess() && result.getData() != null) {
                return result.getData();
            }
        } catch (Exception e) {
            logger.error("向订单中心请求核销券异常",e);
            return null;
        }
        return null;
    }

    /**
     * 查询订单详情
     *
     * @param orderCode
     * @return
     */
    @Override
    public Map<String, Object> orderDetail(String orderCode) {
        try {
            OrderResult<Map<String, Object>> detail = orderApiService.detail(orderCode);
            if (detail.isSuccess() && detail.getData() != null) {
                return detail.getData();
            }
        } catch (Exception e) {
            logger.error("向订单中心查询订单详情时异常e={}",e);
            return null;
        }
        return null;
    }



    /**
     * 关闭订单
     *
     * @author ZhangHui
     * @since 2018-09-10
     */
    @Override
    public String closeOrder(String orderCode) {
        try {
            logger.info("关闭订单 向订单中心请求关闭订单释放卷 orderCode={}", orderCode);
            OrderResult<PaymentDTO> orderResult = orderApiService.closeOrder(orderCode);
            logger.info("关闭订单 订单中心返回 orderResult", orderResult);
            if (orderResult.isSuccess() && orderResult.getCode().equals("200")) {
                return "success";
            }
        } catch (Exception e) {
            logger.error("关闭订单 向订单中心请求关闭订单释放卷 异常 orderCode={}, e={}",orderCode, e);
            return "error";
        }
        return "";
    }

    /**
     * 用户卷列表
     *
     * @author ZhangHui
     * @since 2018-09-11
     */
    @Override
    public List<CouponAvailableDTO> userCoupons() {
        try {
            String userId = AuthNHolder.userId();
            logger.info("优惠券 用户卷列表 userId={}", userId);
            CommonResult<List<CouponAvailableDTO>> listCommonResult = couponApiService.getAvailableCoupons(userId, UserTypeEnum.cheniu_user, BizCodeEnum.car_maintenance_query_order, null, null);
            if (listCommonResult.isSuccess() && listCommonResult.getData() != null) {
                List<CouponAvailableDTO> couponAvailableDTOList = listCommonResult.getData();
                logger.info("优惠券 用户卷列表 订单中心返回数据 couponAvailableDTOList={}",JsonUtils.toJson(couponAvailableDTOList));
                return listCommonResult.getData();
            }
        } catch (Exception e) {
            logger.error("优惠券 向订单中心请求用 户卷列表 异常e={}",e);
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    /**
     * 可用卷数量
     *
     * @author ZhangHui
     * @since 2018-09-11
     */
    @Override
    public Map<String, Integer> couponCount() {
        try {
            String userId = AuthNHolder.userId();
            logger.info("优惠券 可用卷数量 userId={}", userId);
            CommonResult<Map<String, Integer>> mapCommonResult = couponApiService.couponCount(userId, UserTypeEnum.cheniu_user, BizCodeEnum.car_maintenance_query_order);
            if (mapCommonResult.isSuccess() && mapCommonResult.getData() != null) {
                return mapCommonResult.getData();
            }
        } catch (Exception e) {
            logger.error("优惠券 向订单中心请求 可用卷数量 异常e={}",e);
            return null;
        }
        return null;
    }

    /**
     * 释放券
     *
     * @author ZhangHui
     * @since 2018-09-29
     */
    @Override
    public String realeaseCoupon(MaintenanceRecordDO maintenanceRecordDO) {
        try {
            logger.info("释放券 maintenanceRecordDO={}",JsonUtils.toJson(maintenanceRecordDO));
            OperateCouponQO operateCouponQO = new OperateCouponQO();
            operateCouponQO.setCouponCode(maintenanceRecordDO.getCouponCode());
            logger.info("释放券 准备加密Salt={},orderCode={}",finishCouponSalt, maintenanceRecordDO.getOrderId());
            String encrypt = SignUtils.encrypt(finishCouponSalt + maintenanceRecordDO.getOrderId());
            operateCouponQO.setSafeToken(encrypt);
            logger.info("释放券 准备请求订单中心");
            CommonResult<String> result = couponApiService.realeaseCoupon(operateCouponQO);
            logger.info("释放券 订单中心返回结果 result={}",result);
            if (result.isSuccess() && result.getData() != null) {
                return result.getData();
            }
        } catch (Exception e) {
            logger.error("向订单中心请求释放券异常",e);
            return null;
        }
        return null;
    }
}
