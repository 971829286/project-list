package com.souche.bmgateway.model.response.trade;

import com.souche.bmgateway.model.response.CommonResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs.
 *         Created on 18/11/19.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CreatePayOrderResponse extends CommonResponse {

    /*** 交易凭证号 支付时需携带 ***/
    private String tradeVoucherNo;
}
