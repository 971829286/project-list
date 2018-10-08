package com.souche.niu.model.userInformation;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ActivityScreenDo implements Serializable ,Comparable<ActivityScreenDo>{

    private Integer id;
    private String name;
    private String protocol;
    private String image;
    private String target;//目标用户，用逗号隔开，不填默认所有用户
    private String expiryStart; //有效期开始时间 2014-10-01 11:11格式
    private String expiryEnd; //有限期截止时间  2014-10-01 11:11格式

    public ActivityScreenDo() {
    }

    public ActivityScreenDo(Integer id, String name, String protocol, String image, String target, String expiryStart, String expiryEnd) {
        this.id = id;
        this.name = name;
        this.protocol = protocol;
        this.image = image;
        this.target = target;
        this.expiryStart = expiryStart;
        this.expiryEnd = expiryEnd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getExpiryStart() {
        return expiryStart;
    }

    public void setExpiryStart(String expiryStart) {
        this.expiryStart = expiryStart;
    }

    public String getExpiryEnd() {
        return expiryEnd;
    }

    public void setExpiryEnd(String expiryEnd) {
        this.expiryEnd = expiryEnd;
    }

    @Override
    public String toString() {
        return "ActivityScreenDo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", protocol='" + protocol + '\'' +
                ", image='" + image + '\'' +
                ", target='" + target + '\'' +
                ", expiryStart='" + expiryStart + '\'' +
                ", expiryEnd='" + expiryEnd + '\'' +
                '}';
    }

    /**
     * 自定义比较器
     * @param o
     * @return
     */
    public int compareTo(ActivityScreenDo o) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
           // Date dt1 = format.parse(o.getExpiryStart());
            //Date dt2 = format.parse(this.getExpiryStart());
            if (format.parse(this.expiryStart).getTime()<=format.parse(o.getExpiryStart()).getTime()){
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
