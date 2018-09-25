package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class BannerHome extends Config implements Serializable {
    private Integer id;

    private Integer priority;

    private String pic;

    private String mobilePic;

    private String title;

    private String link;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;

    private Integer cUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uTime;

    private Integer uUser;

    private String picUrl;

    private String mobilePicUrl;
    private Integer activityId;

    public String getPicUrl() {
        if(StringUtils.isNotEmpty(this.pic)&&this.pic.indexOf("http")<0) {
            return bucketDomain + this.pic;
        }
        return this.pic;
    }

    public String getMobilePicUrl(){
        if(StringUtils.isNotEmpty(this.mobilePic)&&this.mobilePic.indexOf("http")<0) {
            return bucketDomain + this.mobilePic;
        }
        return this.mobilePic;
    }
}