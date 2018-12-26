package com.souche.bmgateway.model.response.trade;

import com.souche.bmgateway.model.response.CommonResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs. Created on 18/11/19.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WalletDepositResponse extends CommonResponse {
	
	/*** 网银跳转表单数据 ***/
	private String formContent;
	
	/*** 机构订单号 ***/
	private String instOrderNo;
	
	/*** 统一凭证号 ***/
	private String voucherNo;
	
	/*** 扩展信息 ***/
	private String extension;

}
