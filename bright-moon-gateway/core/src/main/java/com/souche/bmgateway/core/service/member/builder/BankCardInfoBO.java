package com.souche.bmgateway.core.service.member.builder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 绑卡信息 业务对象
 *
 * @author chenwj
 * @since 2018/8/23
 */
@Setter
@Getter
@ToString
public class BankCardInfoBO {

    /**
     * 银行卡号
     */
    private String bankCardNo;

    /**
     * 户名
     */
    private String bankCertName;

    /**
     * （支行）联行号
     */
    private String contactLine;

}
