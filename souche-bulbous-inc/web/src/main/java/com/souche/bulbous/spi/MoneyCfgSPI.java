package com.souche.bulbous.spi;

import com.souche.niu.model.MoneyCfgDto;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/12
 **/
public interface MoneyCfgSPI {

    /**
     * 获取金额配置唯一记录
     * @return
     */
    MoneyCfgDto findOne();

    /**
     * 保存金额配置
     * @param moneyCfgDto
     * @return
     */
    int saveMoneyCfg(MoneyCfgDto moneyCfgDto);
}
