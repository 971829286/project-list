package com.souche.niu.model.userInformation;

import java.io.Serializable;

public class IconUtilDo implements Serializable,Comparable<IconUtilDo>{

    private String title;
    private String protocol;
    private String image_2x;
    private String image_3x;
    private String eventKey;
    private int orderNum;
    private boolean firstShow;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getImage_2x() {
        return image_2x;
    }

    public void setImage_2x(String image_2x) {
        this.image_2x = image_2x;
    }

    public String getImage_3x() {
        return image_3x;
    }

    public void setImage_3x(String image_3x) {
        this.image_3x = image_3x;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public boolean isFirstShow() {
        return firstShow;
    }

    public void setFirstShow(boolean firstShow) {
        this.firstShow = firstShow;
    }

    @Override
    public String toString() {
        return "IconUtilDo{" +
                "title='" + title + '\'' +
                ", protocol='" + protocol + '\'' +
                ", image_2x='" + image_2x + '\'' +
                ", image_3x='" + image_3x + '\'' +
                ", eventKey='" + eventKey + '\'' +
                ", orderNum=" + orderNum +
                ", firstShow=" + firstShow +
                '}';
    }

    /**
     * 排序
     * @param o
     * @return
     */
    public int compareTo(IconUtilDo o) {
        if(this.orderNum <= o.getOrderNum()){
            return 1;
        }
        return -1;
    }
}
