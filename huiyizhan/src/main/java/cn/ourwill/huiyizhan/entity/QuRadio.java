package cn.ourwill.huiyizhan.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 单选题选项
 * @author guojunjie
 *
 */
@Data
public class QuRadio implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 所属问题ID
     */
    private String qid;
    /**
     * 选项内容
     */
    private String optionName;
    /**
     * 排序ID
     */
    private Integer orderById;





}
