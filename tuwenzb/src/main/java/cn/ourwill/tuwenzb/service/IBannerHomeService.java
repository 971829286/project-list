package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.BannerHome;

import java.util.List;

/**
 * 　ClassName:IUserService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:51
 */

public interface IBannerHomeService extends IBaseService<BannerHome>{
    @Override
    Integer save(BannerHome bannerHome);
    @Override
    Integer update(BannerHome bannerHome);
    @Override
    Integer delete(Integer id);
    List<BannerHome> findAll(Integer photoLive);
    void updatePriorityById(Integer id,Integer priority);
}
