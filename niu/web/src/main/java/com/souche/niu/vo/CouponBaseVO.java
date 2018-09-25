package com.souche.niu.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author guishangquan
 * @Description
 * @Package com.souche.coupon.model.dto
 * @date 17/4/6
 **/
@Data
public class CouponBaseVO implements Serializable {

    private static final long serialVersionUID = 4922513159260541255L;

    /**
     * 券ID
     */
    private Long coupon_id;
    /**
     * 券批次ID
     */
    private Long coupon_batch_id;
    /**
     * 优惠券状态
     */
    private String status;
    /**
     * 优惠券编号
     */
    private String coupon_code;
    /**
     * 订单编号
     */
    private String order_code;
    /**
     * 订单类型
     */
    private String order_type;
    /**
     * 业务类型
     */
    private String biz_code;
    /**
     * 券名
     */
    private String coupon_name;
    /**
     * 券使用的平台
     */
    private String platform;
    /**
     * 券的有效期文本
     */
    private String expiry_date_text;
    /**
     * 券值
     */
    private BigDecimal amount;
    /**
     * 券值名
     */
    private String amount_name;


    private String will_expired_icon_url;

    private String list_cover_url;

    private Boolean will_expired;
}
