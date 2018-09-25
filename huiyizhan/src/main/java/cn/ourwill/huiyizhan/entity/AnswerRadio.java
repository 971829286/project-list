package cn.ourwill.huiyizhan.entity;

import lombok.Data;

import java.io.Serializable;

/**
 *单选题答案保存
 */
@Data
public class AnswerRadio implements Serializable {

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
     * 对应结果ID（和当前问题的选项ID对应）
     */
    private Integer quItemId;
}
