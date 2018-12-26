package com.souche.bmgateway.model.response.member;

import com.souche.bmgateway.model.response.CommonResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs.
 * Created on 2018-12-14.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class QueryWitnessInfoResponse extends CommonResponse {

    /*** 钱包id ***/
    private String memberId;
    
    /*** 用户收款子账户 前缀 + 用户子账户 ***/
    private String receiveSubAccount;
}
