package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.ActivityContact;
import cn.ourwill.huiyizhan.mapper.ActivityContactMapper;
import cn.ourwill.huiyizhan.service.IActivityContactService;
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
public class ActivityContactServiceImpl extends BaseServiceImpl<ActivityContact> implements IActivityContactService {


    @Autowired
    private ActivityContactMapper activityContactMapper;

    @Override
    public Integer batchSave(Integer id, Integer activityId, List<ActivityContact> activityContacts) {
        //添加默认值
        activityContacts.stream().forEach(entity -> {
            entity.setActivityId(activityId);
            entity.setCTime(new Date());
        });
        return activityContactMapper.batchSave(activityContacts);
    }

    @Override
    public Integer batchUpdate(List<ActivityContact> activityContacts, Integer activityId) {
        List<Integer> pageDataList = new ArrayList<>();
        activityContacts.stream().forEach(entity -> {
            pageDataList.add(entity.getId());
        });

        List<Integer> ids = activityContactMapper.findAllId(activityId);
        ArrayList<Integer> deleteIds = new ArrayList<>();
        for (Integer id : ids) {
            if( !pageDataList.contains(id) ){   // 当前id 所对应的数据已被删除
                deleteIds.add(id);
            }
        }
        if(deleteIds.size()>0)
            activityContactMapper.batchDelete(deleteIds);
        return activityContactMapper.batchUpload(activityContacts);
    }


}
