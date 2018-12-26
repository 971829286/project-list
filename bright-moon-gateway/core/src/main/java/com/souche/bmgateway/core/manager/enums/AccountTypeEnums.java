package com.souche.bmgateway.core.manager.enums;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 账户类型
 *
 * @author zs.
 *         Created on 18/7/19.
 */
@Getter
public enum AccountTypeEnums {
    //个人基本户
    PERSON_BASE_ACCOUNT(101L, "101", true),
    //企业基本户
    ENTERPRISE_BASE_ACCOUNT(201L, "201", true),
    //企业保证金户
    ENTERPRISE_PREPARE_ACCOUNT(202L, "202", true),
    //企业冻结户
    ENTERPRISE_GOLD_ACCOUNT(203L, "203", false),
    //企业在途户
    ENTERPRISE_ENSURE_ACCOUNT(204L, "204", false);

    /*** 账户类型 ***/
    private long code;

    /*** 账户描述 ***/
    private String msg;

    /*** needToActive为true时，代表该账户在会员激活时需要被一起激活 ***/
    private boolean needToActive;

    AccountTypeEnums(long code, String msg, boolean needToActive) {
        this.code = code;
        this.msg = msg;
        this.needToActive = needToActive;
    }

    /**
     * 从配置解析账户类型
     *
     * @param accountTypeDesc
     * @return
     */
    public static List<String> parseAccountTypes(String accountTypeDesc) {
        List<String> accounts = new ArrayList<String>();
        if (StringUtils.isBlank(accountTypeDesc)) {
            return accounts;
        }
        String[] accountTypeCodes = StringUtils.split(accountTypeDesc, ',');
        if (null != accountTypeCodes && 0 < accountTypeCodes.length) {
            for (String accountType : accountTypeCodes) {
                accounts.add(accountType);
            }
        }
        return accounts;
    }

    public static AccountTypeEnums getByMsg(String msg) {
        for (AccountTypeEnums ls : AccountTypeEnums.values()) {
            if (ls.msg.equalsIgnoreCase(msg)) {
                return ls;
            }
        }
        return null;
    }

    public static AccountTypeEnums getByCode(long code) {
        for (AccountTypeEnums ls : AccountTypeEnums.values()) {
            if (ls.code == code) {
                return ls;
            }
        }
        return null;
    }

    public boolean equals(AccountTypeEnums accountTypeEnums){
        return Objects.equals(accountTypeEnums.code,this.code);
    }

}
