package com.souche.niu.model.workTable;

import java.io.Serializable;

public class ElementsDo implements Serializable {

    private String title;
    private double display_num;
    private String iosProtocol;
    private String androidProtocol;
    private String eventKey;
    private String protocol;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getDisplay_num() {
        return display_num;
    }

    public void setDisplay_num(double display_num) {
        this.display_num = display_num;
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

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "ElementsDo{" +
                "title='" + title + '\'' +
                ", display_num=" + display_num +
                ", iosProtocol='" + iosProtocol + '\'' +
                ", androidProtocol='" + androidProtocol + '\'' +
                ", eventKey='" + eventKey + '\'' +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
