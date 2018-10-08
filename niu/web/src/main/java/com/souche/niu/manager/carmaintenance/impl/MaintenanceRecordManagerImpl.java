package com.souche.niu.manager.carmaintenance.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.souche.niu.mapper.CarMaintenanceQueryMapper;
import com.souche.niu.dao.mongo.MongoDao;
import com.souche.niu.manager.carmaintenance.MaintenanceRecordManager;
import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.niu.model.maintenance.respone.CarMaintenance;
import com.souche.niu.model.maintenance.respone.WbReport;
import com.souche.niu.dto.MaintenanceRecordDTO;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.dao.BasicDao;
import com.souche.optimus.exception.system.OptimusParamErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 维保查询的记录以及详情
 * Created by sid on 2018/9/5.
 */
@Service
public class MaintenanceRecordManagerImpl implements MaintenanceRecordManager {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceRecordManagerImpl.class);

    @Autowired
    private MongoDao mongoDao;

    @Autowired
    private BasicDao basicDao;

    @Autowired
    private CarMaintenanceQueryMapper carMaintenanceQueryMapper;

    private static final String CollectionName = "car_maintenance_detail";

    @Override
    public MaintenanceRecordDO createMaintenanceRecord(MaintenanceRecordDO maintenanceRecordDO) {
        logger.info("创建保养记录流水maintenanceRecordDO ={}",JsonUtils.toJson(maintenanceRecordDO));
        if (StringUtils.isEmpty(maintenanceRecordDO.getOrderId())) {
            throw new OptimusParamErrorException("orderId 必填");
        }
        basicDao.insert(maintenanceRecordDO);
        return getCarMaintenance(maintenanceRecordDO.getOrderId());
    }

    @Override
    public MaintenanceRecordDO getCarMaintenance(String orderId) {
        logger.info("查询保养记录流水表orderId ={}",orderId);
        return basicDao.findObjectByPrimaryKey(orderId, MaintenanceRecordDO.class);
    }

    @Override
    public MaintenanceRecordDO updateCarMaintenance(MaintenanceRecordDO maintenanceRecordDO) {
        logger.info("更新保养记录查询流水表 maintenanceRecordDO={}",JsonUtils.toJson(maintenanceRecordDO));
        basicDao.update(maintenanceRecordDO, true);
        return getCarMaintenance(maintenanceRecordDO.getOrderId());
    }

    @Override
    public List<MaintenanceRecordDO> getRecordList(String phone, int pageSize, int page) {
        Integer pageStart = 0;
        if(pageSize == 0){
            pageSize = 6;
        }
        if(page > 1){
            pageStart = (page - 1) * pageSize;
        }
        MaintenanceRecordDTO maintenanceRecordReq = new MaintenanceRecordDTO();
        maintenanceRecordReq.setPhone(phone);
        maintenanceRecordReq.setCurrentPage(pageStart);
        maintenanceRecordReq.setPageSize(pageSize);
        logger.info("订单列表 查询参数 maintenanceRecordReq = {}", maintenanceRecordReq);
        List<MaintenanceRecordDO> maintenanceRecordDOS = carMaintenanceQueryMapper.findlistbyPage(maintenanceRecordReq);
        logger.info("订单列表 查询结果 maintenanceRecordDOS={}", JsonUtils.toJson(maintenanceRecordDOS));
        return maintenanceRecordDOS;
    }

    @Override
    public Map<String, Object> getMaintenanceQueryDetail(String orderId) {
        logger.info("查詢维保详情 订单CODE orderId={}",orderId);
        //根据订单号查询维保信息
        Query query = new Query(Criteria.where("order_id").is(orderId));
        Map<String, Object> map = mongoDao.findOne(query, Map.class, CollectionName);
        return map;
    }

    @Override
    public void saveMaintenanceDetail(CarMaintenance carMaintenance, MaintenanceRecordDO maintenanceRecordDO) {
        logger.info("开始保存维保详情carMaintenance={},maintenanceRecordDO={}",JsonUtils.toJson(carMaintenance),JsonUtils.toJson(maintenanceRecordDO));
        Map<String, Object> detailMap = new HashMap<>();
        detailMap.put("order_id", maintenanceRecordDO.getOrderId());
        detailMap.put("order_type", maintenanceRecordDO.getOrderType());
        detailMap.put("order_image_url", maintenanceRecordDO.getImageUrl());
        detailMap.put("order_vin_number", maintenanceRecordDO.getVinNumber());
        detailMap.put("order_remarks", maintenanceRecordDO.getRemarks());
        detailMap.put("order_save_date", new Date());

        Map<String, Object> detail = new HashMap<>();
        String carModel = carMaintenance.getModelName();
        if (StringUtils.isNoneBlank(carModel)) {
            detail.put("model", carModel);
        }
        List<Map<String, Object>> list = Lists.newArrayList();

        for (WbReport wb : carMaintenance.getWbReportList()) {
            Map<String, Object> tempList = Maps.newHashMap();
            tempList.put("time", wb.getRepairDate());
            tempList.put("name", wb.getRepairType());
            tempList.put("mile", wb.getKmValue());
            tempList.put("content", wb.getRepairContent());
            tempList.put("components", wb.getMateral());
            list.add(tempList);
        }
        detail.put("list", list);
        detailMap.put("order_detail", detail);
        logger.info("保存维保详情detailMap={}",detailMap);
        try {
            mongoDao.save(detailMap, CollectionName);
        }catch (Exception e){
            //不care了
            logger.error("保存出错",e);
        }
    }
}
