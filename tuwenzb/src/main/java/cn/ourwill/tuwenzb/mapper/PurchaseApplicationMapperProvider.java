package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.PurchaseApplication;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class PurchaseApplicationMapperProvider {
	//保存
	public String save(final PurchaseApplication purchaseApplication){
		return new SQL(){
			{
				INSERT_INTO("purchase_application");
				if(purchaseApplication.getUserId()!=null){
					VALUES("user_id","#{userId}");
				}
				if(purchaseApplication.getLicenseType()!=null){
					VALUES("license_type","#{licenseType}");
				}
				if(purchaseApplication.getPhone()!=null){
					VALUES("phone","#{phone}");
				}
				if(purchaseApplication.getContacts()!=null){
					VALUES("contacts","#{contacts}");
				}
				if(purchaseApplication.getWeChat()!=null){
					VALUES("wechat","#{weChat}");
				}
				if(purchaseApplication.getEmail()!=null){
					VALUES("email","#{email}");
				}
				if(purchaseApplication.getCTime()!=null){
					VALUES("c_time","#{cTime}");
				}
				if(purchaseApplication.getStatus()!=null){
					VALUES("status","#{status}");
				}
				if(purchaseApplication.getUTime()!=null){
					VALUES("u_time","#{uTime}");
				}
			}
		}.toString();
	}
	//修改
	public String update(final PurchaseApplication purchaseApplication){
		return new SQL(){
			{
				UPDATE("purchase_application");
				if(purchaseApplication.getUserId()!=null){
					SET("user_id=#{userId}");
				}
				if(purchaseApplication.getLicenseType()!=null){
					SET("license_type=#{licenseType}");
				}
				if(purchaseApplication.getPhone()!=null){
					SET("phone=#{phone}");
				}
				if(purchaseApplication.getContacts()!=null){
					SET("contacts=#{contacts}");
				}
				if(purchaseApplication.getWeChat()!=null){
					VALUES("wechat","#{weChat}");
				}
				if(purchaseApplication.getEmail()!=null){
					VALUES("email","#{email}");
				}
				if(purchaseApplication.getCTime()!=null){
					SET("c_time=#{cTime}");
				}
				if(purchaseApplication.getStatus()!=null){
					SET("status=#{status}");
				}
				if(purchaseApplication.getUTime()!=null){
					SET("u_time=#{uTime}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}
	//修改联系人和手机号
	public String updatePhoneByUserId(final PurchaseApplication purchaseApplication){
		return new SQL(){
			{
				UPDATE("purchase_application");
				if(purchaseApplication.getPhone()!=null){
					SET("phone=#{phone}");
				}
				if(purchaseApplication.getContacts()!=null){
					SET("contacts=#{contacts}");
				}
				if(purchaseApplication.getWeChat()!=null){
					SET("wechat=#{weChat}");
				}
				if(purchaseApplication.getEmail()!=null){
					SET("email=#{email}");
				}
				WHERE("userId=#{userId}");
			}
		}.toString();
	}

	//查找所有
	public String findAll(){
		return new SQL(){
			{
				SELECT("id,user_id,license_type,phone,contacts,wechat,email,c_time,status,u_time");
				FROM("purchase_application");
			}
		}.toString();
	}

	//根据属性查找（使用user参数）
	public String findByProperty(final PurchaseApplication purchaseApplication){
		return new SQL(){
			{
				SELECT("id,user_id,license_type,phone,contacts,wechat,email,c_time,status,u_time");
				FROM("purchase_application");
				if(purchaseApplication.getId()!=null){
					WHERE("id=#{id}");
				}
				if(purchaseApplication.getUserId()!=null){
					WHERE("user_id=#{userId}");
				}
				if(purchaseApplication.getLicenseType()!=null){
					WHERE("license_type=#{licenseType}");
				}
				if(purchaseApplication.getPhone()!=null){
					WHERE("phone=#{phone}");
				}
				if(purchaseApplication.getContacts()!=null){
					WHERE("contacts=#{contacts}");
				}
				if(purchaseApplication.getWeChat()!=null){
					WHERE("wechat=#{weChat}");
				}
				if(purchaseApplication.getEmail()!=null){
					WHERE("email=#{email}");
				}
				if(purchaseApplication.getCTime()!=null){
					WHERE("c_time=#{cTime}");
				}
				if(purchaseApplication.getStatus()!=null){
					WHERE("status=#{status}");
				}
				if(purchaseApplication.getUTime()!=null){
					WHERE("u_time=#{uTime}");
				}
			}
		}.toString();
	}

	//根据属性查找(使用Map参数)
	public String selectByParmer(final Map<String,Object> param){
		return new SQL(){
			{
				SELECT("id,user_id,license_type,phone,contacts,wechat,email,c_time,status,u_time");
				FROM("purchase_application");
				if(param.get("id")!=null){
					WHERE("id=#{id}");
				}
				if(param.get("userId")!=null){
					WHERE("user_id=#{userId}");
				}
				if(param.get("licenseType")!=null){
					WHERE("license_type=#{licenseType}");
				}
				if(param.get("phone")!=null){
					WHERE("phone=#{phone}");
				}
				if(param.get("contacts")!=null){
					WHERE("contacts like concat('%',#{contacts},'%')");
				}
				if(param.get("weChat")!=null){
					WHERE("wechat=#{weChat}");
				}
				if(param.get("email")!=null){
					WHERE("email=#{email}");
				}
				if(param.get("cTime")!=null){
					WHERE("c_time=#{cTime}");
				}
				if(param.get("status")!=null){
					WHERE("status=#{status}");
				}
				if(param.get("uTime")!=null){
					WHERE("u_time=#{uTime}");
				}
				if(param.get("startTime")!=null&&param.get("startTime")!=""){
					WHERE("c_time>=#{startTime}");
				}
				if(param.get("endTime")!=null&&param.get("endTime")!=""){
					WHERE("c_time<=#{endTime}");
				}
			}
		}.toString();
	}

	//根据ID查找
	public String selectById(Integer id){
		return new SQL(){
			{
				SELECT("id,user_id,license_type,phone,contacts,wechat,email,c_time,status,u_time");
				FROM("purchase_application");
				WHERE("id=#{id}");
			}
		}.toString();
	}

	//根据id删除
	public String deleteById(Map param){
		return new SQL(){
			{
				DELETE_FROM("purchase_application");
				WHERE("id=#{id}");
			}
		}.toString();
	}
}
