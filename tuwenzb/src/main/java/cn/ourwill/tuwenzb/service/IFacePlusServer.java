package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.ActivityPhoto;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @program: tuwenzb
 * @description:
 * @author: XuJinniu
 * @create: 2018-05-31 17:00
 **/
public interface IFacePlusServer {

    String getFaceSetToken();

    boolean addFace(String faceSetToken, List<String> faceToken);

    List<String> detect(ActivityPhoto activityPhoto,String faceSetToken,boolean isAddFace);

    List<String> search(String faceSetToken,String imageUrl);
    List<String> searchByFile(String faceSetToken, InputStream inputStream,String imageType);

    boolean removeFace(String faceSetToken,List<String> faceTokens);

    List<String> searchByFaceToken(String faceSetToken, String faceToken);

    boolean addFace(String faceSetToken, String faceToken,String userId);

    boolean setUserId(String faceToken,String userId);
}
