package com.souche.niu.spi;

import com.souche.coupon.model.dto.CouponAvailableDTO;
import com.souche.coupon.model.dto.CouponListDTO;
import com.souche.coupon.model.qo.OperateCouponQO;
import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.order.model.*;

import java.util.List;
import java.util.Map;

/**
 * 订单中心 spi
 * <p/>
 * 仅以此类纪念世界反法西斯战争胜利73周年
 * Created by sid on 2018/9/3.
 */
public interface OrderCenterSPI {

    Object refundPay();
    /**
     * 创建订单
     *
     * @return
     */
    OrderDTO createOrder(OrderRegisterQO orderRegisterQO);

    /**
     * 创建支付单
     *
     * @param paymentOrderQO
     * @return
     */
    PaymentOrderDTO createPaymentOrder(PaymentOrderQO paymentOrderQO);

    /**
     * 修改订单
     *
     * @param orderBizDataQO
     * @return
     */
    OrderResult changeOrder(OrderBizDataQO orderBizDataQO);

    /**
     * 核销券
     *
     * @author ZhangHui
     * @since 2018-09-08
     */
    String finishCoupon(MaintenanceRecordDO maintenanceRecordDO);

    /**
     * 查询订单详情
     *
     * @param orderCode
     * @return
     */
    Map<String, Object> orderDetail(String orderCode);

    /**
     * 获取支付参数
     *

     * @author ZhangHui
     * @since 2018-09-7
     */
    PaymentDTO preparePay(String orderCode, Long amountFens, String payerId, String payType);

    /**
     * 关闭订单
     *
     * @author ZhangHui
     * @since 2018-09-10
     */
    String closeOrder(String orderCode);

    /**
     * 用户卷列表
     *
     * @author ZhangHui
     * @since 2018-09-11
     */
    List<CouponAvailableDTO> userCoupons();

    /**
     * 可用卷数量
     *
     * @author ZhangHui
     * @since 2018-09-11
     */
    Map<String, Integer> couponCount();

    /**
     * 释放卷
     *
     * @author ZhangHui
     * @since 2018-09-29
     */
    String realeaseCoupon(MaintenanceRecordDO maintenanceRecordDO);
}
