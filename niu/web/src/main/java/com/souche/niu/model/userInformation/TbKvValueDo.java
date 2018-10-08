package com.souche.niu.model.userInformation;

import com.souche.optimus.dao.annotation.SqlTable;

import java.io.Serializable;
import java.sql.Date;

@SqlTable("kv_value")
public class TbKvValueDo implements Serializable {

    private int id;

    private String groupId;

    private String key;

    private String value;

    private String desc;

    private Date createAt;

    private Date updateAt;

    private int type;

    private float sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getSort() {
        return sort;
    }

    public void setSort(float sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "TbKvValueDo{" +
                "id=" + id +
                ", groupId='" + groupId + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", desc='" + desc + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", type=" + type +
                ", sort=" + sort +
                '}';
    }
}
