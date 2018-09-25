package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.LightMap;
import cn.ourwill.tuwenzb.mapper.LightMapMapper;
import cn.ourwill.tuwenzb.service.ILightMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/26 11:42
 * @Description
 */
@Service
public class LightMapServiceImpl implements ILightMapService{
    @Autowired
    LightMapMapper lightMapMapper;

    @Override
    public LightMap getById(Integer id) {
        return lightMapMapper.getById(id);
    }
}
