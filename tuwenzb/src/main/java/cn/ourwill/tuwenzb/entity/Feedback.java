package cn.ourwill.tuwenzb.entity;

import lombok.Data;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-06-05 14:21
 **/
@Data
public class Feedback {

    private String id;

    private String name ;

    private String phone;

    private String remark;

    private Integer userId;
}
