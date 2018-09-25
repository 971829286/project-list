package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.Image;
import cn.ourwill.huiyizhan.mapper.ImageMapper;
import cn.ourwill.huiyizhan.service.IFdfsImageService;
import cn.ourwill.huiyizhan.service.IImgService;
import com.qiniu.common.QiniuException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-12 13:38
 **/
@Service
public class ImgServiceImpl implements IImgService{
    @Autowired
    QiniuServiceImpl qiniuService;

    @Autowired
    ImageMapper imageMapper;

    @Autowired
    private IFdfsImageService fdfsImageService;

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Integer save(Image image) throws QiniuException {
//        if (image.getPic() != null && image.getPic().startsWith("temp/")){
//            image.setPic(qiniuService.dataPersistence(image.getId(),image.getPic()));
//        }
        if (image.getPic() != null){
            if(image.getPic().contains("group")){
                Integer count = fdfsImageService.dataPersistence(image.getPic());
                if (count<=0){
                    return 0;
                }
            }
        }
        return imageMapper.insertSelective(image);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer update(Image image) throws QiniuException {
//        if(image.getPic() != null && image.getPic().startsWith("temp/")){
//            image.setPic(qiniuService.dataPersistence(image.getId(),image.getPic()));
//        }
        if(image.getPic() != null){
            Integer count = fdfsImageService.dataPersistence(image.getPic());
            if (count<=0){
                return 0;
            }
        }
        return imageMapper.updateByPrimaryKeySelective(image);
    }

    @Override
    public Image getById(Integer id) {
        return imageMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Image> findAll() {
        return imageMapper.findAll();
    }

    @Override
    public Integer delete(Integer id) {
        return null;
    }
}
