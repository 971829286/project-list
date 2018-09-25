package cn.ourwill.tuwenzb.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityStatistics  implements Serializable {
    private Integer id;

    private Integer activityId;

    private Integer participantsNum;

    private Integer likeNum;

    private Integer commentNum;

    private Integer onlineNum;

    private Integer shareNum;

    private Integer newContentNum;//直播活动内容更新数量，在微信端置顶功能接口中使用


    private Integer realLikeNum;
    private Integer realCommentNum;
    private Integer realShareNum;
    private Integer realParticipantsNum;
}