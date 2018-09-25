package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.ImageTmp;
import cn.ourwill.huiyizhan.mapper.ImageTmpMapper;
import cn.ourwill.huiyizhan.service.IImageTmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-06-13 9:57
 **/
@Service
public class ImageTmpServiceImpl  implements IImageTmpService {

    @Autowired
    ImageTmpMapper imageTmpMapper;

    @Override
    public Integer update (ImageTmp imageTmp){
        return  imageTmpMapper.update(imageTmp);
    }

    @Override
    public List<ImageTmp> selectImageTmp (String tableName, String fieldName){
        return  imageTmpMapper.selectImageTmp(tableName,fieldName);
    }
}
