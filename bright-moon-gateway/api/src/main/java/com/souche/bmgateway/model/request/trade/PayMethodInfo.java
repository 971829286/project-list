package com.souche.bmgateway.model.request.trade;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author zs.
 *         Created on 18/11/20.
 */
@Getter
@Setter
@ToString
public class PayMethodInfo implements Serializable {
    
    /*** 支付方式 BALANCE, POS, ONLINE_BANK, QPAY, CASH ***/
    @NotBlank
    private String payMethod;

    /*** 支付金额 ***/
    @NotBlank
    private String amount;

    /**
     * 支付备注
     * 格式为以逗号分隔的字符串：银行代码,对公/对私：B/C,借记卡/贷记卡：DC=借记卡支付,CC=贷记卡支付)
     * 当支付方式为网银、POS方式时，memo必填。样例：ALIPAY,C,DC
     * 当支付方式为快捷时memo必填, 需要在后面增加B+bankid或N+ bank_account_no
     * 样例：ALIPAY,C,DC,N6228480210599399511或ALIPAY,C,DC,B10599399511
     * 当支付方式为余额时，memo为空默认基本户支付
     **/
    private String memo;

    /*** 扩展参数, 需要根据不同的支付方式传入需要的扩展参数 ***/
    private String extension;
}
