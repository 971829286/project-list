package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.UserBlack;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBlackMapper  extends IBaseMapper<UserBlack>{
    @Delete({
            "delete from user_black",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into user_black (id, user_id, ",
            "force_out_time, reason)",
            "values (#{id,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, ",
            "#{forceOutTime,jdbcType=INTEGER}, #{reason,jdbcType=VARCHAR})"
    })
    int insert(UserBlack record);

    @InsertProvider(type = UserBlackSqlProvider.class, method = "insertSelective")
    int insertSelective(UserBlack record);

    @Select({
            "select",
            "id, user_id, force_out_time, reason",
            "from user_black",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "force_out_time", property = "forceOutTime", jdbcType = JdbcType.INTEGER),
            @Result(column = "reason", property = "reason", jdbcType = JdbcType.VARCHAR)
    })
    UserBlack selectByPrimaryKey(Integer id);

    @UpdateProvider(type = UserBlackSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserBlack record);

    @Update({
            "update user_black",
            "set user_id = #{userId,jdbcType=INTEGER},",
            "force_out_time = #{forceOutTime,jdbcType=INTEGER},",
            "reason = #{reason,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(UserBlack record);

    @Update({
            "update user_black",
            "set force_out_time = #{forceOutTime,jdbcType=INTEGER},",
            "reason = #{reason,jdbcType=VARCHAR}",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    Integer updateByUserId(UserBlack userBlack);


    @Delete({
            "<script>",
            "delete from user_black",
            "where id in ",
            "<foreach collection=\"items\" index=\"index\"   item=\"item\" open=\"(\" close=\")\" separator=\",\">   ",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    Integer deleteByUserIds(@Param("items") List<Integer> items);


    @Select({
            "select",
            "id, user_id, force_out_time, reason",
            "from user_black",
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "force_out_time", property = "forceOutTime", jdbcType = JdbcType.INTEGER),
            @Result(column = "reason", property = "reason", jdbcType = JdbcType.VARCHAR)
    })
    List<UserBlack> findAll();

    @Select({
            "select",
            "ub.id, ub.user_id, ub.force_out_time, ub.reason",
            "from user_black ub",
            "LEFT JOIN user u ON ub.user_id = u.id  ",
            "where u.username = #{userName,jdbcType = VARCHAR }"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "force_out_time", property = "forceOutTime", jdbcType = JdbcType.INTEGER),
            @Result(column = "reason", property = "reason", jdbcType = JdbcType.VARCHAR)
    })
    List<UserBlack> findByUserId(@Param("userName") String userName);
}