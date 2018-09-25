package com.souche.bulbous.manager;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/
public interface MoneyCfgManager {

    /**
     * 保存金额配置
     * @param id
     * @param certification
     * @param noCertification
     * @return
     */
    Map<String, Object> save(Integer id, BigDecimal certification, BigDecimal noCertification);
}
