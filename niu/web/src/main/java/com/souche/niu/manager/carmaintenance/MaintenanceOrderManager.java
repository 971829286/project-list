package com.souche.niu.manager.carmaintenance;

import com.souche.order.model.OrderDTO;

/**
 * Created by sid on 2018/9/5.
 */
public interface MaintenanceOrderManager {

    /**
     * 创建订单以及记录
     *
     * @param vin
     * @param coupon_codes  优惠券
     * @param engine        发动机编号
     * @param license_plate 牌照
     * @return
     */
    OrderDTO createOrder(String vin, String coupon_codes, String engine, String license_plate) throws Exception;


    /**
     * 退款
     *
     * @param orderId
     */
    boolean refund(String orderId);

    /**
     * 发起查询请求
     * @param orderCode
     */
    void queryWbRequest(String orderCode);

}
