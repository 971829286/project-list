package com.souche.bmgateway.model.request;

import com.souche.bmgateway.model.FundDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: huangbin
 * @Description: 分帐请求
 * @Date: Created in 2018/07/09
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class OrderShareApplyRequest extends CommonBaseRequest {

    @NotBlank(message = "外部订单分账请求流水号不能为空")
    @Length(max = 64, message = "外部订单分账请求流水号长度最大为64")
    private String outTradeNo;

    @NotBlank(message = "收款方商户号不能为空")
    @Length(max = 64, message = "收款方商户号长度最大为64")
    private String merchantId;

    /**
     * 支付订单号
     */
    private String paymentOrderNo;

    /**
     * 会员ID
     */
    private String memberId;

    /**
     * 扩展参数
     */
    private String extension;

    /**
     * 天猫订单号
     */
    private String tmOrderNo;

    @NotBlank(message = "关联网商订单号不能为空")
    @Length(max = 64, message = "关联网商订单号长度最大为64")
    private String relateOrderNo;

    @NotBlank(message = "订单金额(金额为分)不能为空")
    @Length(max = 45, message = "订单金额(金额为分)长度最大为45")
    private String totalAmount;

    @NotBlank(message = "币种，默认CNY不能为空")
    @Length(max = 3, message = "币种，默认CNY长度最大为3")
    private String currency;

    @Valid
    private List<FundDetail> payerFundDetails;

    @Valid
    private List<FundDetail> payeeFundDetails;

    @Length(max = 256, message = "扩展信息长度最大为256")
    private String extInfo;

    @Length(max = 256, message = "备注长度最大为256")
    private String memo;
}
