package com.souche.bmgateway.core.manager.model;

import com.netfinworks.common.util.money.Money;
import com.netfinworks.pbs.service.context.vo.PartyInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zs.
 *         Created on 17/12/6.
 */
public class PbsFeeRequest implements Serializable {

    /*** 算费请求编号，用来唯一定位一笔算费请求。<b>必填</b> ***/
    private String requestNo;

    /** 产品编码,  <b>必填</b>*/
    private String productCode;

    /** 支付通道,例如 网银[04],一点冲[07]...*/
    private String payChannel;

    /** 支付发生时间,系统将根据此时间查找生效的定价策略 ，<b>必填</b> */
    private Date paymentInitiate;

    /**
     * <p>应付金额(不包括费用)</p>
     * 入款-付款方在机构应付的金额(算费前) 。</br>
     * 出款-付款方支付的金额(算费前)</br>
     * 转账-付款方支付的金额(算费前)</br>
     * <b>必填</b>
     */
    private Money payableAmount;

    /** 支付参与方支付信息, <b>必填</b>*/
    private List<PartyInfo> partyInfoList;

    /**
     * <p>是否忽略算费过程中的异常，比如策略配置错误等等。</p>
     * 如果ignoreError=ture,则当异常出现的时候返回客户端 0 费用</br>
     * 如果ignoreError=false，则当异常出现时返回异常类型,并中断</br>
     * 默认ignoreError=true。
     * 之所以加上此参数，源于业务要求当计费发生异常时不要影响正常的交易。而在事后解决费用问题(所有算费异常将会保留日志以备后续处理)
     */
    private boolean ignoreError = false;

    /** 扩展信息 */
    private String  extension;
}
