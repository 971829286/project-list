package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.ActivityContact;

import java.util.List;

public interface IActivityContactService extends IBaseService<ActivityContact>{


    Integer batchSave(Integer id, Integer activityId, List<ActivityContact> activityContacts);

    Integer batchUpdate(List<ActivityContact> activityContacts, Integer activityId);
}
