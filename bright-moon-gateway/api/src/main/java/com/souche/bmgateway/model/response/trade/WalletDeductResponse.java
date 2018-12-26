package com.souche.bmgateway.model.response.trade;

import com.souche.bmgateway.model.response.CommonResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 扣款（特殊转账接口）
 *
 * @author zs.
 *         Created on 18/11/26.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletDeductResponse extends CommonResponse {

    /*** 实际扣款金额 ***/
    private String deductAmount;
}
