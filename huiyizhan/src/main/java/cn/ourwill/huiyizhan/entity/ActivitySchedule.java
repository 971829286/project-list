package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ActivitySchedule {
    private Integer id;

    private Integer activityId;     //活动id

    private String scheduleTitle;   //日程标题

    private String schedulePlace;   //日程会场

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date startDate;         //开始时间

    private Integer priority;       //优先级
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;             //创建时间

    private String scheduleDetail;  //日程明细
}