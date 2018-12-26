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
public class MemberSimpleDTO extends WalletBaseDTO {
    private static final long serialVersionUID = 1562636928110237124L;

    /*** 钱包id ***/
    private String memberId;
}
