package cn.ourwill.huiyizhan.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserStatistics implements Serializable {

    /**
     * 发布的会议数目
     */
    private Integer activityCount;

    /**
     * 人气
     */
    private Integer popularity;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 粉丝数目
     */
    private Integer fansCount;


    /**
     * 活动票数
     */
    private Integer ticketCount;

    private static final long serialVersionUID = 1L;

    /**
     * 总的会议数目
     */
    private Integer totalActivityCount;
}