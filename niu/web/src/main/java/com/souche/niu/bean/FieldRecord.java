package com.souche.niu.bean;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/
public class FieldRecord {

    private String key;     //字段名
    private String value;   //字段值
    private String desc;    //字段描述
    private String type;    //字段类型 默认为0

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
