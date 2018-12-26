package com.souche.bmgateway.core.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs.
 * Created on 2018-12-18.
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountSimpleDTO extends WalletBaseDTO {
    private static final long serialVersionUID = -1249679471006545793L;
    
    /*** 账号 ***/
    private String accountNo;
}
