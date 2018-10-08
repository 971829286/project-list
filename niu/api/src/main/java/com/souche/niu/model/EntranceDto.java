package com.souche.niu.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/15
 **/
public class EntranceDto implements Serializable {

    private Integer id;

    private String groupId;
    private Date createdAt;
    private Date updatedAt;

    private String protocol;  //工具页面打开协议

    private String title;   //工具页面标题

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
