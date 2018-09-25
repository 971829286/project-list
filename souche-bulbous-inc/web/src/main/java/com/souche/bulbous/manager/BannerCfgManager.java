package com.souche.bulbous.manager;

import com.souche.bulbous.vo.BannerCfgVo;

import java.util.Map;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/

public interface BannerCfgManager {

    BannerCfgVo findOne();

    /**
     * 保存banner配置
     * @param id
     * @param title
     * @param url
     * @return
     */
    Map<String, Object> save(Integer id, String title, String url,String protocol);
}
