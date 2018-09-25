package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.SignIn;
import cn.ourwill.tuwenzb.entity.SignWall;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:26
 * @Description
 */
@Repository
public interface SignInMapper extends IBaseMapper<SignIn>{
    @InsertProvider(type = SignInMapperProvider.class,method ="save")
    public Integer save(SignIn signIn);

    @UpdateProvider(type = SignInMapperProvider.class,method = "update")
    public Integer update(SignIn signIn);

    @SelectProvider(type = SignInMapperProvider.class,method = "findAll")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "activity_id",property = "activityId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "c_time",property = "cTime")
    })
    public List<SignIn> findAll();

    @SelectProvider(type = SignInMapperProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "activity_id",property = "activityId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "c_time",property = "cTime")
    })
    public SignIn getById(Integer id);

    @DeleteProvider(type = SignInMapperProvider.class,method = "deleteById")
    public Integer delete(Integer id);

    @Select("select id,activity_id,user_id,c_time from sign_in where activity_id = #{activityId} and user_id = #{userId}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "activity_id",property = "activityId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "c_time",property = "cTime")
    })
    List<SignIn> getByUserId(@Param("userId") Integer userId, @Param("activityId") Integer activityId);

//    @Select("select id,activity_id,user_id,nickname,avatar,c_time from sign_wall where activity_id = #{activityId} and ")
    @SelectProvider(type = SignInMapperProvider.class,method = "selectByActivityId")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "activity_id",property = "activityId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "c_time",property = "cTime")
    })
    List<SignIn> getByActivityId(@Param("activityId") Integer activityId, @Param("time") Date time);
}
