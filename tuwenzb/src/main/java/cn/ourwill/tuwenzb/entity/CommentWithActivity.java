package cn.ourwill.tuwenzb.entity;

import cn.ourwill.tuwenzb.utils.GlobalUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/13 0013 12:15
 * @Version1.0
 */
@Data
public class CommentWithActivity extends Comment {
    private String activityBanner;
    private String activityTitle;
    private Integer photoLive;

    public String getActivityBanner() {
        if(StringUtils.isNotEmpty(this.activityBanner)&&this.activityBanner.indexOf("http")<0) {
            if(this.photoLive!=null&&this.photoLive.equals(1))
                return photoBucketDomain+this.activityBanner;
            return bucketDomain + this.activityBanner;
        }
        return this.activityBanner;
    }
}
