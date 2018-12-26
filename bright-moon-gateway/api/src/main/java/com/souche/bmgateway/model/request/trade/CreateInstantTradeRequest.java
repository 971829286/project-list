package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * 收单接口请求基本参数
 *
 * @author zs.
 *         Created on 18/7/12.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CreateInstantTradeRequest extends CommonBaseRequest {

    /*** 支付请求号 ***/
    private String requestNo;

    @Valid
    @NonNull
    private CreatePayOrderRequest createPayOrderRequest;

    /*** 支付方式 目前每次支付只支持一中支付方式 ***/
    @NonNull
    private PayMethodInfo payMethod;

    /*** 支付扩展参数 ***/
    @Size(min = 0, max = 2000, message = "扩展参数长度校验失败，要求0-2000位")
    private String extension;
}
