package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs.
 *         Created on 18/7/17.
 */
@Getter
@Setter
@ToString
public class QueryBankAccountRequest extends CommonBaseRequest {
    private static final long serialVersionUID = -6609094648762231440L;

    /*** 钱包id ***/
    private String memberId;

    /*** 卡类型(DEBIT借记卡, CREDIT信用卡, PASSBOOK存折, OTHER其它) ***/
    private String cardType;

    /*** 卡属性(C对私, B对公) ***/
    private String cardAttribute;

    /*** 卡号 ***/
    private String bankAccountNum;
}
