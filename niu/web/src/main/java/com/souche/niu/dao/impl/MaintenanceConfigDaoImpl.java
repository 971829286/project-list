package com.souche.niu.dao.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.souche.niu.dao.MaintenanceConfigDao;
import com.souche.niu.model.MaintenanceConfigDO;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.dao.BasicDao;
import com.souche.optimus.dao.query.QueryObj;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/17
 **/
@Service("maintenanceConfigDao")
public class MaintenanceConfigDaoImpl implements MaintenanceConfigDao {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceConfigDaoImpl.class);

    @Resource
    private BasicDao basicDao;


    @Override
    public MaintenanceConfigDO findOne() {
        QueryObj obj = new QueryObj(new MaintenanceConfigDO());
        List<MaintenanceConfigDO> list = this.basicDao.findListByQuery(obj, MaintenanceConfigDO.class);
        if (CollectionUtils.isEmpty(list)) {
            logger.info("维保配置表为空");
            return null;
        }
        return list.get(0);
    }

    @Override
    public int save(MaintenanceConfigDO cfgdo) {
        QueryObj obj = new QueryObj(new MaintenanceConfigDO());
        List<MaintenanceConfigDO> list = this.basicDao.findListByQuery(obj, MaintenanceConfigDO.class);
        if (CollectionUtils.isNotEmpty(list)) {
            for (MaintenanceConfigDO cfg : list) {
                this.basicDao.realDelete(cfg, MaintenanceConfigDO.class);
            }
        }
        int count = this.basicDao.insert(cfgdo, MaintenanceConfigDO.class);
        if (count > 0) {
            logger.info("保存维保配置信息成功 {}",JsonUtils.toJson(cfgdo));
            return count;
        }
        logger.info("保存维保配置信息失败 {}", JsonUtils.toJson(cfgdo));
        return count;
    }

    @Override
    public int update(MaintenanceConfigDO cfgdo) {
        if (cfgdo == null) {
            logger.info("修改维保配置信息失败 参数为空");
            throw new OptimusExceptionBase("修改维保配置信息失败 参数为空");
        }
        int count = this.basicDao.update(cfgdo, MaintenanceConfigDO.class,true);
        if (count > 0) {
            logger.info("修改维保配置信息成功 修改列包含：{}",JsonUtils.toJson(cfgdo));
            return count;
        }
        logger.info("修改维保配置信息失败 预修改列包含：{}",JsonUtils.toJson(cfgdo));
        return count;
    }
}
