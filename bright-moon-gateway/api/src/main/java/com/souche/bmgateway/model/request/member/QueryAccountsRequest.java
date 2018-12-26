package com.souche.bmgateway.model.request.member;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zs.
 *         Created on 18/11/20.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class QueryAccountsRequest extends CommonBaseRequest {

    /*** 钱包id ***/
    @NotBlank
    private String memberId;

    /*** 账户类型 只能传一个账户类型 ***/
    private String accountType;

    /*** 账户标识 ***/
    private String memberIdentity;
}
