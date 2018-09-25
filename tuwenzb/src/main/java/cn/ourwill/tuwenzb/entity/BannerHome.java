package cn.ourwill.tuwenzb.entity;

import cn.ourwill.tuwenzb.utils.GlobalUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.util.StringUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class BannerHome extends Config implements Serializable {
    private Integer id;

    private Integer priority;

    private String pic;

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

    private Integer photoLive;

    public String getPicUrl() {
        if(StringUtil.isNotEmpty(this.pic)&&this.pic.indexOf("http")<0) {
            if(this.photoLive.equals(1))
                return photoBucketDomain+this.pic;
            return bucketDomain + this.pic;
        }
        return this.pic;
    }
}