package cn.ourwill.huiyizhan.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * ClassName:IBaseMapper
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:35
 */
@Repository("baseMapper")
public interface IBaseMapper<T>{
    //保存
    int insertSelective(T entity);
    //更新
    int updateByPrimaryKeySelective(T entity);
    //根据id查找对象
    T selectByPrimaryKey(Integer id);
    //查找所有
    List<T> findAll();
    //删除
    int deleteByPrimaryKey(Integer id);
}
