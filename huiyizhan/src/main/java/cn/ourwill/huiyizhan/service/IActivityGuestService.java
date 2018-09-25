package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.ActivityGuest;
import com.qiniu.common.QiniuException;

import java.util.List;

public interface IActivityGuestService extends IBaseService<ActivityGuest>{
    int batchSave(Integer id, Integer activityId, List<ActivityGuest> tickets) throws QiniuException;

    Integer batchUpdate(List<ActivityGuest> activityGuests, Integer activityId) throws QiniuException;
}
