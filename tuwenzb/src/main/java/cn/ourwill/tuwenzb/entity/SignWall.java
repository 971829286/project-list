package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/10 0010 14:24
 * @Descript 签到墙
 * @Version1.0
 */
@Data
public class SignWall extends Config{
    private Integer id;//主键
    private Integer roomId;//房间号
    private Integer userId;//用户id
    private String nickname;//昵称
    private String avatar;//头像
    private String avatarUrl;//头像url
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;//创建时间
    private Integer ranking;//排名

    public String getAvatarUrl() {
        if(StringUtils.isEmpty(avatar)){
            return bucketDomain + defaultAvator;
        }
        if(StringUtils.isNotEmpty(avatar)&&avatar.indexOf("http")<0) {
            return bucketDomain + this.avatar;
        }
        return avatar;
    }
}

