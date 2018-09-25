package com.souche.bulbous.manager.impl;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.manager.BannerCfgManager;
import com.souche.bulbous.spi.BannerCfgSPI;
import com.souche.bulbous.vo.BannerCfgVo;
import com.souche.niu.model.BannerCfgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/13
 **/
@Service("bannerCfgManager")
public class BannerCfgManagerImpl implements BannerCfgManager {

    private static final Logger logger = LoggerFactory.getLogger(BannerCfgManagerImpl.class);

    @Autowired
    private BannerCfgSPI bannerCfgSPI;

    @Override
    public BannerCfgVo findOne() {
        BannerCfgDto dto=this.bannerCfgSPI.findOne();
        if(dto==null){
            logger.info("banner配置记录为空");
            return new BannerCfgVo();
        }
        return new BannerCfgVo(dto);
    }

    @Override
    public Map<String, Object> save(Integer id, String title, String url,String protocol) {
        Map<String, Object> data = new HashMap<>();
        BannerCfgDto dto=new BannerCfgDto();
        dto.setId(id);
        dto.setUrl(url);
        dto.setBannerTitle(title);
        dto.setProtocol(protocol);
        int count = this.bannerCfgSPI.saveBannnerCfg(dto);
        if (count > 0) {
            data.put("success", true);
            data.put("msg", "保存banner配置成功");
            return data;
        }
        logger.info("调用dubbo保存banner配置失败 {}",JSON.toJSONString(dto));
        data.put("success", false);
        data.put("msg", "保存banner配置失败");
        return data;
    }
}
