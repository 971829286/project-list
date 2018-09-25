package cn.ourwill.tuwenzb.entity;

import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/17 16:28
 * @Description
 */
@Data
public class PhotoLog {
    private Integer id;//
    private Integer albumId;//相册id
    private Integer number;//操作数量
    private Integer operaType;//操作类型
    private Integer userId;//操作人
    private Date cTime;//操作时间
}
