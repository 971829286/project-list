package com.souche.niu.model;


import java.io.Serializable;

/**
 * @Description：banner配置表  此表只包含一条记录
 *
 * @remark: Created by wujingtao in 2018/9/12
 **/
public class BannerCfgDto implements Serializable {

    private Integer id;
    private String bannerTitle;
    private String url;
    private String protocol;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
