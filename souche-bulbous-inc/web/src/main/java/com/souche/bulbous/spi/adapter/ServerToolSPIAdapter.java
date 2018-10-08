package com.souche.bulbous.spi.adapter;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.spi.ServerToolSPI;
import com.souche.niu.api.ServerToolService;
import com.souche.niu.model.ServerToolDto;
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
@Service("serverToolSPI")
public class ServerToolSPIAdapter implements ServerToolSPI {

    private static final Logger logger = LoggerFactory.getLogger(ServerToolSPIAdapter.class);

    @Resource
    private ServerToolService serverToolService;

    @Override
    public PageResult<ServerToolDto> findByPage(int page, int pageSize) {
        try {
            PageResult<ServerToolDto> result = this.serverToolService.findByPage(page, pageSize);
            logger.info("调用dubbo分页获取服务工具记录成功");
            return result;
        } catch (OptimusExceptionBase e) {
            logger.info("调用dubbo分页获取服务工具记录失败 {}",e.toString());
            throw e;
        }
    }

    @Override
    public ServerToolDto findById(Integer id) {
        try {
            ServerToolDto dto = this.serverToolService.findById(id);
            logger.info("dubbo服务获取服务工具记录成功 id=[{}] data:{}",id, JSON.toJSONString(dto));
            return dto;
        } catch (OptimusExceptionBase exceptionBase) {
            logger.info("dubbo服务查找服务工具记录失败 ID=[{}] exception:{}",id,exceptionBase.toString());
            throw exceptionBase;
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            this.serverToolService.deleteById(id);
            logger.info("调用dubbo服务删除服务工具记录成功 ID=[{}]",id);
        } catch (OptimusExceptionBase e) {
            logger.info("删除服务工具记录失败 ID=[{}] exception:{}", id, e.toString());
            throw e;
        }
    }

    @Override
    public int update(Integer id, ServerToolDto dto) {
        try {
            int count = this.serverToolService.update(id, dto);
            logger.info("调用dubbo服务修改服务工具记录成功 ID=[{}],data:{}", id, JSON.toJSONString(dto));
            return count;
        } catch (OptimusExceptionBase e) {
            logger.info("调用dubbo服务修改服务工具记录失败 ID=[{}],data:{},exception:{}",id,JSON.toJSONString(dto),e.toString());
            throw  e;
        }
    }

    @Override
    public int save(ServerToolDto dto) {
        try {
            int count = this.serverToolService.save(dto);
            logger.info("调用dubbo服务保存服务工具记录成功 {}", JSON.toJSONString(dto));
            return count;
        } catch (OptimusExceptionBase e) {
            logger.info("调用dubbo服务保存服务工具记录失败 {}", JSON.toJSONString(dto));
            throw  e;
        }
    }
}
