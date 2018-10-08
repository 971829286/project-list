package com.souche.bulbous.spi.adapter;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.spi.BannerCfgSPI;
import com.souche.niu.api.CmsService;
import com.souche.niu.model.BannerCfgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/12
 **/
@Service("bannerCfgSPI")
public class BannerCfgSPIAdapter implements BannerCfgSPI {

    private static final Logger logger = LoggerFactory.getLogger(BannerCfgSPIAdapter.class);

    @Resource
    private CmsService cmsService;

    @Override
    public BannerCfgDto findOne() {
        logger.info("调用dubbo接口获取banner配置记录");
        return this.cmsService.findOneBannerCfgDto();
    }

    @Override
    public int saveBannnerCfg(BannerCfgDto bannerCfgDto) {
        if (bannerCfgDto == null) {
            logger.info("保存banner配置失败 参数为空");
            return 0;
        }
        int count = this.cmsService.saveBannerCfg(bannerCfgDto);
        if (count > 0) {
            logger.info("保存banner配置成功 {}",JSON.toJSONString(bannerCfgDto));
            return count;
        }
        logger.info("保存banner配置失败 {}",JSON.toJSONString(bannerCfgDto));
        return count;
    }
}
