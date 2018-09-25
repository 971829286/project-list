package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.*;
import cn.ourwill.tuwenzb.mapper.ActivityAlbumMapper;
import cn.ourwill.tuwenzb.mapper.ActivityMapper;
import cn.ourwill.tuwenzb.mapper.ActivityPhotoMapper;
import cn.ourwill.tuwenzb.service.*;
import cn.ourwill.tuwenzb.utils.*;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivityPhotoServiceImpl extends BaseServiceImpl<ActivityPhoto> implements IActivityPhotoService {
    @Autowired
    private ActivityPhotoMapper activityPhotoMapper;
    @Autowired
    private ActivityAlbumMapper activityAlbumMapper;
    @Autowired
    IActivityAlbumService activityAlbumService;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private IQiniuService  qiniuService;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    FaceDomain faceDomain;

    @Autowired
    IFacePlusServer facePlusServer;

    @Autowired
    IActivityService activityService;

    @Autowired
    IActivityPhotoTokenService activityPhotoTokenService;
    @Autowired
    IFaceServer                faceServer;

    @Autowired
    AsyncFaceServerImpl asyncFaceServer;

    @Value("${upload.photo.bucketDomain}")
    private String bucketDomain;

    @Override
    @Transactional
    public Map<String, List<String>> batchSave(Integer userId, Integer albumId, List<ActivityPhoto> photoList, Integer photoStatus) {
        ActivityAlbum album = activityAlbumMapper.getById(albumId);
        List<String> truePaths = new ArrayList<>();
        List<String> falsePaths = new ArrayList<>();
        List<ActivityPhoto> disPhotoList = new ArrayList<>();
        disPhotoList.addAll(photoList);
        //过滤重复照片
        List<ActivityPhoto> AllPhotos = activityPhotoMapper.selectByAlbumId(albumId, null, null);
        for (ActivityPhoto photo : photoList) {
            for (ActivityPhoto activityPhoto : AllPhotos) {
                if (activityPhoto.getPhotoName() != null && activityPhoto.getPhotoName().equals(photo.getPhotoPath().substring(photo.getPhotoPath().lastIndexOf("/") + 1, photo.getPhotoPath().lastIndexOf("_")) + photo.getPhotoPath().substring(photo.getPhotoPath().lastIndexOf(".")))) {
                    falsePaths.add(photo.getPhotoPath());
                    disPhotoList.remove(photo);
                    break;
                }
            }
        }
        List<String> newPaths = qiniuService.dataPersistence(album.getActivityId(), albumId, disPhotoList);
        List<ActivityPhoto> photos = new ArrayList<>();
        for (int i = 0; i < newPaths.size(); i++) {
            if (newPaths.get(i) == null) {
                falsePaths.add(disPhotoList.get(i).getPhotoPath());
                continue;
            } else {
                truePaths.add(disPhotoList.get(i).getPhotoPath());
            }
            ActivityPhoto activityPhoto = new ActivityPhoto();
            activityPhoto.setAlbumId(albumId);
            activityPhoto.setUserId(userId);
            activityPhoto.setPhotoPath(newPaths.get(i));
            if (disPhotoList.get(i).getCTime() != null) {
                activityPhoto.setCTime(new Date());
                activityPhoto.setUTime(new Date());
            }
            activityPhoto.setPhotoStatus(photoStatus);
            photos.add(activityPhoto);
        }
//        List<ActivityPhoto> photos = newPaths.stream().filter(photo -> photo!=null).map(photo -> {
//            ActivityPhoto activityPhoto = new ActivityPhoto();
//            activityPhoto.setAlbumId(albumId);
//            activityPhoto.setUserId(userId);
//            activityPhoto.setPhotoPath(photo);
//            activityPhoto.setCTime(new Date());
//            return activityPhoto;
//        }).collect(Collectors.toList());
        Map reMap = new HashMap();
        reMap.put("successList", truePaths);
        reMap.put("errorList", falsePaths);
        if (photos.size() > 0) {
            qiniuService.redExif(photos);
            activityPhotoMapper.batchSave(photos);
            Activity activity = activityService.getById(album.getActivityId());

/*                String faceSetToken = activityService.getFaceSetTokenByFacePlus(activity.getId());
                photos.stream().forEach(e -> {
                    e.setActivityId(activity.getId());
                    facePlusServer.detect(e, faceSetToken);
                    //更新标志位
                    this.updateIsSaveTOFaceSet(e.getId(),1);
                });*/
                asyncFaceServer.syncSave(activity,photos);

        }
        return reMap;
    }

    @Override
    @Transactional
    public Map<String, List<String>> batchSaveByApp(Integer userId, Integer albumId, List<ActivityPhoto> photoList, Integer photoStatus) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        ActivityAlbum album = activityAlbumMapper.getById(albumId);
        List<String> newPaths = qiniuService.dataPersistenceApp(album.getActivityId(), albumId, photoList);
        List<String> truePaths = new ArrayList<>();
        List<String> falsePaths = new ArrayList<>();
        List<ActivityPhoto> photos = new ArrayList<>();
        String photoName = "";
        String photoPath = "";
        for (int i = 0; i < newPaths.size(); i++) {
            if (newPaths.get(i) == null) {
                falsePaths.add(photoList.get(i).getPhotoPath());
                continue;
            } else {
                truePaths.add(photoList.get(i).getPhotoPath());
            }
            ActivityPhoto activityPhoto = new ActivityPhoto();
            activityPhoto.setAlbumId(albumId);
            if (StringUtil.isNotEmpty(photoList.get(i).getAppReadTime()))
                try {
                    activityPhoto.setShootingTime(sdf.parse(photoList.get(i).getAppReadTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            activityPhoto.setUserId(userId);
            activityPhoto.setPhotoPath(newPaths.get(i));
            activityPhoto.setCTime(new Date());
            activityPhoto.setUTime(new Date());
            activityPhoto.setPhotoStatus(photoStatus);
            photoName = photoList.get(i).getPhotoName();
            photoPath = photoList.get(i).getPhotoPath();
            if (StringUtil.isNotEmpty(photoName)) {
                activityPhoto.setPhotoName(photoName);
                activityPhoto.setDownloadName(photoName.replace(photoName.substring(photoName.lastIndexOf(".")), photoPath.substring(photoPath.lastIndexOf("_"))));
            }
            photos.add(activityPhoto);
        }
        Map reMap = new HashMap();
        reMap.put("successList", truePaths);
        reMap.put("errorList", falsePaths);
        if (photos.size() > 0)
            qiniuService.redExif(photos);
        activityPhotoMapper.batchSave(photos);

        //入库
        Activity activity = activityService.getById(album.getActivityId());
//            String faceSetToken = activityService.getFaceSetTokenByFacePlus(activity.getId());
//            photos.stream().forEach(e -> {
//                e.setActivityId(activity.getId());
//                facePlusServer.detect(e, faceSetToken);
//                //更新标志位
//                this.updateIsSaveTOFaceSet(e.getId(),1);
//            });
            asyncFaceServer.syncSave(activity,photos);
        return reMap;
    }

    @Override
    public Integer update(ActivityPhoto photo) {
        ArrayList list = new ArrayList();
        list.add(photo);
        qiniuService.redExif(list);
        photo.setUTime(new Date());
        Integer res = activityPhotoMapper.update(photo);
        //
        if (res != null && res > 0) {
            //重新入库
            //0.删除faceset
            Activity activity = activityService.getByPhotoId(photo.getId());
//                String faceSetToken = activityService.getFaceSetTokenByFacePlus(activity.getId());
//                List<ActivityPhotoToken> activityPhotoTokens = activityPhotoTokenService.getByPhotoId(photo.getId());
//                if (activityPhotoTokens != null && activityPhotoTokens.size() > 0) {
//                    List<String> faceTokens = activityPhotoTokens.stream().map(e -> e.getFaceToken()).collect(Collectors.toList());
//                    facePlusServer.removeFace(faceSetToken, faceTokens);
//                }
//
//                //1.删除原图片
//                activityPhotoTokenService.deleteByPhotoId(photo.getId());
//                //2.入库
//                ActivityPhoto activityPhoto = activityPhotoMapper.getById(photo.getId());
//                activityPhoto.setActivityId(activity.getId());
//                facePlusServer.detect(activityPhoto, faceSetToken);
//                this.updateIsSaveTOFaceSet(activityPhoto.getId(),1);
                asyncFaceServer.syncUpdate(activity,photo);
        }
        return res;
    }

    @Override
    @Transactional
    public Integer batchDelete(List<Integer> photoIds, Integer albumId) {
        List<ActivityPhoto> activityPhotos = activityPhotoMapper.selectNoPublishedByIds(photoIds);
        if (activityPhotos == null || activityPhotos.size() < 1)
            return 0;
        List<String> paths = activityPhotos.stream().map(photo -> {
            redisTemplate.delete("com:" + photo.getId());
            return photo.getPhotoPath();
        }).collect(Collectors.toList());
        qiniuService.delete(paths);
        Integer count = activityPhotoMapper.batchDelete(photoIds, albumId);
        if (count > 0) {
            ActivityAlbum album = activityAlbumMapper.getById(albumId);
            if(album != null) {
                String faceSetToken = activityService.getFaceSetTokenByFacePlus(album.getActivityId());
                Activity activity = activityService.getById(album.getActivityId());
                //删除Token中的照片
                activityPhotos.stream().forEach(e->{
                    List<ActivityPhotoToken> activityPhotoTokens = activityPhotoTokenService.getByPhotoId(e.getId());
                    if (activityPhotoTokens != null && activityPhotoTokens.size() > 0) {
                        List<String> faceTokens = activityPhotoTokens.stream().map(a -> a.getFaceToken()).collect(Collectors.toList());
                        if(activity != null && activity.getIsOpenFaceSearch() == 1) {
                            facePlusServer.removeFace(faceSetToken, faceTokens);
                        }
                    }
                    activityPhotoTokenService.deleteByPhotoId(e.getId());
                });
            }
        }
        return count;
    }

    @Override
    public List<ActivityPhoto> selectByAlbumId(Integer albumId, Integer photoStatus, Integer orderBy) {
        String orderByStr = "order by id desc";
        if (orderBy != null && orderBy.equals(0))
            orderByStr = "order by shooting_time asc";
        if (orderBy != null && orderBy.equals(1))
            orderByStr = "order by shooting_time desc";
        if (orderBy != null && orderBy.equals(2))
            orderByStr = "order by c_time asc";
        if (orderBy != null && orderBy.equals(3))
            orderByStr = "order by c_time desc";
        return activityPhotoMapper.selectByAlbumId(albumId, photoStatus, orderByStr);
    }

    @Override
    public Integer batchPublish(List<Integer> photoIds, Integer albumId, Integer photoStatus) {
        return activityPhotoMapper.batchPublish(photoIds, albumId, photoStatus);
    }

    @Override
    public boolean addLike(Integer commentId, Integer userId) {
        return redisTemplate.opsForZSet().add("photoLike:" + commentId, userId, System.currentTimeMillis());
    }

    @Override
    public boolean cancelLike(Integer commentId, Integer userId) {
        Long result = redisTemplate.opsForZSet().remove("photoLike:" + commentId, userId);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isLiked(Integer commentId, Integer userId) {
        Double result = redisTemplate.opsForZSet().score("photoLike:" + commentId, userId);
        if (result == null) {
            return false;
        }
        return true;
    }

    @Override
    public Integer getLikeNum(Integer commentId) {
        return redisTemplate.opsForZSet().zCard("photoLike:" + commentId).intValue();
    }

    @Override
    public Map replacePhoto(String photoPath, ActivityPhoto originPhoto) {
        //持久化新图
        Activity activity = activityMapper.selectByAlbumId(originPhoto.getAlbumId());
        String oldPath = originPhoto.getPhotoPath();//原图
        String newPath = qiniuService.dataPersistence(activity.getId(), originPhoto.getAlbumId(), photoPath);
        if (StringUtil.isNotEmpty(newPath)) {
            originPhoto.setPhotoPath(newPath);
            //更新图片信息
            qiniuService.redExif(originPhoto);
            originPhoto.setReplaceStatus(1);
            originPhoto.setUTime(new Date());
            //保存新图
            int count = activityPhotoMapper.update(originPhoto);
            if (count > 0) {
                //处理旧图片
                qiniuService.delete(oldPath);

                //入库
                //0,删除faceToken
                if(activity != null && activity.getIsOpenFaceSearch() == 1) {
/*                    String faceSetToken = activityService.getFaceSetTokenByFacePlus(activity.getId());
                    List<ActivityPhotoToken> activityPhotoTokens = activityPhotoTokenService.getByPhotoId(originPhoto.getId());
                    if (activityPhotoTokens != null && activityPhotoTokens.size() > 0) {
                        List<String> faceTokens = activityPhotoTokens.stream().map(e -> e.getFaceToken()).collect(Collectors.toList());
                        facePlusServer.removeFace(faceSetToken, faceTokens);
                    }
                    //1.删除人脸库中的照片
                    activityPhotoTokenService.deleteByPhotoId(originPhoto.getId());
                    //2.重新入库
                    originPhoto.setActivityId(activity.getId());
                    facePlusServer.detect(originPhoto, faceSetToken);
                    this.updateIsSaveTOFaceSet(originPhoto.getId(),1);*/
                    asyncFaceServer.syncReplace(activity,originPhoto);
                }
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        } else {
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }
    }

    @Override
    public Map replacePhotoByName(String photoPath, Integer activityId, Integer albumId) {
        String downloadName = photoPath.substring(photoPath.lastIndexOf("/") + 1, photoPath.lastIndexOf("_")) + photoPath.substring(photoPath.lastIndexOf("."));
        ActivityPhoto originPhoto = activityPhotoMapper.getByDownloadName(downloadName, albumId);
        if (originPhoto != null) {
            //持久化新图
            String oldPath = originPhoto.getPhotoPath();//原图
            String oldPathCopy = oldPath + System.currentTimeMillis();
            Boolean copyStauts = qiniuService.reName(oldPath, oldPathCopy);
            if (copyStauts) {
                String newPath = qiniuService.dataPersistenceForReplace(activityId, originPhoto.getAlbumId(), photoPath);
                if (StringUtil.isNotEmpty(newPath)) {
                    originPhoto.setPhotoPath(newPath);
                    //更新图片信息
                    qiniuService.redExif(originPhoto);
                    originPhoto.setReplaceStatus(1);
                    originPhoto.setUTime(new Date());
                    //保存新图
                    int count = activityPhotoMapper.update(originPhoto);
                    if (count > 0) {
                        //处理旧图片
                        qiniuService.delete(oldPathCopy);

                        //入库
                        //删除faceSet
                        Activity activity = activityService.getById(activityId);
                        if(activity != null && activity.getIsOpenFaceSearch() == 1) {
/*                            String faceSetToken = activityService.getFaceSetTokenByFacePlus(activityId);
                            List<ActivityPhotoToken> activityPhotoTokens = activityPhotoTokenService.getByPhotoId(originPhoto.getId());
                            if (activityPhotoTokens != null && activityPhotoTokens.size() > 0) {
                                List<String> faceTokens = activityPhotoTokens.stream().map(e -> e.getFaceToken()).collect(Collectors.toList());
                                facePlusServer.removeFace(faceSetToken, faceTokens);
                            }
                            //1.删除人脸库中的照片
                            activityPhotoTokenService.deleteByPhotoId(originPhoto.getId());
                            //2.重新入库
                            originPhoto.setActivityId(activityId);
                            facePlusServer.detect(originPhoto, faceSetToken);
                            this.updateIsSaveTOFaceSet(originPhoto.getId(),1);
                            */
                            asyncFaceServer.syncReplace(activity,originPhoto);

                        }
                        return ReturnResult.successResult("data", bucketDomain + newPath, ReturnType.UPDATE_SUCCESS);
                    } else {
                        qiniuService.delete(newPath);
                        qiniuService.reName(oldPathCopy, oldPath);
                        return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
                    }
                } else {
                    return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
                }
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        }
        return ReturnResult.errorResult("替换失败，匹配不到该文件名!");
    }

    @Override
    public List<Integer> photoMove(Integer activityId, Integer fAlbumId, Integer tAlbumId, List<Integer> photoIds) {
        List<ActivityPhoto> photos = activityPhotoMapper.selectNoPublishedByIds(photoIds);
        //1 为该相册生成人脸库
//        String faceSetToken = activityService.getFaceSetTokenByFacePlus(activityId);
        ActivityPhotoToken photoToken = new ActivityPhotoToken();
        //更新列表
        //
        List<Integer> successList = null;
        if (photos != null && photos.size() > 0) {
            successList = qiniuService.movePath(activityId, fAlbumId, tAlbumId, photos);
            photoIds.stream().forEach(e -> {
                photoToken.setAlbumId(tAlbumId);
                photoToken.setPhotoId(e);
                photoToken.setActivityId(activityId);
                activityPhotoTokenService.update(photoToken);
            });
        }

        return successList;
    }

    @Override
    public Integer updateDownLoadStatus(Integer photoId) {
        return activityPhotoMapper.updateDownLoadStatus(photoId);
    }

    @Override
    public Integer updateReplaceStatus(Integer photoId) {
        return activityPhotoMapper.updateReplaceStatus(photoId);
    }

    @Override
    public ActivityPhoto getByDownloadName(String downloadName, Integer albumId) {
        return activityPhotoMapper.getByDownloadName(downloadName, albumId);
    }

    @Override
    public ActivityPhoto getByPhotoName(String photoName, Integer albumId) {
        return activityPhotoMapper.getByPhotoName(photoName, albumId);
    }

    /**
     * 插入日志
     *
     * @param albumId
     * @param userId
     * @param count
     * @param operaType
     * @return
     */
    @Override
    public Integer addPhotoLog(Integer albumId, Integer userId, int count, int operaType) {
        PhotoLog photoLog = new PhotoLog();
        photoLog.setAlbumId(albumId);
        photoLog.setUserId(userId);
        photoLog.setNumber(count);
        photoLog.setOperaType(operaType);
        return activityPhotoMapper.insertLog(photoLog);
    }

    /**
     * 查询更新数量
     *
     * @param albumId
     * @param time
     * @return
     */
    @Override
    public Map selectCountAfterUTime(Integer albumId, Integer photoStatus, Date time) {
        Map reMap = activityPhotoMapper.selectCountAfterUTime(albumId, photoStatus, time);
        Integer count = activityPhotoMapper.selectCountByAlbumId(albumId, photoStatus);
        reMap.put("count", count);
        return reMap;
    }

    @Override
    public List<ActivityPhoto> selectByActivityId(Integer activityId, Integer photoStatus, Integer orderBy) {
        String orderByStr = "order by id desc";
        if (orderBy != null && orderBy.equals(0))
            orderByStr = "order by shooting_time asc";
        if (orderBy != null && orderBy.equals(1))
            orderByStr = "order by shooting_time desc";
        if (orderBy != null && orderBy.equals(2))
            orderByStr = "order by c_time asc";
        if (orderBy != null && orderBy.equals(3))
            orderByStr = "order by c_time desc";
        return activityPhotoMapper.selectByActivityId(activityId, photoStatus, orderByStr);
    }

    @Override
    public Integer getPhotoCount(Integer activityId) {
        return activityPhotoMapper.getPhotoCount(activityId);
    }

    @Override
    public List<ActivityPhoto> getByFaceToken(String faceToken) {
        return activityPhotoMapper.getByFaceToken(faceToken);
    }

    @Override
    public List<ActivityPhoto> getByFaceTokenList(List<String> faceTokens,Integer activityId) {
        return activityPhotoMapper.getByFaceTokenList(faceTokens,activityId);
    }

    @Override
    public List<ActivityPhoto> selectByActivityIdAndIsSaveToFaceSet(Integer activityId, Integer isSaveToFaceSet) {
       return activityPhotoMapper.selectByActivityIdAndIsSaveToFaceSet(activityId,isSaveToFaceSet);
    }

    @Override
    public Integer updateIsSaveTOFaceSet(Integer id, Integer isSaveToFaceSet) {
        return activityPhotoMapper.updateIsSaveTOFaceSet(id, isSaveToFaceSet);
    }

    @Override
    public List<ActivityPhoto> getByIds(List<Integer> ids) {
        return activityPhotoMapper.selectByIds(ids);
    }

    @Override
    public List<String> getPathByIds(List<Integer> ids) {
        return activityPhotoMapper.selectPathByIds(ids);
    }

    @Override
    public List<String> getUrlsByIds(List<Integer> ids) {
        List<String> paths = getPathByIds(ids);
        List<String> urls = paths.stream().map(path->{
            if(StringUtils.isNotEmpty(path)&&path.indexOf("http")<0) {
                return bucketDomain + path;
            }
            return path;
        }).collect(Collectors.toList());
        return urls;
    }


//
//    @Override
//    @Transactional
//    public Integer batchUpdateInfo(Integer size) {
//        List<ActivityPhoto> list = activityPhotoMapper.selectWithoutInfo(size);
//        qiniuService.redExif(list);
//        return activityPhotoMapper.batchUpdateInfo(list);
//    }
}
