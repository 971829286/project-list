package com.souche.bmgateway.core.service.member.builder;

import com.netfinworks.ma.service.base.model.BankAccountInfo;

/**
 * 钱包会员相关构造
 *
 * @author chenwj
 * @since 2018/8/23
 */
public class WalletMemberBuilder {

    /**
     * 绑卡信息
     *
     * @param bankAccountInfo 银行卡信息
     * @param bankCard        银行卡号（解密后）
     * @return BankCardInfoBO
     */
    public static BankCardInfoBO buildBankCardInfo(BankAccountInfo bankAccountInfo, String bankCard) {
        BankCardInfoBO bo = new BankCardInfoBO();
        bo.setBankCardNo(bankCard);
        bo.setBankCertName(bankAccountInfo.getRealName());
        bo.setContactLine(bankAccountInfo.getBranchNo());
        return bo;
    }

}
