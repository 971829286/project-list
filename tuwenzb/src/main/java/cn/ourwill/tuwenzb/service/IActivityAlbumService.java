package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.ActivityAlbum;

import java.util.List;

public interface IActivityAlbumService extends IBaseService<ActivityAlbum> {
    List<ActivityAlbum> selectByActivity(Integer activityId);
    List<ActivityAlbum> selectByActivity(Integer activityId,Integer userId,Integer type);

    int batchSave(Integer activityId, Integer userId, List<ActivityAlbum> activityAlbums);

    int batchUpdate(Integer activityId, Integer userId, List<ActivityAlbum> activityAlbums);
//    String getFaceSetToken(int id);
//
//    String getFaceSetTokenByFacePlus(int id);
}
