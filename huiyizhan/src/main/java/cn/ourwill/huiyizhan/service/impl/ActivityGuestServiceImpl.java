package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.ActivityGuest;
import cn.ourwill.huiyizhan.mapper.ActivityGuestMapper;
import cn.ourwill.huiyizhan.service.IActivityGuestService;
import cn.ourwill.huiyizhan.service.IFdfsImageService;
import cn.ourwill.huiyizhan.service.IQiniuService;
import com.qiniu.common.QiniuException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ActivityGuestServiceImpl extends BaseServiceImpl<ActivityGuest> implements IActivityGuestService {
    @Autowired
    private ActivityGuestMapper activityGuestMapper;
    @Autowired
    private IQiniuService qiniuService;

    @Autowired
    private IFdfsImageService fdfsImageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(ActivityGuest activityGuest) throws QiniuException {
        Integer reCount = activityGuestMapper.insertSelective(activityGuest);
        //图片持久
//        if (StringUtils.isNotEmpty(activityGuest.getGuestAvatar())) {
//            activityGuest.setGuestAvatar(qiniuService.dataPersistence(activityGuest.getActivityId(), activityGuest.getGuestAvatar()));
//            if (activityGuest.getGuestAvatar() != null && activityGuest.getGuestAvatar().startsWith("temp/"))
//                return 0;
//        }
        if (StringUtils.isNotEmpty(activityGuest.getGuestAvatar())) {
            Integer count = fdfsImageService.dataPersistence(activityGuest.getGuestAvatar());
            if (count<=0){
                return 0;
            }
        }
        activityGuestMapper.updateByPrimaryKeySelective(activityGuest);
        return reCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer update(ActivityGuest activityGuest) throws QiniuException {
        //图片持久
//        if (StringUtils.isNotEmpty(activityGuest.getGuestAvatar())) {
//            activityGuest.setGuestAvatar(qiniuService.dataPersistence(activityGuest.getActivityId(), activityGuest.getGuestAvatar()));
//            if (activityGuest.getGuestAvatar() != null && activityGuest.getGuestAvatar().startsWith("temp/"))
//                return 0;
//        }
        if (StringUtils.isNotEmpty(activityGuest.getGuestAvatar())) {
            Integer count = fdfsImageService.dataPersistence(activityGuest.getGuestAvatar());
            if (count<=0){
                return 0;
            }
        }
        int reCount = activityGuestMapper.updateByPrimaryKeySelective(activityGuest);
        return reCount;
    }

    @Override
    public int batchSave(Integer id, Integer activityId, List<ActivityGuest> activityGuests) throws QiniuException {
        for (ActivityGuest guest : activityGuests) {
            //图片持久
//            if (StringUtils.isNotEmpty(guest.getGuestAvatar())) {
//                guest.setGuestAvatar(qiniuService.dataPersistence(activityId, guest.getGuestAvatar()));
//                if (guest.getGuestAvatar() != null && guest.getGuestAvatar().startsWith("temp/"))
//                    return 0;
//            }

            if (StringUtils.isNotEmpty(guest.getGuestAvatar())) {
                Integer count = fdfsImageService.dataPersistence(guest.getGuestAvatar());
                if (count<=0){
                    return 0;
                }
            }
            guest.setActivityId(activityId);
            guest.setCTime(new Date());
        }
        return activityGuestMapper.batchSave(activityGuests);
    }

    @Override
    public Integer batchUpdate(List<ActivityGuest> activityGuests, Integer activityId) throws QiniuException {
        List<Integer> ids = activityGuestMapper.findAllId(activityId);
        List<Integer> pageDataList = new ArrayList<>();
        for (ActivityGuest guest : activityGuests) {
            pageDataList.add(guest.getId());
            //图片持久
//            if (StringUtils.isNotEmpty(guest.getGuestAvatar())&&guest.getGuestAvatar().startsWith("temp/")) {
//                guest.setGuestAvatar(qiniuService.dataPersistence(activityId, guest.getGuestAvatar()));
//                if (guest.getGuestAvatar() != null && guest.getGuestAvatar().startsWith("temp/"))
//                    return 0;
//            }
            if (StringUtils.isNotEmpty(guest.getGuestAvatar())) {
                Integer count = fdfsImageService.dataPersistence(guest.getGuestAvatar());
                if (count<=0){
                    return 0;
                }
            }
        }
        ArrayList<Integer> deleteIds = new ArrayList<>();
        for(Integer id:ids){
            if(  !pageDataList.contains(id) ){ // 当前id 所对应的数据已被删除
                deleteIds.add(id);
            }
        }
        if(deleteIds.size()>0)
            activityGuestMapper.batchDelete(deleteIds);
        return activityGuestMapper.batchUpload(activityGuests);
    }
}
