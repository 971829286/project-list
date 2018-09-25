package com.souche.niu.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.souche.niu.api.OpenScreenService;
import com.souche.niu.bean.KvValue;
import com.souche.niu.dao.KvValueDao;
import com.souche.niu.model.OpenScreenDto;
import com.souche.niu.result.PageResult;
import com.souche.niu.util.KvValueToOpenScreenUtils;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/12
 **/
@Service("openScreenService")
public class OpenScreenServiceImpl implements OpenScreenService {

    private static final Logger logger = LoggerFactory.getLogger(OpenScreenServiceImpl.class);

    private static final String groupId = OptimusConfig.getValue("open.screen.group.id");


    @Autowired
    private KvValueDao kvValueDao;

    @Override
    public int save(OpenScreenDto dto) {
        if (dto == null) {
            logger.info("保存开屏记录失败 参数为空");
            throw new OptimusExceptionBase("保存开屏记录失败 参数为空");
        }
        if (StringUtil.isEmpty(groupId)) {
            logger.info("保存开屏记录失败 未获取到groupId");
            throw new OptimusExceptionBase("保存开屏记录失败 未获取到groupId");
        }
        dto.setGroupId(groupId);
        dto.setCreatedAt(new Date());
        KvValue kvValue = KvValueToOpenScreenUtils.getKvValueByOpenScreen(dto);
        int count = this.kvValueDao.save(kvValue);
        if (count > 0) {
            logger.info("保存开屏记录成功 {}", JsonUtils.toJson(kvValue));
            return count;
        }
        logger.info("保存开屏记录失败 {}",JsonUtils.toJson(kvValue));
        return count;
    }

    @Override
    public int update(Integer id, OpenScreenDto dto) {
        KvValue kv = this.kvValueDao.findById(id);
        if (kv == null) {
            logger.info("修改开屏记录失败 当前ID未查找到记录 id=[{}]",id);
            throw new OptimusExceptionBase("修改开屏记录失败 当前ID未查找到记录 id=" + id);
        }
        if (dto == null) {
            logger.info("修改开屏记录失败 参数为空");
            throw new OptimusExceptionBase("修改开屏记录失败 参数为空");
        }
        dto.setId(id);
        dto.setGroupId(kv.getGroupId());
        dto.setCreatedAt(kv.getCreatedAt());
        dto.setUpdatedAt(new Date());
        KvValue kvValue = KvValueToOpenScreenUtils.getKvValueByOpenScreen(dto);
        int count = this.kvValueDao.update(id, kvValue);
        if (count > 0) {
            logger.info("修改开屏记录成功 id=[{}] kvValue={}",id,JsonUtils.toJson(kvValue));
            return count;
        }
        logger.info("修改开屏记录失败 id=[{}] kvValue={}",id,JsonUtils.toJson(kvValue));
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        this.kvValueDao.deleteById(id);
        logger.info("删除开屏记录失败 ID=[{}]",id);
    }

    @Override
    public PageResult<OpenScreenDto> findByPage(int page, int pageSize, String status) {
        if (StringUtil.isEmpty(groupId)) {
            logger.info("groupId未配置");
            throw new OptimusExceptionBase("groupId未配置");
        }
        List<KvValue> list = this.kvValueDao.findByGroupId(groupId);
        if (CollectionUtils.isEmpty(list)) {
            return new PageResult<>(page, pageSize, 0, null);
        }
        List<OpenScreenDto> dtos = new ArrayList<>();
        for (KvValue kv : list) {
            OpenScreenDto dto = KvValueToOpenScreenUtils.getByKvValue(kv);
            if (StringUtil.isEmpty(status) || status.equals("1")) {
                dtos.add(dto);
                continue;
            }
            if (status.equals(dto.getStatus())) {
                dtos.add(dto);
            }
        }
        if (CollectionUtils.isEmpty(dtos)) {
            logger.info("当前有效期状态未查找到开屏记录 status=[{}]",status);
            return new PageResult<>(page, pageSize, 0, null);
        }
        //对集合按照创建时间排序
        Collections.sort(dtos, new Comparator<OpenScreenDto>() {
            @Override
            public int compare(OpenScreenDto o1, OpenScreenDto o2) {
                if (o1.getCreatedAt().getTime() >= o2.getCreatedAt().getTime()) {
                    return -1;
                }
                return 1;
            }
        });

        //构建分页记录
        List<OpenScreenDto> data = new ArrayList<>();
        for (int i = (page - 1) * pageSize; i < page * pageSize && i < dtos.size(); i++) {
            data.add(dtos.get(i));
        }
        return new PageResult<>(page,pageSize,dtos.size(),data);
    }

    @Override
    public OpenScreenDto findById(Integer id) {
        KvValue kvValue = this.kvValueDao.findById(id);
        OpenScreenDto dto=KvValueToOpenScreenUtils.getByKvValue(kvValue);
        return dto;
    }
}
