package cn.ourwill.tuwenzb.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/11/1 0001 16:51
 * 相册实体，用于照片直播
 * @Version1.0
 */
@Data
public class ActivityAlbum {
    private Integer id;//id
    private Integer activityId;//所属活动id
    private Integer userId;//用户id
    private String albumName;//相册名称
    private String description;//相册描述
    private Integer defaultFlag;//是否默认（1 默认 0 非默认）
    private Date cTime;//创建时间
    /**
     * <pre>
     *  删除状态 ：
     *          0: 未删除
     *          1：已删除
     * </pre>
     */
    private Integer deleteStatus;

    /**
     * 该相册 照片数量
     */
    private Integer photoNumber;

    private String faceSetToken;
}
