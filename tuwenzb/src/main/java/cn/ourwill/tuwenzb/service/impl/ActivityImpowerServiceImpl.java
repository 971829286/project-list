package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.controller.ActivityContentController;
import cn.ourwill.tuwenzb.entity.ActivityImpower;
import cn.ourwill.tuwenzb.mapper.ActivityImpowerMapper;
import cn.ourwill.tuwenzb.mapper.ActivityMapper;
import cn.ourwill.tuwenzb.service.IActivityImpowerService;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/23 0023 15:40
 * 活动授权
 * @Version1.0
 */
@Service
public class ActivityImpowerServiceImpl implements IActivityImpowerService{
    @Autowired
    ActivityImpowerMapper activityImpowerMapper;
    @Autowired
    ActivityMapper activityMapper;

    private static final Logger log = LogManager.getLogger(ActivityImpowerServiceImpl.class);

    @Override
    public Map save(ActivityImpower activityImpower) {
        if(activityImpowerMapper.selectCountByActivityId(activityImpower.getActivityId())>19){
            return ReturnResult.errorResult("超过授权数量上限，无法授权！");
        }
        if(activityImpowerMapper.selectCountByActivityIdUserId(activityImpower.getActivityId(),activityImpower.getUserId())>0){
            return ReturnResult.errorResult("已有授权，无需授权！");
        }
        if(activityImpowerMapper.save(activityImpower)>0) {
            return ReturnResult.successResult("授权成功！");
        }
        return ReturnResult.errorResult("授权失败！");
    }

    @Override
    public Map saveForPhoto(ActivityImpower activityImpower) {
        if(activityImpowerMapper.selectCountByAlbumId(activityImpower.getAlbumId(),activityImpower.getType())>2){
            return ReturnResult.errorResult("超过授权数量上限，无法授权！");
        }
        if(activityImpowerMapper.selectCountByAlbumIdUserId(activityImpower.getAlbumId(),activityImpower.getType(),activityImpower.getUserId())>0){
            return ReturnResult.errorResult("已有授权，无需授权！");
        }
        if(activityImpowerMapper.save(activityImpower)>0) {
            return ReturnResult.successResult("授权成功！");
        }
        return ReturnResult.errorResult("授权失败！");
    }

    @Override
    public Map saveForPhotoAdmin(ActivityImpower activityImpower) {
        if(activityImpowerMapper.selectCountByActivityId(activityImpower.getActivityId())>19){
            return ReturnResult.errorResult("超过授权数量上限，无法授权！");
        }
        if(activityImpowerMapper.selectCountByActivityIdAlbumIdUserIdAdmin(activityImpower.getActivityId(),activityImpower.getUserId())>0){
            return ReturnResult.errorResult("已有授权，无需授权！");
        }
        if(activityImpowerMapper.save(activityImpower)>0) {
            return ReturnResult.successResult("授权成功！");
        }
        return ReturnResult.errorResult("授权失败！");
    }

    @Override
    public List<ActivityImpower> selectByActivityId(Integer activityId) {
        return activityImpowerMapper.selectByActivityId(activityId);
    }

    @Override
    public boolean isImpower(Integer activityId, Integer userId, Integer isImpower) {
//        log.info("+-=-=-=-==-="+isImpower);
        if(isImpower.equals(1)&&activityImpowerMapper.selectCountByActivityIdUserId(activityId,userId)>0){
//            log.info("++++++++"+isImpower);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPhotoImpower(Integer activityId, Integer albumId, Integer userId) {
        if(activityImpowerMapper.selectCountByActivityIdAlbumIdUserId(activityId,albumId,userId)>0)
            return true;
        return false;
    }

    @Override
    public boolean isPhotoAdminImpower(Integer activityId, Integer userId) {
        if(activityImpowerMapper.selectCountByActivityIdAlbumIdUserIdAdmin(activityId,userId)>0)
            return true;
        return false;
    }


    @Override
    public Integer deleteById(Integer id) {
        return activityImpowerMapper.deleteById(id);
    }

    @Override
    public ActivityImpower getById(Integer id) {
        return activityImpowerMapper.getById(id);
    }

    @Override
    public List<ActivityImpower> getByAlbumIdAndActivityId(Integer activityId, Integer albumId) {
        return activityImpowerMapper.getByAlbumIdAndActivityId(activityId,albumId);
    }

    @Override
    public List<ActivityImpower> getAdminByActivityId(Integer id) {
        return activityImpowerMapper.getByAdminActivityId(id);
    }
}
