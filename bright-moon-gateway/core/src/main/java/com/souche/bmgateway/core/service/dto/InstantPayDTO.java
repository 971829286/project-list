package com.souche.bmgateway.core.service.dto;

import com.netfinworks.tradeservice.facade.model.paymethod.PayMethod;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author zs.
 *         Created on 18/11/23.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstantPayDTO extends WalletBaseDTO {
    private static final long serialVersionUID = -4210012253359698092L;

    private String tradeVoucherNo;
    private String paymentVoucherNo;
    private String paymentSourceVoucherNo;
    private String bizProductCode;
    private String buyerAccountNo;
    private List<PayMethod> payMethodList;
    private String buyerId;
    private String accessChannel;
    private Date gmtSubmit;
    private String extension;
}
