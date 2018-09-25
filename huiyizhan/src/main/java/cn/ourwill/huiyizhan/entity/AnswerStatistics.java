package cn.ourwill.huiyizhan.entity;

import lombok.Data;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-05-21 15:45
 **/
@Data
public class AnswerStatistics {

    /**
     * 问题id
     */
    private Integer id;

    /**
     * 问题顺序
     */
    private Integer orderd;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 答案
     */
    private String answer;

    /**
     * 问题类型索引
     */
    private Integer type;

    /**
     * 总量
     */
    private Integer total;
}
