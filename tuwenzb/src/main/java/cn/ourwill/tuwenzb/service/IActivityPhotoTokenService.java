package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.ActivityPhotoToken;

import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-30 16:18
 **/
public interface IActivityPhotoTokenService extends IBaseService<ActivityPhotoToken> {
    @Override
    Integer save(ActivityPhotoToken entity);

    @Override
    Integer update(ActivityPhotoToken entity);

    @Override
    ActivityPhotoToken getById(Integer id);

    @Override
    List<ActivityPhotoToken> findAll();

    @Override
    Integer delete(Integer id);

    Integer deleteByPhotoId(Integer photoId);

    List<ActivityPhotoToken> getByPhotoId(Integer photoId);

    Integer setSamePerson( String samePerson,Integer id);
    List<ActivityPhotoToken> findBySamePerson(String samePerson);

    ActivityPhotoToken findByFaceToken(String faceToken);
}
