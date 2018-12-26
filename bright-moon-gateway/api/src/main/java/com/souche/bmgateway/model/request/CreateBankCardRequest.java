package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 绑定银行卡
 *
 * @author zs.
 *         Created on 18/7/17.
 */
@Getter
@Setter
@ToString
public class CreateBankCardRequest extends CommonBaseRequest {
    private static final long serialVersionUID = -5310654703953528640L;

    /*** 会员id ***/
    @NotBlank(message = "会员id不能为空")
    private String memberId;

    /*** 银行编号 ***/
    @NotBlank(message = "银行编号不能为空")
    private String bankCode;

    /*** 银行名称 ***/
    @NotBlank(message = "银行名称不能为空")
    private String bankName;

    /*** 支行名称 ***/
    private String bankBranch;

    /*** 银行行号，对公卡传支行行号，对私卡传总行行号 ***/
    @NotBlank(message = "银行行号不能为空")
    private String bankBranchNo;

    /*** 银行卡号 ***/
    @NotBlank(message = "银行卡号不能为空")
    private String bankAccountNo;

    /*** 户名 ***/
    @NotBlank(message = "户名不能为空")
    private String accountName;

    /*** 省份 ***/
    @NotBlank(message = "省份不能为空")
    private String province;

    /*** 城市 ***/
    @NotBlank(message = "城市不能为空")
    private String city;

    /*** 卡类型(DEBIT借记卡, CREDIT信用卡, PASSBOOK存折, OTHER其它) ***/
    @NotBlank(message = "卡类型不能为空")
    private String cardType;

    /*** 卡属性(C对私, B对公) ***/
    @NotBlank(message = "卡属性不能为空")
    private String cardAttribute;

    /*** 认证状态(0未认证 1已认证) ***/
    private String isVerified;

    /*** 协议绑定号 ***/
    private String signNum;

    /*** 扩展参数（供应链项目存储绑卡用户真实用户名） ***/
    private String extension;
}
