package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "activity", description = "活动会议entity", subTypes = Activity.class)
public class Activity extends Config {
    //("id")
    private Integer id;
    //("用户id")
    private Integer userId;
    //("活动标题")
    private String activityTitle;
    //("活动类别")
    private Integer activityType;
    //("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date startTime;
    //("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date endTime;
    //("活动地址")
    private String activityAddress;
    //("移动端banner")
    private String activityBannerMobile;
    private String activityBannerMobileUrl;
    //("pc端banner")
    private String activityBanner;
    private String activityBannerUrl;
    //("是否公开显示（0 否,1 是）")
    private Integer isOpen;
    //("是否线上活动（0 否,1 是）")
    private Integer isOnline;
    //("活动日程显示状态（0 否,1 是）")
    private Integer scheduleStatus;
    //("嘉宾显示状态（0 否,1 是）")
    private Integer guestStatus;
    //("合作支持显示状态（0 否,1 是）")
    private Integer partnerStatus;
    //("联系方式显示状态（0 否,1 是）")
    private Integer contactStatus;
    //("活动简介")
    private String activityDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uTime;

    private Integer uId;

    //-1: 不允许修改
    //0 : 允许修改
    //１：活动开始前一天
    //３：活动开始前三天
    //７：活动开始前七天
    private Integer ticketConfig;

    //活动自定义url后缀
    private String customUrl;

    private String username;
    /**
     * 用户昵称
     */
    private String nickname;


    /**
     * <pre>
     * 是否热门
     *  1: yes
     *  0：no
     *  </pre>
     */
    private Integer isHot;

    /**
     * <pre>
     * 是否最近
     *        1: yes
     *        0：no
     * </pre>
     */
    private Integer isRecent;


    /**
     * <pre>
     *     发布状态：
     *      0 ：未发布
     *      1 ：已发布
     * </pre>
     */
    private Integer issueStatus;


    /**
     * 头图类型
     */
    private Integer bannerType;


    /**
     * <pre>
     *     是否已经被收藏：
     *      0：no
     *      1:yes
     * </pre>
     */
    private Integer isCollect = 0;


    /**
     * <pre>
     *      是否此人已经被关注了
     *      0：no
     *      1:yes
     *  </pre>
     */
    private Integer isWatch = 0;


    /**
     * <pre>
     *     是否能被取消发布：
     *      0：no
     *      1:yes
     * </pre>
     */
    private Integer isCancelIssue;

    private UserBasicInfo owner;
    /**
     * 创建人头像 url
     */
    private String avatar;

    private Integer timeStatus;

    private ActivityStatistics activityStatistics;

    /**
     * banner图的id,会议是否是banner的标志
     */
    private Integer bannerId;

    private List<ActivityContact> activityContacts;
    private List<ActivityGuest> activityGuests;
    private List<ActivityPartner> activityPartners;
    private List<ActivitySchedule> activitySchedules;
    private List<ActivityTickets> activityTickets;
    private List<TicketsRecord> ticketsRecords;
    private List<ActivityTickets> validActivityTickets;


    public String getActivityBannerMobileUrl() {
        if (StringUtil.isNotEmpty(this.activityBannerMobile) && this.activityBannerMobile.indexOf("http") < 0) {
            return bucketDomain + this.activityBannerMobile;
        }
        return this.activityBannerMobile;
    }

    public String getActivityBannerUrl() {
        if (StringUtil.isNotEmpty(this.activityBanner) && this.activityBanner.indexOf("http") < 0) {
            return bucketDomain + this.activityBanner;
        }
        return this.activityBanner;
    }

    public Integer getTimeStatus() {
        if (this.startTime != null) {
            Calendar now = Calendar.getInstance();
            Calendar start = Calendar.getInstance();
            start.setTime(this.startTime);
            int diffDays = start.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
            if (diffDays == 1) {
                this.timeStatus = 0;
            } else if (diffDays > 1 && diffDays <= 7) {
                this.timeStatus = 1;
            } else if (diffDays > 7 && diffDays <= 30) {
                this.timeStatus = 2;
            } else {
                this.timeStatus = 3;
            }
        }
        return timeStatus;
    }
}