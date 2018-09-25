package com.souche.niu.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/

public class OpenScreenDto implements Serializable {

    private Integer id;
    private String groupId;
    private Date createdAt;
    private Date updatedAt;
    private String title;     //标题
    private String protocol;   //跳转协议
    private String url;       //图片路径
    private String targetUser;  //目标用户
    private String startTime;     //有效期开始时间
    private String endTime;      //有限期截止时间
    private String status;       //状态 1：表示数据异常未能计算出状态 2：生效 3：未生效 4：已过期

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
