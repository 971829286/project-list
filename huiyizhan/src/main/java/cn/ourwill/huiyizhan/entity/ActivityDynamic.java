package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-04-11 17:59
 **/
@Data
public class ActivityDynamic {

    /**
     * 关注日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date watchDate;


    /**
     * 状态：
     * 0 ： 收藏别人
     * 1 ： 被收藏
     */
    private int status;

    /**
     * 会议ID
     */
    private Integer activityId;


    /**
     * 用戶id
     */
    private Integer userId;


    private Activity activity;

    /**
     * 用户信息
     */
    private User user;

}
