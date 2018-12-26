package com.souche.bmgateway.core.exception;

/**
 * 错误码常量类
 */
public class CommonDefinedException {

    public static final Exception LLPAY_DECRYPT_ERROR = new ErrorCodeException.CommonException(
            "LLPAY_DECRYPT_ERROR", "连连支付扩展参数解密错误");

    public static ErrorCodeException.CommonException SYSTEM_ERROR = new ErrorCodeException.CommonException(
            "SYSTEM_ERROR", "系统内部错误");

    public static ErrorCodeException.CommonException INPUT_CHARSET_ERROR = new ErrorCodeException.CommonException(
            "INPUT_CHARSET_ERROR", "编码类型错误");

    public static ErrorCodeException.CommonException REQUIRED_FIELD_NOT_EXIST = new ErrorCodeException.CommonException(
            "REQUIRED_FIELD_NOT_EXIST",
            "必填字段未填写");

    public static ErrorCodeException.CommonException FIELD_LENGTH_EXCEEDS_LIMIT = new ErrorCodeException.CommonException(
            "FIELD_LENGTH_EXCEEDS_LIMIT",
            "字段长度超过限制");

    public static ErrorCodeException.CommonException FIELD_TYPE_ERROR = new ErrorCodeException.CommonException(
            "FIELD_TYPE_ERROR", "字段类型错误");

    public static ErrorCodeException.CommonException ILLEGAL_SIGN_TYPE = new ErrorCodeException.CommonException(
            "ILLEGAL_SIGN_TYPE", "签名类型不正确");

    public static ErrorCodeException.CommonException ILLEGAL_SIGN = new ErrorCodeException.CommonException(
            "ILLEGAL_SIGN", "验签未通过");

    public static ErrorCodeException.CommonException ILLEGAL_ARGUMENT = new ErrorCodeException.CommonException(
            "ILLEGAL_ARGUMENT", "参数校验未通过");

    public static ErrorCodeException.CommonException ILLEGAL_SERVICE = new ErrorCodeException.CommonException(
            "ILLEGAL_SERVICE", "服务接口不存在");

    public static ErrorCodeException.CommonException ILLEGAL_ID_TYPE = new ErrorCodeException.CommonException(
            "ILLEGAL_ID_TYPE", "ID类型不支持");

    public static ErrorCodeException.CommonException USER_ACCOUNT_NOT_EXIST = new ErrorCodeException.CommonException(
            "USER_ACCOUNT_NOT_EXIST",
            "用户账号不存在");

    public static ErrorCodeException.CommonException MEMBER_ID_NOT_EXIST = new ErrorCodeException.CommonException(
            "MEMBER_ID_NOT_EXIST",
            "用户MemberId不存在");

    public static ErrorCodeException.CommonException PARTNER_ID_NOT_EXIST = new ErrorCodeException.CommonException(
            "PARTNER_ID_NOT_EXIST",
            "合作方Id不存在");

    public static ErrorCodeException.CommonException DUPLICATE_REQUEST_NO = new ErrorCodeException.CommonException(
            "DUPLICATE_REQUEST_NO", "重复的请求号");

    public static ErrorCodeException.CommonException ILLEGAL_OUTER_TRADE_NO = new ErrorCodeException.CommonException(
            "ILLEGAL_OUTER_TRADE_NO",
            "交易订单号不存在");

    public static ErrorCodeException.CommonException ILLEGAL_DATE_FORMAT = new ErrorCodeException.CommonException(
            "ILLEGAL_DATE_FORMAT", "日期格式错误");

    public static ErrorCodeException.CommonException PAY_METHOD_ERROR = new ErrorCodeException.CommonException(
            "ILLEGAL_PAY_ERROR", "支付方式错误");

    public static ErrorCodeException.CommonException ILLEGAL_PAY_METHOD = new ErrorCodeException.CommonException(
            "ILLEGAL_PAY_METHOD", "支付方式未授权");

    public static ErrorCodeException.CommonException OPERATOR_ID_NOT_EXIST = new ErrorCodeException.CommonException(
            "OPERATOR_ID_NOT_EXIST",
            "操作员Id不存在");

    public static ErrorCodeException.CommonException TRADE_NO_MATCH_ERROR = new ErrorCodeException.CommonException(
            "TRADE_NO_MATCH_ERROR",
            "交易号信息有误");

