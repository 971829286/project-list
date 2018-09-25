package com.souche.niu.model;

import com.souche.optimus.dao.annotation.SqlTable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 维保管理实体类
 * Created by sid on 2018/9/5.
 */
@SqlTable("maintenancecfg")
public class MaintenanceConfigDO implements Serializable {

    private Integer id;

    /**
     * 未认证的金额
     */
    private BigDecimal unverifiedPrice;
    /**
     * 已经认证的金额
     */
    private BigDecimal verifiedPrice;

    private String bannerImg;   //banner图片路径

    private String bannerTitle;  //banner标题

    private String bannerProtocol;  //banner跳转协议


    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public String getBannerProtocol() {
        return bannerProtocol;
    }

    public void setBannerProtocol(String bannerProtocol) {
        this.bannerProtocol = bannerProtocol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getUnverifiedPrice() {
        return unverifiedPrice;
    }

    public void setUnverifiedPrice(BigDecimal unverifiedPrice) {
        this.unverifiedPrice = unverifiedPrice;
    }

    public BigDecimal getVerifiedPrice() {
        return verifiedPrice;
    }

    public void setVerifiedPrice(BigDecimal verifiedPrice) {
        this.verifiedPrice = verifiedPrice;
    }
}
