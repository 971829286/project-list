package com.souche.niu.dao;

import com.souche.niu.model.MaintenanceConfigDO;

/**
 * @Description：维保配置数据接口
 *
 * @remark: Created by wujingtao in 2018/9/17
 **/
public interface MaintenanceConfigDao {

    MaintenanceConfigDO findOne();

    /**
     * 该配置表中只包含一条记录 所以执行保存操作前会进行清空操作
     * @param cfgdo
     * @return
     */
    int save(MaintenanceConfigDO cfgdo);

    int update(MaintenanceConfigDO cfgdo);
}
