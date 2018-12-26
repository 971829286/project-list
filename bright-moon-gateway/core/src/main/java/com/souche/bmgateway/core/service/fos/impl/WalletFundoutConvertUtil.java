package com.souche.bmgateway.core.service.fos.impl;
/**
 * @author luobing
 * Create on 2018/11/20
 */

import com.netfinworks.common.util.money.Money;
import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.souche.bmgateway.core.enums.CardAttrEnums;
import com.souche.bmgateway.core.enums.CardTypeEnums;

import com.souche.bmgateway.core.service.dto.WalletFundoutDTO;

public class WalletFundoutConvertUtil {
    /**
     * 构造出款请求参数
     *
     * @param wallentFundoutDTO
     * @return
     */
    public static FundoutRequest convertFundoutRequest(WalletFundoutDTO wallentFundoutDTO) {
        FundoutRequest fundoutRequest = new FundoutRequest();
        fundoutRequest.setMemberId(wallentFundoutDTO.getMemberId());
        fundoutRequest.setAccountNo(wallentFundoutDTO.getAccountNo());
        fundoutRequest.setProductCode(wallentFundoutDTO.getBizProductCode());
        fundoutRequest.setAmount(new Money(wallentFundoutDTO.getAmount()));
        fundoutRequest.setName(wallentFundoutDTO.getBankAccountName());
        fundoutRequest.setCardNo(wallentFundoutDTO.getCardNo());
        fundoutRequest.setCardId(wallentFundoutDTO.getBankCardId());
        fundoutRequest.setExtension(wallentFundoutDTO.getExtension());
        fundoutRequest.setFundoutOrderNo(wallentFundoutDTO.getTradeVoucherNo());
        fundoutRequest.setPaymentOrderNo(wallentFundoutDTO.getPaymentVoucherNo());
        
        fundoutRequest.setBankCode(wallentFundoutDTO.getBankCode());
        fundoutRequest.setBankName(wallentFundoutDTO.getBankName());
        fundoutRequest.setBranchName(wallentFundoutDTO.getBankBranch());
        fundoutRequest.setProv(wallentFundoutDTO.getProv());
        fundoutRequest.setCity(wallentFundoutDTO.getCity());
        fundoutRequest.setCompanyOrPersonal(CardAttrEnums.getByInsCode(wallentFundoutDTO.getCompanyOrPersonal()).getCode());
        fundoutRequest.setBankLineNo(wallentFundoutDTO.getBankLineNo());
		fundoutRequest.setCardType(CardTypeEnums.getByInsCode(wallentFundoutDTO.getCardTye()).getShortCode());
		fundoutRequest.setFee(new Money("0"));
		fundoutRequest.setName(wallentFundoutDTO.getName());
        return fundoutRequest;
    }
}
