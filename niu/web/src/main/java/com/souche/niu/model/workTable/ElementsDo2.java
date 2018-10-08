package com.souche.niu.model.workTable;

import java.io.Serializable;

public class ElementsDo2 implements Serializable {

    private String title;
    private String subtitle;
    /*头像url*/
    private String iconUrl;
    private String eventKey;
    private String iosProtocol;
    private String androidProtocol;
    private int noticeMsgNotice;
    private int noticeMsgNumber;
    private String releaseTime;
    private String releaseTimeEnd;

    public ElementsDo2(String title, String subtitle, String iconUrl, String eventKey, String iosProtocol, String androidProtocol, int noticeMsgNotice, int noticeMsgNumber, String releaseTime, String releaseTimeEnd) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconUrl = iconUrl;
        this.eventKey = eventKey;
        this.iosProtocol = iosProtocol;
        this.androidProtocol = androidProtocol;
        this.noticeMsgNotice = noticeMsgNotice;
        this.noticeMsgNumber = noticeMsgNumber;
        this.releaseTime = releaseTime;
        this.releaseTimeEnd = releaseTimeEnd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getIosProtocol() {
        return iosProtocol;
    }

    public void setIosProtocol(String iosProtocol) {
        this.iosProtocol = iosProtocol;
    }

    public String getAndroidProtocol() {
        return androidProtocol;
    }

    public void setAndroidProtocol(String androidProtocol) {
        this.androidProtocol = androidProtocol;
    }

    public int getNoticeMsgNotice() {
        return noticeMsgNotice;
    }

    public void setNoticeMsgNotice(int noticeMsgNotice) {
        this.noticeMsgNotice = noticeMsgNotice;
    }

    public int getNoticeMsgNumber() {
        return noticeMsgNumber;
    }

    public void setNoticeMsgNumber(int noticeMsgNumber) {
        this.noticeMsgNumber = noticeMsgNumber;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getReleaseTimeEnd() {
        return releaseTimeEnd;
    }

    public void setReleaseTimeEnd(String releaseTimeEnd) {
        this.releaseTimeEnd = releaseTimeEnd;
    }

    @Override
    public String toString() {
        return "ElementsDo2{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", eventKey='" + eventKey + '\'' +
                ", iosProtocol='" + iosProtocol + '\'' +
                ", androidProtocol='" + androidProtocol + '\'' +
                ", noticeMsgNotice=" + noticeMsgNotice +
                ", noticeMsgNumber=" + noticeMsgNumber +
                ", releaseTime='" + releaseTime + '\'' +
                ", releaseTimeEnd='" + releaseTimeEnd + '\'' +
                '}';
    }
}
