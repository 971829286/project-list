package com.souche.bulbous.spi;

import com.souche.niu.model.EntranceDto;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/15
 **/
public interface EntranceSPI {

    EntranceDto findOne();

    int save(EntranceDto dto);
}
