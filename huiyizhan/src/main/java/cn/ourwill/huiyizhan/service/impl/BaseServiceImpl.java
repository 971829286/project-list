package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.mapper.IBaseMapper;
import cn.ourwill.huiyizhan.service.IBaseService;
import com.qiniu.common.QiniuException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**Service基类
 * ClassName:BaseServiceImpl
 * Description：
 * CreatedDate:2017/6/30 15:33
 */

@Service
public class BaseServiceImpl<T> implements IBaseService<T>{

    @Autowired
    private IBaseMapper<T> baseMapper;
    //保存
    @Override
    public Integer save(T entity) throws QiniuException {
        return baseMapper.insertSelective(entity);
    }

    //更新
    @Override
    public Integer update(T entity) throws QiniuException {
        return baseMapper.updateByPrimaryKeySelective(entity);
    }

    //根据id查找对象
    @Override
    public T getById(Integer id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    //查找所有
    @Override
    public List<T> findAll() {
        return baseMapper.findAll();
    }

    //删除
    @Override
    public Integer delete(Integer id) {
        return baseMapper.deleteByPrimaryKey(id);
    }
}
