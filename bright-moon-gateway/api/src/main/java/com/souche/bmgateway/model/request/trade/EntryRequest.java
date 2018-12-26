package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.enums.EntryTypeEnums;
import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 登帐请求参数
 *
 * @author yyx
 */
@Getter
@Setter
@ToString(callSuper = true)
public class EntryRequest extends CommonBaseRequest {
    private static final long serialVersionUID = -1597696332589453502L;

    /** 登帐请求号，非空 **/
    @NotBlank
    private String requestNo;

    /** 业务产品编码 **/
    @NotBlank
    private String bizProductCode;

    /*** 登帐类型 ***/
    @NotEmpty
    private EntryTypeEnums entryType;

    /** 借方会员ID: 支付系统的会员唯一标示 */
    private String drMemberId;

    /** 借方账号类型,在储值系统中存在的账号 **/
    private String drAccountType;

    /** 借记资金属性：CA-贷方资金 DA-借方资金 BI-借贷均可 **/
    private String drFundType;

    /*** 如果是内部账号，则用此 ***/
    private String drInnerAccount;


    /** 贷方会员ID **/
    private String crMemberId;

    /** 贷方账号 **/
    private String crAccountType;

    /** 贷记资金属性：CA-贷方资金 DA-借方资金 BI-借贷均可 **/
    private String crFundType;

    /*** 如果是内部账号，则用此 ***/
    private String crInnerAccount;


    /** 登帐金额 **/
    private String amount;

    /** 登帐备注信息 VARCHAR2(128)**/
    private String memo;

    /** 扩展信息: JSON字符串 **/
    private String extension;
}
