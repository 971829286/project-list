package com.souche.niu.mapper;

import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.niu.dto.MaintenanceRecordDTO;
import java.util.List;

public interface CarMaintenanceQueryMapper {

    List<MaintenanceRecordDO> findlistbyPage(MaintenanceRecordDTO maintenanceRecordReq);

}
