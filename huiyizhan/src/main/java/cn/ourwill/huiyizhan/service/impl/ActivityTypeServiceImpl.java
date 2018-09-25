package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.ActivityType;
import cn.ourwill.huiyizhan.mapper.ActivityTypeMapper;
import cn.ourwill.huiyizhan.service.IActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityTypeServiceImpl implements IActivityTypeService {
    @Autowired
    ActivityTypeMapper activityTypeMapper;
    @Override
    public List<ActivityType> findAll() {
       return activityTypeMapper.findAll();
    }

    @Override
    public ActivityType getActivityTypeById(Integer id) {
        return activityTypeMapper.getActivityTypeById(id);
    }
}
