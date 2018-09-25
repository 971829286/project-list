package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.AppVersion;
import cn.ourwill.tuwenzb.mapper.AppVersionMapper;
import cn.ourwill.tuwenzb.service.IAppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppVersionServiceImpl implements IAppVersionService{
    @Autowired
    private AppVersionMapper appVersionMapper;

    @Override
    public AppVersion findTheLast() {
        return appVersionMapper.findTheLast();
    }
}
