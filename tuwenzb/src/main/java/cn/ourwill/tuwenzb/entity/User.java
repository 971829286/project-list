package cn.ourwill.tuwenzb.entity;

import cn.ourwill.tuwenzb.utils.GlobalUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class User extends Config implements Serializable {
    //用户id
    private Integer id;
    //用户来源类型：0 微信  1 普通用户
    private Integer userfromType;
    //用户类型（0:个人 1:企业  2:试用账户）
    private Integer userType;
    //用户名称
    private String username;
    //昵称
    private String nickname;
    //密码
    @JsonIgnore
    private String password;
    //salt
    @JsonIgnore
    private String salt;
    //头像
    private String avatar;
    //手机
    private String mobPhone;
    //电话
    private String telPhone;
    //微信号
    private String wechatNum;
    //微信unionId
    private String unionId;
    //邮箱
    private String email;
    //qq号
    private String qq;
    //公司、单位
    private String company;
    //地址
    private String address;
    //授权类型（0:未授权  1:包年  2:包时长  9:永久）
    private Integer licenseType;
    //授权截至日期（包年）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date dueDate;
    //授权剩余天数（包时长）
    private Integer remainingDays;
    //授权剩余天数（包年）
    private Integer packYearsDays;

    //照片直播授权类型（0:未授权  1:包年  2:包时长  9:永久）
    private Integer photoLicenseType;
    //照片直播授权截至日期（包年）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date photoDueDate;
    //照片直播授权剩余天数（包时长）
    private Integer photoRemainingDays;
    //照片直播授权剩余天数（包年）
    private Integer photoPackYearsDays;

    //绑定userid，如果是微信用户则存普通用户id，反之
    private Integer boundId;
    //创建人
    private Integer cId;
    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;
    //更新人
    private Integer uId;
    //更新日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uTime;
    //管理级别
    private Integer level;
    //用户刷新access_token（效期为30天）
    @JsonIgnore
    private String refreshToken;
    //refresh_token的创建时间（refresh_token有效期为30天），用于检测refresh_token是否失效
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date  refreshTokenCTime;

    private boolean isDiscounts;

    private Integer discountsType;

    private String avatarUrl;

    private Integer activityCount;

    private Integer commentCount;

    private Integer attentionCount;

    private String appToken;//app凭证
    //渠道参数（不同平台注册，不同的渠道参数）
    private String channel;
    public String getAvatarUrl() {
        if(StringUtils.isEmpty(avatar)){
            return bucketDomain + defaultAvator;
        }
        if(StringUtils.isNotEmpty(avatar)&&avatar.indexOf("http")<0) {
            return bucketDomain + this.avatar;
        }
        return avatar;
    }

    //用户创建的活动列表
    private List<Activity> activityList;
}