package cn.ourwill.huiyizhan.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WatchList implements Serializable {
    private Integer id;

    private Integer activityId;

    private Integer userId;
    //收藏的会议信息
    private Activity activity;

    /**
     * 神马是否关注的 此会议
     */
    private Date watchDate;
}