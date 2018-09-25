package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.ActivityPhotoToken;
import cn.ourwill.tuwenzb.mapper.ActivityPhotoTokenMapper;
import cn.ourwill.tuwenzb.service.IActivityPhotoTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-30 16:20
 **/
@Service
public class ActivityPhotoTokenServiceImpl implements IActivityPhotoTokenService {

    @Autowired
    ActivityPhotoTokenMapper activityPhotoTokenMapper;
    @Override
    public Integer save(ActivityPhotoToken entity) {
        return activityPhotoTokenMapper.save(entity);
    }

    @Override
    public Integer update(ActivityPhotoToken entity) {
        return activityPhotoTokenMapper.update(entity);
    }

    @Override
    public ActivityPhotoToken getById(Integer id) {
        return activityPhotoTokenMapper.getById(id);
    }

    @Override
    public List<ActivityPhotoToken> findAll() {
        return activityPhotoTokenMapper.findAll();
    }

    @Override
    public Integer delete(Integer id) {
        return null;
    }

    @Override
    public Integer deleteByPhotoId(Integer photoId) {
        return activityPhotoTokenMapper.deleteByPhotoId(photoId);
    }

    @Override
    public List<ActivityPhotoToken> getByPhotoId(Integer photoId) {
        return activityPhotoTokenMapper.getByPhotoId(photoId);
    }

    @Override
    public Integer setSamePerson(String samePerson, Integer id) {
      return activityPhotoTokenMapper.setSamePerson(samePerson,id);
    }

    @Override
    public List<ActivityPhotoToken> findBySamePerson(String samePerson) {
        return  activityPhotoTokenMapper.findBySamePerson(samePerson);
    }

    @Override
    public ActivityPhotoToken findByFaceToken(String faceToken) {
        return activityPhotoTokenMapper.findByFaceToken(faceToken);
    }
}
