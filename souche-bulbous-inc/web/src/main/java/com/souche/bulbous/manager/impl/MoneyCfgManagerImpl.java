package com.souche.bulbous.manager.impl;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.manager.MoneyCfgManager;
import com.souche.bulbous.spi.MoneyCfgSPI;
import com.souche.niu.model.MoneyCfgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/13
 **/
@Service("moneyCfgManager")
public class MoneyCfgManagerImpl implements MoneyCfgManager {

    private static final Logger logger = LoggerFactory.getLogger(MoneyCfgManagerImpl.class);

    @Autowired
    private MoneyCfgSPI moneyCfgSPI;

    @Override
    public Map<String, Object> save(Integer id, BigDecimal certification, BigDecimal noCertification) {
        Map<String, Object> data = new HashMap<>();
        MoneyCfgDto dto=new MoneyCfgDto();
        dto.setId(id);
        dto.setCertification(certification);
        dto.setNoCertification(noCertification);
        int count = moneyCfgSPI.saveMoneyCfg(dto);
        if(count>0){
            data.put("success", true);
            data.put("msg", "保存金额配置成功");
            return data;
        }
        data.put("success", false);
        data.put("msg", "保存金额配置失败");
        logger.info("调用dubbo保存金额配置失败 {}",JSON.toJSONString(dto));
        return data;
    }
}
