package com.souche.niu.api;

import com.souche.niu.model.EntranceDto;

/**
 * @Description：入口配置接口
 *
 * @remark: Created by wujingtao in 2018/9/15
 **/
public interface EntranceService {

    /**
     * 获取入口配置唯一记录
     * @return
     */
    EntranceDto findOne();

    /**
     * 保存入口配置
     * 根据ID是否为空判断是保存/修改
     * @param dto
     * @return
     */
    int save(EntranceDto dto);
}
