package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author yyx
 */
@Getter
@Setter
@ToString
@Deprecated
public class EntryRequest extends CommonBaseRequest {
    private static final long serialVersionUID = -1597696332589453502L;

    /** 借方会员ID: 支付系统的会员唯一标示 */
    private String drMemberId;

    /** 借方账号,在储值系统中存在的账号 **/
    private String drAccountNo;

    /** 借记资金属性：CA-贷方资金 DA-借方资金 BI-借贷均可 **/
    private String drFundType;

    /** 贷方会员ID **/
    private String crMemberId;

    /** 贷方账号 **/
    private String crAccountNo;

    /** 贷记资金属性：CA-贷方资金 DA-借方资金 BI-借贷均可 **/
    private String crFundType;

    /** 登帐金额 **/
    private String amount;

    /** 关联支付凭证号 **/
    private String relatePaymentVoucherNo;

    /** 业务产品编码 **/
    private String bizProductCode;

    /** 支付凭证号，非空 **/
    private String paymentVoucherNo;

    /** 支付编码，可空 **/
    private String paymentCode;

    /** 支付目的备注信息 **/
    private String memo;

    /** 支付事件回调地址 **/
    private String callbackAddress;

    /** 控制参数列表 **/
    private List<String> controlParamList;

    /** 扩展信息: JSON字符串 **/
    private String extension;
}
