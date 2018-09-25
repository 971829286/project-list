package com.souche.niu.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.souche.niu.api.ActivityScreenService;
import com.souche.niu.bean.KvValue;
import com.souche.niu.dao.KvValueDao;
import com.souche.niu.model.ActivityScreenDto;
import com.souche.niu.result.PageResult;
import com.souche.niu.util.KvValueToActivityScreenUtils;
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
 * @remark: Created by wujingtao in 2018/9/15
 **/
@Service("activityScreenService")
public class ActivityScreenServiceImpl implements ActivityScreenService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityScreenServiceImpl.class);

    private static final String groupId = OptimusConfig.getValue("activity.screen.group.id");

    @Autowired
    private KvValueDao kvValueDao;

    @Override
    public int save(ActivityScreenDto dto) {
        if (dto == null) {
            logger.info("保存活动浮窗记录失败 参数为空");
            throw new OptimusExceptionBase("保存活动浮窗记录失败 参数为空");
        }
        if (StringUtil.isEmpty(groupId)) {
            logger.info("保存开屏记录失败 未获取到groupId");
            throw new OptimusExceptionBase("保存活动浮窗记录失败 未获取到groupId");
        }
        dto.setGroupId(groupId);
        dto.setCreatedAt(new Date());
        KvValue kvValue = KvValueToActivityScreenUtils.getKvValueByOpenScreen(dto);
        int count = this.kvValueDao.save(kvValue);
        if (count > 0) {
            logger.info("保存活动弄浮窗记录成功 {}", JsonUtils.toJson(kvValue));
            return count;
        }
        logger.info("保存活动浮窗记录失败 {}",JsonUtils.toJson(kvValue));
        return count;
    }

    @Override
    public int update(Integer id, ActivityScreenDto dto) {
        KvValue kv = this.kvValueDao.findById(id);
        if (kv == null) {
            logger.info("修改活动浮窗记录失败 当前ID未查找到记录 id=[{}]",id);
            throw new OptimusExceptionBase("修改开屏记录失败 当前ID未查找到记录 id=" + id);
        }
        if (dto == null) {
            logger.info("修改活动浮窗记录失败 参数为空");
            throw new OptimusExceptionBase("修改活动浮窗记录失败 参数为空");
        }
        dto.setId(id);
        dto.setGroupId(kv.getGroupId());
        dto.setCreatedAt(kv.getCreatedAt());
        dto.setUpdatedAt(new Date());
        KvValue kvValue = KvValueToActivityScreenUtils.getKvValueByOpenScreen(dto);
        int count = this.kvValueDao.update(id, kvValue);
        if (count > 0) {
            logger.info("修改活动浮窗记录成功 id=[{}] kvValue={}",id,JsonUtils.toJson(kvValue));
            return count;
        }
        logger.info("修改活动浮窗记录失败 id=[{}] kvValue={}",id,JsonUtils.toJson(kvValue));
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        this.kvValueDao.deleteById(id);
        logger.info("删除活动浮窗记录失败 ID=[{}]",id);
    }

    @Override
    public PageResult<ActivityScreenDto> findByPage(int page, int pageSize, String status) {
        if (StringUtil.isEmpty(groupId)) {
            logger.info("groupId未配置");
            throw new OptimusExceptionBase("groupId未配置");
        }
        List<KvValue> list = this.kvValueDao.findByGroupId(groupId);
        if (CollectionUtils.isEmpty(list)) {
            return new PageResult<>(page, pageSize, 0, null);
        }
        List<ActivityScreenDto> dtos = new ArrayList<>();
        for (KvValue kv : list) {
            ActivityScreenDto dto = KvValueToActivityScreenUtils.getByKvValue(kv);
            if (StringUtil.isEmpty(status) || status.equals("1")) {
                dtos.add(dto);
                continue;
            }
            if (status.equals(dto.getStatus())) {
                dtos.add(dto);
            }
        }
        if (CollectionUtils.isEmpty(dtos)) {
            logger.info("当前有效期状态未查找到活动浮窗记录 status=[{}]",status);
            return new PageResult<>(page, pageSize, 0, null);
        }
        //对集合按照创建时间排序
        Collections.sort(dtos, new Comparator<ActivityScreenDto>() {
            @Override
            public int compare(ActivityScreenDto o1, ActivityScreenDto o2) {
                if (o1.getCreatedAt().getTime() >= o2.getCreatedAt().getTime()) {
                    return -1;
                }
                return 1;
            }
        });

        //构建分页记录
        List<ActivityScreenDto> data = new ArrayList<>();
        for (int i = (page - 1) * pageSize; i < page * pageSize && i < dtos.size(); i++) {
            data.add(dtos.get(i));
        }
        return new PageResult<>(page,pageSize,dtos.size(),data);
    }

    @Override
    public ActivityScreenDto findById(Integer id) {
        KvValue kvValue = this.kvValueDao.findById(id);
        ActivityScreenDto dto=KvValueToActivityScreenUtils.getByKvValue(kvValue);
        return dto;
    }
}
