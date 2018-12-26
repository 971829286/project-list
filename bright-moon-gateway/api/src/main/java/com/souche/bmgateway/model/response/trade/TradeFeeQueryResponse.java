package com.souche.bmgateway.model.response.trade;

import com.souche.bmgateway.model.response.CommonResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 手续费返回值
 *
 * @author zs.
 *         Created on 18/11/19.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TradeFeeQueryResponse extends CommonResponse {

    /*** 买家id ***/
    private String buyerId;

    /*** 买家费用 ***/
    private String buyerFee;

    /*** 卖家id ***/
    private String sellerId;

    /*** 卖家费用 ***/
    private String sellerFee;
}
