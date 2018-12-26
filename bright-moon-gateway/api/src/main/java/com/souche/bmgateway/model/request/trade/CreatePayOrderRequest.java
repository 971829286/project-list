package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author zs.
 *         Created on 18/11/19.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CreatePayOrderRequest extends CommonBaseRequest {

    /*** 买家ID 没有则传anymous ***/
    @NotBlank
    private String buyerId;

    /*** 用户在商户平台下单时候的ip地址 ***/
    private String buyerIp;

    /*** 客户端名称 ***/
    private String appName;


    /**************************************** 交易类目 ***********************************/

    /*** 外部订单号 ***/
    @NotBlank
    private String outerTradeNo;

    /*** 业务订单号 ***/
    @NotBlank
    private String bizOrderNo;

    /*** 业务产品编码 ***/
    @NotBlank(message = "业务产品编码不能为空")
    @Size(min = 5, max = 32, message = "业务产品编码长度校验失败，要求5-32位")
    private String bizProductCode;

    /*** 产品标题 ***/
    @NotBlank
    private String subject;

    /*** 产品描述信息 ***/
    @Size(min = 0, max = 500, message = "产品描述长度校验失败，要求0-500位")
    private String productDesc;

    /*** 产品展现URL ***/
    @Size(min = 0, max = 256, message = "商品展示URL长度校验失败，要求0-500位")
    private String showUrl;

    /*** 交易金额 ***/
    @NotBlank(message = "交易金额不能为空")
    private String tradeAmount;

    /*** 金币金额 ***/
    private String coinAmount;

    /*** 分账方信息 ***/
    @Valid
    private List<SplitInfo> splitInfoList;


    //可以支持业务不传卖家id和卖家账户类型，根据业务产品编码去查配置
    /*** 卖家id ***/
    private String sellerId;

    /*** 卖家账户类型 如果账户类型不传则默认进基本户 ***/
    private String sellerAccountType;


    /*** 交易过期时间 ***/
    private Date expirationTime;

    /*** 扩展参数 ***/
    private String extension;


    /**************************************** 交易类目 ***********************************/

    /*** 服务器异步通知地址 用于通过GatewayAccessInfo传入pns，如果不传，则不会通知，业务系统也可以监听MQ获取交易结果 ***/
    private String notifyUrl;

}
