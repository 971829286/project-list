package com.souche.bmgateway.model.request;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * 修改企业会员请求参数
 * @author zs.
 *         Created on 18/8/6.
 */
@Getter
@Setter
@ToString
public class ModifyEnterpriseInfoRequest extends CommonBaseRequest {

    /*** 钱包id ***/
    @NotBlank(message = "会员id不能为空")
    private String memberId;

    /*** 企业名称 ***/
    private String enterpriseName;

    /*** 会员名称、企业简称 ***/
    private String memberName;

    /*** 企业法人 ***/
    private String legalPerson;

    /*** 法人手机号码 ***/
    private String legalPersonPhone;

    /*** 企业网址 ***/
    private String website;

    /*** 企业地址 ***/
    private String address;

    /*** 执照号 ***/
    private String licenseNo;

    /*** 营业执照所在地 ***/
    private String licenseAddress;

    /*** 营业执照过期日 ***/
    private Date licenseExpireDate;

    /*** 营业范围 ***/
    private String businessScope;

    /*** 联系电话 ***/
    private String telephone;

    /*** 组织机构代码 ***/
    private String organizationNo;

    /*** 企业简介 ***/
    private String summary;
}
