package com.souche.bmgateway.model.request.trade;


import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 转账请求参数
 *
 * @author zs
 */
@Setter
@Getter
@ToString(callSuper = true)
public class TransferAccountRequest extends CommonBaseRequest {

    /*** 外部请求号 ***/
    @NotBlank
    private String outerTradeNo;

    /*** 业务订单号 ***/
    @NotBlank
    private String bizOrderNo;

    /*** 产品码 ***/
    @NotBlank
    private String bizProductCode;


    /********************** 转账双方可以不传，通过业务产品码匹配 **********************/
    /*** 入款会员id ***/
    private String fundinMemberId;

    /*** 入款会员账户类型 ***/
    private String fundinAccoutType;

    /*** 入款账户标识 ***/
    private String fundinIdentityNo;

    /*** 出款会员id ***/
    private String fundoutMemberId;

    /*** 出款会员账户类型 ***/
    private String fundoutAccoutType;

    /*** 出款账户标识 ***/
    private String fundoutIdentityNo;



    /*** 转账金额 ***/
    @NotBlank
    private String transferAmount;

    /*** 服务器异步通知地址 可以为空，为空的话则不会接收最终的交易结果异步通知或通过监听MQ来获取异步结果 ***/
    private String notifyUrl;

    /*** 转账备注 ***/
    private String tradeMemo;

    /*** 扩展参数 ***/
    private String extension;
}
