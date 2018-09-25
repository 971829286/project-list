package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.mapper.IBaseMapper;
import cn.ourwill.tuwenzb.service.IBaseService;
import org.apache.ibatis.ognl.IntHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**Service基类
 * 　ClassName:BaseServiceImpl
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:33
 */

@Service
public class BaseServiceImpl<T> implements IBaseService<T>{

    @Autowired
    private IBaseMapper<T> baseMapper;
    //保存
    @Override
    public Integer save(T entity) {
        return baseMapper.save(entity);
    }

    //更新
    @Override
    public Integer update(T entity) {
        return baseMapper.update(entity);
    }

    //根据id查找对象
    @Override
    public T getById(Integer id) {
        return baseMapper.getById(id);
    }

    //查找所有
    @Override
    public List<T> findAll() {
        return baseMapper.findAll();
    }

    //删除
    @Override
    public Integer delete(Integer id) {
        Map<String,Object> map=new HashMap();
        map.put("id",id);
        return baseMapper.delete(map);
    }
}
