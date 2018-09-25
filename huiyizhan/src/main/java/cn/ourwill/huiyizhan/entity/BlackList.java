package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/19 0019 11:24
 * @Version1.0
 */
@Data
public class BlackList {
    private Integer id;//主键
    private Integer userId;//目标用户id
    private Integer status;//状态 0:已解封，1：封禁中，2：永久封禁
    private Integer type;//封禁类型：1:3天 2:30天 0:永久
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date startDate;//开始日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date endDate;//结束日期
    private String reason;//封禁原因
    private Integer cId;//创建人
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;//创建时间
    private Integer uId;//更新人
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uTime;//更新时间

    private String username;//用户名
    private String nickname;//用户昵称
}
