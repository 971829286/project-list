package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Survey implements Serializable {
    /**
     * 问卷ID
     */
    private Integer id;

    /**
     * 问卷标题
     */
    private String title;

    /**
     * 问卷描述
     */
    private String description;

    /**
     * 问卷状态（0:关闭,1:开启,2:结束）
     */
    private Integer status;

    /**
     * 问卷截止时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date deadLine;

    /**
     * 问卷创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createDate;

    /**
     * 问卷所属用户
     */
    private Integer uid;

    /**
     * 问卷类型(0:问卷,1:投票)
     */
    private Integer type;

    /**
     * 删除状态：0:未删除，1：已删除
     */
//    private Integer deleteStatus;

    /**
     * 背景图片
     */
    private String bgPicture;

    /**
     * 可访问人群（0：所有人，1：仅活动参与者）
     */
    private Integer visitType;

    /**
     * 问卷所属活动id
     */
    private Integer activityId;

    /**
     * 问卷提交成功提示信息
     */
    private String submitInfo;

    /**
     * 总数量
     */
    private Integer totalNum;

    /**
     * 今日
     */
    private Integer todayNum;

    /**
     * 投票地址
     */
    private String voteAddress;

    /**
     * 发送方式（0，短信，1，电子邮件，2，公众号推送）
     */
    private Integer sendWay;

    /**
     * 问题集合
     */
    private List<SurveyQuestion> surveyQuestions;

    private static final long serialVersionUID = 1L;
}