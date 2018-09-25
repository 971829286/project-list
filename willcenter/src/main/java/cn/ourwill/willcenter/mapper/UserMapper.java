package cn.ourwill.willcenter.mapper;

import cn.ourwill.willcenter.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper extends IBaseMapper<User>{

	@InsertProvider(type = UserMapperProvider.class,method ="save")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public Integer save(User user);
	
	@UpdateProvider(type = UserMapperProvider.class,method = "updateUserInfo")
	public Integer update(User user);

	@UpdateProvider(type = UserMapperProvider.class,method = "updateUserInfoByUuid")
	public Integer updateByUuid(User user);

	@Update("update user set password = #{password},salt = #{salt} where id = #{userId}")
	Integer changePWD(@Param("userId") Integer userId,@Param("password") String password,@Param("salt") String salt);

	@Update("update user set password = #{password},salt = #{salt} where uuid = #{uuid}")
	Integer changePWDByUuid(@Param("uuid") String uuid,@Param("password") String password,@Param("salt") String salt);

	@SelectProvider(type = UserMapperProvider.class,method = "findAll")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "uuid",property = "UUID"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "level",property = "level"),
			@Result( column = "version",property = "version"),
			@Result( column = "info",property = "info")
	})
	public List<User> findAll();

	@SelectProvider(type = UserMapperProvider.class,method = "selectByParmer")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "uuid",property = "UUID"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "due_date",property = "dueDate"),
			@Result( column = "remaining_days",property = "remainingDays"),
			@Result( column = "pack_years_days",property = "packYearsDays"),
			@Result( column = "bound_id",property = "boundId"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "refresh_token",property = "refreshToken"),
			@Result( column = "refresh_token_c_time",property = "refreshTokenCTime"),
			@Result( column = "level",property = "level"),
			@Result( column = "version",property = "version"),
			@Result( column = "info",property = "info")
	})
	public List<User> selectByParams(Map param);

	@SelectProvider(type = UserMapperProvider.class,method = "selectById")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "uuid",property = "UUID"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "level",property = "level"),
			@Result( column = "version",property = "version"),
			@Result( column = "info",property = "info")
	})
	public User getById(Integer id);

	@DeleteProvider(type = UserMapperProvider.class,method = "deleteById")
	public Integer delete(Map param);

	//更改用户类型
	// （0:个人 1:企业）
	@UpdateProvider(type = UserMapperProvider.class,method = "updateUserType")
    public Integer updateUserType(User user);

	//根据用户名查找
	@SelectProvider(type = UserMapperProvider.class,method = "selectByUsername")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "uuid",property = "UUID"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "level",property = "level"),
			@Result( column = "version",property = "version"),
			@Result( column = "info",property = "info")
	})
    public User selectByUsername(String username);

	//根据用户名查找
	@SelectProvider(type = UserMapperProvider.class,method = "selectByUsernameOrPhone")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "uuid",property = "UUID"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "level",property = "level"),
			@Result( column = "version",property = "version"),
			@Result( column = "info",property = "info")
	})
	public User selectByUsernameOrPhone(String username);

	//根据用户名查找
	@SelectProvider(type = UserMapperProvider.class,method = "selectByMobPhone")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "uuid",property = "UUID"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "level",property = "level"),
			@Result( column = "version",property = "version"),
			@Result( column = "info",property = "info")
	})
    User selectByMobPhone(String mobPhone);

	@Update("update user set mob_phone = #{newPhone},version = version + 1 where id = #{id}")
	Integer updatePhone(@Param("newPhone") String newPhone,@Param("id") Integer id);

	@Update("update user set version = version + 1 where uuid = #{uuid}")
	Integer updateUserVersion(@Param("uuid") String uuid);

	@Select("select version from user where uuid = #{uuid}")
	Integer getUserVersion(String uuid);

	@Select("select * from user where uuid = #{uuid}")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "uuid",property = "UUID"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "level",property = "level"),
			@Result( column = "version",property = "version"),
			@Result( column = "info",property = "info")

	})
	User getUserByUUID(@Param("uuid") String uuid);


	//@SelectProvider(type = UserMapperProvider.class,method = "selectByUnionId")
	@Select("select "+
			"id,uuid,user_type,username,nickname,password,salt,avatar,mob_phone,tel_phone,email,qq,company,address," +
			"c_id,c_time,u_id,u_time,level,version,info,unionid " +
			" from " +
			"user " +
			"where unionid = #{unionid}")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "uuid",property = "UUID"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "level",property = "level"),
			@Result( column = "version",property = "version"),
			@Result( column = "info",property = "info"),
			@Result( column = "unionid",property = "unionid")
	})
	User getUserByUnionid(@Param("unionid") String unionid);



    //根据用户名查找
    @SelectProvider(type = UserMapperProvider.class,method = "selectByEmailOrPhone")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "uuid",property = "UUID"),
            @Result( column = "user_type",property = "userType"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "password",property = "password"),
            @Result( column = "salt",property = "salt"),
            @Result( column = "avatar",property = "avatar"),
            @Result( column = "mob_phone",property = "mobPhone"),
            @Result( column = "tel_phone",property = "telPhone"),
            @Result( column = "email",property = "email"),
            @Result( column = "qq",property = "qq"),
            @Result( column = "company",property = "company"),
            @Result( column = "address",property = "address"),
            @Result( column = "c_id",property = "cId"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_id",property = "uId"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "level",property = "level"),
            @Result( column = "version",property = "version"),
            @Result( column = "info",property = "info")
    })
    public User selectByEmailOrPhone(String phoneOrEmail);

	@Update("update user set unionid = #{unionid} where uuid = #{uuid}")
	Integer changeUnionIdByUuid(@Param("unionid") String unionid,@Param("uuid") String uuid);

	@UpdateProvider(type = UserMapperProvider.class,method = "updateEmailOrMobPhone")
    @Options(useGeneratedKeys = true,keyProperty = "id")
	Integer updateEmailOrMobPhone(User user);

	@Update("update user set email = #{email},version = version + 1 where id = #{id}")
	@Options(useGeneratedKeys = true,keyProperty = "id")
	Integer updateEmail(@Param("id")Integer id,@Param("email") String email);

	@Select("select id from user where uuid = ''")
	@Results({
			@Result(id = true,column = "id",property = "id")
	})
    List<User> selectWithOutUuid();

	@Update("<script>" +
			"INSERT INTO user (id,uuid) "+
			"VALUES " +
			"<foreach collection=\"users\" index=\"index\" item=\"item\" separator=\",\"> "+
			"(#{item.id},#{item.UUID})"+
			"</foreach> "+
			"ON DUPLICATE KEY UPDATE "+
			"uuid = VALUES(uuid) "+
			"</script>" )
	int batchUpdateUUID(@Param("users") List<User> users);
}
