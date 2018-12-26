package com.souche.bmgateway.vo;

import com.souche.optimus.common.util.Base64Util;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 清算卡
 *
 * @author chenwj
 * @since 2018/7/16
 */
@Setter
@Getter
@ToString
public class BankCardVO implements Serializable {

    private String bankCardNo;

    private String bankCertName;

    private String accountType;

    private String contactLine;

    private String branchName;

    private String branchProvince;

    private String branchCity;

    private String certType;

    private String certNo;

    private String cardHolderAddress;

    private String bankCode;

    public String genJsonBase64() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("BankCardNo", bankCardNo);
        obj.put("BankCertName", bankCertName);
        obj.put("AccountType", accountType);
        obj.put("ContactLine", contactLine);
        obj.put("BranchName", branchName);
        obj.put("BranchProvince", branchProvince);
        obj.put("BranchCity", branchCity);
        obj.put("CertType", certType);
        obj.put("CertNo", certNo);
        obj.put("CardHolderAddress", cardHolderAddress);
        obj.put("BankCode", bankCode);
        return Base64Util.encrypt(obj.toString());
    }

}
