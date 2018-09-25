package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:00
 * @Description 弹幕
 */
@Data
public class BulletScreen extends Config{
    private Integer id;//主键
    private Integer roomId;//房间号
    private Integer userId;//用户号
    private String nickname;//昵称
    private String avatar;//头像
    private String content;//内容
    private String img;//图片
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;//创建时间
    private Integer checkStatus;//审核状态
    private String avatarUrl;//头像url
    private String imgUrl;//图片url

    public String getAvatarUrl() {
        if(StringUtils.isEmpty(avatar)){
            return bucketDomain + defaultAvator;
        }
        if(StringUtils.isNotEmpty(avatar)&&avatar.indexOf("http")<0) {
            return bucketDomain + this.avatar;
        }
        return avatar;
    }

    public String getImgUrl() {
        if(StringUtils.isNotEmpty(img)&&img.indexOf("http")<0) {
            return bucketDomain + this.img;
        }
        return img;
    }

}
