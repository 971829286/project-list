package cn.ourwill.huiyizhan.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/5/16 15:10
 * @Description
 */
@Repository
public interface SysSequenceMapper {
    @Select("select nextval(#{sqName}) from dual")
    int nextVal(@Param("sqName") String sqName);
}
