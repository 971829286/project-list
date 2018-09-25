package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.ActivitySchedule;

import java.util.List;

/**
 *  会议站
 *  会议服务
 *
 * @author uusao
 * @create 2018-03-21 10:36
 **/
public interface IActivityScheduleService extends IBaseService<ActivitySchedule>{


    int batchSave(Integer id, Integer activityId, List<ActivitySchedule> activitySchedules);

    Integer batchUpdate(List<ActivitySchedule> activitySchedules, Integer activityId);
}
