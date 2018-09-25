package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.ActivityPhoto;

import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-29 18:17
 **/
public interface IFaceServer {
    List<String> detect(Integer photoId,boolean isUpdate,String faceSetToken);

    List<String> detect(ActivityPhoto activityPhoto, boolean isUpdate,String faceSetToken);

    boolean addFace(String faceSetToken,String faceToken);

    List<String> search(String faceSetToken,String imageUrl);

}
