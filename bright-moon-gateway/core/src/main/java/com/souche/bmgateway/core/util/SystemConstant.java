package com.souche.bmgateway.core.util;

/**
 * <p>常量容器</p>
 *
 * @author xuxizun
 */
public interface SystemConstant {
    /**
     * 编码
     */
    String ENCODE = "UTF-8";

    String ISO_ENCODE = "ISO-8859-1";
    /**
     * 外部系统编码
     */
    String EXTRA_ENCODE = "GB2312";
    /**
     * 文件相关编码
     */
    String FILE_ENCODE = "GBK";
    /**
     * 占位符前缀
     */
    String PLACEHOLDER_PREFIX = "$";
    /**
     * 系统用户
     */
    String SYSTEM_OPERATOR = "system";

    /**
     * 默认订单号，在订单号为空情况使用
     */
    String DEFAULT_BIZ_NO = "-1";
    /**
     * 密码加密盐值，需与security配置保持一致
     */
    String ENCODE_SALT = null;
    /**
     * 信息目录名称
     */
    String META_INFO = "META-INF";
    /**
     * 目录名称
     */
    String REPORT = "report";

    /**
     * 超级用户角色
     */
    String SUPER_USER_ROLE_CODE = "ADMIN_ROLE";
    /**
     * 默认用户角色
     */
    String DEFUALT_USER_ROLE_CODE = "USER_ROLE";
    /**
     * 默认用户权限
     */
    String DEFUALT_USER_PERMISSION_KEY = "ROLE_USER";
    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "888888";

    /**
     * 成功字符串标识
     */
    String TRUE_STRING = "Y";
    /**
     * 失败字符串标识
     */
    String FALSE_STRING = "N";
    /**
     * 成功数字标识
     */
    int TRUE_INT = 1;
    /**
     * 失败数字标识
     */
    int FASLE_INT = 0;
    /**
     * 系统编号
     */
    String SOURCE_CODE = "0458";
    /**
     * 新系统标志
     */
    String NEW_SYSTEM = "NEW";

    //对账操作员备注
    int COMPARE_MEMO = 601;

    //汇总确认操作员备注
    int COMFIRE_MEMO = 602;

    /**
     * 资金对账批次号，用资金对账余额调节提交审核时保存在审核参数里
     */
    String PAC_FUNDS_BATCH_ID = "pacFundsBatchId";

    int DEFAULT_PAGE_SIZE = 20;
}
