package com.souche.bmgateway.core.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs.
 * Created on 2018-12-19.
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TradeQueryDTO extends WalletBaseDTO {
    private static final long serialVersionUID = 4253690441613578991L;

    /*** 交易凭证号 ***/
    private String tradeVoucherNo;
}
