package com.souche.niu.manager.cms.impl;

import com.souche.niu.manager.cms.CmsManager;
import com.souche.niu.mapper.MaintenanceCfgMapper;
import com.souche.niu.model.Banner;
import com.souche.niu.model.KValue;
import com.souche.niu.model.MaintenanceConfigDO;
import com.souche.niu.model.maintenance.MaintenanceCfgDO;
import com.souche.niu.util.BannerUtil;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.dao.BasicDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by sid on 2018/9/6.
 */
@Service("cmsManager")
public class CmsManagerImpl implements CmsManager {

    private static final Logger logger = LoggerFactory.getLogger(CmsManagerImpl.class);

    @Resource
    private BasicDao basicDao;

    @Autowired
    private MaintenanceCfgMapper maintenanceCfgMapper;

    private String defaultBannerImg = OptimusConfig.getValue("defaultBannerImg");
    /**
     * 查询维保设置
     *
     * @return
     */
    @Override
    public MaintenanceConfigDO queryMaintenance() {
        MaintenanceConfigDO m = null;
        try {
            logger.info("查询维保设置 准备查询");
            List<MaintenanceCfgDO> maintenanceCfgDOS = maintenanceCfgMapper.queryListMaintenanceCfg();
            logger.info("查询维保设置 查询结果 maintenanceCfgDOS = {}", JsonUtils.toJson(maintenanceCfgDOS));
            m = new MaintenanceConfigDO();
            //数据库中没有数据，使用默认配置
            if(maintenanceCfgDOS.size() == 0){
                //默认BannerImg
                m.setBannerImg(defaultBannerImg);
                //默认未认证金额
                m.setUnverifiedPrice(new BigDecimal(15).setScale(2,BigDecimal.ROUND_DOWN));
                //默认已认证金额
                m.setVerifiedPrice(new BigDecimal(8).setScale(2,BigDecimal.ROUND_DOWN));
                return m;
            }
            MaintenanceCfgDO maintenanceCfgDO = maintenanceCfgDOS.get(0);
            if(StringUtils.isEmpty(maintenanceCfgDO.getBannerImg())){
                //默认BannerImg
                m.setBannerImg(defaultBannerImg);
            }
            if(StringUtils.isEmpty(maintenanceCfgDO.getUnverifiedPrice().toString())){
                //默认未认证金额
                m.setUnverifiedPrice(new BigDecimal(15).setScale(2,BigDecimal.ROUND_DOWN));
            }
            if(StringUtils.isEmpty(maintenanceCfgDO.getVerifiedPrice().toString())){
                //默认已认证金额
                m.setVerifiedPrice(new BigDecimal(8).setScale(2,BigDecimal.ROUND_DOWN));
            }
            m.setBannerImg(maintenanceCfgDO.getBannerImg());
            //未认证金额
            m.setUnverifiedPrice(maintenanceCfgDO.getUnverifiedPrice().setScale(2,BigDecimal.ROUND_DOWN));
            //已认证金额
            m.setVerifiedPrice(maintenanceCfgDO.getVerifiedPrice().setScale(2,BigDecimal.ROUND_DOWN));
        } catch (Exception e) {
            logger.error("查询维保设置 异常 e={}",e);
        }
        return m;
    }

    @Override
    public List<Banner> getBannerListByStatus(Integer status, Integer page, Integer pageSize) {
        if(status != null && Objects.equals(status,3)){
            KValue kValue = new KValue();
            Integer groupId = Integer.parseInt(OptimusConfig.getValue("banner.groupId","115"));
            String imageHead = OptimusConfig.getValue("image.head");
            kValue.setGroupId(groupId);
            List<KValue> list = basicDao.findListBySample(kValue,KValue.class);
            List<Banner> resList = new ArrayList<>();
            list.stream().forEach((e) -> {
                Banner banner = BannerUtil.convertValueToBanner(e.getBanner());
                if (banner != null) {
                    banner.setBannerId(e.getId());
                    banner.setDateCreate(e.getDateCreate());
                    if (!banner.getImage().startsWith("http://") || !banner.getImage().startsWith("https://")) {
                        String image = imageHead + banner.getImage();
                        banner.setImage(image);
                    }
                    resList.add(banner);
                }
            });
            if(resList != null && resList.size() > 0) {
                return resList;
            }
        }
        return null;
    }

}
