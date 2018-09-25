package cn.ourwill.tuwenzb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import cn.ourwill.tuwenzb.entity.User;

@Repository
public interface UserMapper extends IBaseMapper<User>{

	//授权管理  wanghao
	@UpdateProvider(type=UserMapperProvider.class,method = "updateAuthorization")
	public Integer updateAuthorization(User user);

	@InsertProvider(type = UserMapperProvider.class,method ="save")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public Integer save(User user);
	
	@UpdateProvider(type = UserMapperProvider.class,method = "updateUserInfo")
	public Integer update(User user);

	@Update("update user set password = #{password},salt = #{salt} where id = #{userId}")
	Integer changePWD(@Param("userId") Integer userId,@Param("password") String password,@Param("salt") String salt);


	@SelectProvider(type = UserMapperProvider.class,method = "findAll")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "userFrom_type",property = "userfromType"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "wechat_num",property = "wechatNum"),
			@Result( column = "union_id",property = "unionId"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "due_date",property = "dueDate"),
			@Result( column = "remaining_days",property = "remainingDays"),
			@Result( column = "pack_years_days",property = "packYearsDays"),
			@Result( column = "photo_license_type",property = "photoLicenseType"),
			@Result( column = "photo_due_date",property = "photoDueDate"),
			@Result( column = "photo_remaining_days",property = "photoRemainingDays"),
			@Result( column = "photo_pack_years_days",property = "photoPackYearsDays"),
			@Result( column = "bound_id",property = "boundId"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "refresh_token",property = "refreshToken"),
			@Result( column = "refresh_token_c_time",property = "refreshTokenCTime"),
			@Result( column = "level",property = "level")
	})
	public List<User> findAll();

	@SelectProvider(type = UserMapperProvider.class,method = "selectByParmer")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "userFrom_type",property = "userfromType"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "wechat_num",property = "wechatNum"),
			@Result( column = "union_id",property = "unionId"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "due_date",property = "dueDate"),
			@Result( column = "remaining_days",property = "remainingDays"),
			@Result( column = "pack_years_days",property = "packYearsDays"),
			@Result( column = "photo_license_type",property = "photoLicenseType"),
			@Result( column = "photo_due_date",property = "photoDueDate"),
			@Result( column = "photo_remaining_days",property = "photoRemainingDays"),
			@Result( column = "photo_pack_years_days",property = "photoPackYearsDays"),
			@Result( column = "bound_id",property = "boundId"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "refresh_token",property = "refreshToken"),
			@Result( column = "refresh_token_c_time",property = "refreshTokenCTime"),
			@Result( column = "level",property = "level"),
			@Result( column = "channel",property = "channel")
	})
	public List<User> selectByParams(Map param);

	@SelectProvider(type = UserMapperProvider.class,method = "selectById")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "userFrom_type",property = "userfromType"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "wechat_num",property = "wechatNum"),
			@Result( column = "union_id",property = "unionId"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "due_date",property = "dueDate"),
			@Result( column = "remaining_days",property = "remainingDays"),
			@Result( column = "pack_years_days",property = "packYearsDays"),
			@Result( column = "photo_license_type",property = "photoLicenseType"),
			@Result( column = "photo_due_date",property = "photoDueDate"),
			@Result( column = "photo_remaining_days",property = "photoRemainingDays"),
			@Result( column = "photo_pack_years_days",property = "photoPackYearsDays"),
			@Result( column = "bound_id",property = "boundId"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "refresh_token",property = "refreshToken"),
			@Result( column = "refresh_token_c_time",property = "refreshTokenCTime"),
			@Result( column = "level",property = "level")
	})
	public User getById(Integer id);

	@DeleteProvider(type = UserMapperProvider.class,method = "deleteById")
	public Integer delete(Map param);

	//根据微信号（openid）查找微信用户的refresh_token
	@SelectProvider(type = UserMapperProvider.class,method = "selectByWechatNum")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "userFrom_type",property = "userfromType"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "wechat_num",property = "wechatNum"),
			@Result( column = "union_id",property = "unionId"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "due_date",property = "dueDate"),
			@Result( column = "remaining_days",property = "remainingDays"),
			@Result( column = "pack_years_days",property = "packYearsDays"),
			@Result( column = "photo_license_type",property = "photoLicenseType"),
			@Result( column = "photo_due_date",property = "photoDueDate"),
			@Result( column = "photo_remaining_days",property = "photoRemainingDays"),
			@Result( column = "photo_pack_years_days",property = "photoPackYearsDays"),
			@Result( column = "bound_id",property = "boundId"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "refresh_token",property = "refreshToken"),
			@Result( column = "refresh_token_c_time",property = "refreshTokenCTime"),
			@Result( column = "level",property = "level")
	})
	public List<User> selectByWechatNum(String wechatNum);

	//刷新微信用户的refresh_token
	@UpdateProvider(type = UserMapperProvider.class,method = "updateRefreshToken")
	public Integer updateRefreshToken(User user);

	//更改用户类型
	// （0:个人 1:企业）
	@UpdateProvider(type = UserMapperProvider.class,method = "updateUserType")
    public Integer updateUserType(User user);

	//根据用户名查找
	@SelectProvider(type = UserMapperProvider.class,method = "selectByUsername")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "userFrom_type",property = "userfromType"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "wechat_num",property = "wechatNum"),
			@Result( column = "union_id",property = "unionId"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "due_date",property = "dueDate"),
			@Result( column = "remaining_days",property = "remainingDays"),
			@Result( column = "pack_years_days",property = "packYearsDays"),
			@Result( column = "photo_license_type",property = "photoLicenseType"),
			@Result( column = "photo_due_date",property = "photoDueDate"),
			@Result( column = "photo_remaining_days",property = "photoRemainingDays"),
			@Result( column = "photo_pack_years_days",property = "photoPackYearsDays"),
			@Result( column = "bound_id",property = "boundId"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "refresh_token",property = "refreshToken"),
			@Result( column = "refresh_token_c_time",property = "refreshTokenCTime"),
			@Result( column = "level",property = "level")
	})
    public User selectByUsername(String username);

	//根据用户名查找
	@SelectProvider(type = UserMapperProvider.class,method = "selectByUsernameOrPhone")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "userFrom_type",property = "userfromType"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "wechat_num",property = "wechatNum"),
			@Result( column = "union_id",property = "unionId"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "due_date",property = "dueDate"),
			@Result( column = "remaining_days",property = "remainingDays"),
			@Result( column = "pack_years_days",property = "packYearsDays"),
			@Result( column = "photo_license_type",property = "photoLicenseType"),
			@Result( column = "photo_due_date",property = "photoDueDate"),
			@Result( column = "photo_remaining_days",property = "photoRemainingDays"),
			@Result( column = "photo_pack_years_days",property = "photoPackYearsDays"),
			@Result( column = "bound_id",property = "boundId"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "refresh_token",property = "refreshToken"),
			@Result( column = "refresh_token_c_time",property = "refreshTokenCTime"),
			@Result( column = "level",property = "level")
	})
	public User selectByUsernameOrPhone(String username);

	//消费天数
	@Update("update user u set u.remaining_days=u.remaining_days-#{days} where u.id=#{userId}")
	public Integer consumeRemain(@Param("days") Integer days,@Param("userId") Integer userId);

	//包年消费天数
	@Update("update user u set u.pack_years_days=u.pack_years_days-#{days} where u.id=#{userId}")
	public Integer consumeRemainYear(@Param("days") Integer days,@Param("userId") Integer userId);

	//消费天数(照片直播)
	@Update("update user u set u.photo_remaining_days=u.photo_remaining_days-#{days} where u.id=#{userId}")
	public Integer photoConsumeRemain(@Param("days") Integer days,@Param("userId") Integer userId);

	//包年消费天数(照片直播)
	@Update("update user u set u.photo_pack_years_days=u.photo_pack_years_days-#{days} where u.id=#{userId}")
	public Integer photoConsumeRemainYear(@Param("days") Integer days,@Param("userId") Integer userId);

	@Update("UPDATE user SET bound_id = NULL,wechat_num = CASE WHEN userFrom_type = 1 THEN NULL ELSE wechat_num END WHERE id = #{id}")
    void updateBoundId(@Param("id") Integer id);

	//根据用户名查找
	@SelectProvider(type = UserMapperProvider.class,method = "selectByMobPhone")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "userFrom_type",property = "userfromType"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "wechat_num",property = "wechatNum"),
			@Result( column = "union_id",property = "unionId"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "due_date",property = "dueDate"),
			@Result( column = "remaining_days",property = "remainingDays"),
			@Result( column = "pack_years_days",property = "packYearsDays"),
			@Result( column = "photo_license_type",property = "photoLicenseType"),
			@Result( column = "photo_due_date",property = "photoDueDate"),
			@Result( column = "photo_remaining_days",property = "photoRemainingDays"),
			@Result( column = "photo_pack_years_days",property = "photoPackYearsDays"),
			@Result( column = "bound_id",property = "boundId"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "refresh_token",property = "refreshToken"),
			@Result( column = "refresh_token_c_time",property = "refreshTokenCTime"),
			@Result( column = "level",property = "level")
	})
    User selectByMobPhone(String mobPhone);

	@Update("update user set mob_phone = #{newPhone} where id = #{id}")
	Integer updatePhone(@Param("newPhone") String newPhone,@Param("id") Integer id);

	@Select("select u.id,u.username,u.nickname,u.avatar from activity a left join activity_impower ai on a.id = ai.activity_id left join user u on ai.user_id = u.id where a.id = #{activityId} and ai.type = 2")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "userFrom_type",property = "userfromType"),
			@Result( column = "user_type",property = "userType"),
			@Result( column = "username",property = "username"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "password",property = "password"),
			@Result( column = "salt",property = "salt"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "mob_phone",property = "mobPhone"),
			@Result( column = "tel_phone",property = "telPhone"),
			@Result( column = "wechat_num",property = "wechatNum"),
			@Result( column = "union_id",property = "unionId"),
			@Result( column = "email",property = "email"),
			@Result( column = "qq",property = "qq"),
			@Result( column = "company",property = "company"),
			@Result( column = "address",property = "address"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "due_date",property = "dueDate"),
			@Result( column = "remaining_days",property = "remainingDays"),
			@Result( column = "pack_years_days",property = "packYearsDays"),
			@Result( column = "photo_license_type",property = "photoLicenseType"),
			@Result( column = "photo_due_date",property = "photoDueDate"),
			@Result( column = "photo_remaining_days",property = "photoRemainingDays"),
			@Result( column = "photo_pack_years_days",property = "photoPackYearsDays"),
			@Result( column = "bound_id",property = "boundId"),
			@Result( column = "c_id",property = "cId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_id",property = "uId"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "refresh_token",property = "refreshToken"),
			@Result( column = "refresh_token_c_time",property = "refreshTokenCTime"),
			@Result( column = "level",property = "level")
	})
    List<User> getAdminUser(Integer activityId);

	@Update("update user set union_id = #{unionId} where union_id is null and (id = #{id} or bound_id = #{id})")
    int updateUnionId(@Param("id") Integer id, @Param("unionId") String unionId);
}
