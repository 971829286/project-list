package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.util.StringUtil;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityPartner extends Config{
    private Integer id;

    private Integer activityId;

    private String partnerType;

    private String logoPics;

    private Integer priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;

    private String logoUrl;

    public String getLogoUrl() {
        if(StringUtil.isNotEmpty(this.logoPics)&&this.logoPics.indexOf("http")<0){
            return bucketDomain+this.logoPics;
        }
        return this.logoPics;
    }
}