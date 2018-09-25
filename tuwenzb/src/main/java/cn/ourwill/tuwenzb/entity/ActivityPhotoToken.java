package cn.ourwill.tuwenzb.entity;

import lombok.Data;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-29 15:49
 **/
@Data
public class ActivityPhotoToken {
    private Integer id;
    private Integer albumId;
    private Integer userId;
    private String faceToken;
    private Integer photoId;
    private String faceSetToken;
    private Integer activityId;
    private String samePerson;
}
