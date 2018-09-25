package com.souche.niu.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.souche.niu.api.ServerToolService;
import com.souche.niu.bean.KvValue;
import com.souche.niu.dao.KvValueDao;
import com.souche.niu.model.ServerToolDto;
import com.souche.niu.result.PageResult;
import com.souche.niu.util.ServerToolUtil;
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
 * @author XuJinNiu
 * @since 2018-09-14
 */
@Service("serverToolService")
public class ServerToolServiceImpl implements ServerToolService {

    private static final Logger logger = LoggerFactory.getLogger(OpenScreenServiceImpl.class);

    private static final String groupId = OptimusConfig.getValue("server.tool.groupId");


    @Autowired
    private KvValueDao kvValueDao;

    @Override
    public int save(ServerToolDto dto) {
        if (dto == null) {
            logger.info("保存serverTool记录失败 参数为空");
            throw new OptimusExceptionBase("保存serverTool记录失败 参数为空");
        }
        if (StringUtil.isEmpty(groupId)) {
            logger.info("保存serverTool记录失败 未获取到groupId");
            throw new OptimusExceptionBase("保存serverTool记录失败 未获取到groupId");
        }
        dto.setGroupId(groupId);
        dto.setCreatedAt(new Date());
        KvValue kvValue = ServerToolUtil.getKvValueByServerTool(dto);
        int count = kvValueDao.save(kvValue);
        if(count > 0){
            logger.info("serverTool保存成功 {}", JsonUtils.toJson(kvValue));
            return count;
        }
        logger.info("serverTool保存成功 {}",JsonUtils.toJson(kvValue));
        return count;
    }

    @Override
    public int update(Integer id, ServerToolDto dto) {
        KvValue kv = this.kvValueDao.findById(id);
        if (kv == null) {
            logger.info("修改serverTool失败 当前ID未查找到记录 id=[{}]",id);
            throw new OptimusExceptionBase("修改serverTool失败 当前ID未查找到记录 id=" + id);
        }
        if (dto == null) {
            logger.info("修改serverTool失败 参数为空");
            throw new OptimusExceptionBase("修改serverTool失败 参数为空");
        }
        dto.setId(id);
        dto.setGroupId(kv.getGroupId());
        dto.setCreatedAt(kv.getCreatedAt());
        dto.setUpdatedAt(new Date());
        KvValue kvValue = ServerToolUtil.getKvValueByServerTool(dto);
        int count = this.kvValueDao.update(id, kvValue);
        if (count > 0) {
            logger.info("修改开屏记录成功 id=[{}] kvValue={}",id,JsonUtils.toJson(kvValue));
            return count;
        }
        logger.info("修改开屏记录失败 id=[{}] kvValue={}",id,JsonUtils.toJson(kvValue));
        return count;
    }

    @Override
    public ServerToolDto findById(Integer id) {
        KvValue kvValue = this.kvValueDao.findById(id);
        ServerToolDto dto=ServerToolUtil.getByKvValue(kvValue);
        return dto;
    }

    @Override
    public void deleteById(Integer id) {
       this.kvValueDao.deleteById(id);
    }

    @Override

    public PageResult<ServerToolDto> findByPage(int page, int pageSize) {
        if (StringUtil.isEmpty(groupId)) {
            logger.info("groupId未配置");
            throw new OptimusExceptionBase("groupId未配置");
        }
        List<KvValue> list = this.kvValueDao.findByGroupId(groupId);
        if (CollectionUtils.isEmpty(list)) {
            return new PageResult<>(page, pageSize, 0, null);
        }
        List<ServerToolDto> dtos = new ArrayList<>();
        list.stream().forEach(e -> {
            ServerToolDto dto = ServerToolUtil.getByKvValue(e);
            if(dto != null){
                dtos.add(dto);
            }
        });
        if (CollectionUtils.isEmpty(dtos)) {
            logger.info("当前查找记录为空");
            return new PageResult<>(page, pageSize, 0, null);
        }
        //对集合按照创建时间排序
        Collections.sort(dtos, new Comparator<ServerToolDto>() {
            @Override
            public int compare(ServerToolDto o1, ServerToolDto o2) {
                //todo 兼容老数据  老数据没有orderNum字段
                if (StringUtil.isNotEmpty(o1.getOrderNum()) && StringUtil.isNotEmpty(o2.getOrderNum())) {
                    Integer orderNum1 = Integer.parseInt(o1.getOrderNum());
                    Integer orderNum2 = Integer.parseInt(o2.getOrderNum());
                    if (orderNum1 <= orderNum2) {
                        return 1;
                    }
                }
                return -1;
            }
        });

        //分页
        int stepSize = (page - 1) * pageSize;
        List<ServerToolDto> data = new ArrayList<>();
        dtos.stream()
                .skip(stepSize)
                .limit(pageSize)
                .forEach(e->data.add(e));
        return new PageResult<>(page,pageSize,dtos.size(),data);
    }
}
