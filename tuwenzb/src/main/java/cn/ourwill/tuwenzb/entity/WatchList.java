package cn.ourwill.tuwenzb.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class WatchList  implements Serializable {
    private Integer id;

    private Integer activityId;

    private Integer userId;
    //活动（直播）信息
    private Activity activity;
}