package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.mapper.BannerHomeMapper;
import cn.ourwill.tuwenzb.service.IBannerHomeService;
import cn.ourwill.tuwenzb.entity.BannerHome;
import cn.ourwill.tuwenzb.service.IQiniuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerHomeServiceImpl extends BaseServiceImpl<BannerHome> implements IBannerHomeService {
    @Autowired
    private BannerHomeMapper bannerHomeMapper;
    @Autowired
    private IQiniuService qiniuService;
    @Override
    public Integer save(BannerHome bannerHome){
        if(bannerHome.getPhotoLive()!=null&&bannerHome.getPhotoLive().equals(1)) {
            //图片持久
            if(bannerHome.getPic()!=null&&bannerHome.getPic().startsWith("temp/"))
                bannerHome.setPic(qiniuService.dataPersistence(bannerHome.getId(),null,bannerHome.getPic()));
        }
        return bannerHomeMapper.save(bannerHome);
    }
    @Override
    public Integer update(BannerHome bannerHome){
        if(bannerHome.getPhotoLive()!=null&&bannerHome.getPhotoLive().equals(1)) {
            //图片持久
            if(bannerHome.getPic()!=null&&bannerHome.getPic().startsWith("temp/"))
                bannerHome.setPic(qiniuService.dataPersistence(bannerHome.getId(),null,bannerHome.getPic()));
        }
        return bannerHomeMapper.update(bannerHome);
    }

    @Override
    public Integer delete(Integer id){
        BannerHome bannerHome = bannerHomeMapper.getById(id);
        if(bannerHome==null)
            return 0;
        qiniuService.delete(bannerHome.getPic());
        return bannerHomeMapper.delete(id);
    }

    @Override
    public List<BannerHome> findAll(Integer photoLive) {
        return bannerHomeMapper.findAll(photoLive);
    }

    @Override
    public void updatePriorityById(Integer id, Integer priority) {
        bannerHomeMapper.updatePriorityById(id,priority);
    }
}
