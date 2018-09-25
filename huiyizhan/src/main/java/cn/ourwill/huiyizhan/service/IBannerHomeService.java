package cn.ourwill.huiyizhan.service;


import cn.ourwill.huiyizhan.entity.BannerHome;
import com.qiniu.common.QiniuException;

import java.util.List;

/**
 * 　ClassName:IUserService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:51
 */

public interface IBannerHomeService extends IBaseService<BannerHome>{
    @Override
    Integer save(BannerHome bannerHome) throws QiniuException;
    @Override
    Integer update(BannerHome bannerHome) throws QiniuException;
    @Override
    Integer delete(Integer id) ;
    List<BannerHome> findAll();
    void updatePriorityById(Integer id, Integer priority);
    BannerHome selectById(Integer id);
}
