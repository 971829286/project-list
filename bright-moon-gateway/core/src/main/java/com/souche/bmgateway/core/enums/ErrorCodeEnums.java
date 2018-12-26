package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常code枚举
 *
 * @author zs.
 * Created on 18/7/18.
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnums {
	SYSTEM_ERROR("SYSTEM_ERROR", "系统内部错误"),
	RET_OBJECT_IS_NULL("RET_OBJECT_IS_NULL", "接口响应不能为空"),
	RET_PARAM_IS_NULL("RET_PARAM_IS_NULL", "接口响应参数不能为空"),
	DAO_OPERATION_ERROR("DAO_OPERATION_ERROR", "数据库操作异常"),
	INVOKE_OPEN_PLATFORM_ERROR("INVOKE_OPEN_PLATFORM_ERROR", "调用开放平台失败"),
	INVOKE_HTTP_ERROR("INVOKE_HTTP_ERROR", "调用HTTP接口失败"),
	MERCHANT_SETTLE_ERROR("MERCHANT_SETTLE_ERROR", "网商返回商户入驻失败"),
	QUERY_MER_RESULT_ERROR("QUERY_MER_RESULT_ERROR", "查询商户入驻结果失败"),
	ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "参数错误"),
	REPEAT_REQUEST_ERROR("REPEAT_REQUEST_ERROR", "重复的请求"),
	MEMBER_NOT_EXIST("MEMBER_NOT_EXIST", "会员不存在"),
	UMPAY_AUTH_ERROR("UMPAY_AUTH_ERROR", "联动实名失败"),
	MANAGER_SERVICE_ERROR("MANAGER_SERVICE_ERROR", "外部服务调用异常"),
	VOUCHER_SERVICE_ERROR("VOUCHER_SERVICE_ERROR", "记录凭证信息异常"),
	QUERY_SHOP_INFO_ERROR("QUERY_SHOP_INFO_ERROR", "查询商户信息异常"),
	QUERY_BANKCARD_INFO_ERROR("QUERY_BANKCARD_INFO_ERROR", "查询绑卡信息异常"),
	QUERY_ACCOUNT_OPEN_LIC_ERROR("QUERY_ACCOUNT_OPEN_LIC_ERROR", "查询开户许可证信息异常"),
	PAY_METHOD_ERROR("QUERY_BANKCARD_INFO_ERROR", "查询绑卡信息异常"),
	MERCHANT_NOT_SETTLE("MERCHANT_NOT_SETTLE", "商户未入驻"),
	VOUCHER_INFO_NOT_EXIST("VOUCHER_INFO_NOT_EXIST", "凭证信息不存在"),
	ILLEGAL_OUTER_TRADE_NO("ILLEGAL_OUTER_TRADE_NO", "非法的外部订单号");

	/*** code ***/
	private String code;

	/*** 异常信息 ***/
	private String message;
}
