package cn.ourwill.tuwenzb.entity;

import cn.ourwill.tuwenzb.utils.GlobalUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
//@EqualsAndHashCode(callSuper = true)
public class Comment extends Config implements Serializable {
    private Integer id;

    private Integer parentId;

    private Integer activityId;

    private Integer photoId;

    private String content;

    private Integer userId;

    private Integer parentUserId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;

    private String nickname;

    private String avatar;

    private Integer check;//0 未审核，1 删除 2 通过

    private String deviceid;

    private String ip;

    private String verifyCode;

    private String avatarUrl;

    private String activityTitle;

    private Integer likeNum;//点赞数

    private boolean isLiked;//是否已点赞

    private Comment parentComment;

    private ActivityPhoto activityPhoto;

    public String getAvatarUrl() {
        if(StringUtils.isNotEmpty(this.avatar)&&this.avatar.indexOf("http")<0) {
            return bucketDomain + this.avatar;
        }
        return this.avatar;
    }
}