    public static ErrorCodeException.CommonException TRADE_DATA_MATCH_ERROR = new ErrorCodeException.CommonException(
            "TRADE_DATA_MATCH_ERROR",
            "交易信息有误");

    public static ErrorCodeException.CommonException PREPAY_DATA_MATCH_ERROR = new ErrorCodeException.CommonException(
            "PREPAY_DATA_MATCH_ERROR",
            "订金下订信息有误");

    public static ErrorCodeException.CommonException ILLEGAL_ROYALTY_PARAMETERS = new ErrorCodeException.CommonException(
            "ILLEGAL_ROYALTY_PARAMETERS",
            "分润账号集错误");

    public static ErrorCodeException.CommonException MOBILE_NOT_EXIST = new ErrorCodeException.CommonException(
            "MOBILE_NOT_EXIST", "用户手机号不存在");

    public static ErrorCodeException.CommonException TRADE_AMOUNT_MATCH_ERROR = new ErrorCodeException.CommonException(
            "TRADE_AMOUNT_MATCH_ERROR",
            "交易内金额不匹配");

    public static ErrorCodeException.CommonException TRADE_PAY_MATCH_ERROR = new ErrorCodeException.CommonException(
            "TRADE_PAY_MATCH_ERROR",
            "交易与支付金额不匹配");

    public static ErrorCodeException.CommonException ILLEGAL_PREPAY_NO = new ErrorCodeException.CommonException(
            "ILLEGAL_PREPAY_NO", "订金下订单号错误");

    public static ErrorCodeException.CommonException ILLEGAL_ACCESS_SWITCH_SYSTEM = new ErrorCodeException.CommonException(
            "ILLEGAL_ACCESS_SWITCH_SYSTEM",
            "商户不允许访问该类型的接口");

    public static ErrorCodeException.CommonException ILLEGAL_REFUND_AMOUNT = new ErrorCodeException.CommonException(
            "ILLEGAL_REFUND_AMOUNT",
            "退款金额信息错误");

    public static ErrorCodeException.CommonException ILLEGAL_REQUEST = new ErrorCodeException.CommonException(
            "ILLEGAL_REQUEST", "风控未通过");

    public static ErrorCodeException.CommonException ILLEGAL_AMOUNT_FORMAT = new ErrorCodeException.CommonException(
            "ILLEGAL_AMOUNT_FORMAT",
            "金额格式错误");

    public static ErrorCodeException.CommonException ILLEGAL_ENSURE_AMOUNT = new ErrorCodeException.CommonException(
            "ILLEGAL_ENSURE_AMOUNT",
            "担保金额信息错误");

    public static ErrorCodeException.CommonException ILLEGAL_TIME_INTERVAL = new ErrorCodeException.CommonException(
            "ILLEGAL_TIME_INTERVAL",
            "时间区间错误");

    public static ErrorCodeException.CommonException CREATE_PERSON_MEMBER_ERROR = new ErrorCodeException.CommonException(
            "CREATE_PERSON_MEMBER_ERROR",
            "交易中创建个人会员失败");

    public static ErrorCodeException.CommonException CREATE_ACCOUNT_ERROR = new ErrorCodeException.CommonException(
            "CREATE_ACCOUNT_ERROR",
            "交易中创建账户失败");

    public static ErrorCodeException.CommonException QUERY_MEMBER_ERROR = new ErrorCodeException.CommonException(
            "QUERY_MEMBER_ERROR", "查询会员信息失败");

    public static ErrorCodeException.CommonException ACCOUNT_TYPE_NOT_SUPPORTED = new ErrorCodeException.CommonException(
            "ACCOUNT_TYPE_NOT_SUPPORTED",
            "账户类型不支持");

    public static ErrorCodeException.CommonException TRADE_TYPE_NOT_SUPPORTED = new ErrorCodeException.CommonException(
            "TRADE_TYPE_NOT_SUPPORTED",
            "交易类型不支持");

    public static ErrorCodeException.CommonException ILLEGAL_FUND_TYPE = new ErrorCodeException.CommonException(
            "ILLEGAL_FUND_TYPE", "资金类型不支持");

    public static ErrorCodeException.CommonException TOTAL_AMOUNT_MATCH_ERROR = new ErrorCodeException.CommonException(
            "TOTAL_AMOUNT_MATCH_ERROR", "赔偿订金金额+交易分润金额必须等于原下订金额");

