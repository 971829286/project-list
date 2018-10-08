package com.souche.niu.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * VO
 *
 * @since 2018-09-10
 * @author ZhangHui
 */
public class MaintenanceRecordVO implements Serializable {

    private static final long serialVersionUID = 3462670258576004656L;

    private String order_id;
    //'0 订单生成 1 付款成功 2 查询失败 3 查询中 4 查询结果成功 5 暂无结果 6 查询结果失败 7 品牌不支持 -1 订单关闭',
    private int status;
    private BigDecimal price;
    private String brand;
    private String code;
    private String remark;
    private String vin_code;
    private String app_name;
    private BigDecimal current_pay_price;
    private String time;
    private String extend_info;
    private String detailUrl;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVin_code() {
        return vin_code;
    }

    public void setVin_code(String vin_code) {
        this.vin_code = vin_code;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public BigDecimal getCurrent_pay_price() {
        return current_pay_price;
    }

    public void setCurrent_pay_price(BigDecimal current_pay_price) {
        this.current_pay_price = current_pay_price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExtend_info() {
        return extend_info;
    }

    public void setExtend_info(String extend_info) {
        this.extend_info = extend_info;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
