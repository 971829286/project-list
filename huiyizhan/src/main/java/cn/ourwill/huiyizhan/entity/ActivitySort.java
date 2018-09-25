package cn.ourwill.huiyizhan.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivitySort implements Serializable {
    private Integer id;

    /**
     * 热门会议id
     */
    private Integer hotActivityId;

    /**
     * 最近会议id
     */
    private Integer recentActivityId;

    private static final long serialVersionUID = 1L;
}