package com.souche.niu.spi;

/**
 * Created by sid on 2018/9/10.
 */
public interface SpaySPI {

    /**
     * 退款
     * @param orderId 订单号
     * @param refundFree 退款金额
     * @return
     */
    boolean refund(String orderId,int refundFree);
}
