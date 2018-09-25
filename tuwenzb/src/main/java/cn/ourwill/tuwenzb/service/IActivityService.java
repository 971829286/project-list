package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityType;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 　ClassName:IUserService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:51
 */

public interface IActivityService extends IBaseService<Activity> {
    @Override
    public Integer save(Activity entity);

    @Override
    public Integer update(Activity entity);

    //根据用户id查找活动
    public List<Activity> selectByUserId(Integer userId, Integer photoLive);

    public List<Activity> selectByParams(Map param);

    boolean checkOwner(HttpServletRequest request, Integer id);

    int close(Integer id);

    int open(Integer id);

    List<Activity> selectWithOld(Map param);

    Activity getByIdWithOld(Integer id);

    @Override
    Integer delete(Integer id);

    Integer updataImpower(Integer activityId, Integer isImpower);

    List<ActivityType> getAllType();

    List<String> getAllDate(Integer activityId);

    List<Activity> selectAdvance();

    Activity selectByAlbumId(Integer albumId);

    List<Activity> selectByImpower(Integer userId, Integer photoLive);

    Activity getByPhotoId(Integer photoId);

    Integer updateAutoPublish(Integer activityId, Integer status);

    List<Activity> selectByImpowerApp(Integer userId, Integer photoLive);

    /**
     * 更新 该会议默认展示的相册
     *
     * @param id
     * @param displayAlbumId
     * @return
     */
    Integer upDateDisplayAlbum(Integer id, Integer userId, Integer displayAlbumId);

    String getFaceSetTokenByFacePlus(int id);

    Integer setActivityFaceSearch(Integer isOpenFaceSearch,Integer id);

    List<Activity> getActivityIng(LocalDateTime now);
}
