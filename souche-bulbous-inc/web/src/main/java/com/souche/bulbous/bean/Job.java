package com.souche.bulbous.bean;

import com.souche.bulbous.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;

public class Job {
    private String id;
    private int totalCount;
    private int nowCount;
    private String url;
    private String status;


    public Job() {
        super();
        this.id = UUIDUtils.getID10();
        this.status = StatusEnum.exporting.name();
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public int getProgress() {
        if (StringUtils.isNotEmpty(url)) {
            return 100;
        }
        if (nowCount == 0) {
            return 0;
        }
        if (totalCount == 0) {
            return 100;
        }
        Double progress = (new Double(nowCount) / new Double(totalCount)) * 99;
        return progress.intValue();
    }


    public int getTotalCount() {
        return totalCount;
    }


    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }


    public int getNowCount() {
        return nowCount;
    }


    public void setNowCount(int nowCount) {
        this.nowCount = nowCount;
    }


    public void addNowCount(int count) {
        this.nowCount += count;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public enum StatusEnum {
        noDate("无数据"),
        error("导出出错"),
        exporting("正在导出"),
        over("导出完毕");

        String displayName;


        StatusEnum(String displayName) {
            this.displayName = displayName;
        }
    }
}
