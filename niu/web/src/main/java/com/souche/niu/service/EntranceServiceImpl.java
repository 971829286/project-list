package com.souche.niu.service;

import com.souche.niu.api.EntranceService;
import com.souche.niu.bean.KvValue;
import com.souche.niu.dao.KvValueDao;
import com.souche.niu.model.EntranceDto;
import com.souche.niu.util.KvValueToEntranceUtils;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/15
 **/
@Service("entranceService")
public class EntranceServiceImpl implements EntranceService {

    private static final Logger logger = LoggerFactory.getLogger(EntranceServiceImpl.class);

    private static final String groupId = OptimusConfig.getValue("service.tool.entrance.group.id");

    @Autowired
    private KvValueDao kvValueDao;

    @Override
    public EntranceDto findOne() {
        List<KvValue> list = this.kvValueDao.findByGroupId(groupId);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        KvValue kvValue = list.get(0);
        return KvValueToEntranceUtils.getByKvValue(kvValue);
    }

    @Override
    public int save(EntranceDto dto) {
        if (dto == null) {
            logger.error("保存入口配置失败 参数为空");
            throw new OptimusExceptionBase("保存入口配置失败 参数为空");
        }
        if (StringUtil.isEmpty(groupId)) {
            logger.error("保存入口配置失败 入口配置groupID为空");
            throw new OptimusExceptionBase("入口配置groupID为空");
        }
        dto.setGroupId(groupId);
        KvValue kvValue = KvValueToEntranceUtils.getKvValueByEntrance(dto);
        int count=0;
        if (kvValue.getId() == null) {
            //todo 删除库里原有所有入口配置 原则上入口配置只要一条记录
            List<KvValue> list = this.kvValueDao.findByGroupId(groupId);
            if (CollectionUtils.isNotEmpty(list)) {
                for (KvValue value : list) {
                    this.kvValueDao.deleteById(value.getId());
                }
            }
            kvValue.setCreatedAt(new Date());
            count = kvValueDao.save(kvValue);
            return count;
        }
        kvValue.setUpdatedAt(new Date());
        count = this.kvValueDao.update(kvValue.getId(), kvValue);
        return count;
    }

}
