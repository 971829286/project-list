package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityAlbum;
import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import cn.ourwill.tuwenzb.entity.ActivityType;
import cn.ourwill.tuwenzb.mapper.ActivityAlbumMapper;
import cn.ourwill.tuwenzb.mapper.ActivityMapper;
import cn.ourwill.tuwenzb.service.*;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class ActivityServiceImpl extends BaseServiceImpl<Activity> implements IActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityAlbumMapper activityAlbumMapper;

    @Autowired
    private IQiniuService qiniuService;

    @Autowired
    private IActivityAlbumService activityAlbumService;

    @Autowired
    private IFacePlusServer facePlusServer;

    @Autowired
    private IActivityPhotoService activityPhotoService;

    @Autowired
    private AsyncFaceServerImpl asyncFaceServer;
    @Override
    @Transactional
    public Integer save(Activity activity) {
        Integer reCount = activityMapper.save(activity);
        if (activity.getPhotoLive() != null && activity.getPhotoLive().equals(1)) {
            //图片持久
            if (StringUtils.isNotEmpty(activity.getBanner())) {
                activity.setBanner(qiniuService.dataPersistence(activity.getId(), null, activity.getBanner()));
                if (activity.getBanner() != null && activity.getBanner().startsWith("temp/"))
                    return 0;
            }
            if (StringUtils.isNotEmpty(activity.getQrCode())) {
                activity.setQrCode(qiniuService.dataPersistence(activity.getId(), null, activity.getQrCode()));
                if (activity.getQrCode() != null && activity.getQrCode().startsWith("temp/"))
                    return 0;
            }
            if (StringUtils.isNotEmpty(activity.getScreen())) {
                activity.setScreen(qiniuService.dataPersistence(activity.getId(), null, activity.getScreen()));
                if (activity.getScreen() != null && activity.getScreen().startsWith("temp/"))
                    return 0;
            }
        }
        activityMapper.update(activity);
        if (activity.getPhotoLive() != null && activity.getPhotoLive().equals(1)) {
            List<ActivityAlbum> albumsList = activity.getActivityAlbums();
            if (albumsList == null) {
                ActivityAlbum album = new ActivityAlbum();
                album.setActivityId(activity.getId());
                album.setAlbumName("默认分区");
                album.setCTime(new Date());
                album.setDefaultFlag(1);
                album.setUserId(activity.getUserId());
                activityAlbumMapper.save(album);
            } else {
                activityAlbumService.batchSave(activity.getId(), activity.getUserId(), albumsList);
            }
        }
        return reCount;
    }

    @Override
    public Integer update(Activity activity) {
        if (activity.getPhotoLive() != null && activity.getPhotoLive().equals(1)) {
            //图片持久
            if (activity.getBanner() != null && activity.getBanner().startsWith("temp/")) {
                activity.setBanner(qiniuService.dataPersistence(activity.getId(), null, activity.getBanner()));
                if (activity.getBanner().startsWith("temp/"))
                    return 0;
            }
            if (activity.getQrCode() != null && activity.getQrCode().startsWith("temp/")) {
                activity.setQrCode(qiniuService.dataPersistence(activity.getId(), null, activity.getQrCode()));
                if (activity.getQrCode().startsWith("temp/"))
                    return 0;
            }
            if (activity.getScreen() != null && activity.getScreen().startsWith("temp/")) {
                activity.setScreen(qiniuService.dataPersistence(activity.getId(), null, activity.getScreen()));
                if (activity.getScreen().startsWith("temp/"))
                    return 0;
            }
            activityAlbumService.batchUpdate(activity.getId(), activity.getUserId(), activity.getActivityAlbums());

        }
        return activityMapper.update(activity);
    }

    //根据用户id查找活动
    @Override
    public List<Activity> selectByUserId(Integer userId, Integer photoLive) {
        return activityMapper.selectByUserId(userId, photoLive);
    }

    @Override
    public List<Activity> selectByParams(Map param) {
        return activityMapper.selectByParam(param);
    }

    @Override
    public boolean checkOwner(HttpServletRequest request, Integer id) {
        Integer userId = GlobalUtils.getUserId(request);
        Integer level = (Integer) GlobalUtils.getSessionValue(request, "userLevel");
        if (level != null && level.equals(1)) return true;
        Activity activity = activityMapper.getById(id);
        if (activity != null && activity.getUserId().equals(userId)) {
            return true;
        }
        return false;
    }

    @Override
    public int close(Integer id) {
        return activityMapper.updateStatus(0, id);
    }

    @Override
    public int open(Integer id) {
        return activityMapper.updateStatus(1, id);
    }

    @Override
    public List<Activity> selectWithOld(Map param) {
        return activityMapper.selectWithOld(param);
    }

    @Override
    public Activity getByIdWithOld(Integer id) {
        return activityMapper.getByIdWithOld(id);
    }

    @Override
    public Integer delete(Integer id) {
        return activityMapper.updateStatus(3, id);
    }

    @Override
    public Integer updataImpower(Integer activityId, Integer isImpower) {
        Activity activity = new Activity();
        activity.setId(activityId);
        activity.setIsImpower(isImpower);
        if (isImpower.equals(0)) {
            RedisUtils.deleteByKey("ImpowerCode:" + activityId);
        } else if (isImpower.equals(1)) {
            String impowerCode = GlobalUtils.getRandomString(6);
            RedisUtils.set("ImpowerCode:" + activityId, impowerCode);
        }
        return activityMapper.update(activity);
    }

    @Override
    public List<ActivityType> getAllType() {
        return activityMapper.getAllType();
    }

    @Override
    public List<String> getAllDate(Integer activityId) {
        return activityMapper.getAllDate(activityId);
    }

    @Override
    public List<Activity> selectAdvance() {
        return activityMapper.selectAdvance();
    }

    @Override
    public Activity selectByAlbumId(Integer albumId) {
        return activityMapper.selectByAlbumId(albumId);
    }

    @Override
    public List<Activity> selectByImpower(Integer userId, Integer photoLive) {
        if (photoLive.equals(1)) {//照片直播
            return activityMapper.selectByImpowerPhotoPC(userId, photoLive, 0);
        }
        return activityMapper.selectByImpower(userId, photoLive);
    }

    @Override
    public Activity getByPhotoId(Integer photoId) {
        return activityMapper.getByPhotoId(photoId);
    }

    @Override
    public Integer updateAutoPublish(Integer activityId, Integer status) {
        return activityMapper.updateAutoPublish(activityId, status);
    }

    @Override
    public List<Activity> selectByImpowerApp(Integer userId, Integer photoLive) {
        return activityMapper.selectByImpowerPhotoPC(userId, photoLive, 1);
    }

    @Override
    public Integer upDateDisplayAlbum(Integer id, Integer userId, Integer displayAlbumId) {
        // 获取当前会议的  相册 id 列表
        List<Integer> ids = activityAlbumMapper.getActivityAlbumIdsByActivityID(id);
        for (Integer i : ids) {
            if (displayAlbumId - i == 0)
                activityAlbumMapper.updateDefaultFlag(id, i, 1);
            else
                activityAlbumMapper.updateDefaultFlag(id, i, 0);
        }
        return activityMapper.pDateDisplayAlbum(id, displayAlbumId);
    }

    /**
     * 获取人脸识别库TOken,如果不存在就去注册
     *
     * @param id
     * @return
     */
    @Override
    public String getFaceSetTokenByFacePlus(int id) {
        Activity activity = activityMapper.getById(id);
        if (activity != null && StringUtils.isEmpty(activity.getFaceSetToken())) {
            String faceSetToken = facePlusServer.getFaceSetToken();
            if (StringUtils.isNotEmpty(faceSetToken)) {
                activity.setFaceSetToken(faceSetToken);
                activityMapper.update(activity);
                return faceSetToken;
            }
            return null;
        }
        return activity.getFaceSetToken();
    }

    @Override
    public Integer setActivityFaceSearch(Integer isOpenFaceSearch, Integer id) {
        Integer flag = 0;
        if (isOpenFaceSearch == 1) {
            flag = activityMapper.setActivityFaceSearch(isOpenFaceSearch, id);
            if (flag > 0) {
                String faceSetToken = this.getFaceSetTokenByFacePlus(id);
                //获取标志位是2和0的照片进行合并
                List<ActivityPhoto> activityPhotos = activityPhotoService.selectByActivityIdAndIsSaveToFaceSet(id, 0);
                List<ActivityPhoto> activityPhotos1 = activityPhotoService.selectByActivityIdAndIsSaveToFaceSet(id, 2);
                activityPhotos.addAll(activityPhotos1);
                activityPhotos.stream().forEach(e -> {
                    e.setActivityId(id);
                    facePlusServer.detect(e, faceSetToken,true);
                    activityPhotoService.updateIsSaveTOFaceSet(e.getId(), 1);
                });
            }
        } else if (isOpenFaceSearch == 2) {
            flag = activityMapper.setActivityFaceSearch(isOpenFaceSearch,id);
            //获取标志位是1和0的照片进行合并
            List<ActivityPhoto> photos = activityPhotoService.selectByActivityIdAndIsSaveToFaceSet(id, 0);
            List<ActivityPhoto> photos2 = activityPhotoService.selectByActivityIdAndIsSaveToFaceSet(id, 1);
            photos.addAll(photos2);
            asyncFaceServer.syncSave(activityMapper.getById(id),photos);
        } else {
            flag = activityMapper.setActivityFaceSearch(isOpenFaceSearch, id);
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityIng(LocalDateTime now) {
        return activityMapper.getActivityIng(now);
    }
}
