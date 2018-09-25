package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.ActivityTickets;
import cn.ourwill.huiyizhan.entity.User;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/23 18:33
 * @Description
 */
public interface IActivityTicketsService extends IBaseService<ActivityTickets> {
    int batchSave(Integer userId, Integer activityId, List<ActivityTickets> tickets);

    List<ActivityTickets> getByActivityId(Integer activityId);

    int batchUpdate(Integer activityId, List<ActivityTickets> activityTickets,User user);

    List<ActivityTickets> getValidByActivityId(Integer activityId);
}
