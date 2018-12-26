package com.souche.bmgateway.core.enums;

import com.netfinworks.dts.service.facade.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: huangbin
 * @Description: 交易类型转换
 * @Date: Created in 2018/05/16
 * @Modified By:
 */
@Getter
@AllArgsConstructor
public enum BusinessTypeConver {

    TRADE("trade_status_sync","trade_status","TRADE_FINISHED",BusinessType.INSTANTTRADE, "交易"),
    REFUND("refund_status_sync", "refund_status","REFUND_SUCCESS",BusinessType.REFUND, "退款"),
    DEPOSIT("deposit_status_sync","deposit_status","DEPOSIT_SUCCESS",BusinessType.DEPOSIT, "充值"),
    TRANSFER("transfer_status_sync", "transfer_status","TRANSFER_SUCCESS",BusinessType.TRANSFER, "转账"),
    AGENTDEDUCT("AGENTDEDUCT","","", BusinessType.INSTANTTRADE, "代扣");

    private String code;
    private String statusParam;
    private String successStatus;
    private BusinessType businessType;
    private String message;

    public static BusinessTypeConver getBusinessTypeByCode(String code) {
        for (BusinessTypeConver businessTypeConver : BusinessTypeConver.values()) {
            if (businessTypeConver.getCode().equals(code)) {
                return businessTypeConver;
            }
        }
        return null;
    }
}

