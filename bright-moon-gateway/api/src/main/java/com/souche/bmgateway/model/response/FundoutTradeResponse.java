package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@Deprecated
public class FundoutTradeResponse  extends CommonResponse {
    private static final long serialVersionUID = -63905042374739428L;

    private String fundoutOrderNo;
    private String paymentOrderNo;
    private String memberId;

    @Override
    public String toString() {
        return "FundoutResponse{" +
                "tradeVoucherNo='" + fundoutOrderNo + '\'' +
                ", paymentVoucherNo='" + paymentOrderNo + '\'' +
                ", memberId='" + memberId + '\'' +
                "} " + super.toString();
    }
}
