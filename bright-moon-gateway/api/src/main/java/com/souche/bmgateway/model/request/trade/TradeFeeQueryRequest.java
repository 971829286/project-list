package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 查询手续费请求参数
 *
 * @author zs.
 *         Created on 18/12/3.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TradeFeeQueryRequest extends CommonBaseRequest {

    /*** 算费请求编号，用来唯一定位一笔算费请求 ***/
    @NotBlank
    private String requestNo;

    /*** 产品编码 ***/
    @NotBlank
    private String bizProductCode;

    /*** 支付通道,例如 网银[04],一点冲[07]... ***/
    @NotBlank
    private String payChannel;

    /*** 应付金额(算费前的金额) ***/
    @NotBlank
    private String payableAmount;

    /*** 买家id ***/
    private String buyerId;

    /*** 卖家id ***/
    private String sellerId;

    /*** 扩展信息 ***/
    private String extension;
}
