package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 商户入驻申请
 *
 * @author: chenwj
 * @since: 2018/7/18
 */
@Getter
@Setter
@ToString
public class MerchantSettledRequest extends CommonBaseRequest {

    @NotBlank(message = "会员号不能为空")
    @Length(max = 32, message = "钱包id超长")
    private String memberId;

    @NotBlank(message = "店铺code不能为空")
    @Length(max = 32, message = "店铺code超长")
    private String shopCode;

    @NotBlank(message = "商户在天猫的门店id不能为空")
    @Length(max = 64, message = "商户在天猫的门店id超长")
    private String storeId;

    @NotBlank(message = "入驻结果回调地址")
    private String callbackUrl;

    /**
     * 短信验证码（预留字段，以后迭代需要）
     */
    private String autoCode;

}
