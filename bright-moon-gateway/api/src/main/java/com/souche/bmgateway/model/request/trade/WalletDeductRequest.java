package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

/**
 * 扣款请求参数
 * 与转账的区别就是该接口转账前会先查询转出方钱包余额，有多少扣多少
 *
 *
 * @author zs.
 *         Created on 18/11/19.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletDeductRequest extends CommonBaseRequest {
    
    @Valid
    @NotEmpty
    private TransferAccountRequest transferAccountRequest;

}
