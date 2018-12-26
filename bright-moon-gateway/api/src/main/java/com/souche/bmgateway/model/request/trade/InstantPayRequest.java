package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zs.
 *         Created on 18/11/19.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstantPayRequest extends CommonBaseRequest {

    /*** 支付请求号 ***/
    @NotBlank
    private String requestNo;

    /** 交易凭证号 */
    @NotBlank(message = "交易凭证号不能为空")
    private String tradeVoucherNo;

    /*** 支付方式 目前每次支付只支持一中支付方式 ***/
    @NotNull
    @Valid
    private PayMethodInfo payMethod;

    /*** 扩展参数 ***/
    @Size(min = 0, max = 2000, message = "扩展参数长度校验失败，要求0-2000位")
    private String extension;

}
