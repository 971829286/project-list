package com.souche.niu.service.cms;

import com.alibaba.fastjson.JSON;
import com.souche.niu.api.CmsService;
import com.souche.niu.dao.MaintenanceConfigDao;
import com.souche.niu.manager.cms.CmsManager;
import com.souche.niu.model.*;
import com.souche.niu.util.BannerUtil;
import com.souche.optimus.cache.CacheService;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.dao.BasicDao;
import com.souche.optimus.dao.query.QueryObj;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by sid on 2018/9/5.
 */
@Service("cmsService")
public class CmsServiceImpl implements CmsService {

    private static final Logger logger = LoggerFactory.getLogger(CmsServiceImpl.class);

    @Autowired
    private CmsManager cmsManager;

    @Resource
    private BasicDao basicDao;

    @Resource
    private MaintenanceConfigDao maintenanceConfigDao;

    @Autowired
    private CacheService cacheService;

    @Override
    public MaintenanceConfigDO queryMaintenance() {
        return cmsManager.queryMaintenance();
    }

    @Override
    public List<Banner> getBannerListByStatus(Integer status, Integer page, Integer pageSize) {
       return cmsManager.getBannerListByStatus(status,page,pageSize);
    }

    @Override
    public Integer getBannerListCountByStatus(Integer status) {
        if(status != null && Objects.equals(status,3)){
            KValue kValue = new KValue();
            QueryObj queryObj = new QueryObj(kValue);
            //TODO 186
            Integer groupId = Integer.parseInt(OptimusConfig.getValue("banner.groupId","115"));
            queryObj.addQuery("KValue.groupId = "+groupId+"",null);
            return basicDao.countByQuery(queryObj);

        }
        return null;
    }

    @Override
    public Banner getBannerById(Integer bannerId) {
        Banner banner = null;
        if(bannerId != null){
            String imageHead = OptimusConfig.getValue("image.head");
            KValue kValue = basicDao.findObjectByPrimaryKey(bannerId,KValue.class);
            banner = BannerUtil.convertValueToBanner(kValue.getBanner());
            if(!banner.getImage().startsWith("http://") || !banner.getImage().startsWith("https://")){
                String image = imageHead + banner.getImage();
                banner.setImage(image);
            }
            banner.setBannerId(kValue.getId());
            banner.setDateCreate(kValue.getDateCreate());
        }
        return banner;
    }

    @Override
    public Boolean deleteById(Integer bannerId) {
        Integer flag = 0;
        if(bannerId != null){
            flag  = basicDao.realDeleteByPrimaryKey(bannerId, KValue.class);
        }
        return flag != 0;
    }

    @Override
    public Boolean updateBanner(Integer bannerId, Banner banner) {
        Integer flag = 0;
        if(bannerId != null && banner != null){
            Date date = new Date();
            KValue kValue = new KValue();
            Integer groupId = Integer.parseInt(OptimusConfig.getValue("banner.groupId","115"));
            kValue.setGroupId(groupId);
            kValue.setDateUpdate(date);
            kValue.setBanner(BannerUtil.convertBannerToValue(banner));
            kValue.setId(bannerId);
            flag = basicDao.update(kValue, true);
        }
        return flag >= 0;
    }


    @Override
    public Integer saveBanner(Banner banner) {
        if(banner != null){
            Date date = new Date();
            KValue kValue = new KValue();
            Integer groupId = Integer.parseInt(OptimusConfig.getValue("banner.groupId","115"));
            kValue.setGroupId(groupId);
            kValue.setBanner(BannerUtil.convertBannerToValue(banner));
            kValue.setDateCreate(date);
            kValue.setDateUpdate(date);
            return basicDao.insert(kValue);
        }
        return null;
    }

    @Override
    public BannerCfgDto findOneBannerCfgDto() {
        String mainCfgCacheData = cacheService.get("maintenanceKey");
        logger.info("缓存中查找维保配置信息");
        if (StringUtil.isEmpty(mainCfgCacheData)) {
            logger.info("维保配置信息缓存中未查到");
        }
        MaintenanceConfigDO cfgdo = JSON.parseObject(mainCfgCacheData, MaintenanceConfigDO.class);
        if (cfgdo == null) {
            cfgdo=this.maintenanceConfigDao.findOne();
            logger.info("数据库中查找维保配置信息 ");
            if (cfgdo == null) {
                return null;
            }
        }
        BannerCfgDto dto = new BannerCfgDto();
        dto.setId(cfgdo.getId());
        dto.setProtocol(cfgdo.getBannerProtocol());
        dto.setBannerTitle(cfgdo.getBannerTitle());
        dto.setUrl(cfgdo.getBannerImg());
        return dto;
    }

