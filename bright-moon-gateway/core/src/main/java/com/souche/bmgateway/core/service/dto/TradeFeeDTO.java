package com.souche.bmgateway.core.service.dto;

import com.netfinworks.common.util.money.Money;
import com.netfinworks.pbs.service.context.vo.PartyInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 查询手续费信息
 *
 * @author zs.
 *         Created on 18/11/30.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TradeFeeDTO extends WalletBaseDTO {
    private static final long serialVersionUID = -8816013434427607330L;

    /*** 算费请求编号，用来唯一定位一笔算费请求 ***/
    private String requestNo;

    /*** 产品编码 ***/
    private String productCode;

    /*** 支付通道,例如 网银[04],一点冲[07]... ***/
    private String payChannel;

    /*** 支付发生时间,系统将根据此时间查找生效的定价策略 ***/
    private Date paymentInitiate;

    /*** 应付金额(算费前的金额) ***/
    private Money payableAmount;

    /*** 支付参与方支付信息***/
    private List<PartyInfo> partyInfoList;

    /*** 扩展信息 ***/
    private String extension;
}
