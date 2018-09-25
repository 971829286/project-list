package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.util.StringUtil;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityGuest extends Config {
    private Integer id;

    private Integer activityId;

    private String guestName;

    private String guestIdentity;

    private String guestAvatar;

    private String guestAvatarUrl;

    private Integer priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;

    private String guestIntro;

    public String getGuestAvatarUrl() {
        if(StringUtil.isNotEmpty(this.guestAvatar)&&this.guestAvatar.indexOf("http")<0){
            return bucketDomain+this.guestAvatar;
        }
        return this.guestAvatar;
    }
}