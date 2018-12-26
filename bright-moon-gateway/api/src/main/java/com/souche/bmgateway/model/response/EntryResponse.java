package com.souche.bmgateway.model.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yyx
 */
@Getter
@Setter
@Deprecated
public class EntryResponse  extends CommonResponse {
    private static final long serialVersionUID = 1L;
    private String bizPaymentSeqNo;
    private String bizPaymentState;
    private String bizSubState;

    @Override
    public String toString() {
        return "EntryResponse{" +
                "bizPaymentSeqNo='" + bizPaymentSeqNo + '\'' +
                ", bizPaymentState='" + bizPaymentState + '\'' +
                ", bizSubState='" + bizSubState + '\'' +
                "} " + super.toString();
    }
}
