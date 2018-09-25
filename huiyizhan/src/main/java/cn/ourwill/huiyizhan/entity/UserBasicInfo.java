package cn.ourwill.huiyizhan.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/4/12 16:17
 * @Description
 */
@Data
public class UserBasicInfo extends Config implements Serializable {
    //用户id
    private Integer id;
    //唯一id
    private String UUID;
    //用户名称
    private String username;
    //昵称
    private String nickname;
    //管理级别
    private Integer level;
    private String avatar;

    private String avatarUrl;
    //用户间接
    private String info;

    /**
     * 是否互粉：
     *   1：是
     *   0：否
     */
    private Integer mutualFansStatus;


    /**
     * <pre>
     * 我是否关注了他：
     *          0：yes
     *          1:no
     *  </pre>
     */
    private Integer meToHim ;


    /**
     * <pre>
     * 他是否关注了我：
     *          0：yes
     *          1:no
     *  </pre>
     */
    private Integer HimTome ;

    /**
     * 个性签名
     */
    private String personalizedSignature;

    /**
     * 统计信息
     */
    private UserStatistics userStatistics;

    /**
     * 我关注的主办方的会议，点击进入相应的会议，最多显示  3 个
     */
    private List<Activity> activities;

    public String getAvatarUrl() {
        if(StringUtils.isEmpty(avatar)){
            return willcenterBucketDomain + defaultAvator;
        }
        if(StringUtils.isNotEmpty(avatar)&&avatar.indexOf("http")<0) {
            return willcenterBucketDomain + this.avatar;
        }
        return avatar;
    }

    public String getUsername() {
        if(StringUtils.isNotEmpty(username)&&username.length()>3)
            return username.replace(username.substring(1, username.length()-1), "***");
        return username;
    }
}
