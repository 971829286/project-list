package com.souche.bulbous.spi.adapter;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.spi.OpenScreenSPI;
import com.souche.niu.api.OpenScreenService;
import com.souche.niu.model.OpenScreenDto;
import com.souche.niu.result.PageResult;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/14
 **/
@Service("openScreenSPI")
public class OpenScreenSPIAdapter implements OpenScreenSPI {

    private static final Logger logger = LoggerFactory.getLogger(OpenScreenSPIAdapter.class);

    @Resource
    private OpenScreenService openScreenService;

    @Override
    public int save(OpenScreenDto dto) {
        try {
            int count = this.openScreenService.save(dto);
            if (count > 0) {
                logger.info("保存开屏记录成功 {}", JSON.toJSONString(dto));
                return count;
            }
            logger.info("保存开屏记录失败 {}",JSON.toJSONString(dto));
            return count;
        } catch (OptimusExceptionBase e) {
            logger.info("保存失败 {}",e.toString());
            throw e;
        }
    }

    @Override
    public int update(Integer id, OpenScreenDto dto) {
        try {
            int count = this.openScreenService.update(id, dto);
            if (count > 0) {
                logger.info("修改开屏记录成功 id=[{}],{}",id,JSON.toJSONString(dto));
                return count;
            }
            logger.info("修改开屏记录失败 ID=[{}],{}",id,JSON.toJSONString(dto));
            return count;
        } catch (OptimusExceptionBase e) {
            logger.info("修改开屏配置记录失败 {}",e.toString());
            throw e;
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            this.openScreenService.deleteById(id);
            logger.info("删除开屏记录成功 ID=[{}]",id);
        } catch (OptimusExceptionBase e) {
            logger.info("删除开屏记录失败 ID=[{}],exception:{}",id,e.toString());
            throw e;
        }
    }

    @Override
    public PageResult<OpenScreenDto> findByPage(int page, int pageSize, String status) {
        try {
            PageResult<OpenScreenDto> result = this.openScreenService.findByPage(page, pageSize, status);
            return result;
        } catch (OptimusExceptionBase exceptionBase) {
            logger.info("查找开屏记录失败 {}",exceptionBase.toString());
            throw exceptionBase;
        }
    }

    @Override
    public OpenScreenDto findById(Integer id) {
        return this.openScreenService.findById(id);
    }

}