    public static ErrorCodeException.CommonException TRANSFER_TYPE_NOT_SUPPORTED = new ErrorCodeException.CommonException(
            "TRANSFER_TYPE_NOT_SUPPORTED", "转账类型不支持");

    public static ErrorCodeException.CommonException CARD_ATTRIBUTE_NOT_SUPPORTED = new ErrorCodeException.CommonException(
            "CARD_ATTRIBUTE_NOT_SUPPORTED", "卡属性只支持 B对公，C对私");

    public static ErrorCodeException.CommonException CARD_TYPE_NOT_SUPPORTED = new ErrorCodeException.CommonException(
            "CARD_TYPE_NOT_SUPPORTED", "卡类型只支持 DEBIT借记，CREDIT贷记");

    public static ErrorCodeException.CommonException ACCOUNT_NAME_DECRYPT_ERROR = new ErrorCodeException.CommonException(
            "ACCOUNT_NAME_DECRYPT_ERROR", "银行卡账户名解密失败");

    public static ErrorCodeException.CommonException CARD_NO_DECRYPT_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_DECRYPT_ERROR", "银行卡号解密失败");

    public static ErrorCodeException.CommonException IS_WEB_ACCESS_NOT_SUPPORTED = new ErrorCodeException.CommonException(
            "IS_WEB_ACCESS_NOT_SUPPORTED", "是否是网站访问只支持Y是，N否");

    public static ErrorCodeException.CommonException CARD_NO_VERIFY_FAIL_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_FAIL_NO_MATCH_ERROR", "卡号，身份证，姓名接口调用异常");

    public static ErrorCodeException.CommonException CARD_NO_VERIFY_FAIL_EMPTY_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_FAIL_NO_MATCH_ERROR", "缺少必填信息");

    public static ErrorCodeException.CommonException CARD_NO_VERIFY_FAIL_NO_MATCH_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_FAIL_NO_MATCH_ERROR", "验证不一致（卡号，身份证，姓名关联至少有一处不一致）");

    public static ErrorCodeException.CommonException CARD_NO_NAME_VERIFY_FAIL_NO_MATCH_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_NAME_VERIFY_FAIL_NO_MATCH_ERROR", "验证不一致（卡号， 姓名关联不一致）");

    public static ErrorCodeException.CommonException CARD_NO_VERIFY_FAIL_MATCH_IDCODE_NOTSURE = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_FAIL_MATCH_IDCODE_NOTSURE", "核查一致，但是身份验证不确定");

    public static ErrorCodeException.CommonException CARD_NO_VERIFY_FAIL_NO_MATCH_IDCODE_FAIL = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_FAIL_NO_MATCH_IDCODE_FAIL", "核查一致，但是身份验证失败");

    public static ErrorCodeException.CommonException CARD_NO_VERIFY_FAIL_WJ_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_FAIL_WJ_ERROR", "身份证和传的身份证不同");

    public static ErrorCodeException.CommonException CARD_NO_VERIFY_FAIL_WJ_EMPTY_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_FAIL_WJ_ERROR", "查询不到用户");

    public static ErrorCodeException.CommonException CARD_NO_VERIFY_FAIL_OTHER_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_FAIL_OTHER_ERROR", "其他错误");

    public static ErrorCodeException.CommonException CARD_NO_VERIFY_UPDATE_LEGALNAME_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_UPDATE_LEGALNAME_ERROR", "更新企业法人姓名异常");

    public static ErrorCodeException.CommonException CARD_NO_VERIFY_FAIL_IOSBUG_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_FAIL_IOSBUG_ERROR", "卡号姓名关联不一致，请联系车牛工作人员");

    public static ErrorCodeException.CommonException ROUTE_ERROR = new ErrorCodeException.CommonException(
            "CARD_NO_VERIFY_FAIL_IOSBUG_ERROR", "系统内部异常");

    public static ErrorCodeException.CommonException TASK_CONFIG_ERROR = new ErrorCodeException.CommonException(
            "TASK_CONFIG_ERROR", "任务配置异常");

    public static ErrorCodeException.CommonException SAVE_SHARE_APPLY_ERROR = new ErrorCodeException.CommonException(
            "SAVE_SHARE_APPLY_ERROR", "保存分账申请信息失败");

}

