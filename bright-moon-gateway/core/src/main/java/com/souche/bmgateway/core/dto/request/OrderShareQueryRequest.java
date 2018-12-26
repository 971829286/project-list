package com.souche.bmgateway.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: huangbin
 * @Description: 单笔分帐查询请求
 * @Date: Created in 2018/07/09
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class OrderShareQueryRequest {

    /**
     * 请求流水号
     */
    private String ReqMsgId;

    /**
     * 收款方商户号
     */
    private String MerchantId;

    /**
     * 关联网商订单号
     */
    private String RelateOrderNo;

    /**
     * 外部订单分账请求流水号
     */
    private String OutTradeNo;

    /**
     * 分账单号
     */
    private String ShareOrderNo;

}
