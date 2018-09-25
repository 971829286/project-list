package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.entity.UserBase;
import cn.ourwill.huiyizhan.entity.UserBasicInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper extends IBaseMapper<User>{
    String colums="id,uuid,level,nickname,username,avatar,mob_phone,tel_phone,email,qq,company,address,version,info,unionid";
    @Delete({
            "delete from user",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into user (id, uuid, level, ",
            "nickname)",
            "values (#{id,jdbcType=INTEGER}, #{UUID,jdbcType=CHAR}, #{level,jdbcType=INTEGER}, ",
            "#{nickname,jdbcType=VARCHAR})"
    })
    int insert(User record);

    @InsertProvider(type=UserSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertSelective(User record);

    @Select({
            "select",
             colums,
            "from user",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "uuid",property = "UUID"),
            @Result( column = "level",property = "level"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "username",property = "username"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "mob_phone",property = "mobPhone"),
            @Result( column = "tel_phone",property = "telPhone"),
            @Result( column = "email",property = "email"),
            @Result( column = "qq",property = "qq"),
            @Result( column = "company",property = "company"),
            @Result( column = "address",property = "address"),
            @Result( column = "version",property = "version"),
            @Result(column="info", property="info"),
            @Result(column="unionid", property="unionid")

    })
    User selectByPrimaryKey(Integer id);

    @Select({
            "select",
            "id,uuid,level,nickname,username,avatar,info",
            "from user",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "uuid",property = "UUID"),
            @Result( column = "level",property = "level"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "username",property = "username"),
            @Result( column = "avatar",property = "avatar"),
            @Result(column="info", property="info")
    })
    UserBasicInfo getBasicInfoById(Integer id);

    @Select({
            "select",
             colums,
            "from user",
            "where uuid = #{uuid,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "uuid",property = "UUID"),
            @Result( column = "level",property = "level"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "username",property = "username"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "mob_phone",property = "mobPhone"),
            @Result( column = "tel_phone",property = "telPhone"),
            @Result( column = "email",property = "email"),
            @Result( column = "qq",property = "qq"),
            @Result( column = "company",property = "company"),
            @Result( column = "address",property = "address"),
            @Result( column = "version",property = "version"),
            @Result(column="info", property="info"),
            @Result(column="unionid", property="unionid")
    })
    User selectByUuid(String uuid);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(User record);

    @Update({
            "update user",
            "set uuid = #{UUID,jdbcType=CHAR},",
            "level = #{level,jdbcType=INTEGER},",
            "nickname = #{nickname,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(User record);

    @Select("select version from user where uuid = #{uuid}")
    Integer getUserVersion(String uuid);


    @SelectProvider(type = UserSqlProvider.class,method = "findAll")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "uuid",property = "UUID"),
            @Result( column = "level",property = "level"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "username",property = "username"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "mob_phone",property = "mobPhone"),
            @Result( column = "tel_phone",property = "telPhone"),
            @Result( column = "email",property = "email"),
            @Result( column = "qq",property = "qq"),
            @Result( column = "company",property = "company"),
            @Result( column = "address",property = "address"),
            @Result( column = "version",property = "version"),
            @Result(column="info", property="info"),
            @Result(column="unionid", property="unionid")
    })
    public List<User> findAll();

    @Select("select "+colums+" from user where username = #{username}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "uuid",property = "UUID"),
            @Result( column = "level",property = "level"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "username",property = "username"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "mob_phone",property = "mobPhone"),
            @Result( column = "tel_phone",property = "telPhone"),
            @Result( column = "email",property = "email"),
            @Result( column = "qq",property = "qq"),
            @Result( column = "company",property = "company"),
            @Result( column = "address",property = "address"),
            @Result( column = "version",property = "version"),
            @Result(column="info", property="info"),
            @Result(column="unionid", property="unionid")
    })
    public User selectUserByUsername(String username);

    @Select("select "+colums+" from user where mob_phone = #{mobPhone}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "uuid",property = "UUID"),
            @Result( column = "level",property = "level"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "username",property = "username"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "mob_phone",property = "mobPhone"),
            @Result( column = "tel_phone",property = "telPhone"),
            @Result( column = "email",property = "email"),
            @Result( column = "qq",property = "qq"),
            @Result( column = "company",property = "company"),
            @Result( column = "address",property = "address"),
            @Result( column = "version",property = "version"),
            @Result(column="info", property="info"),
            @Result(column="unionid", property="unionid")
    })
    public User selectUserByMobPhone(String mobPhone);

    @SelectProvider(type = UserSqlProvider.class,method = "selectByParams")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "uuid",property = "UUID"),
            @Result( column = "level",property = "level"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "username",property = "username"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "mob_phone",property = "mobPhone"),
            @Result( column = "tel_phone",property = "telPhone"),
            @Result( column = "email",property = "email"),
            @Result( column = "qq",property = "qq"),
            @Result( column = "company",property = "company"),
            @Result( column = "address",property = "address"),
            @Result( column = "version",property = "version"),
            @Result(column="info", property="info")
    })
    List<User> selectByParams(Map<String, String> param);

//    @UpdateProvider(type = UserSqlProvider.class,method = "updateUserBase")
    /*@Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "uuid",property = "UUID"),
            @Result( column = "level",property = "level"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "username",property = "username"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "mob_phone",property = "mobPhone"),
            @Result( column = "tel_phone",property = "telPhone"),
            @Result( column = "email",property = "email"),
            @Result( column = "qq",property = "qq"),
            @Result( column = "company",property = "company"),
            @Result( column = "address",property = "address"),
            @Result( column = "version",property = "version")
    })*/

    @Update("update user set nickname = #{nickname}," +
            "username=#{username},avatar = #{avatar},mob_phone=#{mobPhone},tel_phone=#{telPhone}," +
            "email=#{email},qq=#{qq},company=#{company},address=#{address},version=#{version},info=#{info},unionid= #{unionid} where uuid=#{UUID}")
    int updateUserByUUID(UserBase user);

    @Select("select id from user")
    List<Integer> findAllId();

    @Select({
            "select",
            "id,uuid,level,nickname,username,avatar,info,unionid "+
            "from user",
            "where unionid = #{unionid}"
    })
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "uuid",property = "UUID"),
            @Result( column = "level",property = "level"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "username",property = "username"),
            @Result( column = "avatar",property = "avatar"),
            @Result(column="info", property="info"),
            @Result(column="unionid", property="unionid")
    })
    UserBase getUserByUnionid(@Param("unionid") String unionid);

    @Update("update user set unionid = #{unionid} where uuid = #{uuid}")
    Integer changeUnionIdByUuid(@Param("unionid") String unionid,@Param("uuid") String uuid);

}