package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.voucher.service.facade.domain.voucher.ControlVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.PaymentVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.RefundVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.SimpleOrderVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.TradeVoucherInfo;
import com.souche.bmgateway.core.exception.ManagerException;

import java.util.List;
import java.util.Map;

/**
 * @author zs.
 *         Created on 18/11/15.
 */
public interface VoucherManager {

    /**
     * 记录交易统一凭证（转账&充值&提现）
     *
     * @param simpleOrderVoucherInfo
     * @return
     * @throws ManagerException
     */
    String recordUnifiedVoucher(SimpleOrderVoucherInfo simpleOrderVoucherInfo) throws ManagerException;


    /**
     * 记录支付凭证
     *
     * @param paymentVoucherInfo
     * @return
     * @throws ManagerException
     */
    String recordPaymentVoucher(PaymentVoucherInfo paymentVoucherInfo) throws ManagerException;


    /**
     * 记录退款凭证
     *
     * @param refundVoucherInfo
     * @return
     * @throws ManagerException
     */
    String recordRefundVoucher(RefundVoucherInfo refundVoucherInfo) throws ManagerException;


    /**
     * 记录控制凭证（冻结&解冻&修改金额）
     *
     * @param controlVoucherInfo
     * @return
     * @throws ManagerException
     */
    String saveControlRecord(ControlVoucherInfo controlVoucherInfo) throws ManagerException;


    /**
     * 查询凭证(解冻)
     *
     * @param outerTradeNo
     * @param source
     * @param voucherType
     * @return
     */
    String queryTradeBy(String outerTradeNo, String source, String voucherType) throws ManagerException;


    /**
     * 批量记录交易凭证（用户即时交易和担保交易）
     *
     * @param tradeVoucherInfoList
     * @return Map<外部交易号, 统一凭证号>
     * @throws ManagerException
     */
    Map<String, String> batchRecordTradeVoucher(List<TradeVoucherInfo> tradeVoucherInfoList) throws ManagerException;


    /**
     * 查询交易凭证信息
     *
     * @param voucherNo
     * @return
     * @throws ManagerException
     */
    TradeVoucherInfo queryTradeByVoucherNo(String voucherNo) throws ManagerException;

}
