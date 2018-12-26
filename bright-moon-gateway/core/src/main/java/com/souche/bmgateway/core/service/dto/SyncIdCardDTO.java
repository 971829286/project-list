package com.souche.bmgateway.core.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs.
 * Created on 2018-12-18.
 */
@Setter
@Getter
@ToString(callSuper = true)
public class SyncIdCardDTO extends WalletBaseDTO {
    private static final long serialVersionUID = -3395405941044688685L;
    
    /*** 钱包id ***/
    private String memberId;

    /*** 身份证号 ***/
    private String idCard;
}
