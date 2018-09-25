package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import cn.ourwill.tuwenzb.entity.ActivityPhotoToken;
import cn.ourwill.tuwenzb.mapper.ActivityMapper;
import cn.ourwill.tuwenzb.mapper.ActivityPhotoMapper;
import cn.ourwill.tuwenzb.service.IActivityPhotoService;
import cn.ourwill.tuwenzb.service.IActivityPhotoTokenService;
import cn.ourwill.tuwenzb.service.IActivityService;
import cn.ourwill.tuwenzb.service.IFacePlusServer;
import cn.ourwill.tuwenzb.utils.FaceDomain;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:人脸识别异步同步
 * @author: XuJinNiu
 * @create: 2018-06-06 11:09
 **/
@Service
@EnableAsync
public class AsyncFaceServerImpl {
    @Autowired
    FaceDomain faceDomain;

    @Autowired
    IFacePlusServer facePlusServer;

    @Autowired
    IActivityService activityService;

    @Autowired
    IActivityPhotoTokenService activityPhotoTokenService;
    @Autowired
    IActivityPhotoService      activityPhotoService;

    @Autowired
    private ActivityPhotoMapper activityPhotoMapper;

    @Async
    public Future<Boolean> syncSave(Activity activity, List<ActivityPhoto> photos) {
        if (activity != null && activity.getIsOpenFaceSearch() == 1) {
            String faceSetToken = activityService.getFaceSetTokenByFacePlus(activity.getId());
            photos.stream().forEach(e -> {
                e.setActivityId(activity.getId());
                facePlusServer.detect(e, faceSetToken, true);
                //更新标志位
                activityPhotoService.updateIsSaveTOFaceSet(e.getId(), 1);
            });
        }
        if (activity != null && activity.getIsOpenFaceSearch() == 2) {
            String faceSetToken = activityService.getFaceSetTokenByFacePlus(activity.getId());
            for (ActivityPhoto activityPhoto : photos) {
                activityPhoto.setActivityId(activity.getId());
                //每张照片都去侦测,获取faceSet
                List<String> faceTokens = facePlusServer.detect(activityPhoto, faceSetToken, false);
                for (String faceToken : faceTokens) {
                    //每个faceSet都去查一下,看是否在face++的库中
                    List<String> resList = facePlusServer.searchByFaceToken(faceSetToken, faceToken);
                    //存在
                    if (resList != null && resList.size() > 0) {
                        for (String userId : resList) {
                            //1. 将照片存入db
                            ActivityPhotoToken activityPhotoTokenTMP = new ActivityPhotoToken();
                            activityPhotoTokenTMP.setFaceToken(faceToken);//1
                            activityPhotoTokenTMP.setAlbumId(activityPhoto.getAlbumId());//2
                            activityPhotoTokenTMP.setUserId(activityPhoto.getUserId());//3
                            activityPhotoTokenTMP.setPhotoId(activityPhoto.getId());//4
                            activityPhotoTokenTMP.setActivityId(activity.getId());//5
                            activityPhotoTokenTMP.setSamePerson(userId);
                            activityPhotoTokenService.save(activityPhotoTokenTMP);
                            activityPhotoService.updateIsSaveTOFaceSet(activityPhoto.getId(), 2);
                        }
                    } else {
                        //不存在
                        //1. 将本照片直接存入DB
                        //2. 将本照片存入face++
                        String samePerson = UUID.randomUUID().toString();
                        facePlusServer.addFace(faceSetToken, faceToken, samePerson);
                        ActivityPhotoToken activityPhotoToken = new ActivityPhotoToken();
                        activityPhotoToken.setFaceToken(faceToken);//1
                        activityPhotoToken.setAlbumId(activityPhoto.getAlbumId());//2
                        activityPhotoToken.setUserId(activityPhoto.getUserId());//3
                        activityPhotoToken.setPhotoId(activityPhoto.getId());//4
                        activityPhotoToken.setActivityId(activity.getId());//5
                        activityPhotoToken.setSamePerson(samePerson);
                        activityPhotoTokenService.save(activityPhotoToken);
                        activityPhotoService.updateIsSaveTOFaceSet(activityPhoto.getId(), 2);
                    }
                }
            }
        }
        return new AsyncResult<>(true);
    }

