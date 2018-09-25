package com.souche.bulbous.vo;

import com.souche.bulbous.utils.URLUtils;
import com.souche.niu.model.OpenScreenDto;
import com.souche.optimus.common.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/14
 **/

public class OpenScreenVo {

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private String id;
    private String title;  //标题
    private String protocol;  //协议
    private String url;       //图片URL
    private String targetUser; //目标用户
    private Date dateCreat;   //创建时间
    private String period;  //有效期
    private String status;  //有效期状态
    private String startTime;
    private String endTime;
    private Map<String,Object> photo;  //包含 name url status

    public OpenScreenVo(OpenScreenDto dto) {
        this.id = dto.getId() + "";
        this.title=dto.getTitle();
        this.protocol=dto.getProtocol();
        this.url=dto.getUrl();
        this.targetUser=dto.getTargetUser();
        this.dateCreat=dto.getCreatedAt();
        this.startTime=dto.getStartTime();
        this.endTime=dto.getEndTime();
        photo = new HashMap<>();
        if (StringUtil.isNotEmpty(dto.getUrl())) {
            photo.put("status", "done");
            photo.put("name", URLUtils.getNameByUrl(dto.getUrl()));
            photo.put("url", dto.getUrl());
        }
        this.period = this.getPeriod(dto.getStartTime(), dto.getEndTime());
        this.status = this.getStatusByTime(dto.getStartTime(), dto.getEndTime());

    }

    public OpenScreenVo() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        photo = new HashMap<>();
        if (StringUtil.isNotEmpty(url)) {
            photo.put("status", "done");
            photo.put("name", URLUtils.getNameByUrl(url));
            photo.put("url", url);
        }
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> getPhoto() {
        return photo;
    }

    public void setPhoto(Map<String, Object> photo) {
        this.photo = photo;
    }

    private String getStatusByTime(String startTime, String endTime) {
        if (StringUtil.isEmpty(startTime)) {
            return "非法状态";
        }
        if (StringUtil.isEmpty(endTime)) {
            return "非法状态";
        }
        Date now = new Date();
        try {
            if (now.getTime() < df.parse(startTime).getTime()) {
                return "未生效";
            }
            if (now.getTime() > df.parse(endTime).getTime()) {
                return "已过期";
            }
            return "生效";
        } catch (Exception e) {
            return "非法状态";
        }
    }

    private String getPeriod(String startTime, String endTime) {
        if (StringUtil.isEmpty(startTime)) {
            return "";
        }
        if (StringUtil.isEmpty(endTime)) {
            return "";
        }
        return startTime + " ~ " + endTime;
    }
}
