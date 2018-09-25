package com.souche.bulbous.manager.impl;

import com.souche.bulbous.manager.OpenScreenManager;
import com.souche.bulbous.spi.OpenScreenSPI;
import com.souche.bulbous.vo.OpenScreenVo;
import com.souche.niu.model.OpenScreenDto;
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
 * @remark: Created by wujingtao in 2018/9/14
 **/
@Service("OpenScreenManager")
public class OpenScreenManagerImpl implements OpenScreenManager {

    private static final Logger logger = LoggerFactory.getLogger(OpenScreenManagerImpl.class);

    @Autowired
    private OpenScreenSPI openScreenSPI;

    @Override
    public PageResult<OpenScreenVo> findByPage(int page, int pageSize, String status) {

        PageResult<OpenScreenDto> pageDtos = this.openScreenSPI.findByPage(page, pageSize, status);
        if (CollectionUtils.isEmpty(pageDtos.getItem())) {
            logger.info("开屏记录列表为空");
            return new PageResult<OpenScreenVo>(page, pageSize, 0, null);
        }
        List<OpenScreenDto> dtoList=pageDtos.getItem();
        List<OpenScreenVo> voList = new ArrayList<>();
        for (OpenScreenDto dto : dtoList) {
            OpenScreenVo vo = new OpenScreenVo(dto);
            voList.add(vo);
        }
        PageResult<OpenScreenVo> result = new PageResult<>(pageDtos.getPage(), pageDtos.getPageSize(), pageDtos.getTotal(), voList);
        return result;
    }

    @Override
    public OpenScreenVo findById(String id) {
        if (StringUtil.isEmpty(id)) {
            logger.info("获取开屏记录失败 ID为空");
            return null;
        }
        try {
            OpenScreenDto dto = this.openScreenSPI.findById(Integer.parseInt(id));
            if (dto == null) {
                logger.info("当前ID未查找到开屏记录 id=[{}]",id);
                return null;
            }
            OpenScreenVo vo = new OpenScreenVo(dto);
            return vo;
        } catch (OptimusExceptionBase exceptionBase) {
            logger.info("获取开屏记录失败 {}",exceptionBase.toString());
            throw exceptionBase;
        }
    }
}
