package cn.ourwill.huiyizhan.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBlack implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * <pre>
     * 封禁时间:
     *   0:3天
     *   1:30天
     *   2：永久
     * </pre>
     */
    private Integer forceOutTime;

    /**
     * 原因
     */
    private String reason;

    private static final long serialVersionUID = 1L;
}