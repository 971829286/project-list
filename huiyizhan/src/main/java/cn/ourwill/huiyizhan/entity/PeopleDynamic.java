package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-04-11 16:19
 **/
@Data
public class PeopleDynamic {
    /**
     * 用户id
     */
    private int userId;
    /**
     * 关注人id
     */
    private int watchUserId;
    /**
     * 关注日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date watchDate;


    /**
     * 状态：
     * 0 ： 关注别人
     * 1 ： 被关注
     */
    private int Status;


    /**
     * 用戶信息
     */
   private User user;

   private String personalizedSignature;

}
