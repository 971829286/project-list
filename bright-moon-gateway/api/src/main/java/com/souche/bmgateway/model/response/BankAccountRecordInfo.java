package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 卡信息
 * @author zs.
 *         Created on 18/7/17.
 */
@Getter
@Setter
@ToString
public class BankAccountRecordInfo implements Serializable {
    private static final long serialVersionUID = 3558325792180587752L;

    private String bankcardId;
    private String memberId;
    private String bankCode;
    private String bankName;
    private String bankBranch;
    private String bankAccountNumMask;
    private String realName;
    private String province;
    private String city;
    private Integer cardType;
    private Integer cardAttribute;
    private Integer isVerified;
    private String alias;
    private String cardSkin;
    private String isSigning;
    private Integer status;
    private String extention;
    private String payAttribute;
    private String certType;
    private String certNum;
    private String mobileNum;
    private Date activateDate;
    private String channelCode;
    private String branchNo;
    private Integer accountRoleType;
}
