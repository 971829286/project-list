package com.souche.bulbous.dto;

import java.util.Date;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/3
 **/
public class ExampleClass{
    private String id;
    private String number;
    private String title;
    private String url;
    private Date dateCreat;
    private String period;
    private String status;

    public ExampleClass() {
    }

    public ExampleClass(String number, String title, String pic, Date dateCreat, String period, String status, String operating) {
        this.number = number;
        this.title = title;
        this.url = pic;
        this.dateCreat = dateCreat;
        this.period = period;
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateCreat() {
        return dateCreat;
    }

    public void setDateCreat(Date dateCreat) {
        this.dateCreat = dateCreat;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
