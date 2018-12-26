package com.souche.bmgateway.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商户入驻
 *
 * @author chenwj
 * @since 2018/7/25
 */
@Setter
@Getter
@ToString
public class MerchantSettleRequest extends CommonRequest {

    /**
     * 外部商户号。合作商对商户的自定义编码，要求同一个合作商下保持唯一
     */
    private String outMerchantId;

    /**
     * 商户名称。有营业执照的，要求与营业执照上的名称一致
     */
    private String merchantName;

    /**
     * 商户类型。可选值：
     * 02:个体工商户
     * 03:企业商户
     */
    private String merchantType;

    /**
     * DealType	商户经营类型。可选值：
     * 01:实体特约商户
     * 02:网络特约商户
     * 03:实体兼网络特约商户
     */
    private String dealType;

    /**
     * 商户清算资金是否支持T+0到账。可选值：
     * N：不支持【目前因合规原因暂不开放T0】
     */
    private String supportPrepayment;

    /**
     * 结算方式。商户清算资金结算方式，可选值：
     * 01：结算到他行卡
     * 05：结算到子户
     */
    private String settleMode;

    /**
     * 支付宝线上经营类目
     */
    private String mcc;

    /**
     * 支付宝线上经营类目
     */
    private String onlineMcc;

    /**
     * 商户详情列表
     */
    private String merchantDetail;

    /**
     * 支付宝线上站点信息
     */
    private String siteInfo;

    /**
     * 支持交易类型列表
     */
    private String tradeTypeList;

    /**
     * 支持支付渠道列表
     */
    private String payChannelList;

    /**
     * 手续费列表
     */
    private String feeParamList;

    /**
     * 清算卡参数
     */
    private String bankCardParam;

    /**
     * 手机验证码。
     * 备注：合作方系统须先调网商短信验证码发送接口，BizType=6，商户获取验证码后在申请页面回填验证码，合作方系统通过本接口统一上送
     */
    private String authCode;

    /**
     * 外部交易号。
     * 合作方系统生成的外部交易号，同一交易号被视为同一笔交易
     */
    private String outTradeNo;

    /**
     * 该字段用于决定商户是否可自主选择让其买家使用花呗分期，包括商户承担手续费和用户承担手续费两种模式。目前仅支持用户承担手续费模式。可选值：
     * Y：支持
     * N：不支持(默认值)
     */
    private String supportStage;

    /**
     * 商户在进行微信支付H5支付时所使用的公众号相关信息的类型
     */
    private String partnerType;

    /**
     * 大搜车在支付宝的pid
     */
    private String alipaySource;

    /**
     * 外部请求单号，用于查问题
     */
    private String reqMsgId;

}
