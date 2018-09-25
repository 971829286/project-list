package cn.ourwill.tuwenzb.entity;

import lombok.Data;

/**
 *  wanghao
 *  用于图标开关的json转换
 */
@Data
public class IconSwitch {

    //0 关闭      1 打开

    //home键
    private Integer home;

    //参与人数图标
    private Integer participation;

    //参与人数图标
    private Integer praise;

    //关注图标
    private Integer attention;


}
