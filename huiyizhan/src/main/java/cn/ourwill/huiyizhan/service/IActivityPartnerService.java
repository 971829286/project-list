package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.ActivityPartner;
import com.qiniu.common.QiniuException;

import java.util.List;

/**
 *  会议站
 *  会议服务
 *
 * @author uusao
 * @create 2018-03-21 10:36
 **/
public interface IActivityPartnerService extends IBaseService<ActivityPartner>{


    int batchSave(Integer id, Integer activityId, List<ActivityPartner> tickets) throws QiniuException;

    Integer batchUpdate(List<ActivityPartner> activityPartners, Integer activityId) throws QiniuException;
}
