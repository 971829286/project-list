package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.BulletScreen;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:03
 * @Description
 */
@Repository
public interface BulletScreenMapper extends IBaseMapper<BulletScreen>{
    @InsertProvider(type = BulletScreenMapperProvider.class,method ="save")
    public Integer save(BulletScreen bulletScreen);

    @UpdateProvider(type = BulletScreenMapperProvider.class,method = "update")
    public Integer update(BulletScreen bulletScreen);

    @SelectProvider(type = BulletScreenMapperProvider.class,method = "findAll")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "room_id",property = "roomId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "content",property = "content"),
            @Result( column = "img",property = "img"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "check_status",property = "checkStatus")
    })
    public List<BulletScreen> findAll();

    @SelectProvider(type = BulletScreenMapperProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "room_id",property = "roomId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "content",property = "content"),
            @Result( column = "img",property = "img"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "check_status",property = "checkStatus")
    })
    public BulletScreen getById(Integer id);

    @DeleteProvider(type = BulletScreenMapperProvider.class,method = "deleteById")
    public Integer delete(Integer id);

    @SelectProvider(type = BulletScreenMapperProvider.class,method = "selectByRoomId")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "room_id",property = "roomId"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "content",property = "content"),
            @Result( column = "img",property = "img"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "check_status",property = "checkStatus")
    })
    List<BulletScreen> getByRoomId(@Param("roomId") Integer roomId,@Param("time") Date time);
}