    @Override
    public int saveBannerCfg(BannerCfgDto bannerCfgDto) {
        if (bannerCfgDto == null) {
            logger.info("保存banner配置失败 参数为空");
            throw new OptimusExceptionBase("保存banner配置失败 参数为空");
        }
        int count=0;
        String data=null;
        //todo ID不空 进行更新操作
        if (bannerCfgDto.getId() != null) {
            MaintenanceConfigDO maintenanceConfigDO = this.basicDao.findObjectByPrimaryKey(bannerCfgDto.getId(), MaintenanceConfigDO.class);
            if (maintenanceConfigDO == null) {
                logger.info("保存banner配置失败 当前ID未查找到维保配置信息 ID=[{}]",bannerCfgDto.getId());
                throw new OptimusExceptionBase("保存banner配置失败 当前ID未查找到维保配置信息 ID="+bannerCfgDto.getId());
            }
            maintenanceConfigDO.setBannerTitle(bannerCfgDto.getBannerTitle());
            maintenanceConfigDO.setBannerProtocol(bannerCfgDto.getProtocol());
            maintenanceConfigDO.setBannerImg(bannerCfgDto.getUrl());
            count = this.maintenanceConfigDao.update(maintenanceConfigDO);
            data=JSON.toJSONString(maintenanceConfigDO);
            cacheService.set("maintenanceKey",data,60*60);
            logger.info("更新维保配置缓存 {}",data);
            return count;
        }

        //todo ID为空执行保存操作
        MaintenanceConfigDO cfgdo=new MaintenanceConfigDO();
        cfgdo.setBannerImg(bannerCfgDto.getUrl());
        cfgdo.setBannerProtocol(bannerCfgDto.getProtocol());
        cfgdo.setBannerTitle(bannerCfgDto.getBannerTitle());
        count = this.maintenanceConfigDao.save(cfgdo);
        data=JSON.toJSONString(cfgdo);
        cacheService.set("maintenanceKey",data,60*60);
        logger.info("保存维保配置到缓存 {}",data);
        return count;
    }

    @Override
    public MoneyCfgDto findOneMoneyCfgDto() {
        String mainCfgCacheData = cacheService.get("maintenanceKey");
        logger.info("缓存中查找维保配置信息");
        if (StringUtil.isEmpty(mainCfgCacheData)) {
            logger.info("维保配置信息缓存中未查到");
        }
        MaintenanceConfigDO cfgdo = JSON.parseObject(mainCfgCacheData, MaintenanceConfigDO.class);
        if (cfgdo == null) {
            cfgdo=this.maintenanceConfigDao.findOne();
            logger.info("数据库中查找维保配置信息");
            if (cfgdo == null) {
                return null;
            }
        }
        MoneyCfgDto dto = new MoneyCfgDto();
        dto.setId(cfgdo.getId());
        dto.setNoCertification(cfgdo.getUnverifiedPrice());
        dto.setCertification(cfgdo.getVerifiedPrice());
        return dto;
    }

    @Override
    public int saveMoneyCfg(MoneyCfgDto moneyCfgDto) {
        if (moneyCfgDto == null) {
            logger.info("保存维保配置信息失败 参数为空");
            throw new OptimusExceptionBase("保存维保配置信息失败 参数为空");
        }
        int count=0;
        String data=null;
        //todo ID不为空执行更新操作
        if (moneyCfgDto.getId() != null) {
            MaintenanceConfigDO maintenanceConfigDO = this.basicDao.findObjectByPrimaryKey(moneyCfgDto.getId(), MaintenanceConfigDO.class);
            if (maintenanceConfigDO == null) {
                logger.info("保存维保配置信息失败 当前ID未查找到配置信息 ID=[{}]",moneyCfgDto.getId());
                throw new OptimusExceptionBase("保存维保配置信息失败 当前ID未查找到配置信息 ID=" + moneyCfgDto.getId());
            }
            maintenanceConfigDO.setUnverifiedPrice(moneyCfgDto.getNoCertification());
            maintenanceConfigDO.setVerifiedPrice(moneyCfgDto.getCertification());
            count=this.maintenanceConfigDao.update(maintenanceConfigDO);
            data=JSON.toJSONString(maintenanceConfigDO);
            cacheService.set("maintenanceKey",data,60*60);
            logger.info("更新维保配置信息到缓存 {}",data);
            return count;
        }

        //todo ID为空执行保存操作
        MaintenanceConfigDO cfgdo=new MaintenanceConfigDO();
        cfgdo.setUnverifiedPrice(moneyCfgDto.getNoCertification());
        cfgdo.setVerifiedPrice(moneyCfgDto.getCertification());
        count = this.maintenanceConfigDao.save(cfgdo);
        data=JSON.toJSONString(cfgdo);
        cacheService.set("maintenanceKey",data,60*60);
        logger.info("保存维保配置信息到缓存 {}",data);
        return count;
    }
}
