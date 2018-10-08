package com.souche.bulbous.vo;

import com.souche.bulbous.utils.URLUtils;
import com.souche.optimus.common.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description：服务工具展示类
 *
 * @remark: Created by wujingtao in 2018/9/11
 **/
public class ServiceToolVo {

    private String id;
    private String title;
    private String protocol;
    private String url2X;
    private Map<String,Object> photo2X;
    private Map<String,Object> photo3X;
    private String url3X;
    private String clickPoint;
    private String isShow;
    private String orderNum;

    public ServiceToolVo(String id, String title, String protocol, String url2X, String url3X,String clickPoint, String isShow, String orderNum) {
        this.id = id;
        this.title = title;
        this.protocol = protocol;
        this.url2X = url2X;
        this.url3X = url3X;
        this.clickPoint=clickPoint;
        this.isShow = isShow;
        this.orderNum = orderNum;
        String name2x = URLUtils.getNameByUrl(url2X);
        photo2X = new HashMap<>();
        if (StringUtil.isNotEmpty(name2x)) {
            photo2X.put("name", name2x);
            photo2X.put("status", "done");
            photo2X.put("url", url2X);
        }
        String name3x = URLUtils.getNameByUrl(url3X);
        photo3X = new HashMap<>();
        if (StringUtil.isNotEmpty(name3x)) {
            photo3X.put("name", name3x);
            photo3X.put("status", "done");
            photo3X.put("url", url3X);
        }
    }

    public ServiceToolVo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrl2X() {
        return url2X;
    }

    public void setUrl2X(String url2X) {
        this.url2X = url2X;
        String name = URLUtils.getNameByUrl(url2X);
        photo2X = new HashMap<>();
        if (StringUtil.isNotEmpty(name)) {
            photo2X.put("name", name);
            photo2X.put("status", "done");
            photo2X.put("url", url2X);
        }
    }

    public String getUrl3X() {
        return url3X;
    }

    public void setUrl3X(String url3X) {
        this.url3X = url3X;
        String name = URLUtils.getNameByUrl(url3X);
        photo3X = new HashMap<>();
        if (StringUtil.isNotEmpty(name)) {
            photo3X.put("name", name);
            photo3X.put("status", "done");
            photo3X.put("url", url3X);
        }
    }

    public String getClickPoint() {
        return clickPoint;
    }

    public void setClickPoint(String clickPoint) {
        this.clickPoint = clickPoint;
    }

    public Map<String, Object> getPhoto2X() {
        return photo2X;
    }

    public void setPhoto2X(Map<String, Object> photo2X) {
        this.photo2X = photo2X;
    }

    public Map<String, Object> getPhoto3X() {
        return photo3X;
    }

    public void setPhoto3X(Map<String, Object> photo3X) {
        this.photo3X = photo3X;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
