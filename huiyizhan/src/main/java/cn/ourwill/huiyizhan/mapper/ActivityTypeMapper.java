package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ActivityType;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ActivityTypeMapper extends IBaseMapper<ActivityType>{
    @Select({
            "select",
            "id,type,type_name",
            "from activity_type"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.INTEGER, id=true),
            @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
            @Result(column="type_name", property="typeName", jdbcType=JdbcType.VARCHAR)
    })
    List<ActivityType> findAll();

    @Select({
            "select",
            "id,type,type_name",
            "from activity_type where id = #{id}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.INTEGER, id=true),
            @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
            @Result(column="type_name", property="typeName", jdbcType=JdbcType.VARCHAR)
    })
    ActivityType getActivityTypeById(Integer id);
}
