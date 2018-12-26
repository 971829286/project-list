package com.souche.bmgateway.core.dto.mq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BillChargeInfo {

    private String serialNo;

    private String fee;

    public BillChargeInfo(String serialNo, String fee) {
        this.serialNo = serialNo;
        this.fee = fee;
    }
}
