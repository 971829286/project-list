package com.souche.bmgateway.model.response.member;

import com.souche.bmgateway.model.response.CommonResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 用户账户信息
 *
 * @author zs.
 *         Created on 18/11/20.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class QueryAccountsResponse extends CommonResponse {
    
    /*** 钱包id ***/
    private String memberId;

    /*** 账户列表 ***/
    List<WalletAccountInfo> walletAccountInfoList;
}
