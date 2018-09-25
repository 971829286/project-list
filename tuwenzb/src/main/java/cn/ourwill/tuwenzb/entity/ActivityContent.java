package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ActivityContent extends Config implements Serializable {
    private Integer id;

    private Integer activityId;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uTime;

    private String img;

    private List<String> imgUrls;

    private String video;

    private String videoUrl;

    private String videoLink;

    private String waterMark;

    private String stickSign;

    public List<String> getImgUrls() {
        try {
//            String bucketDomain = GlobalUtils.getConfig("upload.bucketDomain");
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            Gson gson = new Gson();
            List<String> imgUrls = gson.fromJson(this.img, type);
            List<String> reList = imgUrls.stream().map(img -> {
                if(StringUtils.isNotEmpty(img)&&img.indexOf("http")<0)
                    return img = bucketDomain + img;
                else
                    return img;
            }).collect(Collectors.toList());
            return reList;
        }catch (Exception e){
//            e.printStackTrace();
            return null;
        }
    }

    public String getVideoUrl() {
        if(StringUtils.isNotEmpty(this.video)&&this.video.indexOf("http")<0){
            return bucketDomain+this.video;
        }
        return this.video;
    }
}