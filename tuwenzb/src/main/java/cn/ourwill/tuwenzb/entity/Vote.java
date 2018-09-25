package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Vote  implements Serializable {

    private Integer id;

    private Integer activityId;//创建投票人id

    private Integer userId;//创建投票人id

    private String title;//投票标题

    private String introduction;//投票描述

    private List<VoteOptions> voteOptions;//投票选项

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date endTime;//投票截止时间

    private Integer dateType;//期限时间类型 0:自定义 1:3天 2:7天 3:30天

    private Integer isRadio;//是否单选  0:单选 1:多选

    private Integer isVisible;//投票结果  0:投票后可见  1:所有人可见

    private Integer votedTotal;//投票总数

    private Integer status;//投票状态 1:进行中 -1:投票结束

    private Integer isVoted;//当前登陆人是否投票   0:未投票 1:已投票
}
