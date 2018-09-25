package cn.ourwill.tuwenzb.entity;

import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Activity extends Config implements Serializable {
    private Integer id;

    private Integer userId;
    @JsonIgnore
    private Integer priority;

    private String title;

    private String introduction;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date endTime;

    private String site;

    private String organizer;

    private String publisher;

    private Integer generalizeStatus;

    private String generalizeName;

    private String generalizeLink;

    private String phone;

    private String email;

    private String banner;

    private String watermark;

    private String waterComplete;

    private String waterConfig;

    private Integer checkType;//审核类型（0:先审后发 1:先发后审）

    private Integer status;//0 关闭  1  打开  3 删除 4 试用数据

    private Integer statuss;//直播状态 查询用

    private Integer isImpower;//是否对外授权

    private String impowerUrl;//授权链接

    private String qrCode;//推广二维码图片

    private String screen;//闪屏图片

    private String docx;//导出文档

    private Integer liveStatus;//0 直播未开始，1 正在直播， 2 直播已结束

    private Integer liveDays;//直播天数

    private Integer classical;//是否经典案例，0 不是  1 是

    private Integer type;//直播分类

    private Integer contentNumber;//内容数量

    private Integer switchPassword;//密码开关

    @JsonIgnore
    private String password;//密码
    private String inputPwd;//传入密码（传入参数用）

//    private Integer isPassword;//是否需要密码 （0：不需要 1 需要）

    private Integer photoLive;//0 : 图文直播，1：照片直播 (产品分类）

    /**
     * 活动相册分区
     */
    private List<ActivityAlbum> activityAlbums;

    private List<ActivityImpower> adminUser;//管理员 照片直播

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date showStartTime;//对外宣布直播开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date showEndTime;//对外宣布直播结束时间

    private String typeName;//需要显示的直播分类

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uTime;

    private String bannerUrl;

    private String activityUrl;

    private String waterMarkUrl;

    private String watermarkImg;

    private String qrCodeUrl;

    private String screenUrl;

    private String docxUrl;

    //是否旧数据
    private Integer isOld;
    //参与人数 参数用
    private Integer participantsNum;
    //点赞数 参数用
    private Integer likeNum;

    //活动相关统计（参与人数、点赞数量）
    private ActivityStatistics activityStatistics;
    //活动内容
    private ActivityContent activityContent;

    //当前用户是否已关注
    Boolean isAttention;

    //当前用户修改权限
    Boolean modifyPermission;

    //图标开关 0关闭 1打开
    //四个属性的字符串 home(home键) participation(参与人数显示) praise(点赞数显示) attention(关注按钮)
    private String iconSwitch;

    //用于返回json 前端使用
    private IconSwitch iconSwitchShow;

    private String username; //创建者用户名

    private String nickname;//创建者昵称

    private String avatarUrl;//创建者头像

    private Integer isVote;//是否有投票

    private Integer isAutoPublish;//APP端是否自动发布 0 否,1是

    private String faceSetToken;

    private Integer isOpenFaceSearch;//是否开启人脸识别
    private String iframe; //

    private Integer expectLikeNum;

    private Integer expectParticipateNum;

    /**
     * 该会议需要展示的相册的名称：空-默认分区
     */
    private Integer displayAlbumId;

    public IconSwitch getIconSwitchShow() {
        Gson gson = new Gson();
        iconSwitchShow = gson.fromJson(this.iconSwitch, IconSwitch.class);
        return this.iconSwitchShow;
    }

    private Integer model; //默认展示形式（0：图文，1：图片）

    public String getBannerUrl() {
        if (StringUtil.isNotEmpty(this.banner) && this.banner.indexOf("http") < 0) {
            if (this.photoLive != null && this.photoLive.equals(1))
                return photoBucketDomain + this.banner;
            return bucketDomain + this.banner;
        }
        return this.banner;
    }

    public String getWatermarkImg() {
        if (StringUtil.isNotEmpty(this.watermark) && this.watermark.indexOf("http") < 0) {
            if (this.photoLive != null && this.photoLive.equals(1))
                return photoBucketDomain + this.watermark;
            return bucketDomain + this.watermark;
        }
        return null;
    }

    public String getQrCodeUrl() {
        if (StringUtil.isNotEmpty(this.qrCode) && this.qrCode.indexOf("http") < 0) {
            if (this.photoLive != null && this.photoLive.equals(1))
                return photoBucketDomain + this.qrCode;
            return bucketDomain + this.qrCode;
        }
        return this.qrCode;
    }

    public String getScreenUrl() {
        if (StringUtil.isNotEmpty(this.screen) && this.screen.indexOf("http") < 0) {
            if (this.photoLive != null && this.photoLive.equals(1))
                return photoBucketDomain + this.screen;
            return bucketDomain + this.screen;
        }
        return this.screen;
    }

    public String getDocxUrl() {
        if (StringUtil.isNotEmpty(this.docx) && this.docx.indexOf("http") < 0) {
            if (this.photoLive != null && this.photoLive.equals(1))
                return photoBucketDomain + this.docx;
            return bucketDomain + this.docx;
        }
        return this.docx;
    }

    public String getImpowerUrl() {
        return systemDomain + "/live/news/" + this.id + "/true";
    }

    public ActivityStatistics getActivityStatistics() {
        this.activityStatistics = RedisUtils.getByActivityId(this.getId());
        return activityStatistics;
    }

    public Integer getLiveStatus() {
        if (startTime == null || endTime == null) return null;
        Date now = new Date();
        if (now.before(startTime)) liveStatus = 0;
        else if (now.after(startTime) && now.before(endTime)) liveStatus = 1;
        else liveStatus = 2;
        return liveStatus;
    }

    public String getActivityUrl() {
        if (this.photoLive != null && this.photoLive.equals(1))
            return systemDomain + "/photolive/news/" + this.id;
        if (this.id.equals(1954)) {
            return systemDomain + "/live/news/" + this.id + "?2873474";
        }
        return systemDomain + "/live/news/" + this.id;
    }

    public Integer getLiveDays() {
        if (this.liveDays == null && this.startTime != null && this.endTime != null) {
            return GlobalUtils.daysOfTwo(this.startTime, this.endTime);
        }
        return this.liveDays;
    }
}
