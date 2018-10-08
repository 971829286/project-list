package com.souche.niu.model.maintenance;

import com.souche.optimus.dao.annotation.SqlPrimaryKey;
import com.souche.optimus.dao.annotation.SqlTable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sid on 2018/9/5.z
 */
@SqlTable("car_maintenance_query")
public class MaintenanceRecordDO implements Serializable {

    private static final long serialVersionUID = 3462670258576004656L;

    //订单号
    @SqlPrimaryKey
    private String orderId;
    //买家手机号
    private String phone;
    //图片链接
    private String imageUrl;
    //0 图片链接 1 vin码
    private String imageType;
    //取到类型.
    private int orderType;
    //'0 订单生成 1 付款成功 2 查询失败 3 查询中 4 查询结果成功 5 暂无结果 6 查询结果失败 7 品牌不支持 -1 订单关闭',
    private int status;
    private BigDecimal price;
    private String brand;
    private String code;
    private String remarks;
    private String vinNumber;
    private String queryOrderId;
    private int refundStatus;
    private BigDecimal payPrice;
    private BigDecimal currentPrice;
    private String unpassReason;
    private String unpassStatus;
    private String appName;
    private String notifyUrl;
    private String detailUrl;
    private String couponCode;
    private BigDecimal currentPayPrice;
    private String engine;
    private String licensePlate;
    private String drivingLicence;

    private Date date_create;
    private Date date_update;

    public MaintenanceRecordDO(){

    }


    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public MaintenanceRecordDO(String orderId, int status, String unpassStatus, String unpassReason, String queryOrderId,String detailUrl){
        this.orderId=orderId;
        this.status=status;
        this.unpassStatus=unpassStatus;
        this.unpassReason=unpassReason;
        this.queryOrderId=queryOrderId;
        this.detailUrl=detailUrl;
    }

    public MaintenanceRecordDO(String orderId, int status, int refundStatus){
        this.orderId=orderId;
        this.status=status;
        this.refundStatus=refundStatus;
    }

    public MaintenanceRecordDO(String orderId, int refundStatus){
        this.orderId=orderId;
        this.refundStatus=refundStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getQueryOrderId() {
        return queryOrderId;
    }

    public void setQueryOrderId(String queryOrderId) {
        this.queryOrderId = queryOrderId;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getUnpassReason() {
        return unpassReason;
    }

    public void setUnpassReason(String unpassReason) {
        this.unpassReason = unpassReason;
    }

    public String getUnpassStatus() {
        return unpassStatus;
    }

    public void setUnpassStatus(String unpassStatus) {
        this.unpassStatus = unpassStatus;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getCurrentPayPrice() {
        return currentPayPrice;
    }

    public void setCurrentPayPrice(BigDecimal currentPayPrice) {
        this.currentPayPrice = currentPayPrice;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDrivingLicence() {
        return drivingLicence;
    }

    public void setDrivingLicence(String drivingLicence) {
        this.drivingLicence = drivingLicence;
    }

    public Date getDate_create() {
        return date_create;
    }

    public void setDate_create(Date date_create) {
        this.date_create = date_create;
    }

    public Date getDate_update() {
        return date_update;
    }

    public void setDate_update(Date date_update) {
        this.date_update = date_update;
    }




}
