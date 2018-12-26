package com.souche.bmgateway.model.request.member;

import com.souche.bmgateway.enums.ActivateStatus;
import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author luobing 2018/11/29
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CreatePersonalMemberRequest extends CommonBaseRequest {
    private static final long serialVersionUID = -1661737942138618328L;

    /*** 平台类型 用于区分同机构下的不用平台会员，例如车易拍机构下的（车牛，车易拍） ***/
    @NotBlank(message = "平台类型不能为空")
    private String platformType;

    /*** 会员名称 ***/
    private String memberName;


    /***************** 手机号和邮箱不全为空 *****************/
    /*** 手机号 ***/
    @Pattern(regexp = "^1[3-9][0-9]\\d{8}$", message = "手机号不合法")
    private String mobile;

    /*** 邮箱 ***/
    private String email;

    /*** 用户中心userId ***/
    private String uid;

    /*** 真实姓名 ***/
    private String realName;

    /*** 身份证号 ***/
    private String idCardNo;

    /*** 是否激活 ***/
    @NotNull
    private ActivateStatus isActive;

    /*** 是否实名认证通过 T：已认证，F：未认证，默认没有认证 ***/
    private String isVerify;

    /*** 扩展参数 ***/
    private String extention;

}