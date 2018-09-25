package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.SignWall;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import javax.ws.rs.PathParam;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:26
 * @Description
 */
@Repository
public interface SignWallMapper extends IBaseMapper<SignWall>{
    @InsertProvider(type = SignWallMapperProvider.class,method ="save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer save(SignWall signWall);

    @UpdateProvider(type = SignWallMapperProvider.class,method = "update")
    public Integer update(SignWall signWall);

    @SelectProvider(type = SignWallMapperProvider.class,method = "findAll")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "room_id",property = "roomId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "c_time",property = "cTime")
    })
    public List<SignWall> findAll();

    @SelectProvider(type = SignWallMapperProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "room_id",property = "roomId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "c_time",property = "cTime")
    })
    public SignWall getById(Integer id);

    @DeleteProvider(type = SignWallMapperProvider.class,method = "deleteById")
    public Integer delete(Integer id);

    @Select("select id,room_id,user_id,nickname,avatar,c_time from sign_wall where room_id = #{roomId} and user_id = #{userId}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "room_id",property = "roomId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "c_time",property = "cTime")
    })
    List<SignWall> getByUserId(@Param("userId") Integer userId,@Param("roomId") Integer roomId);

//    @Select("select id,room_id,user_id,nickname,avatar,c_time from sign_wall where room_id = #{roomId} and ")
    @SelectProvider(type = SignWallMapperProvider.class,method = "selectByRoomId")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "room_id",property = "roomId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "c_time",property = "cTime")
    })
    List<SignWall> getByRoomId(@Param("roomId") Integer roomId,@Param("time") Date time);

    @Select("SELECT count(id)+1 FROM sign_wall where room_id = #{roomId} and c_time < #{cTime}")
    Integer getRanking(@Param("roomId") Integer roomId, @Param("cTime") Date cTime);
}
