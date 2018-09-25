package cn.ourwill.huiyizhan.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WatchListPeople implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 被关注人的id
     */
    private Integer watchUserId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 神马是否关注的 此人
     */
    private Date watchDate;

    private static final long serialVersionUID = 1L;


}