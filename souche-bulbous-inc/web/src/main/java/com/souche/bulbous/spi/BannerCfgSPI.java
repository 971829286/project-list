package com.souche.bulbous.spi;

import com.souche.niu.model.BannerCfgDto;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/12
 **/
public interface BannerCfgSPI {


    /**
     * 获取banner唯一记录
     * @return
     */
    BannerCfgDto findOne();

    /**
     * 保存banner配置
     * @param bannerCfgDto
     * @return
     */
    int saveBannnerCfg(BannerCfgDto bannerCfgDto);
}
