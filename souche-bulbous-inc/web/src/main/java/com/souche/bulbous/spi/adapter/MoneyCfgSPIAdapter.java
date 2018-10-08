package com.souche.bulbous.spi.adapter;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.spi.MoneyCfgSPI;
import com.souche.niu.api.CmsService;
import com.souche.niu.model.MoneyCfgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/12
 **/
@Service("moneyCfgSPI")
public class MoneyCfgSPIAdapter implements MoneyCfgSPI {

    private static final Logger logger = LoggerFactory.getLogger(MoneyCfgSPIAdapter.class);

    @Resource
    private CmsService cmsService;

    @Override
    public MoneyCfgDto findOne() {
        MoneyCfgDto moneyCfgDto=this.cmsService.findOneMoneyCfgDto();
        logger.info("调用dubbo接口获取金额配置唯一记录 {}",JSON.toJSONString(moneyCfgDto));
        if (moneyCfgDto == null) {
            return new MoneyCfgDto();
        }
        return moneyCfgDto;
    }

    @Override
    public int saveMoneyCfg(MoneyCfgDto moneyCfgDto) {
        if(moneyCfgDto==null){
            logger.info("保存金额配置失败 参数为空 {}",JSON.toJSONString(moneyCfgDto));
            return 0;
        }
        int count = this.cmsService.saveMoneyCfg(moneyCfgDto);
        if (count > 0) {
            logger.info("保存金额配置成功 {}",JSON.toJSONString(moneyCfgDto));
            return count;
        }
        logger.info("保存金额配置失败 {}", JSON.toJSONString(moneyCfgDto));
        return count;
    }
}
