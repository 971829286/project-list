package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author zs.
 *         Created on 18/7/17.
 */
@Getter
@Setter
@ToString
public class BankAccountListResponse extends CommonResponse {
    private static final long serialVersionUID = 8860331099524556792L;

    /*** 银行卡列表 ***/
    private List<BankAccountRecordInfo> bankAccountRecordInfoList;

}
