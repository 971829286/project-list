package com.souche.niu.model.maintenance.respone;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sid on 2018/9/3.
 */
public class CarMaintenance implements Serializable {

    private static final long serialVersionUID = 3716158828552063999L;

    private String authId;
    private String clientCode;
    //客户端类型
    private String serviceType;
    //签证
    private String sign;

    private String serviceNo;
    //查询渠道
    private String finalVendorCode;
    //外部编号么?
    private String outOrderNo;
    private String createTime;
    private String reportTime;
    private String status;
    private String statusDesc;
    //vim码
    private String vin;
    //品牌名称
    private String brandName;
    //车系
    private String seriesName;
    //车型
    private String modelName;
    //出厂第
    private String productionArea;
    //生产日期
    private String makeDate;
    private String displacement;
    //变速箱
    private String transmissionType;
    private String effluentStandard;
    private String leaveFactoryDate;
    //工厂名称
    private String factoryName;
    //最后到店时间
    private String lasttimeToShop;
    //公里数
    private String kmValue;

    private List<WbReport> wbReportList;


    private String accidentNumbers;
    private String reportSketch;
    private String structurePartStatus;
    private String structurePartStatusDesc;
    private String importantComponentsStatus;
    private String importantComponentsStatusDesc;
    private String enginestatus;
    private String enginestatusDesc;
    private String gasbagStatus;
    private String gasbagStatusDesc;
    private String kmValueStatus;
    private String kmValueStatusDesc;
    private String showCode;
    private String showReportUrl;


    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public List<WbReport> getWbReportList() {
        return wbReportList;
    }

    public void setWbReportList(List<WbReport> wbReportList) {
        this.wbReportList = wbReportList;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getFinalVendorCode() {
        return finalVendorCode;
    }

    public void setFinalVendorCode(String finalVendorCode) {
        this.finalVendorCode = finalVendorCode;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getProductionArea() {
        return productionArea;
    }

    public void setProductionArea(String productionArea) {
        this.productionArea = productionArea;
    }

    public String getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(String makeDate) {
        this.makeDate = makeDate;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getEffluentStandard() {
        return effluentStandard;
    }

    public void setEffluentStandard(String effluentStandard) {
        this.effluentStandard = effluentStandard;
    }

    public String getLeaveFactoryDate() {
        return leaveFactoryDate;
    }

    public void setLeaveFactoryDate(String leaveFactoryDate) {
        this.leaveFactoryDate = leaveFactoryDate;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getLasttimeToShop() {
        return lasttimeToShop;
    }

    public void setLasttimeToShop(String lasttimeToShop) {
        this.lasttimeToShop = lasttimeToShop;
    }

    public String getKmValue() {
        return kmValue;
    }

    public void setKmValue(String kmValue) {
        this.kmValue = kmValue;
    }

    public String getAccidentNumbers() {
        return accidentNumbers;
    }

    public void setAccidentNumbers(String accidentNumbers) {
        this.accidentNumbers = accidentNumbers;
    }

    public String getReportSketch() {
        return reportSketch;
    }

    public void setReportSketch(String reportSketch) {
        this.reportSketch = reportSketch;
    }

    public String getStructurePartStatus() {
        return structurePartStatus;
    }

    public void setStructurePartStatus(String structurePartStatus) {
        this.structurePartStatus = structurePartStatus;
    }

    public String getStructurePartStatusDesc() {
        return structurePartStatusDesc;
    }

    public void setStructurePartStatusDesc(String structurePartStatusDesc) {
        this.structurePartStatusDesc = structurePartStatusDesc;
    }

    public String getImportantComponentsStatus() {
        return importantComponentsStatus;
    }

    public void setImportantComponentsStatus(String importantComponentsStatus) {
        this.importantComponentsStatus = importantComponentsStatus;
    }

    public String getImportantComponentsStatusDesc() {
        return importantComponentsStatusDesc;
    }

    public void setImportantComponentsStatusDesc(String importantComponentsStatusDesc) {
        this.importantComponentsStatusDesc = importantComponentsStatusDesc;
    }

    public String getEnginestatus() {
        return enginestatus;
    }

    public void setEnginestatus(String enginestatus) {
        this.enginestatus = enginestatus;
    }

    public String getEnginestatusDesc() {
        return enginestatusDesc;
    }

    public void setEnginestatusDesc(String enginestatusDesc) {
        this.enginestatusDesc = enginestatusDesc;
    }

    public String getGasbagStatus() {
        return gasbagStatus;
    }

    public void setGasbagStatus(String gasbagStatus) {
        this.gasbagStatus = gasbagStatus;
    }

    public String getGasbagStatusDesc() {
        return gasbagStatusDesc;
    }

    public void setGasbagStatusDesc(String gasbagStatusDesc) {
        this.gasbagStatusDesc = gasbagStatusDesc;
    }

    public String getKmValueStatus() {
        return kmValueStatus;
    }

    public void setKmValueStatus(String kmValueStatus) {
        this.kmValueStatus = kmValueStatus;
    }

    public String getKmValueStatusDesc() {
        return kmValueStatusDesc;
    }

    public void setKmValueStatusDesc(String kmValueStatusDesc) {
        this.kmValueStatusDesc = kmValueStatusDesc;
    }

    public String getShowCode() {
        return showCode;
    }

    public void setShowCode(String showCode) {
        this.showCode = showCode;
    }

    public String getShowReportUrl() {
        return showReportUrl;
    }

    public void setShowReportUrl(String showReportUrl) {
        this.showReportUrl = showReportUrl;
    }
}
