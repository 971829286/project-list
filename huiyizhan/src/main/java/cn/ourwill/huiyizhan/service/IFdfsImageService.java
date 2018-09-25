package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.FdfsImage;

public interface IFdfsImageService {

    Integer save(FdfsImage fdfsImage);

    Integer update(FdfsImage fdfsImage);

    Integer insert(String url);

    Integer dataPersistence (String path);

    void delete(String path) throws Exception;
}
