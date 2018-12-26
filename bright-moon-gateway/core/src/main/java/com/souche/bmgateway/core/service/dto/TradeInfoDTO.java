package com.souche.bmgateway.core.service.dto;

import com.netfinworks.common.util.money.Money;
import com.netfinworks.tradeservice.facade.enums.TradeType;
import com.netfinworks.tradeservice.facade.model.SplitParameter;
import com.netfinworks.tradeservice.facade.model.paymethod.PayMethod;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 即时交易传输对象
 * @author zs.
 *         Created on 18/11/20.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TradeInfoDTO extends WalletBaseDTO {
    private static final long serialVersionUID = -7206136788748490826L;

    /*** 交易原始凭证号 ***/
    private String tradeSourceVoucherNo;

    /*** 交易凭证号 ***/
    private String tradeVoucherNo;

    /*** 业务订单号 ***/
    private String bizNo;

    /*** 交易类型 ***/
    private TradeType tradeType;

    /*** 买家id ***/
    private String buyerId;
    
    /*** 买家ip ***/
    private String buyerIp;

    /*** 买家账号 ***/
    private String buyerAccountNo;

    /*** 业务产品码 ***/
    private String bizProductCode;

    /*** 产品描述 ***/
    private String productDesc;

    /*** 产品展示url ***/
    private String showUrl;

    /*** 交易金额 ***/
    private Money tradeAmount;

    /*** 金币金额 ***/
    private Money coinAmount;

    /*** 分润集 ***/
    private List<SplitParameter> splitParameterList;

    /*** 卖方钱包id ***/
    private String sellerId;

    /*** 卖方账号 ***/
    private String sellerAccountNo;

    /*** 交易备注 存放商品标题 ***/
    private String tradeMemo;

    /*** 扩展参数 ***/
    private String extension;

    /*** 订单失效时间 ***/
    private Date gmtInvalid;

    /*** 异步通知地址 ***/
    private String notifyUrl;


    /************************** 支付相关参数 **************************/
    private String paymentVoucherNo;
    private String paymentSourceVoucherNo;
    private List<PayMethod> payMethodList;
    private String payExtension;

}
