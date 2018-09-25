package cn.ourwill.huiyizhan.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * 多选题 选项表
 */
@Data
public class QuCheckbox implements Serializable {

    //所属题
    private String qid;

    //选项内容
    private String optionName;

    //排序ID
    private Integer orderById;

    private Integer order;







}
