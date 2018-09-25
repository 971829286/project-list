package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 调查问卷问题
 * @Author: zhaoqing
 * @Date: 2018/7/9 ❤❤ 19:18
 */ 
@Data
public class SurveyAnswer implements Serializable {
    /**
     * 答案id
     */
    private Integer id;


    /**
     * 问题id
     */
    private Integer surveyQuestionId;

    /**
     * 问题类型
     */
    private Integer surveyQuestionType;

    /**
     * 答案
     */
    private String answer;

    /**
     * 来源：从哪回答的问题
     */
    private String source;
    /**
     * 答案类型
     */
    private Integer type;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createDate;

    /**
     * 答案题号：方便统计
     */
    private Integer orderd;
    private static final long serialVersionUID = 1L;

    /**
     * 答案对象
     */
    private List<SurveyAnswer> answerList;
//    private List<Map<String,Object>> answerList;
}