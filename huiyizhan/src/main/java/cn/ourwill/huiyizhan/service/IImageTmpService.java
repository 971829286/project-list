package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.ImageTmp;

import java.util.List;

public interface IImageTmpService {

    public Integer update (ImageTmp imageTmp);

    public List<ImageTmp> selectImageTmp (String tableName, String fieldName);
}
