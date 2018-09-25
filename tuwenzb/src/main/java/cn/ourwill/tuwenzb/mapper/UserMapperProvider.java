package cn.ourwill.tuwenzb.mapper;

import org.apache.ibatis.jdbc.SQL;

import cn.ourwill.tuwenzb.entity.User;

import java.util.Map;

public class UserMapperProvider {
	private String colums="id,userFrom_type,user_type,username,nickname,password,salt,avatar,mob_phone,tel_phone,wechat_num,union_id,email,qq,company,address,license_type,due_date,remaining_days,pack_years_days,photo_license_type,photo_due_date,photo_remaining_days,photo_pack_years_days,bound_id, " +
			"c_id,c_time,u_id,u_time,level,channel,refresh_token,refresh_token_c_time";
	private String simpleColums="id,userFrom_type,user_type,username,nickname,password,salt,avatar,mob_phone,tel_phone,wechat_num,union_id,email,qq,company,address,license_type,due_date,remaining_days,pack_years_days,photo_license_type,photo_due_date,photo_remaining_days,photo_pack_years_days,bound_id, " +
			"c_id,c_time,u_id,u_time,level,channel";

	//更新用户授权信息
	public String updateAuthorization(User user){
		return new SQL(){
			{
				UPDATE("user");
				if(user.getLicenseType()!=null){
					SET("license_type=#{licenseType}");
				}
				if(user.getDueDate()!=null){
					SET("due_date=#{dueDate}");
				}
				if(user.getRemainingDays()!=null){
					SET("remaining_days=#{remainingDays}");
				}
				if(user.getPackYearsDays()!=null){
					SET("pack_years_days=#{packYearsDays}");
				}
				if(user.getPhotoLicenseType()!=null){
					SET("photo_license_type=#{photoLicenseType}");
				}
				if(user.getPhotoDueDate()!=null){
					SET("photo_due_date=#{photoDueDate}");
				}
				if(user.getPhotoRemainingDays()!=null){
					SET("photo_remaining_days=#{photoRemainingDays}");
				}
				if(user.getPhotoPackYearsDays()!=null){
					SET("photo_pack_years_days=#{photoPackYearsDays}");
				}
				if(user.getCId()!=null){
					SET("c_id=#{cId}");
				}
				if(user.getCTime()!=null){
					SET("c_time=#{cTime}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}


	//保存
	public String save(final User user){
		return new SQL(){
			{
				INSERT_INTO("user");
				if(user.getUserfromType()!=null){
					VALUES("userFrom_type", "#{userfromType}");
				}
				if(user.getUserType()!=null){
					VALUES("user_type", "#{userType}");
				}
				if(user.getUsername()!=null){
					VALUES("username", "#{username}");
				}
				if(user.getNickname()!=null){
					VALUES("nickname", "#{nickname}");
				}
				if(user.getPassword()!=null){
					VALUES("password", "#{password}");
				}
				if(user.getSalt()!=null){
					VALUES("salt", "#{salt}");
				}
				if(user.getAvatar()!=null){
					VALUES("avatar", "#{avatar}");
				}
				if(user.getMobPhone()!=null){
					VALUES("mob_phone", "#{mobPhone}");
				}
				if(user.getTelPhone()!=null){
					VALUES("tel_phone", "#{telPhone}");
				}
				if(user.getWechatNum()!=null){
					VALUES("wechat_num", "#{wechatNum}");
				}
				if(user.getUnionId()!=null){
					VALUES("union_id", "#{unionId}");
				}
				if(user.getEmail()!=null){
					VALUES("email", "#{email}");
				}
				if(user.getQq()!=null){
					VALUES("qq", "#{qq}");
				}
				if(user.getCompany()!=null){
					VALUES("company", "#{company}");
				}
				if(user.getAddress()!=null){
					VALUES("address", "#{address}");
				}
				if(user.getLicenseType()!=null){
					VALUES("license_type", "#{licenseType}");
				}
				if(user.getDueDate()!=null){
					VALUES("due_date", "#{dueDate}");
				}
				if(user.getRemainingDays()!=null){
					VALUES("remaining_days", "#{remainingDays}");
				}
				if(user.getPackYearsDays()!=null){
					VALUES("pack_years_days","#{packYearsDays}");
				}
				if(user.getPhotoLicenseType()!=null){
					VALUES("photo_license_type", "#{photoLicenseType}");
				}
				if(user.getPhotoDueDate()!=null){
					VALUES("photo_due_date", "#{photoDueDate}");
				}
				if(user.getPhotoRemainingDays()!=null){
					VALUES("photo_remaining_days", "#{photoRemainingDays}");
				}
				if(user.getPhotoPackYearsDays()!=null){
					VALUES("photo_pack_years_days","#{photoPackYearsDays}");
				}
				if(user.getBoundId()!=null){
					VALUES("bound_id", "#{boundId}");
				}
				if(user.getCId()!=null){
					VALUES("c_id", "#{cId}");
				}
				if(user.getCTime()!=null){
					VALUES("c_time", "#{cTime}");
				}
				if(user.getUId()!=null){
					VALUES("u_id", "#{uId}");
				}
				if(user.getUTime()!=null){
					VALUES("u_time", "#{uTime}");
				}
				if(user.getLevel()!=null){
					VALUES("level", "#{level}");
				}
				if(user.getRefreshToken()!=null){
					VALUES("refresh_token", "#{refreshToken}");
				}
				if(user.getRefreshTokenCTime()!=null){
					VALUES("refresh_token_c_time", "#{refreshTokenCTime}");
				}
				if(user.getChannel() != null){
					VALUES("channel", "#{channel}");
				}
			}
		}.toString();
	}

	//刷新微信用户的refresh_token
	public String updateRefreshToken(final User user){
		return new SQL(){
			{
				UPDATE("user");
				if(user.getRefreshToken()!=null){
					SET("refresh_token=#{refreshToken}");
				}
				if(user.getRefreshTokenCTime()!=null){
					SET("refresh_token_c_time=#{refreshTokenCTime}");
				}
				WHERE("wechat_num=#{wechatNum}");
			}
		}.toString();
	}

	//更改用户类型
	// （0:个人 1:企业）
	public String updateUserType(final User user){
		return new SQL(){
			{
				UPDATE("user");
					SET("user_type=#{userType}");
				WHERE("id=#{id}");
			}
		}.toString();
	}

	//根据微信号（openid）查找微信用户的refresh_token
	public String selectByWechatNum(String wechatNum){
		return new SQL(){
			{
				SELECT(colums);
				FROM("user");
				WHERE("wechat_num=#{wechatNum}");
			}
		}.toString();
	}
	//根据用户名查找
	public String selectByUsername(String username){
		return new SQL(){
			{
				SELECT(colums);
				FROM("user");
				WHERE("username=#{username}");
			}
		}+" limit 1".toString();
	}

	//根据手机号查找
	public String selectByMobPhone(String mobPhone){
		return new SQL(){
			{
				SELECT(colums);
				FROM("user");
				WHERE("mob_phone=#{mobPhone}");
			}
		}+" limit 1".toString();
	}

	//根据手机号查找
	public String selectByUsernameOrPhone(String str){
		return new SQL(){
			{
				SELECT(colums);
				FROM("user");
				WHERE("username = #{username} or mob_phone=#{mobPhone}");
			}
		}+" limit 1".toString();
	}

	//修改
	public String updateUserInfo(final User user){
		return new SQL(){
			{
				UPDATE("user");
//				if(user.getUserfromType()!=null){
//					SET("userFrom_type=#{userfromType}");
//				}
				if(user.getUserType()!=null){
					SET("user_type=#{userType}");
				}
				if(user.getUsername()!=null){
					SET("username=#{username}");
				}
				if(user.getNickname()!=null){
					SET("nickname=#{nickname}");
				}
//				if(user.getPassword()!=null){
//					SET("password=#{password}");
//				}
				if(user.getAvatar()!=null){
					SET("avatar=#{avatar}");
				}
//				if(user.getSalt()!=null){
//					SET("salt=#{salt}");
//				}
				if(user.getMobPhone()!=null){
					SET("mob_phone=#{mobPhone}");
				}
				if(user.getTelPhone()!=null){
					SET("tel_phone=#{telPhone}");
				}
				if(user.getWechatNum()!=null){
					SET("wechat_num=#{wechatNum}");
				}
				if(user.getUnionId()!=null){
					SET("union_id=#{unionId}");
				}
				if(user.getEmail()!=null){
					SET("email=#{email}");
				}
				if(user.getQq()!=null){
					SET("qq=#{qq}");
				}
				if(user.getCompany()!=null){
					SET("company=#{company}");
				}
				if(user.getAddress()!=null){
					SET("address=#{address}");
				}
//				if(user.getLicenseType()!=null){
//					SET("license_type=#{licenseType}");
//				}
//				if(user.getDueDate()!=null){
//					SET("due_date=#{dueDate}");
//				}
//				if(user.getRemainingDays()!=null){
//					SET("remaining_days=#{remainingDays}");
//				}
				if(user.getBoundId()!=null){
					SET("bound_id=#{boundId}");
				}
				if(user.getCId()!=null){
					SET("c_id=#{cId}");
				}
				if(user.getCTime()!=null){
					SET("c_time=#{cTime}");
				}
				if(user.getUId()!=null){
					SET("u_id=#{uId}");
				}
				if(user.getUTime()!=null){
					SET("u_time=#{uTime}");
				}
//				if(user.getLevel()!=null){
//					SET("level=#{level}");
//				}
				WHERE("id=#{id}");
			}
		}.toString();
	}
	//查找所有
	public String findAll(){
		return new SQL(){
			{
				SELECT(simpleColums);
				FROM("user");
			}
		}.toString();
	}

	//根据属性查找（使用user参数）
	public String findByProperty(final User user){
		return new SQL(){
			{
				SELECT(simpleColums);
				FROM("user");
				if(user.getId()!=null){
					WHERE("id=#{id}");
				}
				if(user.getUserfromType()!=null){
					WHERE("userFrom_type=#{userfromType}");
				}
				if(user.getUserType()!=null){
					WHERE("user_type=#{userType}");
				}
				if(user.getUsername()!=null){
					WHERE("username=#{username}");
				}
				if(user.getNickname()!=null){
					WHERE("nickname=#{nickname}");
				}
				if(user.getPassword()!=null){
					WHERE("password=#{password}");
				}
				if(user.getAvatar()!=null){
					WHERE("avatar=#{avatar}");
				}
				if(user.getMobPhone()!=null){
					WHERE("mob_phone=#{mobPhone}");
				}
				if(user.getTelPhone()!=null){
					WHERE("tel_phone=#{telPhone}");
				}
				if(user.getWechatNum()!=null){
					WHERE("wechat_num=#{wechatNum}");
				}
				if(user.getUnionId()!=null){
					WHERE("union_id=#{unionId}");
				}
				if(user.getEmail()!=null){
					WHERE("email=#{email}");
				}
				if(user.getQq()!=null){
					WHERE("qq=#{qq}");
				}
				if(user.getCompany()!=null){
					WHERE("company=#{company}");
				}
				if(user.getAddress()!=null){
					WHERE("address=#{address}");
				}
				if(user.getLicenseType()!=null){
					WHERE("license_type=#{licenseType}");
				}
				if(user.getDueDate()!=null){
					WHERE("due_date=#{dueDate}");
				}
				if(user.getRemainingDays()!=null){
					WHERE("remaining_days=#{remainingDays}");
				}
				if(user.getPackYearsDays()!=null){
					WHERE("pack_years_days=#{packYearsDays}");
				}
				if(user.getCId()!=null){
					WHERE("c_id=#{cId}");
				}
				if(user.getCTime()!=null){
					WHERE("c_time=#{cTime}");
				}
				if(user.getUId()!=null){
					WHERE("u_id=#{uId}");
				}
				if(user.getUTime()!=null){
					WHERE("u_time=#{uTime}");
				}
				if(user.getLevel()!=null){
					WHERE("level=#{level}");
				}

			}
		}.toString();
	}

	//根据属性查找(使用Map参数)
	public String selectByParmer(final Map<String,Object> param){
		return new SQL(){
			{
				SELECT(simpleColums);
				FROM("user");
				if(param.get("id")!=null&&param.get("id")!=""){
					WHERE("id=#{id}");
				}
				if(param.get("userfromType")!=null&&param.get("userfromType")!=""){
					WHERE("userFrom_type=#{userfromType}");
				}
				if(param.get("userType")!=null&&param.get("userType")!=""){
					WHERE("user_type=#{userType}");
				}
				if(param.get("username")!=null&&param.get("username")!=""){
					WHERE("username=#{username}");
				}
				if(param.get("nickname")!=null&&param.get("nickname")!=""){
					WHERE("nickname=#{nickname}");
				}
				if(param.get("password")!=null&&param.get("password")!=""){
					WHERE("password=#{password}");
				}
				if(param.get("avatar")!=null&&param.get("avatar")!=""){
					WHERE("avatar=#{avatar}");
				}
				if(param.get("mobPhone")!=null&&param.get("mobPhone")!=""){
					WHERE("mob_phone=#{mobPhone}");
				}
				if(param.get("telPhone")!=null&&param.get("telPhone")!=""){
					WHERE("tel_phone=#{telPhone}");
				}
				if(param.get("wechatNum")!=null&&param.get("wechatNum")!=""){
					WHERE("wechat_num=#{wechatNum}");
				}
				if(param.get("unionId")!=null&&param.get("unionId")!=""){
					WHERE("union_id=#{unionId}");
				}
				if(param.get("email")!=null&&param.get("email")!=""){
					WHERE("email=#{email}");
				}
				if(param.get("qq")!=null&&param.get("qq")!=""){
					WHERE("qq=#{qq}");
				}
				if(param.get("company")!=null&&param.get("company")!=""){
					WHERE("company=#{company}");
				}
				if(param.get("address")!=null&&param.get("address")!=""){
					WHERE("address=#{address}");
				}
				if(param.get("licenseType")!=null&&param.get("licenseType")!=""){
					WHERE("license_type=#{licenseType}");
				}
				if(param.get("dueDate")!=null&&param.get("dueDate")!=""){
					WHERE("due_date=#{dueDate}");
				}
				if(param.get("remainingDays")!=null&&param.get("remainingDays")!=""){
					WHERE("remaining_days=#{remainingDays}");
				}
				if(param.get("packYearsDays")!=null&&param.get("packYearsDays")!=""){
					WHERE("pack_years_days=#{packYearsDays}");
				}
				if(param.get("cId")!=null&&param.get("cId")!=""){
					WHERE("c_id=#{cId}");
				}
				if(param.get("cTime")!=null&&param.get("cTime")!=""){
					WHERE("c_time=#{cTime}");
				}
				if(param.get("uId")!=null&&param.get("uId")!=""){
					WHERE("u_id=#{uId}");
				}
				if(param.get("uTime")!=null&&param.get("uTime")!=""){
					WHERE("u_time=#{uTime}");
				}
				if(param.get("level")!=null&&param.get("level")!=""){
					WHERE("level=#{level}");
				}
				if(param.get("channel")!=null&&param.get("channel")!=""){
					WHERE("channel=#{channel}");
				}
			}
		}.toString();
	}

	//根据ID查找
	public String selectById(Integer id){
		return new SQL(){
			{
				SELECT(simpleColums);
				FROM("user");
				WHERE("id=#{id}");
			}
		}.toString();
	}

	//根据id删除
	public String deleteById(Map param){
		return new SQL(){
			{
				DELETE_FROM("user");
				WHERE("id=#{id}");
			}
		}.toString();
	}


}
