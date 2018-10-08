package com.souche.niu.manager.carmaintenance;

import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.niu.model.maintenance.respone.CarMaintenance;
import java.util.List;
import java.util.Map;

/**
 * Created by sid on 2018/9/5.
 */
public interface MaintenanceRecordManager {

    /**
     * 创建车辆保养记录
     *
     * @return
     */
    MaintenanceRecordDO createMaintenanceRecord(MaintenanceRecordDO maintenanceRecordDO);

    /**
     * 记录查询
     *
     * @param orderId
     * @return
     */
    MaintenanceRecordDO getCarMaintenance(String orderId);

    /**
     * @param maintenanceRecordDO
     * @return
     */
    MaintenanceRecordDO updateCarMaintenance(MaintenanceRecordDO maintenanceRecordDO);

    /**
     * 分页查询
     *
     * @param phone
     * @param pageSize
     * @param page
     * @return
     */
    List<MaintenanceRecordDO> getRecordList(String phone, int pageSize, int page);

    /**
     * 查询详情
     * @param orderId
     * @return
     */
    Map<String,Object> getMaintenanceQueryDetail(String orderId);

    /**
     * 保存详情
     * @param carMaintenance
     */
    void saveMaintenanceDetail(CarMaintenance carMaintenance,MaintenanceRecordDO maintenanceRecordDO);
}
