package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.BannerHome;
import cn.ourwill.huiyizhan.entity.Image;
import com.qiniu.common.QiniuException;

import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-12 13:37
 **/
public interface IImgService extends IBaseService<Image>{
    //保存
    public Integer save(Image Image) throws QiniuException;

    //更新
    public Integer update(Image Image) throws QiniuException;

    //根据id查找对象
    public Image getById(Integer id);

    //查找所有
    public List<Image> findAll();

}
