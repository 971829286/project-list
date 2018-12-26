package com.souche.bmgateway.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @Author: huangbin
 * @Description: 资金明细
 * @Date: Created in 2018/07/24
 * @Modified By:
 */
@Getter
@Setter
@ToString
public class FundDetail implements Serializable {

    @NotBlank(message = "额度不能为空")
    @Length(max = 15, message = "额度长度最大为15")
    private String amount;

    @NotBlank(message = "币种，默认CNY不能为空")
    @Length(max = 3, message = "币种，默认CNY长度最大为3")
    private String currency;

    @NotBlank(message = "参与方id不能为空")
    @Length(max = 64, message = "参与方id长度最大为64")
    private String participantId;

    @NotBlank(message = "参与方类型不能为空")
    @Length(max = 16, message = "参与方类型长度最大为16")
    private String participantType;

    @Length(max = 32, message = "用途长度最大为64")
    private String purpose;

    @Length(max = 256, message = "扩展信息长度最大为256")
    private String extInfo;

    @Length(max = 256, message = "明细备注长度最大为256")
    private String remark;

}
