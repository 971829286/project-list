package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 查询用户信息
 *
 * @author zs.
 *         Created on 18/7/31.
 */
@Getter
@Setter
@ToString
public class QueryWalletMemberInfoRequest extends CommonBaseRequest {
    private static final long serialVersionUID = -5387224958905953454L;

    /*** 会员id ***/
    @NotBlank(message = "会员id不能为空")
    private String memberId;
}
