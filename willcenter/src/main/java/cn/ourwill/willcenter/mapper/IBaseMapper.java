package cn.ourwill.willcenter.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 　ClassName:IBaseMapper
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:35
 */
@Repository("baseMapper")
public interface IBaseMapper<T>{
    //保存
    public Integer save(T entity);
    //更新
    public Integer update(T entity);
    //根据id查找对象
    public List<T> findAll();
    //查找所有
    public T getById(Integer id);
    //删除
    public Integer delete(Map map);
}
