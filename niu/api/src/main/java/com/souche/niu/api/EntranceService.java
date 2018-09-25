package com.souche.niu.api;

import com.souche.niu.model.EntranceDto;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/15
 **/
public interface EntranceService {

    EntranceDto findOne();

    int save(EntranceDto dto);
}
