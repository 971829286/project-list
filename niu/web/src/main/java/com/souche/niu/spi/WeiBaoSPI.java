package com.souche.niu.spi;

import com.souche.niu.model.maintenance.request.QueryMaintenanceRequest;

import java.util.Map;

/**
 * 维保查询接口
 * Created by sid on 2018/9/3.
 */
public interface WeiBaoSPI {

    /**
     *
     * @return
     */
    Map<String,Object> thirdServiceQuery(QueryMaintenanceRequest queryMaintenanceRequest);
}