    @Async
    public Future<Boolean> syncUpdate(Activity activity, ActivityPhoto photo) {
        if (activity != null && activity.getIsOpenFaceSearch() == 1) {
            String faceSetToken = activityService.getFaceSetTokenByFacePlus(activity.getId());
            List<ActivityPhotoToken> activityPhotoTokens = activityPhotoTokenService.getByPhotoId(photo.getId());
            if (activityPhotoTokens != null && activityPhotoTokens.size() > 0) {
                List<String> faceTokens = activityPhotoTokens.stream().map(e -> e.getFaceToken()).collect(Collectors.toList());
                facePlusServer.removeFace(faceSetToken, faceTokens);
            }
            //1.删除原图片
            activityPhotoTokenService.deleteByPhotoId(photo.getId());
            //2.入库
            ActivityPhoto activityPhoto = activityPhotoMapper.getById(photo.getId());
            activityPhoto.setActivityId(activity.getId());
            facePlusServer.detect(activityPhoto, faceSetToken, true);
            activityPhotoService.updateIsSaveTOFaceSet(activityPhoto.getId(), 1);
        }
        if (activity != null && activity.getIsOpenFaceSearch() == 2) {
            //1.删除原图片
            activityPhotoTokenService.deleteByPhotoId(photo.getId());
            //2.入库
            String faceSetToken = activityService.getFaceSetTokenByFacePlus(activity.getId());
            ActivityPhoto activityPhoto = activityPhotoMapper.getById(photo.getId());
            List<String> faceTokens = facePlusServer.detect(activityPhoto, faceSetToken, false);
            for (String faceToken : faceTokens) {
                //每个faceSet都去查一下,看是否在face++的库中
                List<String> resList = facePlusServer.searchByFaceToken(faceSetToken, faceToken);
                //存在
                if (resList != null && resList.size() > 0) {
                    for (String userId : resList) {
                        //1. 将照片存入db
                        ActivityPhotoToken activityPhotoTokenTMP = new ActivityPhotoToken();
                        activityPhotoTokenTMP.setFaceToken(faceToken);//1
                        activityPhotoTokenTMP.setAlbumId(activityPhoto.getAlbumId());//2
                        activityPhotoTokenTMP.setUserId(activityPhoto.getUserId());//3
                        activityPhotoTokenTMP.setPhotoId(activityPhoto.getId());//4
                        activityPhotoTokenTMP.setActivityId(activity.getId());//5
                        activityPhotoTokenTMP.setSamePerson(userId);
                        activityPhotoTokenService.save(activityPhotoTokenTMP);
                        activityPhotoService.updateIsSaveTOFaceSet(activityPhoto.getId(), 2);
                    }
                } else {
                    //不存在
                    //1. 将本照片直接存入DB
                    //2. 将本照片存入face++
                    String samePerson = UUID.randomUUID().toString();
                    facePlusServer.addFace(faceSetToken, faceToken, samePerson);
                    ActivityPhotoToken activityPhotoToken = new ActivityPhotoToken();
                    activityPhotoToken.setFaceToken(faceToken);//1
                    activityPhotoToken.setAlbumId(activityPhoto.getAlbumId());//2
                    activityPhotoToken.setUserId(activityPhoto.getUserId());//3
                    activityPhotoToken.setPhotoId(activityPhoto.getId());//4
                    activityPhotoToken.setActivityId(activity.getId());//5
                    activityPhotoToken.setSamePerson(samePerson);
                    activityPhotoTokenService.save(activityPhotoToken);
                    activityPhotoService.updateIsSaveTOFaceSet(activityPhoto.getId(), 2);
                }
            }

        }
        return new AsyncResult<>(true);
    }

    @Async
    public Future<Boolean> syncReplace(Activity activity, ActivityPhoto originPhoto) {
        if (activity != null && activity.getIsOpenFaceSearch() == 1) {
            String faceSetToken = activityService.getFaceSetTokenByFacePlus(activity.getId());
            List<ActivityPhotoToken> activityPhotoTokens = activityPhotoTokenService.getByPhotoId(originPhoto.getId());
            if (activityPhotoTokens != null && activityPhotoTokens.size() > 0) {
                List<String> faceTokens = activityPhotoTokens.stream().map(e -> e.getFaceToken()).collect(Collectors.toList());
                facePlusServer.removeFace(faceSetToken, faceTokens);
            }
            //1.删除人脸库中的照片
            activityPhotoTokenService.deleteByPhotoId(originPhoto.getId());
            //2.重新入库
            originPhoto.setActivityId(activity.getId());
            facePlusServer.detect(originPhoto, faceSetToken, true);
            activityPhotoService.updateIsSaveTOFaceSet(originPhoto.getId(), 1);
        }
        if (activity != null && activity.getIsOpenFaceSearch() == 2) {
            activityPhotoTokenService.deleteByPhotoId(originPhoto.getId());
            //2.入库
            String faceSetToken = activityService.getFaceSetTokenByFacePlus(activity.getId());
            ActivityPhoto activityPhoto = activityPhotoMapper.getById(originPhoto.getId());
            List<String> faceTokens = facePlusServer.detect(activityPhoto, faceSetToken, false);

            for (String faceToken : faceTokens) {
                //每个faceSet都去查一下,看是否在face++的库中
                List<String> resList = facePlusServer.searchByFaceToken(faceSetToken, faceToken);
                //存在
                if (resList != null && resList.size() > 0) {
                    for (String userId : resList) {
                        //1. 将照片存入db
                        ActivityPhotoToken activityPhotoTokenTMP = new ActivityPhotoToken();
                        activityPhotoTokenTMP.setFaceToken(faceToken);//1
                        activityPhotoTokenTMP.setAlbumId(activityPhoto.getAlbumId());//2
                        activityPhotoTokenTMP.setUserId(activityPhoto.getUserId());//3
                        activityPhotoTokenTMP.setPhotoId(activityPhoto.getId());//4
                        activityPhotoTokenTMP.setActivityId(activity.getId());//5
                        activityPhotoTokenTMP.setSamePerson(userId);
                        activityPhotoTokenService.save(activityPhotoTokenTMP);
                        activityPhotoService.updateIsSaveTOFaceSet(activityPhoto.getId(), 2);
                    }
                } else {
                    //不存在
                    //1. 将本照片直接存入DB
                    //2. 将本照片存入face++
                    String samePerson = UUID.randomUUID().toString();
                    facePlusServer.addFace(faceSetToken, faceToken, samePerson);
                    ActivityPhotoToken activityPhotoToken = new ActivityPhotoToken();
                    activityPhotoToken.setFaceToken(faceToken);//1
                    activityPhotoToken.setAlbumId(activityPhoto.getAlbumId());//2
                    activityPhotoToken.setUserId(activityPhoto.getUserId());//3
                    activityPhotoToken.setPhotoId(activityPhoto.getId());//4
                    activityPhotoToken.setActivityId(activity.getId());//5
                    activityPhotoToken.setSamePerson(samePerson);//6
                    activityPhotoTokenService.save(activityPhotoToken);
                    activityPhotoService.updateIsSaveTOFaceSet(activityPhoto.getId(), 2);
                }
            }
        }
        return new AsyncResult<>(true);
    }
}
