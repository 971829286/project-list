package com.souche.niu.manager.cms;

import com.souche.niu.model.Banner;
import com.souche.niu.model.MaintenanceConfigDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sid on 2018/9/5.
 */
public interface CmsManager {

    /**
     * 查询维保设置
     *
     * @return
     */
    MaintenanceConfigDO queryMaintenance();


    List<Banner>  getBannerListByStatus(Integer status, Integer page, Integer pageSize);

}
