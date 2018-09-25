package cn.ourwill.huiyizhan.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 文本题答案保存
 */
@Data
public class AnswerText implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 所属问卷ID
     */
    private Integer surveyId;
    /**
     *对应答案信息表ID
     */
    private Integer surveyAnswerId;
    /**
     * 所属问题ID
     */
    private Integer qid;
    /**
     * 文本题答案
     */
    private String answer;



}
