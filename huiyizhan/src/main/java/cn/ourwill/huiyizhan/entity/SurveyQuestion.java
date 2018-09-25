package cn.ourwill.huiyizhan.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class SurveyQuestion implements Serializable {
    /**
     * 问题id
     */
    private Integer id;

    /**
     * 问卷id
     */
    private Integer surveyId;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型索引：
     *      0-填空，
     *      1-单选，
     *      2-多选，
     *      3-打星题，
     *      4-图片选择题
     */
    private Integer type;

    private String typeText;

    /**
     * 顺序
     */
    private Integer orderd;

    /**
     * 是否必须:0-不，1-必须
     */
    private Integer required;

    /**
     * 选项内容，一个集合,json数组
     */
    private String optionContent;

    private Integer disabled;

    /**
     * 答案
     */
    private List<SurveyAnswer> answer;

    private Integer total;

    private Map data;

    /**
     * 导出图表类型-柱状图-饼状图-条形图
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}