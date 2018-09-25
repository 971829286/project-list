package cn.ourwill.huiyizhan.service;

import com.qiniu.common.QiniuException;

import java.util.List;

/**
 * 　ClassName:IBaseService
 * Description：
 * Demo:hasee
 * CreatedDate:2017/6/30 15:30
 */
public interface IBaseService<T> {
    //保存
    public Integer save(T entity) throws QiniuException;

    //更新
    public Integer update(T entity) throws QiniuException;

    //根据id查找对象
    public T getById(Integer id);

    //查找所有
    public List<T> findAll();

    //删除
    public Integer delete(Integer id);
}
