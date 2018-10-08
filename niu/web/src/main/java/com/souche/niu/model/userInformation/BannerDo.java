package com.souche.niu.model.userInformation;

import java.io.Serializable;

public class BannerDo implements Serializable,Comparable<BannerDo> {

    private String desc;

    private String image_url;

    private String protocol;

    private int orderNum;

    private int status;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BannerDo{" +
                "desc='" + desc + '\'' +
                ", image_url='" + image_url + '\'' +
                ", protocol='" + protocol + '\'' +
                ", orderNum=" + orderNum +
                ", status=" + status +
                '}';
    }

    /**
     * 根据orderNum排序
     * @param o
     * @return
     */
    public int compareTo(BannerDo o) {
        if (this.orderNum <= o.getOrderNum()){
            return 1;
        }
        return -1;
    }
}
