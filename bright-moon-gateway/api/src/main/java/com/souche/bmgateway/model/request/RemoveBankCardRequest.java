package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;


@Getter
@Setter
@ToString
public class RemoveBankCardRequest implements Serializable {
    private static final long serialVersionUID = -5310654703953528648L;

    /*** 会员id ***/
    @NotBlank(message = "会员id不能为空")
    private String memberId;

    /*** 银行卡Id ***/
    @NotBlank(message = "银行卡Id")
    private String bankcardId;

}
