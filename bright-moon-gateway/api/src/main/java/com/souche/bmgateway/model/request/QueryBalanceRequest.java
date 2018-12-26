package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zs.
 *         Created on 18/7/17.
 */
@Setter
@Getter
@ToString
public class QueryBalanceRequest extends CommonBaseRequest {
    private static final long serialVersionUID = 7332892515030132220L;

    /*** 会员编号 ***/
    @NotBlank
    private String memberId;

    /*** 账户类型 ***/
    private String accountType;

    /*** 账户标识 用于区分相同账户类型的不同账户例如（区分冻结金中的消费贷冻结金，供应链冻结金） ***/
    private String accountIdentity;
}
