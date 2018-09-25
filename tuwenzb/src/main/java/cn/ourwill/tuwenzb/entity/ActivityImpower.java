package cn.ourwill.tuwenzb.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/23 0023 14:47
 * @Version1.0
 */
@Data
public class ActivityImpower extends Config{
    private Integer id;
    private Integer activityId;
    private Integer albumId;
    private Integer userId;
    private Integer type;
    private String username;
    private String nickname;
    private Integer status;
    private String avatarUrl;
    private Date cTime;

    public String getAvatarUrl() {
        if(StringUtils.isEmpty(avatarUrl)){
            return bucketDomain + defaultAvator;
        }
        if(StringUtils.isNotEmpty(avatarUrl)&&avatarUrl.indexOf("http")<0) {
            return bucketDomain + this.avatarUrl;
        }
        return avatarUrl;
    }
}
