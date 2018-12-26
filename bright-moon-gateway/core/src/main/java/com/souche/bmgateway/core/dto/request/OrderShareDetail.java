package com.souche.bmgateway.core.dto.request;

import com.souche.bmgateway.model.FundDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: huangbin
 * @Description: 分账记录业务明细表
 * @Date: Created in 2018/07/17
 * @Modified By:
 */
@Setter
@Getter
@ToString
public class OrderShareDetail {

    private String OutTradeNo;

    private String paymentOrderNo;

    private String tmOrderNo;

    private String ReqMsgId;

    private String MerchantId;

    private String MemberId;

    private String RelateOrderNo;

    private String TotalAmount;

    private String Currency;

    private List<FundDetail> PayerFundDetails;

    private List<FundDetail> PayeeFundDetails;

    private String ExtInfo;

    private String Memo;

    private String Extension;

}
