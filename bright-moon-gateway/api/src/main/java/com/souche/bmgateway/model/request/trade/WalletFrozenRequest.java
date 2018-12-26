package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/*** 
 * @author zs.
 *         Created on 18/11/19.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletFrozenRequest extends CommonBaseRequest {

    /*** 商户订单号 ***/
    @NotBlank
    private String outerTradeNo;

    /*** 产品编码 ***/
    @NotBlank
    private String bizProductCode;

    /*** 钱包id ***/
    @NotBlank
    private String memberId;

    /*** 账户类型 ***/
    @NotBlank
    private String accountType;

    /*** 账户标识 ***/
    private String accountIdentity;

    /*** 冻结金额 ***/
    @NotBlank
    private String freezeAmount;

    /*** 扩展参数 ***/
    private String extension;

    /*** 备注 ***/
    private String memo;
}
