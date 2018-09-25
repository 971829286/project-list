package cn.ourwill.tuwenzb.service;

import java.util.List;

/**
 * 　ClassName:IBaseService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:30
 */
public interface IBaseService<T> {
    //保存
    public Integer save(T entity);
    //更新
    public Integer update(T entity);
    //根据id查找对象
    public T getById(Integer id);
    //查找所有
    public List<T> findAll();
    //删除
    public Integer delete(Integer id);
}
