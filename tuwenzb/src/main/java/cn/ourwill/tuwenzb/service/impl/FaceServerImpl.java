package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import cn.ourwill.tuwenzb.entity.ActivityPhotoToken;
import cn.ourwill.tuwenzb.mapper.ActivityAlbumMapper;
import cn.ourwill.tuwenzb.mapper.ActivityMapper;
import cn.ourwill.tuwenzb.mapper.ActivityPhotoMapper;
import cn.ourwill.tuwenzb.mapper.ActivityPhotoTokenMapper;
import cn.ourwill.tuwenzb.service.IActivityAlbumService;
import cn.ourwill.tuwenzb.service.IActivityPhotoTokenService;
import cn.ourwill.tuwenzb.service.IFaceServer;
import cn.ourwill.tuwenzb.utils.FaceDomain;
import cn.ourwill.tuwenzb.utils.HttpUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-29 18:17
 **/
@Service
@Slf4j
public class FaceServerImpl implements IFaceServer {
    @Autowired
    private ActivityPhotoMapper activityPhotoMapper;
    @Autowired
    private ActivityAlbumMapper activityAlbumMapper;

    @Autowired
    private ActivityMapper      activityMapper;
    @Autowired
    FaceDomain faceDomain;

    @Autowired
    IActivityPhotoTokenService activityPhotoTokenService;
    @Autowired
    IActivityAlbumService activityAlbumService;

    /**
     *监测facetoken,并入库
     * @param photoId
     * @return
     */
    @Override
    public List<String> detect(Integer photoId,boolean isUpdate,String faceSetToken) {
        ActivityPhoto activityPhoto = activityPhotoMapper.getById(photoId);
        if(activityPhoto != null && activityPhoto.getPhotoPath() != null){
            String url = faceDomain.getDetect();
//            System.out.println(activityPhoto.getPhotoPathUrl());
            //侦测
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("image_url",activityPhoto.getPhotoPathUrl()));
            JsonNode resNode = HttpUtils.sendPost(url,JsonNode.class,list,null);
            //解析
            if(resNode.get("error_message") == null){
                List<String> resList = new ArrayList<>();
                JsonNode faces = resNode.get("result").get("faces");
                if(faces != null && faces.size() > 0){
                    Iterator<JsonNode> iterator = faces.iterator();
                    while(iterator.hasNext()){
                        JsonNode next = iterator.next();
                        //获取Token
                        String faceToken = next.get("face_token").asText();
                        //入库
                        if(addFace(faceSetToken, faceToken)){
                            ActivityPhotoToken activityPhotoToken = new ActivityPhotoToken();
                            activityPhotoToken.setFaceToken(faceToken);//1
                            activityPhotoToken.setAlbumId(activityPhoto.getAlbumId());//2
                            activityPhotoToken.setUserId(activityPhoto.getUserId());//3
                            activityPhotoToken.setPhotoId(activityPhoto.getId());//4
                            if(isUpdate){
                                activityPhotoTokenService.update(activityPhotoToken);
                            }else{
                                activityPhotoTokenService.save(activityPhotoToken);
                            }
                            resList.add(faceToken);
                        }
                    }
                    return resList;
                }
            }

        }
        return null;
    }

    /**
     *监测facetoken,并入库
     * @return
     */
    @Override
    public List<String> detect(ActivityPhoto activityPhoto, boolean isUpdate,String faceSetToken) {
        if(activityPhoto != null && activityPhoto.getPhotoPath() != null){
//            String faceSetToken = activityAlbumService.getFaceSetToken(activityPhoto.getAlbumId());
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("image_url",activityPhoto.getPhotoPathUrl()));
            String url = faceDomain.getDetect();
//            String url = faceDomain.getDetect()+"?image_url=http://imgsrc.baidu.com/imgad/pic/item/4a36acaf2edda3ccab7b1a3f0be93901213f9216.jpg";
            //侦测
            JsonNode resNode = HttpUtils.sendPost(url,JsonNode.class,list,null);
            //解析
            if(resNode.get("error_message") == null){
                List<String> resList = new ArrayList<>();
                JsonNode faces = resNode.get("result").get("faces");
                if(faces != null && faces.size() > 0){
                    Iterator<JsonNode> iterator = faces.iterator();
                    while(iterator.hasNext()){
                        JsonNode next = iterator.next();
                        //获取Token
                        String faceToken = next.get("face_token").asText();
//                        System.out.println(faceToken);
                        //入库
                        if(addFace(faceSetToken, faceToken)){
                            ActivityPhotoToken activityPhotoToken = new ActivityPhotoToken();
                            activityPhotoToken.setFaceToken(faceToken);//1
                            activityPhotoToken.setAlbumId(activityPhoto.getAlbumId());//2
                            activityPhotoToken.setUserId(activityPhoto.getUserId());//3
                            activityPhotoToken.setPhotoId(activityPhoto.getId());//4
                            if(isUpdate){
                                activityPhotoTokenService.update(activityPhotoToken);
                            }else{
                                activityPhotoTokenService.save(activityPhotoToken);
                            }
                            resList.add(faceToken);
                        }
                    }
                    return resList;
                }
            }

        }
        return null;
    }

    /**
     * 将人脸添加到faceset
     * @param faceSetToken
     * @param faceToken
     * @return
     */
    @Override
    public boolean addFace(String faceSetToken, String faceToken) {
        if(StringUtils.isEmpty(faceSetToken) || StringUtils.isEmpty(faceToken)){
            return false;
        }
        String url = faceDomain.geAddface();
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("faceset_token",faceSetToken));
        list.add(new BasicNameValuePair("face_token",faceToken));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map map = HttpUtils.sendPost(url, Map.class,list,null);
        if(map.get("code").equals(1)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 搜索人脸
     * @param faceSetToken
     * @param imageUrl
     * @return
     */
    @Override
    public List<String> search(String faceSetToken, String imageUrl) {
        if(StringUtils.isEmpty(faceSetToken) || StringUtils.isEmpty(imageUrl)){
            return null;
        }
        String url = faceDomain.getSearch();
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("faceset_token",faceSetToken));
        list.add(new BasicNameValuePair("image_url",imageUrl));
        JsonNode resJsonNode = HttpUtils.sendPost(url, JsonNode.class,list,null);
        JsonNode results = resJsonNode.get("results");
        if(results != null && results.size() > 0){
            Iterator<JsonNode> iterator = results.iterator();
            List<String> resList = new ArrayList<>();
            while (iterator.hasNext()){
                JsonNode next = iterator.next();
                String faceToken = next.get("face_token").asText();
                resList.add(faceToken);
            }
            return resList;
        }
        return null;
    }
}
