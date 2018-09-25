package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import static cn.ourwill.huiyizhan.entity.Config.bucketDomain;
import static cn.ourwill.huiyizhan.entity.Config.defaultAvator;
import static cn.ourwill.huiyizhan.entity.Config.willcenterBucketDomain;

/**
 * @description: 搜索结果bean
 * @author: XuJinNiu
 * @create: 2018-05-05 20:31
 **/
@Data
public class SearchBean {
    //会议id
    private Integer id;
    private Integer userId;
    private String  activityTitle;
    private String  activityAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date    startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date    endTime;
    private String  activityDescription;
    private String  activityBanner;
    private String  activityBannerMobile;
    private String  avatar;
    private String  company;
    private String  nickName;
    private String  userName;
    private String  typeName;


    private String  activityBannerUrl;
    private String  activityBannerMobileUrl;
    private String  avatarUrl;

    public String getActivityBannerUrl() {
        if (StringUtils.isNotEmpty(activityBanner) && activityBanner.indexOf("http") < 0) {
            return bucketDomain + activityBanner;
        }
        return  activityBanner;
    }

    public String getActivityBannerMobileUrl() {
        if (StringUtils.isNotEmpty(activityBannerMobileUrl) && activityBannerMobileUrl.indexOf("http") < 0) {
            return bucketDomain + activityBannerMobileUrl;
        }
        return  activityBannerMobileUrl;
    }

    public String getAvatarUrl() {
        if(StringUtils.isEmpty(avatar)){
            return willcenterBucketDomain + defaultAvator;
        }
        if(StringUtils.isNotEmpty(avatar)&&avatar.indexOf("http")<0) {
            return willcenterBucketDomain + this.avatar;
        }
        return avatar;
    }
}
