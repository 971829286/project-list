package com.souche.niu.bean;

import com.souche.optimus.dao.annotation.SqlTable;

import java.util.Date;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/
@SqlTable("kv_value")
public class KvValue {

    Integer id;
    String groupId;
    String value;
    Date createdAt;
    Date updatedAt;
    Integer type;
    Float sort;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Float getSort() {
        return sort;
    }

    public void setSort(Float sort) {
        this.sort = sort;
    }
}
