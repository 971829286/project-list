package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zs.
 *         Created on 18/7/12.
 */
@Getter
@Setter
public class WalletMemberResponse extends CommonResponse {
    private static final long serialVersionUID = 8453368179537685096L;

    /*** 会员id ***/
    private String memberId;

}
