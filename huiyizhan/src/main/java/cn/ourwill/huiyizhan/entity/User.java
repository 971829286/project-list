package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class User extends UserBase {

//    /**
//     * 管理级别
//     */
//    private Integer level;
//
//    /**
//     * 昵称
//     */
//    private String nickname;


    /**
     * 神马是否关注的 此人（查询粉丝 、关注人时使用）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date watchDate;


    /**
     * 用户统计信息
     */
    private UserStatistics userStatistics;

    /**
     * 关注人的信息
     */
    private List<User> beWatchedUsers;


    /**
     * 粉丝信息
     */
    private List<User> fansUsers;


    private static final long serialVersionUID = 1L;
}