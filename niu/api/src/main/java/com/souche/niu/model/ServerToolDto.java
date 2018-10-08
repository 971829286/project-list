package com.souche.niu.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/14
 **/
@AllArgsConstructor
@NoArgsConstructor
public class ServerToolDto implements Serializable {
    private Integer id;
    private String groupId;
    private Date createdAt;
    private Date updatedAt;
    private String title;   //标题
    private String protocol;  //跳转协议
    private String url2X;    //图标
    private String url3X;
    private String clickPoint;  //点击事件埋点
    private Boolean firstShow;   //是否在首页显示
    private String orderNum;   //排序值

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

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

    public String getUrl2X() {
        return url2X;
    }

    public void setUrl2X(String url2X) {
        this.url2X = url2X;
    }

    public String getUrl3X() {
        return url3X;
    }

    public void setUrl3X(String url3X) {
        this.url3X = url3X;
    }

    public String getClickPoint() {
        return clickPoint;
    }

    public void setClickPoint(String clickPoint) {
        this.clickPoint = clickPoint;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Boolean getFirstShow() {
        return firstShow;
    }

    public void setFirstShow(Boolean firstShow) {
        this.firstShow = firstShow;
    }

}
