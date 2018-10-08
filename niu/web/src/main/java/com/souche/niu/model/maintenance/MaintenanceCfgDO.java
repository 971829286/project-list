package com.souche.niu.model.maintenance;

import java.math.BigDecimal;

public class MaintenanceCfgDO {

    private int id;

    private BigDecimal unverifiedPrice;

    private BigDecimal verifiedPrice;

    private String bannerImg;

    private String bannerTitle;

    private String bannerProtocol;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
