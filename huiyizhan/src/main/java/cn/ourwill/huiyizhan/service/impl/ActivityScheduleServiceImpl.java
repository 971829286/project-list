package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.ActivitySchedule;
import cn.ourwill.huiyizhan.mapper.ActivityScheduleMapper;
import cn.ourwill.huiyizhan.service.IActivityScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author uusao
 * @create 2018-03-21 10:38
 **/
@Service
public class ActivityScheduleServiceImpl extends BaseServiceImpl<ActivitySchedule> implements IActivityScheduleService {

    @Autowired
    private ActivityScheduleMapper activityScheduleMapper;

    @Override
    public int batchSave(Integer id, Integer activityId, List<ActivitySchedule> activitySchedules) {
        //添加默认值
        //添加默认值
        activitySchedules.stream().forEach(entity -> {
            entity.setActivityId(activityId);
            entity.setCTime(new Date());
        });
        return activityScheduleMapper.batchSave(activitySchedules);
    }

    @Override
    public Integer batchUpdate(List<ActivitySchedule> activitySchedules, Integer activityId) {
        List<Integer> ids = activityScheduleMapper.findAllId(activityId);

        List<Integer> pageDataList = new ArrayList<>();
        activitySchedules.stream().forEach(entity -> {
            pageDataList.add(entity.getId());
        });
        ArrayList<Integer> deleteIds = new ArrayList<>();
        for(Integer id:ids){
            if(  !pageDataList.contains(id) ){ // 当前id 所对应的数据已被删除
                deleteIds.add(id);
            }
        }
        if(deleteIds.size()>0)
            activityScheduleMapper.batchDelete(deleteIds);
        return activityScheduleMapper.batchUpDate(activitySchedules);
    }
}
