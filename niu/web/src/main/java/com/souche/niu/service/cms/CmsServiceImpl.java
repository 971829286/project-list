package com.souche.niu.service.cms;

import com.alibaba.fastjson.JSON;
import com.souche.dictionary.api.bean.Area;
import com.souche.dictionary.api.bean.AreaVO;
import com.souche.dictionary.api.bean.CityVO;
import com.souche.dictionary.api.service.AreaService;
import com.souche.niu.api.CmsService;
import com.souche.niu.dao.MaintenanceConfigDao;
import com.souche.niu.manager.cms.CmsManager;
import com.souche.niu.model.*;
import com.souche.niu.util.BannerUtil;
import com.souche.optimus.cache.CacheService;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.dao.BasicDao;
import com.souche.optimus.dao.query.QueryObj;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private CacheService cacheService;

    @Resource
    private AreaService areaService;

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
        logger.info("进入 CmsServiceImpl.getBannerListCountByStatus()");
        if(status != null){
            KValue kValue = new KValue();
            if(status != 3){
                kValue.setStatus(status);
            }
            QueryObj queryObj = new QueryObj(kValue);
            Integer groupId = Integer.parseInt(OptimusConfig.getValue("banner.groupId","115"));
            queryObj.addQuery("KValue.groupId = "+groupId+"",null);
            if(status != 3){
                queryObj.addQuery("KValue.status = " + status, null);
            }
            int count = basicDao.countByQuery(queryObj);
            logger.info("查找成功,查找到"+count+"条数据");
            return count;
        }
        logger.info("查找的记录数为空:参数status:"+status);
        return null;
    }

    @Override
    public Banner getBannerById(Integer bannerId) {
        logger.info("进入CmsServiceImpl.getBannerById(),参数bannerId:"+bannerId);
        Banner banner = null;
        if(bannerId != null){
            KValue kValue = basicDao.findObjectByPrimaryKey(bannerId,KValue.class);
            logger.info("获取数据成功:kvValue={}",kValue);
            banner = BannerUtil.convertValueToBanner(kValue.getBanner());
            banner.setBannerId(kValue.getId());
            banner.setTargetCity(this.targetCityToFe(kValue.getTargetCity()));
            banner.setDateCreate(kValue.getDateCreate());
        }
        logger.info("退出CmsServiceImpl.getBannerById()");
        return banner;
    }

    @Override
    public Boolean deleteById(Integer bannerId) {
        logger.info("进入CmsServiceImpl.deleteById(),参数bannerId:"+bannerId);
        Integer flag = 0;
        if(bannerId != null){
            flag  = basicDao.realDeleteByPrimaryKey(bannerId, KValue.class);
        }
        if(flag == 0){
            logger.error("删除Banner失败,bannerId:"+bannerId);
            return false;
        }else{
            logger.info("删除Banner成功,bannerId:"+bannerId);
            return true;
        }
    }

    @Override
    public Boolean updateBanner(Integer bannerId, Banner banner) {
        logger.info("进入CmsServiceImpl.updateBanner(),参数bannerId:"+bannerId+"   banner:"+banner);
        Integer flag = 0;
        if(bannerId != null && banner != null){
            Date date = new Date();
            KValue kValue = new KValue();
            Integer groupId = Integer.parseInt(OptimusConfig.getValue("banner.groupId","115"));
            kValue.setGroupId(groupId);
            kValue.setDateUpdate(date);
            kValue.setBanner(BannerUtil.convertBannerToValue(banner));
            kValue.setId(bannerId);
            if(banner.getStatus() != null){
                kValue.setStatus(banner.getStatus());
            }
            kValue.setTargetCity(BannerUtil.getTargetCityCodeStr(banner.getTargetCity()));
            flag = basicDao.update(kValue, true);
        }
        if(flag == 0){
            logger.error("更新记录失败,参数:bannerId"+bannerId+"  banner"+banner);
            return false;
        }else{
            logger.info("更新记录成功");
            return true;
        }
    }


    @Override
    public Integer saveBanner(Banner banner) {
        logger.info("进入CmsServiceImpl.saveBanner(),参数banner:"+banner);
        if(banner != null){
            Date date = new Date();
            KValue kValue = new KValue();
            Integer groupId = Integer.parseInt(OptimusConfig.getValue("banner.groupId","115"));
            //默认未上架
            kValue.setStatus(0);
            banner.setStatus(0);
            kValue.setGroupId(groupId);
            kValue.setBanner(BannerUtil.convertBannerToValue(banner));
            kValue.setDateCreate(date);
            kValue.setDateUpdate(date);
            kValue.setTargetCity(BannerUtil.getTargetCityCodeStr(banner.getTargetCity()));
            logger.info("数据保存成功:"+banner);
            return basicDao.insert(kValue);
        }
        logger.error("数据保存失败");
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

    @Override
    public List<City> getCityList() {
        logger.info("进入CmsServiceImpl.getCityList()");
        //获取所有城市
//        areaService.
        List<AreaVO> allArea = areaService.getAllArea();
        List<City> resList = new ArrayList<>();
        for(AreaVO areaVO : allArea){
            //直辖市
            if(Objects.equals(areaVO.getLabel(),"上海") || Objects.equals(areaVO.getLabel(),"北京")||
               Objects.equals(areaVO.getLabel(),"天津") || Objects.equals(areaVO.getLabel(),"重庆")){
                //直辖市
                City directlyCity = new City(areaVO.getValue(),areaVO.getLabel());
                resList.add(directlyCity);
            }
            //获取市
            if(areaVO.getChildren() != null){
                for(AreaVO cityArea :  areaVO.getChildren()){
                    City city = new City(cityArea.getValue(),cityArea.getLabel());
                    resList.add(city);
                }
            }
        }
        logger.info("获取到城市的数量为:"+resList.size());
        return resList;
    }
    //获取全国城市的编码
    private List<String> getCityCode(){
        List<City> cityList = getCityList();
        List<String> resList = new ArrayList<>();
        cityList.parallelStream().forEach(e -> resList.add(e.getValue()));
        return resList;
    }


    private List<String> targetCityToFe(String targetCityCode){
        if(StringUtil.isNotEmpty(targetCityCode)){
            String[] cities = targetCityCode.split(",");
            List<String> resList = new ArrayList<>();
            for(int i = 0; i < cities.length; i ++){
                Area area = areaService.loadAreaByCode(cities[i].trim());
                String item =  area.getCityName() + "(" + area.getCityCode()+ ")";
                resList.add(item);
            }
            return resList;
        }
        return null;
    }
}
