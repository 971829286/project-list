package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.util.StringUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/11/1 0001 16:54
 * @Version1.0
 */
@Data
public class ActivityPhoto extends Config{
    private Integer id;//id
    private Integer userId;//用户id
    private Integer albumId;//所属相册id
    private String photoPath;//照片路径
    private Integer photoStatus;//照片状态（0，未发布 1 已发布）
    private Integer likeNum;//点赞数
    private boolean isLiked;//是否已点赞
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;//创建时间
    private String photoPathUrl;

    private String downloadName;//下载编号
    private String photoName;//原文件名;
    private Integer photoSize;//照片大小
    private Integer photoHeight;//高度（pixels）
    private Integer photoWidth;//宽度（pixels）
    private Integer photoOrientation;//旋转
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date shootingTime;//拍摄时间;
    private String appReadTime;//app传入拍摄时间
    private Integer downloadStatus;//下载状态: 0:未下载 1:已下载
    private Integer replaceStatus;//替换状态: 0:未替换 1:已替换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uTime;//更新时间
    private String username;
    private String nickname;
    private String avatarUrl;
    private Integer activityId;
    private Integer isSaveToFaceSet;
    public String getPhotoPathUrl() {
        if(StringUtil.isNotEmpty(this.photoPath)&&this.photoPath.indexOf("http")<0){
            return photoBucketDomain+this.photoPath;
        }
        return this.photoPath;
    }
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
