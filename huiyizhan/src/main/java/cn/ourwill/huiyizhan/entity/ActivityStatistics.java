package cn.ourwill.huiyizhan.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityStatistics implements Serializable {
    private Integer id;

    /**
     * 观看人数
     */
    private Integer watchCount;

    /**
     * 收藏人数
     */
    private Integer collectCount;

    /**
     * 会议ID
     */
    private Integer activityId;

    private static final long serialVersionUID = 1L;
}