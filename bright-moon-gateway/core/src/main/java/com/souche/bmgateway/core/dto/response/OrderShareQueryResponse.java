package com.souche.bmgateway.core.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: huangbin
 * @Description: 单笔分帐查询响应
 * @Date: Created in 2018/07/09
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class OrderShareQueryResponse {

    /**
     * 返回码组件对象
     */
    private ResponseInfo responseInfo;

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

    /**
     * 订单金额(金额为分)
     */
    private String TotalAmount;

    /**
     * 币种，默认CNY
     */
    private String Currency;

    /**
     * 分账时间
     */
    private String ShareDate;

    /**
     * 状态(SUCCESS, FAIL)
     */
    private String Status;

    /**
     * 错误码
     */
    private String ErrorCode;

    /**
     * 错误描述
     */
    private String ErrorDesc;

    /**
     * 扩展信息
     */
    private String ExtInfo;

    /**
     * 备注
     */
    private String Memo;

}
