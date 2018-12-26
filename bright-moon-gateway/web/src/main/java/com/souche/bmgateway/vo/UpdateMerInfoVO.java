package com.souche.bmgateway.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 修改结算卡信息
 *
 * @author chenwj
 * @since 2018/8/21
 */
@Getter
@Setter
@ToString
public class UpdateMerInfoVO {

    /**
     * 银行卡号
     */
    @NotBlank(message = "银行卡号不能为空")
    private String bankCardNo;

    /**
     * 银行名称
     */
    @NotBlank(message = "银行名称不能为空")
    private String bankCode;

    /**
     * 户名
     */
    @NotBlank(message = "户名不能为空")
    private String bankCertName;

    /**
     * 联行号
     */
    @NotBlank(message = "联行号不能为空")
    private String contactLine;

    /**
     * 支行名
     */
    @NotBlank(message = "支行名不能为空")
    private String branchName;

    /**
     * 支行所在省
     */
    @NotBlank(message = "支行所在省不能为空")
    private String branchProvince;

    /**
     * 支行所在市
     */
    @NotBlank(message = "支行所在市不能为空")
    private String branchCity;

    /**
     * 证件号
     */
    @NotBlank(message = "证件号不能为空")
    private String certNo;

    /**
     * 维金ID
     */
    @NotBlank(message = "维金ID不能为空")
    private String merchantId;

    /**
     * 店铺code
     */
    @NotBlank(message = "店铺code不能为空")
    private String shopCode;

}
