package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 商户入驻结果查询
 *
 * @author: chenwj
 * @since: 2018/7/24
 */
@Getter
@Setter
@ToString
public class MerchantQueryRequest extends CommonBaseRequest {

    @NotBlank(message = "会员号不能为空")
    @Length(max = 32, message = "钱包id超长")
    private String memberId;

}
