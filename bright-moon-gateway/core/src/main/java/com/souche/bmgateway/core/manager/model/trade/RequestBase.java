package com.souche.bmgateway.core.manager.model.trade;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.souche.bmgateway.core.enums.ProcessStateKind;
import com.souche.bmgateway.core.enums.SignTypeKind;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

/**
 * <p>商户请求对象的基类</p>
 * @author sirk
 */
@Setter
@Getter
@ToString
public class RequestBase {

    private String service;

    private String version;

    private String partnerId;

    private String inputCharset;

    private String sign;

    //枚举不验证    @Validate(type = ValidateTypeKind.Length, name = "签名方式", maxLength = 10, required = true)
    private String signType;

    private String returnUrl;

    private String memo;


    private String isWebAccess;

    //处理状态
    private ProcessStateKind state;

    // 内部交易号，统一凭证返回
    private String innerTradeNo;

    // 买方memberID
    private String buyerMemberId;
    // 买方基础账户
    private String buyerAccount;
    // 统一凭证号list
    private List<String> voucherNoList;
    // 支付统一凭证号
    private String payVoucherNo;
    // 原交易凭证号list
    private List<String> outerTradeNoList;

    private String referUrl;

    private String userAgent;

    // 访问渠道，Pc还是手机
    private String accessChannel;

}
