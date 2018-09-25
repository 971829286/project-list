package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.BlackList;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/19 0019 12:02
 * @Version1.0
 */
@Repository
public interface BlackListMapper extends IBaseMapper<BlackList> {
    @InsertProvider(type = BlackListSqlProvider.class,method ="save")
    public int insertSelective(BlackList blackList);

    @UpdateProvider(type = BlackListSqlProvider.class,method = "update")
    public int updateByPrimaryKeySelective(BlackList blackList);

    @SelectProvider(type = BlackListSqlProvider.class,method = "findAll")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "status",property = "status"),
            @Result( column = "start_date",property = "startDate"),
            @Result( column = "end_date",property = "endDate"),
            @Result( column = "reason",property = "reason"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "c_id",property = "cId"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "u_id",property = "uId")
    })
    public List<BlackList> findAll();

    @SelectProvider(type = BlackListSqlProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "status",property = "status"),
            @Result( column = "start_date",property = "startDate"),
            @Result( column = "end_date",property = "endDate"),
            @Result( column = "reason",property = "reason"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "c_id",property = "cId"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "u_id",property = "uId")
    })
    public BlackList selectByPrimaryKey(Integer id);

    @DeleteProvider(type = BlackListSqlProvider.class,method = "deleteById")
    public int deleteByPrimaryKey(Integer id);

    @SelectProvider(type = BlackListSqlProvider.class,method = "selectByParam")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "status",property = "status"),
            @Result( column = "start_date",property = "startDate"),
            @Result( column = "end_date",property = "endDate"),
            @Result( column = "reason",property = "reason"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "c_id",property = "cId"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "u_id",property = "uId"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname")
    })
    public List<BlackList> getByParam(Map param);

    @Select({"select sum(case when status = 0 then 0",
            "when status = 2 then 1",
            " when (status = 1 and now()> start_date and now()< end_date) then 1",
            " else 0 end",
            ") as count",
            "from black_list where user_id = #{userId}"})
    Integer getOperantByUserId(@Param("userId") Integer userId);

    @Update("<script>" +
            "update black_list set `status` = 0 "+
            "<where> and id in " +
            "<foreach collection=\"ids\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\"> "+
            " #{item}"+
            "</foreach> "+
            "</where> "+
            "</script>" )
    Integer unlock(@Param("ids") List ids);

    @Update("update black_list set `status` = 0 where `status` = 1 and end_date<now()")
    Integer refreshBlack();
}
