package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.ActivityPartner;
import cn.ourwill.huiyizhan.mapper.ActivityPartnerMapper;
import cn.ourwill.huiyizhan.service.IActivityPartnerService;
import cn.ourwill.huiyizhan.service.IFdfsImageService;
import cn.ourwill.huiyizhan.service.IQiniuService;
import com.qiniu.common.QiniuException;
import org.apache.commons.lang3.StringUtils;
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
public class ActivityPartnerServiceImpl extends BaseServiceImpl<ActivityPartner> implements IActivityPartnerService {

    @Autowired
    private ActivityPartnerMapper activityPartnerMapper;
    @Autowired
    private IQiniuService qiniuService;

    @Autowired
    private IFdfsImageService fdfsImageService;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Integer save(ActivityPartner activityPartner) throws QiniuException {
        Integer reCount = activityPartnerMapper.insertSelective(activityPartner);
        //图片持久
//        if(StringUtils.isNotEmpty(activityPartner.getLogoPics())) {
//            activityPartner.setLogoPics(qiniuService.dataPersistence(activityPartner.getActivityId(), activityPartner.getLogoPics()));
//            if(activityPartner.getLogoPics()!=null&&activityPartner.getLogoPics().startsWith("temp/"))
//                return 0;
//        }
        if(StringUtils.isNotEmpty(activityPartner.getLogoPics())) {
            Integer count = fdfsImageService.dataPersistence(activityPartner.getLogoPics());
            if (count<=0){
                return 0;
            }
        }
        activityPartnerMapper.updateByPrimaryKeySelective(activityPartner);
        return reCount;
    }

    @Override
    public Integer update(ActivityPartner activityPartner) throws QiniuException {
        //图片持久
//        if(StringUtils.isNotEmpty(activityPartner.getLogoPics())) {
//            activityPartner.setLogoPics(qiniuService.dataPersistence(activityPartner.getActivityId(), activityPartner.getLogoPics()));
//            if(activityPartner.getLogoPics()!=null&&activityPartner.getLogoPics().startsWith("temp/"))
//                return 0;
//        }
        if(StringUtils.isNotEmpty(activityPartner.getLogoPics())) {
            Integer count = fdfsImageService.dataPersistence(activityPartner.getLogoPics());
            if (count<=0){
                return 0;
            }
        }
        int reCount = activityPartnerMapper.updateByPrimaryKeySelective(activityPartner);
        return reCount;
    }

    @Override
    public int batchSave(Integer id, Integer activityId, List<ActivityPartner> activityPartners) throws QiniuException {
        //添加默认值
        //添加默认值
        for (ActivityPartner partner : activityPartners) {
//            if(StringUtils.isNotEmpty(partner.getLogoPics())) {
//                partner.setLogoPics(qiniuService.dataPersistence(activityId, partner.getLogoPics()));
//                if(partner.getLogoPics()!=null&&partner.getLogoPics().startsWith("temp/"))
//                    return 0;
//            }
            if(StringUtils.isNotEmpty(partner.getLogoPics())) {
                Integer count = fdfsImageService.dataPersistence(partner.getLogoPics());
                if (count<=0){
                    return 0;
                }
            }
            partner.setActivityId(activityId);
            partner.setCTime(new Date());
        }
        return activityPartnerMapper.batchSave(activityPartners);
    }

    @Override
    public Integer batchUpdate(List<ActivityPartner> activityPartners, Integer activityId) throws QiniuException {
        List<Integer> ids = activityPartnerMapper.findAllId(activityId);
        List<Integer> pageDataList = new ArrayList<>();
//        activityPartners.stream().forEach(entity -> {
//            pageDataList.add(entity.getId());
//        });
        for (ActivityPartner partner: activityPartners) {
            pageDataList.add(partner.getId());
//            if(StringUtils.isNotEmpty(partner.getLogoPics())&&partner.getLogoPics().startsWith("temp/")) {
//                partner.setLogoPics(qiniuService.dataPersistence(activityId, partner.getLogoPics()));
//                if(partner.getLogoPics()!=null&&partner.getLogoPics().startsWith("temp/"))
//                    return 0;
//            }
            if(StringUtils.isNotEmpty(partner.getLogoPics())) {
                Integer count = fdfsImageService.dataPersistence(partner.getLogoPics());
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
            activityPartnerMapper.batchDelete(deleteIds);
        return activityPartnerMapper.batchUpdate(activityPartners);
    }
}
