package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 创建企业会员请求参数
 *
 * @author zs.
 *         Created on 18/7/12.
 */
@Getter
@Setter
@ToString
public class CreateEnterpriseMemberRequest extends CommonBaseRequest {

    private static final long serialVersionUID = 2382484591600441582L;

    /*** 平台类型 用于区分同机构下的不用平台会员，例如车易拍机构下的（车牛，车易拍）,默认LOGIN_NAME兼容 ***/
    @NotBlank(message = "平台类型不能为空")
    private String platformType = "3";

    /*** 登录名 ***/
    @NotBlank(message = "登录名不能为空")
    @Length(max = 50, message = "登录名超长")
    private String loginName;

    /*** 用户中心userId, 可以为空 ***/
    @Length(max = 32, message = "uid超长")
    private String uid;

    /*** 企业名称 ***/
    @Length(max = 100, message = "企业名称超长")
    private String enterpriseName;

    /*** 会员名称、企业简称 ***/
    @Length(max = 25, message = "会员名称、企业简称超长")
    private String memberName;

    /*** 企业法人 ***/
    @Length(max = 25, message = "企业法人超长")
    private String legalPerson;

    /*** 法人手机号码 ***/
    @Pattern(regexp = "^1[3-9][0-9]\\d{8}$", message = "法人手机号不合法")
    private String legalPersonPhone;

    /*** 企业网址 ***/
    @Length(max = 64, message = "企业网址超长")
    private String website;

    /*** 企业地址 ***/
    @Length(max = 100, message = "企业地址超超长")
    private String address;

    /*** 执照号 ***/
    @Length(max = 128, message = "执照号超长")
    private String licenseNo;

    /*** 营业执照所在地 ***/
    @Length(max = 100, message = "营业执照所在地超长")
    private String licenseAddress;

    /*** 营业执照过期日 ***/
    private Date licenseExpireDate;

    /*** 营业范围 ***/
    @Length(max = 512, message = "营业范围超长")
    private String businessScope;

    /*** 联系电话 ***/
    @Pattern(regexp = "^1[3-9][0-9]\\d{8}$", message = "联系电话不合法")
    private String telephone;

    /*** 组织机构代码 ***/
    @Length(max = 128, message = "组织机构代码超长")
    private String organizationNo;

    /*** 企业简介 ***/
    @Length(max = 512, message = "企业简介超长")
    private String summary;

    /*** 扩展参数 ***/
    private String extension;
}
