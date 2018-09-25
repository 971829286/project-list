package cn.ourwill.willcenter.mapper;

import cn.ourwill.willcenter.entity.User;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class UserMapperProvider {
	private String colums="id,uuid,user_type,username,nickname,password,salt,avatar,mob_phone,tel_phone,email,qq,company,address," +
			"c_id,c_time,u_id,u_time,level,version,info,unionid";
//	private String simpleColums="id,uuid,user_type,username,nickname,password,salt,avatar,mob_phone,tel_phone,email,qq,company,address," +
//			"c_id,c_time,u_id,u_time,level";
//
	//保存
	public String save(final User user){
		return new SQL(){
			{
				INSERT_INTO("user");
				if(user.getUUID()!=null){
					VALUES("uuid", "#{UUID}");
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
				if(user.getEmail()!=null){
					VALUES("email", "#{email}");
				}
				if(user.getQq()!=null){
					VALUES("qq", "#{qq}");
				}
				if(user.getCompany()!=null){
					VALUES("company", "#{company}");
				}
				if(user.getAddress()!=null) {
					VALUES("address", "#{address}");
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
				if(user.getInfo() != null){
					VALUES("info", "#{info}");
				}
				if(user.getUnionid() != null){
					VALUES("unionid", "#{unionid}");
				}
				if(user.getUserfromType() != null){
					VALUES("user_from_type", "#{userfromType}");
				}
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
					SET("version = version + 1");
				WHERE("id=#{id}");
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

	//根据用户名或手机号查找
	public String selectByUsernameOrPhone(String str){
		return new SQL(){
			{
				SELECT(colums);
				FROM("user");
				WHERE("username = #{username} or mob_phone=#{mobPhone}");
			}
		}+" limit 1".toString();
	}

    //根据邮箱或手机号查找
    public String selectByEmailOrPhone(String str){
        return new SQL(){
            {
                SELECT(colums);
                FROM("user");
                WHERE("email = #{email} or mob_phone=#{mobPhone}");
            }
        }+" limit 1".toString();
    }

	//修改
	public String updateUserInfo(final User user){
		return new SQL(){
			{
				UPDATE("user");
				if(user.getUUID()!=null){
					SET("uuid=#{UUID}");
				}
				if(user.getUserType()!=null){
					SET("user_type=#{userType}");
				}
				if(user.getUsername()!=null){
					SET("username=#{username}");
				}
				if(user.getNickname()!=null){
					SET("nickname=#{nickname}");
				}
				if(user.getAvatar()!=null){
					SET("avatar=#{avatar}");
				}
				if(user.getMobPhone()!=null){
					SET("mob_phone=#{mobPhone}");
				}
				if(user.getTelPhone()!=null){
					SET("tel_phone=#{telPhone}");
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
				if(user.getInfo()!=null){
					SET("info=#{info}");
				}
				if(user.getUnionid()!=null){
					SET("unionid=#{unionid}");
				}
				if(user.getUserfromType()!=null){
					SET("user_from_type=#{userfromType}");
				}
				SET("version=version+1");
				WHERE("id=#{id}");
			}
		}.toString();
	}

	//修改
	public String updateUserInfoByUuid(final User user){
		return new SQL(){
			{
				UPDATE("user");
				if(user.getUserType()!=null){
					SET("user_type=#{userType}");
				}
				if(user.getUsername()!=null){
					SET("username=#{username}");
				}
				if(user.getNickname()!=null){
					SET("nickname=#{nickname}");
				}
				if(user.getAvatar()!=null){
					SET("avatar=#{avatar}");
				}
				if(user.getMobPhone()!=null){
					SET("mob_phone=#{mobPhone}");
				}
				if(user.getTelPhone()!=null){
					SET("tel_phone=#{telPhone}");
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
				if(user.getInfo()!=null){
					SET("info=#{info}");
				}
				SET("version=version+1");
				WHERE("uuid=#{UUID}");
			}
		}.toString();
	}
	//查找所有
	public String findAll(){
		return new SQL(){
			{
				SELECT(colums);
				FROM("user");
			}
		}.toString();
	}

	//根据属性查找（使用user参数）
	public String findByProperty(final User user){
		return new SQL(){
			{
				SELECT(colums);
				FROM("user");
				if(user.getId()!=null){
					WHERE("id=#{id}");
				}
				if(user.getUUID()!=null){
					WHERE("uuid=#{UUID}");
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
				SELECT(colums);
				FROM("user");
				if(param.get("id")!=null&&param.get("id")!=""){
					WHERE("id=#{id}");
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
			}
		}.toString();
	}

	//根据ID查找
	public String selectById(Integer id){
		return new SQL(){
			{
				SELECT(colums);
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

	//根据Unionid找
	public String selectByUnionId(String unionid){
		return new SQL(){
			{
				SELECT(colums);
				FROM("user");
				WHERE("unionid=#{unionid}");
			}
		}.toString();
	}

	//更新用户手机或者邮箱
	public String updateEmailOrMobPhone(User user){
		return new SQL(){
			{
				UPDATE("user");
				if(user.getEmail() != null){
					SET("email=#{email}");
				}
				if(user.getMobPhone() != null){
					SET("mob_phone=#{mobPhone}");
				}
				SET("version=version+1");
				WHERE("uuid=#{UUID}");
			}
		}.toString();
	}


}
