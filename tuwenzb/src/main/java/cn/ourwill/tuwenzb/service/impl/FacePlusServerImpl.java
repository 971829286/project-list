package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import cn.ourwill.tuwenzb.entity.ActivityPhotoToken;
import cn.ourwill.tuwenzb.service.IActivityPhotoTokenService;
import cn.ourwill.tuwenzb.service.IFacePlusServer;
import cn.ourwill.tuwenzb.utils.FaceDomain;
import cn.ourwill.tuwenzb.utils.HttpUtils;
import cn.ourwill.tuwenzb.utils.ImgUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-31 17:00
 **/
@Service
@Slf4j
public class FacePlusServerImpl implements IFacePlusServer {

    @Autowired
    FaceDomain faceDomain;
    @Value("${face.plus.api.key}")
    private String API_KEY;
    @Value("${face.plus.api.secret}")
    private String API_SECRET;
    @Autowired
    IActivityPhotoTokenService activityPhotoTokenService;

    /**
     * 获取faceSetToken
     *
     * @return
     */
    @Override
    public String getFaceSetToken() {
        //封装参数
        String url = faceDomain.getFacePlusCreate();
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("api_secret", API_SECRET));
        list.add(new BasicNameValuePair("api_key", API_KEY));
        //发送请求
        JsonNode jsonNode = HttpUtils.sendPost(url, JsonNode.class, list, null);
        //处理结果
        if (jsonNode.get("error_message") == null) {
            System.out.println(jsonNode.get("faceset_token").asText());
            return jsonNode.get("faceset_token").asText();
        } else {
            return null;
        }
    }

    /**
     * 把faceToken添加到FaceSetToken
     *
     * @param faceSetToken
     * @param faceToken
     * @return
     */
    @Override
    public boolean addFace(String faceSetToken, List<String> faceToken) {
        if (StringUtils.isEmpty(faceSetToken) || faceToken == null) {
            return false;
        }
        //封装参数
        String url = faceDomain.getFacePlusAddFace();
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("api_secret", API_SECRET));
        list.add(new BasicNameValuePair("api_key", API_KEY));
        list.add(new BasicNameValuePair("faceset_token", faceSetToken));
        if (faceToken.size() < 5) {
            String faceTokenStr = faceToken.stream().collect(Collectors.joining(","));
            list.add(new BasicNameValuePair("face_tokens", faceTokenStr));
            JsonNode jsonNode = HttpUtils.sendPost(url, JsonNode.class, list, null);
            if (jsonNode.get("error_message") == null) {
                return true;
            }
            return false;
        } else {
            int limit = (int) Math.ceil(faceToken.size() / 5.0);
            Stream.iterate(0, n -> n + 1).limit(limit).forEach(e -> {
                String res = faceToken.stream().skip(e * 5).limit(5).collect(Collectors.joining(","));
                list.add(new BasicNameValuePair("face_tokens", res));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                JsonNode jsonNode = HttpUtils.sendPost(url, JsonNode.class, list, null);
                if (jsonNode.get("error_message") == null) {
                    log.info("填入成功");
                }
                list.remove(3);
            });

            return true;
        }
    }

    /**
     * 进行侦测,获取到facetoken后添加到faceSetToken
     *
     * @param activityPhoto
     * @param faceSetToken
     * @return
     */
    @Override
    public List<String> detect(ActivityPhoto activityPhoto, String faceSetToken, boolean isAddFace) {
        List<String> resList = null;
        if (activityPhoto == null)
            return null;
        try {
            //封装参数
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("api_secret", API_SECRET));
            list.add(new BasicNameValuePair("api_key", API_KEY));
            String imageBase64 = ImgUtil.getImageBase64(activityPhoto.getPhotoPathUrl());
            list.add(new BasicNameValuePair("image_base64", imageBase64));
            //发送请求
            //避免请求频繁
            Thread.sleep(100);
            JsonNode jsonNode = HttpUtils.sendPost(faceDomain.getFacePlusDetect(), JsonNode.class, list, null);
            //处理结果
            if (jsonNode.get("error_message") == null) {
                JsonNode faces = jsonNode.get("faces");
                resList = getFaceToken(faces, false);
                //faceToken存入DB
                if (isAddFace == true) {
                    if (addFace(faceSetToken, resList)) {
                        resList.stream().forEach(e -> {
                            ActivityPhotoToken activityPhotoToken = new ActivityPhotoToken();
                            activityPhotoToken.setFaceToken(e);//1
                            activityPhotoToken.setAlbumId(activityPhoto.getAlbumId());//2
                            activityPhotoToken.setUserId(activityPhoto.getUserId());//3
                            activityPhotoToken.setPhotoId(activityPhoto.getId());//4
                            activityPhotoToken.setActivityId(activityPhoto.getActivityId());//5
                            activityPhotoTokenService.save(activityPhotoToken);
                        });
                    }
                }
                if (isAddFace == false) {
                    return resList;
                }
            }
            return resList;
        } catch (Exception e) {
            log.info("FacePlusServerImpl.detect", e);
            return resList;
        }
    }

    @Override
    public List<String> searchByFaceToken(String faceSetToken, String faceToken) {
        if (StringUtils.isEmpty(faceSetToken) || StringUtils.isEmpty(faceToken)) return null;
        List<String> resList = null;
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("api_secret", API_SECRET));
        list.add(new BasicNameValuePair("api_key", API_KEY));
        list.add(new BasicNameValuePair("face_token", faceToken));
        list.add(new BasicNameValuePair("faceset_token", faceSetToken));
        list.add(new BasicNameValuePair("return_result_count", "1"));
        JsonNode jsonNode = HttpUtils.sendPost(faceDomain.getFacePlusSearch(), JsonNode.class, list, null);
        if (jsonNode.get("error_message") == null) {
            JsonNode faces = jsonNode.get("results");
            resList = getReslutUserId(faces);
        }
        return resList;
    }

    @Override
    public boolean addFace(String faceSetToken, String faceToken, String userId) {
        if (StringUtils.isEmpty(faceSetToken) || faceToken == null || StringUtils.isEmpty(userId)) {
            return false;
        }
        //封装参数
        String url = faceDomain.getFacePlusAddFace();
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("api_secret", API_SECRET));
        list.add(new BasicNameValuePair("api_key", API_KEY));
        list.add(new BasicNameValuePair("faceset_token", faceSetToken));
        list.add(new BasicNameValuePair("face_tokens", faceToken));
        JsonNode jsonNode = HttpUtils.sendPost(url, JsonNode.class, list, null);
        if (jsonNode.get("error_message") == null) {
            return setUserId(faceToken,userId);
        }
        return false;
    }

    @Override
    public boolean setUserId(String faceToken, String userId) {
        if(StringUtils.isEmpty(faceToken) || StringUtils.isEmpty(userId)) return false;
        String url = faceDomain.getSetUserId();
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("api_secret", API_SECRET));
        list.add(new BasicNameValuePair("api_key", API_KEY));
        list.add(new BasicNameValuePair("user_id", userId));
        list.add(new BasicNameValuePair("face_token", faceToken));
        JsonNode jsonNode = HttpUtils.sendPost(url, JsonNode.class, list, null);
        if (jsonNode.get("error_message") == null) {
            return true;
        }
        return false;
    }

    /**
     * 查找
     *
     * @param faceSetToken
     * @param imageUrl
     * @return
     */
    @Override
    public List<String> search(String faceSetToken, String imageUrl) {
        if (StringUtils.isEmpty(imageUrl) || StringUtils.isEmpty(faceSetToken)) return null;
        List<String> resList = null;
        //封装参数
        List<NameValuePair> list = new ArrayList<>();
        String imageBase64 = ImgUtil.getImageBase64(imageUrl);
        list.add(new BasicNameValuePair("api_secret", API_SECRET));
        list.add(new BasicNameValuePair("api_key", API_KEY));
        list.add(new BasicNameValuePair("image_base64", imageBase64));
        list.add(new BasicNameValuePair("faceset_token", faceSetToken));
        list.add(new BasicNameValuePair("return_result_count", "5"));
        //发送请求
        JsonNode jsonNode = HttpUtils.sendPost(faceDomain.getFacePlusSearch(), JsonNode.class, list, null);
        //处理结果
        if (jsonNode.get("error_message") == null) {
            JsonNode faces = jsonNode.get("results");
            resList = getFaceToken(faces, true);
        }
        return resList;
    }

    @Override
    public List<String> searchByFile(String faceSetToken, InputStream is, String imageType) {
        if (StringUtils.isEmpty(faceSetToken)) return null;
        List<String> resList = null;
        //封装参数
        //压缩图片
//        InputStream compressInputStream = ImgUtil.getCompressInputStream(is, imageType);
        InputStreamBody inputStreamBody = new InputStreamBody(is, "face." + imageType);
//        FileBody fileBody = new FileBody());
        try {
            HttpEntity imageFile = MultipartEntityBuilder.create()
                    .addPart("api_key", new StringBody(API_KEY, Charset.forName("utf-8")))
                    .addPart("api_secret", new StringBody(API_SECRET, Charset.forName("utf-8")))
                    .addPart("image_file", inputStreamBody)
                    .addPart("faceset_token", new StringBody(faceSetToken, Charset.forName("utf-8")))
                    .addPart("return_result_count", new StringBody("5", Charset.forName("utf-8")))
                    .build();
            //发送请求
            JsonNode jsonNode = HttpUtils.sendPost(faceDomain.getFacePlusSearch(), JsonNode.class, null, imageFile);
            //处理结果
            if (jsonNode.get("error_message") == null) {
                JsonNode faces = jsonNode.get("results");
                resList = getFaceToken(faces, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }

    @Override
    public boolean removeFace(String faceSetToken, List<String> faceTokens) {
        if (StringUtils.isEmpty(faceSetToken) || faceTokens == null || faceTokens.size() == 0) return false;
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("api_secret", API_SECRET));
        list.add(new BasicNameValuePair("api_key", API_KEY));
        list.add(new BasicNameValuePair("faceset_token", faceSetToken));
        String faceTokensStr = faceTokens.stream().collect(Collectors.joining(","));
        list.add(new BasicNameValuePair("face_tokens", faceTokensStr));
        JsonNode jsonNode = HttpUtils.sendPost(faceDomain.getFacePlusRemove(), JsonNode.class, list, null);
        if (jsonNode.get("error_message") == null) {
            return true;
        }
        return false;
    }

    //工具类
    private List<String> getFaceToken(JsonNode jsonNode, boolean isFilter) {
        if (jsonNode == null || jsonNode.size() == 0) {
            return null;
        }
        Iterator<JsonNode> iterator = jsonNode.iterator();
        List<String> resList = new ArrayList<>();
        while (iterator.hasNext()) {
            JsonNode next = iterator.next();
            if (isFilter) {
                int confidence = next.get("confidence").asInt();
                //相关度>85
                if (confidence >= 80) {
                    String s = next.get("face_token").asText();
                    resList.add(s);
                }
            } else {
                String s = next.get("face_token").asText();
                resList.add(s);
            }
        }
        return resList;
    }

    //工具类
    private List<String> getReslutUserId(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.size() == 0) {
            return null;
        }
        Iterator<JsonNode> iterator = jsonNode.iterator();
        List<String> resList = new ArrayList<>();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            int confidence = node.get("confidence").asInt();
            if (confidence >= 80) {
//                String faceToken = node.get("face_token").asText();
                String userId = node.get("user_id").asText();
                resList.add(userId);
            }
        }
        return resList;
    }


}
