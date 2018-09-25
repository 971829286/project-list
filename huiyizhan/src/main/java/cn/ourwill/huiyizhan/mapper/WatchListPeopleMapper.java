package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.PeopleDynamic;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.entity.UserBasicInfo;
import cn.ourwill.huiyizhan.entity.WatchListPeople;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListPeopleMapper extends IBaseMapper<WatchListPeople> {
    @InsertProvider(type = WatchListPeopleSqlProvider.class, method = "save")
    public Integer save(WatchListPeople watchListPeople);

    @UpdateProvider(type = WatchListPeopleSqlProvider.class, method = "update")
    public Integer update(WatchListPeople watchListPeople);

    @SelectProvider(type = WatchListPeopleSqlProvider.class, method = "findAll")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "watch_user_id", property = "watchUserId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "watch_user_id", property = "user", one = @One(
                    select = "cn.ourwill.huiyizhan.mapper.UserMapper.getById",
                    fetchType = FetchType.EAGER
            ))
    })
    public List<WatchListPeople> findAll();

    @SelectProvider(type = WatchListPeopleSqlProvider.class, method = "selectById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "watch_user_id", property = "watchUserId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "watch_user_id", property = "user", one = @One(
                    select = "cn.ourwill.huiyizhan.mapper.UserMapper.getById",
                    fetchType = FetchType.EAGER
            ))
    })
    public WatchListPeople getById(Integer id);

    @DeleteProvider(type = WatchListPeopleSqlProvider.class, method = "deleteById")
    public Integer delete(WatchListPeople watchListPeople);

    //根据用户id查找
    @SelectProvider(type = WatchListPeopleSqlProvider.class, method = "getWatchAll")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "level", property = "level"),
            @Result(column = "nickname", property = "nickname"),
            @Result(column="personalized_signature", property="personalizedSignature"),
            @Result(column = "id", property = "beWatchedUsers",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.WatchListPeopleMapper.getWatchPeopleInfo",
                            fetchType = FetchType.EAGER)
            ),
            @Result(column = "id", property = "fansUsers",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.WatchListPeopleMapper.getFansInfo",
                            fetchType = FetchType.EAGER)
            )
    })
    User getWatchAll(@Param("id") Integer id);

    /**
     * 获取当前用户关注人的数目
     */
    @Select("select count(0) from watch_list_people where user_id = #{userId}")
    Integer selectCountByUserId(@Param("userId") Integer userId);

    /**
     * 获取当前用户粉丝的数目
     */
    @Select("select count(0) from watch_list_people where watch_user_id = #{watchUserId}")
    Integer selectCountByWatchUserId(@Param("watchUserId") Integer watchUserId);

    //按用户id和活动id  校验
    @Select("select id as id,watch_user_id as watchUserId,user_id as userId from watch_list_people where watch_user_id = #{watchUserId} and user_id = #{userId}")
    WatchListPeople selectByWatchUserAndUser(@Param("watchUserId") Integer watchUserId, @Param("userId") Integer userId);


    @SelectProvider(type = WatchListPeopleSqlProvider.class, method = "getWatchPeopleInfo")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "level", property = "level"),
            @Result(column = "uuid", property = "UUID"),
            @Result(column = "username", property = "username"),
            @Result(column = "nickname", property = "nickname"),
            @Result(column = "avatar", property = "avatar"),
            @Result(column = "info", property = "info"),
            @Result(column="personalized_signature", property="personalizedSignature"),
            @Result(column = "id",property = "activities",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.ActivityMapper.findByUserIdThree",
                            fetchType = FetchType.EAGER)

            )

    })
    List<UserBasicInfo> getWatchPeopleInfo(@Param("id") Integer id);


    @SelectProvider(type = WatchListPeopleSqlProvider.class, method = "getFansInfo")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "level", property = "level"),
            @Result(column = "uuid", property = "UUID"),
            @Result(column = "username", property = "username"),
            @Result(column = "nickname", property = "nickname"),
            @Result(column = "avatar", property = "avatar"),
            @Result(column = "info", property = "info"),
            @Result(column="personalized_signature", property="personalizedSignature"),
            @Result(column = "id",property = "activities",
                    one = @One(select = "cn.ourwill.huiyizhan.mapper.ActivityMapper.findByUserIdThree",
                            fetchType = FetchType.EAGER)

            )

    })
    List<UserBasicInfo> getFansInfo(@Param("id") Integer id);

    @SelectProvider(type = WatchListPeopleSqlProvider.class, method = "getPeopleDynamicBase")
    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "watch_user_id", property = "watchUserId"),
            @Result(column = "watch_date", property = "watchDate"),
        //--------------------------------------------用户信息----------------------------------------------
            @Result(column = "id", property = "user.id"),
            @Result(column = "level", property = "user.level"),
            @Result(column = "nickname", property = "user.nickname"),
            @Result(column = "username", property = "user.username"),
            @Result(column = "avatar", property = "user.avatar"),
            @Result(column = "mob_phone", property = "user.mobPhone"),
            @Result(column = "tel_phone", property = "user.telPhone"),
            @Result(column = "email", property = "user.email"),
            @Result(column = "qq", property = "user.qq"),
            @Result(column = "company", property = "user.company"),
            @Result(column = "address", property = "user.address"),
            @Result(column = "version", property = "user.version"),
            @Result(column = "info", property = "user.info"),
            @Result(column="personalized_signature", property="personalizedSignature")
    })
    List<PeopleDynamic> getPeopleDynamic(Integer userId);

    @Select("select distinct watch_user_id from watch_list_people group by watch_user_id")
    List<Integer> getWatchUserIdFromWatchList();
}