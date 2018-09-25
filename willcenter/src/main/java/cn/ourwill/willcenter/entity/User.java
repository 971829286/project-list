package cn.ourwill.willcenter.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class User extends Config implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户id
    private Integer id;
    //唯一id
    private String UUID;
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
    //邮箱
    private String email;
    //qq号
    private String qq;
    //公司、单位
    private String company;
    //地址
    private String address;

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

    private String avatarUrl;
    //用户信息版本号
    private Integer version;

    //用户简介
    private String info;

    //微信用户统一标识
    private String unionid;

    //用户来源类型：0 微信  1 普通用户
    @JsonIgnore
    private Integer userfromType;

    /**
     * 验证码
     */
    private String code;

    /**
     * 短信验证码
     */
    private String smsCode;

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