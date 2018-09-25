package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.UserStatistics;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStatisticsMapper {

    public String tableName = "user_statistics";
    public String column = "user_id,popularity";

    @Delete({
            "delete from user_statistics",
            "where userId = #{user_id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer userId);

    @Insert({
            "insert into user_statistics (user_id, ",
            "popularity) ",
            "values ( #{userId,jdbcType=INTEGER}, ",
            "#{popularity,jdbcType=INTEGER})"
    })
    int insert(UserStatistics record);

    @InsertProvider(type = UserStatisticsSqlProvider.class, method = "insertSelective")
    int insertSelective(UserStatistics record);

    @Select({
            "select",
            "user_id, popularity",
            "from user_statistics",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "popularity", property = "popularity", jdbcType = JdbcType.INTEGER),
    })
    UserStatistics selectByPrimaryKey(Integer userId);

    @UpdateProvider(type = UserStatisticsSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserStatistics record);

    @Update({
            "update user_statistics",
            "set popularity = #{popularity,jdbcType=INTEGER},",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(UserStatistics record);


    @Insert("<script> " +
            "insert into " + tableName +
            "(" + column + ") " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> " +
            "(#{item.userId},#{item.popularity})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE " +
            "user_id = VALUES(user_id)," +
            "popularity = VALUES(popularity) " +
            "</script>")
    Integer batchUpdate(@Param("items") List<UserStatistics> items);


    @Select({
            "select",
            "user_id, popularity",
            "from user_statistics",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "popularity", property = "popularity", jdbcType = JdbcType.INTEGER),
            //TODO 补充数据中心  ----> 活动票数
    })
    UserStatistics getByUserId(@Param("userId") int userId);


    @Select({
            "select",
            "user_id, popularity",
            "from user_statistics"
    })
    @Results({
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
            @Result(column = "popularity", property = "popularity", jdbcType = JdbcType.INTEGER)
    })
    List<UserStatistics> findAll();
}