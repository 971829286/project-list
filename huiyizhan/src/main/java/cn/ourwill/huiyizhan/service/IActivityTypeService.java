package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.ActivityType;

import java.util.List;

public interface IActivityTypeService {
    List<ActivityType> findAll();
    ActivityType getActivityTypeById(Integer id);
}
