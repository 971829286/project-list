package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityPhoto;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/11/1 0001 18:09
 * @Version1.0
 */
public interface IActivityPhotoService extends IBaseService<ActivityPhoto> {
    //批量插入
    Map<String,List<String>> batchSave(Integer userId, Integer albumId, List<ActivityPhoto> photosList, Integer photoStatus);

    //批量插入
    Map<String,List<String>> batchSaveByApp(Integer userId, Integer albumId, List<ActivityPhoto> photosList, Integer photoStatus) throws ParseException;

    @Override
    Integer update(ActivityPhoto photo);

    Integer batchDelete(List<Integer> photoIds, Integer albumId);

    List<ActivityPhoto> selectByAlbumId(Integer albumId,Integer photoStatus,Integer orderBy);

    Integer batchPublish(List<Integer> photoIds,Integer albumId,Integer photoStatus);

    boolean addLike(Integer photoId,Integer userId);

    boolean cancelLike(Integer photoId, Integer userId);

    boolean isLiked(Integer photoId, Integer userId);

    Integer getLikeNum(Integer photoId);

    Map replacePhoto(String photoPath, ActivityPhoto originPhooto);

    Map replacePhotoByName(String photoPath,Integer activityId,Integer albumId);

    List<Integer> photoMove(Integer activityId,Integer fAlbumId, Integer tAlbumId, List<Integer> photoIds);

    Integer updateDownLoadStatus(Integer photoId);

    Integer updateReplaceStatus(Integer photoId);

    ActivityPhoto getByDownloadName(String downloadName, Integer albumId);

    ActivityPhoto getByPhotoName(String photoName, Integer albumId);

    Integer addPhotoLog(Integer albumId, Integer userId, int count, int operaType);

    Map selectCountAfterUTime(Integer albumId, Integer photoStatus, Date time);

    List<ActivityPhoto> selectByActivityId(Integer activityId, Integer photoStatus, Integer orderBy);

    Integer getPhotoCount(Integer activityId);

    List<ActivityPhoto> getByFaceToken(String faceToken);

    List<ActivityPhoto> getByFaceTokenList(List<String> faceTokens,Integer activityId);

    //获取该活动下尚未入人脸库的照片
    List<ActivityPhoto> selectByActivityIdAndIsSaveToFaceSet(Integer activityId,Integer isSaveToFaceSet);

    //更新是否入库的标识
    Integer updateIsSaveTOFaceSet(Integer id,Integer isSaveToFaceSet);

    List<ActivityPhoto> getByIds( List<Integer> ids);

    List<String> getPathByIds( List<Integer> ids);

    List<String> getUrlsByIds( List<Integer> ids);
}
