package cn.ourwill.huiyizhan.service.impl;


import cn.ourwill.huiyizhan.entity.BannerHome;
import cn.ourwill.huiyizhan.mapper.BannerHomeMapper;
import cn.ourwill.huiyizhan.service.IBannerHomeService;
import cn.ourwill.huiyizhan.service.IFdfsImageService;
import cn.ourwill.huiyizhan.service.IQiniuService;
import com.qiniu.common.QiniuException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BannerHomeServiceImpl extends BaseServiceImpl<BannerHome> implements IBannerHomeService {
    @Autowired
    private BannerHomeMapper bannerHomeMapper;
    @Autowired
    private IQiniuService qiniuService;

    @Autowired
    private IFdfsImageService fdfsImageService;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Integer save(BannerHome bannerHome) throws QiniuException {
        //图片持久
//        if (bannerHome.getPic() != null && bannerHome.getPic().startsWith("temp/"))
//            bannerHome.setPic(qiniuService.dataPersistence(bannerHome.getId(),bannerHome.getPic()));
//        if (bannerHome.getMobilePic() != null && bannerHome.getMobilePic().startsWith("temp/"))
//            bannerHome.setMobilePic(qiniuService.dataPersistence(bannerHome.getId(),bannerHome.getMobilePic()));
        if (bannerHome.getPic() != null ){
            Integer count = fdfsImageService.dataPersistence(bannerHome.getPic());
        }
        if (bannerHome.getMobilePic() != null ){
            Integer count = fdfsImageService.dataPersistence(bannerHome.getMobilePic());
        }
        return bannerHomeMapper.insertSelective(bannerHome);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Integer update(BannerHome bannerHome) throws QiniuException {
        //图片持久
//        if (bannerHome.getPic() != null && bannerHome.getPic().startsWith("temp/"))
//            bannerHome.setPic(qiniuService.dataPersistence(bannerHome.getId(),bannerHome.getPic()));
//        if (bannerHome.getMobilePic() != null && bannerHome.getMobilePic().startsWith("temp/"))
//            bannerHome.setMobilePic(qiniuService.dataPersistence(bannerHome.getId(),bannerHome.getMobilePic()));
        if (bannerHome.getPic() != null ){
            Integer count = fdfsImageService.dataPersistence(bannerHome.getPic());
        }
        if (bannerHome.getMobilePic() != null ){
            Integer count = fdfsImageService.dataPersistence(bannerHome.getMobilePic());
        }
        return bannerHomeMapper.updateByPrimaryKeySelective(bannerHome);
    }

    @Override
    public Integer delete(Integer id){
        BannerHome bannerHome = bannerHomeMapper.selectByPrimaryKey(id);
        if (bannerHome == null)
            return 0;
        //如果是从会议里取出来的,则不删除图片
        try {
            if(bannerHome.getActivityId() == null || bannerHome.getActivityId() < 0){
    //            qiniuService.delete(bannerHome.getPic());
                fdfsImageService.delete(bannerHome.getPic());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bannerHomeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<BannerHome> findAll() {
        return bannerHomeMapper.findAll();
    }

    @Override
    public void updatePriorityById(Integer id, Integer priority) {
        bannerHomeMapper.updatePriorityById(id, priority);
    }

    @Override
    public BannerHome selectById(Integer id) {
        return bannerHomeMapper.selectById(id);
    }

}
