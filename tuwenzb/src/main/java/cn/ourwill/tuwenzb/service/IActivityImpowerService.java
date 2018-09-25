package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.ActivityImpower;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/23 0023 15:39
 * @Version1.0
 */
public interface IActivityImpowerService {

    Map save(ActivityImpower activityImpower);

    Map saveForPhoto(ActivityImpower activityImpower);

    Map saveForPhotoAdmin(ActivityImpower activityImpower);

    List<ActivityImpower> selectByActivityId(Integer activityId);

    boolean isImpower(Integer activityId,Integer userId,Integer isImpower);

    boolean isPhotoImpower(Integer activityId,Integer albumId, Integer userId);

    boolean isPhotoAdminImpower(Integer activityId, Integer userId);

    Integer deleteById(Integer id);

    ActivityImpower getById(Integer id);

    List<ActivityImpower> getByAlbumIdAndActivityId(Integer activityId, Integer albumId);

    List<ActivityImpower> getAdminByActivityId(Integer id);
}
