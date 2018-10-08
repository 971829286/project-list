package com.souche.niu.model.maintenance.request;

import java.io.Serializable;

/**
 * 查询请求参数
 * Created by sid on 2018/9/4.
 */
public class QueryMaintenanceRequest implements Serializable{

    private static final long serialVersionUID = -7580330550053771473L;
    //授权码
    private String authId = "2018090801";
    //签名
    private String sign = "";
    //请求源
    private String clientCode="H5";
    //业务来源
    private String businessSource = "DSC";
    //业务编码
    private String businessCode = "CNAPP_WB";
    //服务类型编码
    private String serviceType = "WB";
    //VIM码
    private String vin;
    //请求单号
    private String outOrderNo;
    //通知地址
    private String notifyUrl;

    public QueryMaintenanceRequest(){}

   public QueryMaintenanceRequest(String vin,String outOrderNo,String  notifyUrl){
       this.vin=vin;
       this.outOrderNo=outOrderNo;
       this.notifyUrl=notifyUrl;
   }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getBusinessSource() {
        return businessSource;
    }

    public void setBusinessSource(String businessSource) {
        this.businessSource = businessSource;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
