package com.souche.bulbous.spi.adapter;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.spi.ActivityScreenSPI;
import com.souche.niu.api.ActivityScreenService;
import com.souche.niu.model.ActivityScreenDto;
import com.souche.niu.result.PageResult;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/15
 **/
@Service("activityScreenSPI")
public class ActivityScreenSPIAdapter implements ActivityScreenSPI {

    private static final Logger logger = LoggerFactory.getLogger(ActivityScreenSPIAdapter.class);

    @Resource
    private ActivityScreenService activityScreenService;

    @Override
    public int save(ActivityScreenDto dto) {
        try {
            int count = this.activityScreenService.save(dto);
            if (count > 0) {
                logger.info("保存活动浮窗记录成功 {}", JSON.toJSONString(dto));
                return count;
            }
            logger.info("保存活动浮窗记录失败 {}", JSON.toJSONString(dto));
            return count;
        } catch (OptimusExceptionBase e) {
            logger.info("保存活动浮窗记录失败 {}", e.toString());
            throw e;
        }
    }

    @Override
    public int update(Integer id, ActivityScreenDto dto) {
        try {
            int count = this.activityScreenService.update(id, dto);
            if (count > 0) {
                logger.info("修改活动浮窗记录成功 id=[{}],{}", id, JSON.toJSONString(dto));
                return count;
            }
            logger.info("修改活动浮窗记录失败 ID=[{}],{}", id, JSON.toJSONString(dto));
            return count;
        } catch (OptimusExceptionBase e) {
            logger.info("修改活动浮窗记录失败 {}", e.toString());
            throw e;
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            this.activityScreenService.deleteById(id);
            logger.info("删除活动浮窗记录成功 ID=[{}]",id);
        } catch (OptimusExceptionBase e) {
            logger.info("删除活动浮窗记录失败 ID=[{}]，exception:{}",id,e.toString());
            throw e;
        }
    }

    @Override
    public PageResult<ActivityScreenDto> findByPage(int page, int pageSize, String status) {
        try {
            PageResult<ActivityScreenDto> result = this.activityScreenService.findByPage(page, pageSize, status);
            return result;
        } catch (OptimusExceptionBase exceptionBase) {
            logger.info("查找活动浮窗记录失败 {}",exceptionBase.toString());
            throw exceptionBase;
        }
    }

    @Override
    public ActivityScreenDto findById(Integer id) {
        return this.activityScreenService.findById(id);
    }
}
