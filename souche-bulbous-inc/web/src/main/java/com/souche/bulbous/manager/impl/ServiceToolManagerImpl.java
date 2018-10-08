package com.souche.bulbous.manager.impl;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.manager.ServiceToolManager;
import com.souche.bulbous.spi.ServerToolSPI;
import com.souche.bulbous.vo.ServiceToolVo;
import com.souche.niu.model.ServerToolDto;
import com.souche.niu.result.PageResult;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.StringUtil;
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
@Service("serviceToolManager")
public class ServiceToolManagerImpl implements ServiceToolManager {

    private static final Logger logger = LoggerFactory.getLogger(ServiceToolManagerImpl.class);

    @Autowired
    private ServerToolSPI serverToolSPI;

    @Override
    public PageResult<ServiceToolVo> findByPage(int page, int pageSize) {
        PageResult<ServerToolDto> dtos = this.serverToolSPI.findByPage(page, pageSize);
        if (CollectionUtils.isEmpty(dtos.getItem())) {
            logger.info("分页获取服务工具记录为空");
            return new PageResult<>(page, pageSize, 0, null);
        }
        List<ServiceToolVo> voList = new ArrayList<>();
        for (ServerToolDto dto : dtos.getItem()) {
            ServiceToolVo vo = this.getByServerToolDto(dto);
            voList.add(vo);
        }
        return new PageResult<>(page, pageSize, dtos.getTotal(), voList);
    }

    @Override
    public int save(ServiceToolVo vo) {
        ServerToolDto dto=this.getByServiceToolVo(vo);
        logger.info(JSON.toJSONString(dto));
        int count=0;
        if (StringUtil.isEmpty(vo.getId())) {
            count = this.serverToolSPI.save(dto);
            return count;
        }
        count = this.serverToolSPI.update(Integer.parseInt(vo.getId()), dto);
        return count;
    }

    @Override
    public ServiceToolVo findById(Integer id) {
        ServerToolDto dto = this.serverToolSPI.findById(id);
        ServiceToolVo vo = this.getByServerToolDto(dto);
        return vo;
    }

    @Override
    public void deleteById(Integer id) {
        this.serverToolSPI.deleteById(id);
    }

    private ServiceToolVo getByServerToolDto(ServerToolDto dto) {
        if (dto == null) {
            return null;
        }
        ServiceToolVo vo = new ServiceToolVo();
        vo.setId(dto.getId()+"");
        vo.setTitle(dto.getTitle());
        vo.setProtocol(dto.getProtocol());
        vo.setUrl2X(dto.getUrl2X());
        vo.setUrl3X(dto.getUrl3X());
        vo.setClickPoint(dto.getClickPoint());
        vo.setOrderNum(dto.getOrderNum());
        if (dto.getFirstShow() == null) {
            vo.setIsShow("否");
        }else {
            vo.setIsShow(dto.getFirstShow()==true?"是":"否");
        }
        return vo;
    }

    private ServerToolDto getByServiceToolVo(ServiceToolVo vo) {
        if (vo == null) {
            return null;
        }
        ServerToolDto dto = new ServerToolDto();
        if (StringUtil.isNotEmpty(vo.getId())) {
            dto.setId(Integer.parseInt(vo.getId()));
        }
        dto.setTitle(vo.getTitle());
        dto.setProtocol(vo.getProtocol());
        dto.setUrl2X(vo.getUrl2X());
        dto.setUrl3X(vo.getUrl3X());
        dto.setClickPoint(vo.getClickPoint());
        dto.setOrderNum(vo.getOrderNum());
        dto.setFirstShow(Boolean.parseBoolean(vo.getIsShow()));
        return dto;
    }
}
