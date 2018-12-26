package com.souche.bmgateway.core.constant;

/**
 * 数据字典.
 *
 * @author luoanan
 */
public class Constants {
    // 是否入驻成功
    public static class SettledFlag {
        // 0：未入驻
        public static final String NO = "0";
        // 1: 入驻成功
        public static final String YES = "1";
    }

    // 文件用途
    public static class Purpose {
        // 身份证正面
        public static final String CETIFICATION = "/FILE_PURPOSE_CETIFICATION/";
        // 身份证反面
        public static final String CETIFICATION_BACK = "/FILE_PURPOSE_CETIFICATION_BACK/";
        // 营业执照
        public static final String BUSINESS_LICENSE = "/BUSINESS_LICENSE/";
        // 开户许可证
        public static final String ACCOUNT_OPEN_LICENSE = "/ACCOUNT_OPEN_LICENSE/";
    }

    public static final String MYBANK_UPLOAD_PIC = "ant.mybank.merchantprod.merchant.uploadphoto";

    public static final String MYBANK_MERCHANT_RESULT = "ant.mybank.merchantprod.merchant.register.query";

    public static final String MYBANK_MERCHANT_REGISTER = "ant.mybank.merchantprod.merchant.unified.register";

    public static final String MYBANK_MODIFY_MERCHANT = "ant.mybank.merchantprod.merchant.unified.updateMerchant";

    public static final String SITE_TYPE = "01";

    public static final String SITE_URL = "http://hmc-shop.prepub.cgacar.com/index.html";

    public static final String FEE_VALUE = "0.0055";

    public static final String ACCOUNT_TYPE = "02";

    public static final String MERCHANT_TYPE = "03";

    public static final String DEAL_TYPE = "03";

    public static final String NOT_SUPPORT = "N";

    public static final String SETTLE_MODE = "05";

    public static final String MCC = "2016062900190124";

    public static final String ONLINE_MCC = "5531";

    public static final String ONLINE_MCC_NEW = "7957";

    public static final String PARTNER_TYPE = "03";

    public static final String ZERO = "0";

    public static final String BANK_ACCOUNT_NO = "bankAccountNumTiket";

    public static final String SUCCESS_CODE = "200";

    public static final String PHOTO_URL = "PhotoUrl";

    public static final String RESULT_STATUS = "ResultStatus";

    public static final String REGISTER_STATUS = "RegisterStatus";

    public static final String RESULT_CODE = "ResultCode";

    public static final String MYBANK_SUCCESS_CODE = "0000";

    public static final String MYBANK_SUCCESS_FLAG = "S";

    public static final String QUERY_MER_RESULT_FAIL = "2";

    public static final String FAIL_REASON = "FailReason";

    public static final String QUERY_MER_RESULT_SUCCESS = "1";

    /*** 联动 ***/
    public static final String SERVICE = "comm_auth";
    public static final String CHARSET = "utf-8";
    public static final String SIGN_TYPE = "RSA";
    public static final String VERSION = "1.0";
    public static final String AUTH_TYPE = "1";
    public static final String AUTH_MODE = "3";
    public static final String UMPAY_SUCCESS_CODE = "0000";

    public static final String ANYMOUS = "anymous";

    public static final String SUCCESS_RETURN_CODE = "S0001";

    public static final String PAYMENT_CODE = "1001";

    public static final String KEY_BANK_CODE = "BANK_CODE";

    public static final String KEY_COMPANY_OR_PERSONAL = "COMPANY_OR_PERSONAL";

    public static final String KEY_DBCR = "DBCR";

    public static final String ACCESS_CHANNEL = "WEB";

    /*** 车易拍收款子账户前缀 ***/ //TODO 待开户
    public static final String RECEIVE_ACCOUNT_PREFIX = "5555018457986162";

    public static final String PARTNER_ID = "partnerId";

    public static final String DEFAULT_PARTNER_ID = "188888888888";

    public static final String ACCT_IDENTITY = "acctIdentity";
}
