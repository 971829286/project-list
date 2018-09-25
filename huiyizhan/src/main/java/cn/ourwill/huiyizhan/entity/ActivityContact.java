package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ActivityContact implements Serializable {
    private Integer id;

    private Integer activityId;

    private String contactName;

    private String contactPhone;

    private String contactWechat;

    private String contactEmail;

    private String contactAddress;

    private Integer priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;

}