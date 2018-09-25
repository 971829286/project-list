package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-06-13 13:59
 **/
@Data
public class FdfsImage {
    private Integer id;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uploadTime;
    private Integer flag;
}
