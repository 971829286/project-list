package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.AppVersion;

public interface IAppVersionService {
    AppVersion findTheLast();
}
