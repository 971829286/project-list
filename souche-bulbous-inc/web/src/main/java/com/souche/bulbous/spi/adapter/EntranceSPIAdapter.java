package com.souche.bulbous.spi.adapter;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.spi.EntranceSPI;
import com.souche.niu.api.EntranceService;
import com.souche.niu.model.EntranceDto;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/15
 **/
@Service("entranceSPI")
public class EntranceSPIAdapter implements EntranceSPI {

    private static final Logger logger = LoggerFactory.getLogger(EntranceSPIAdapter.class);

    @Resource
    private EntranceService entranceService;

    @Override
    public EntranceDto findOne() {
        try {
            return this.entranceService.findOne();
        } catch (OptimusExceptionBase exceptionBase) {
            logger.info("获取入口配置记录错误 {}", exceptionBase.toString());
            throw exceptionBase;
        }
    }

    @Override
    public int save(EntranceDto dto) {
        try {
            int count = this.entranceService.save(dto);
            if (count > 0) {
                logger.info("保存入口配置记录成功 {}", JSON.toJSONString(dto));
                return count;
            }
            logger.info("保存入口配置记录失败 {}", JSON.toJSONString(dto));
            return count;
        } catch (OptimusExceptionBase e) {
            logger.info("保存入口配置记录失败 {}", e.toString());
            throw e;
        }
    }
}
