package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zs.
 *         Created on 18/11/19.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletUnFrozenRequest extends CommonBaseRequest {
	private static final long serialVersionUID = -2194365309343432883L;

	/*** 商户请求号 ***/
	@NotBlank
	private String outerTradeNo;

	/*** 原始商户网站唯一交易订单号 ***/
	@NotBlank
	private String origOuterTradeNo;

	/*** 产品码**/
	@NotBlank
	private String bizProductCode;

	/*** 金额**/
	@NotBlank
	private String unFrozenAmount;

	/*** 扩展参数 ***/
	private String extension;

	/*** 备注 ***/
	private String memo;

}
