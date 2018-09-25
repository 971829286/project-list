package com.souche.bulbous.manager.impl;

import com.souche.bulbous.manager.ActivityScreenManager;
import com.souche.bulbous.spi.ActivityScreenSPI;
import com.souche.bulbous.vo.ActivityScreenVo;
import com.souche.niu.model.ActivityScreenDto;
import com.souche.niu.result.PageResult;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/15
 **/
@Service("activityScreenManager")
public class ActivityScreenManagerImpl implements ActivityScreenManager {

    private static final Logger logger = LoggerFactory.getLogger(ActivityScreenManagerImpl.class);

    @Autowired
    private ActivityScreenSPI activityScreenSPI;

    @Override
    public PageResult<ActivityScreenVo> findByPage(int page, int pageSize, String status) {
        PageResult<ActivityScreenDto> pageDtos = this.activityScreenSPI.findByPage(page, pageSize, status);
        if (CollectionUtils.isEmpty(pageDtos.getItem())) {
            logger.info("活动浮窗记录列表为空");
            return new PageResult<>(page, pageSize, 0, null);
        }
        List<ActivityScreenDto> dtoList=pageDtos.getItem();
        List<ActivityScreenVo> voList = new ArrayList<>();
        for (ActivityScreenDto dto : dtoList) {
            ActivityScreenVo vo = new ActivityScreenVo(dto);
            voList.add(vo);
        }
        PageResult<ActivityScreenVo> result = new PageResult<>(pageDtos.getPage(), pageDtos.getPageSize(), pageDtos.getTotal(), voList);
        return result;
    }

    @Override
    public ActivityScreenVo findById(String id) {
        if (StringUtil.isEmpty(id)) {
            logger.info("获取活动浮窗记录失败 ID为空");
            return null;
        }
        try {
            ActivityScreenDto dto = this.activityScreenSPI.findById(Integer.parseInt(id));
            if (dto == null) {
                logger.info("当前ID未查找到活动浮窗记录 id=[{}]",id);
                return null;
            }
            ActivityScreenVo vo = new ActivityScreenVo(dto);
            return vo;
        } catch (OptimusExceptionBase exceptionBase) {
            logger.info("获取活动浮窗记录失败 {}",exceptionBase.toString());
            throw exceptionBase;
        }
    }
}
