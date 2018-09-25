package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class CommentMapperProvider {
	//保存
	public String save(final Comment comment){
		return new SQL(){
			{
				INSERT_INTO("comment");
				if(comment.getParentId()!=null){
					VALUES("parent_id","#{parentId}");
				}
				if(comment.getActivityId()!=null){
					VALUES("activity_id","#{activityId}");
				}
				if(comment.getPhotoId()!=null){
					VALUES("photo_id","#{photoId}");
				}
				if(comment.getContent()!=null){
					VALUES("content","#{content}");
				}
				if(comment.getUserId()!=null){
					VALUES("user_id","#{userId}");
				}
				if(comment.getParentUserId()!=null){
					VALUES("parent_user_id","#{parentUserId}");
				}
				if(comment.getCTime()!=null){
					VALUES("c_time","#{cTime}");
				}
				if(comment.getNickname()!=null){
					VALUES("nickname","#{nickname}");
				}
				if(comment.getAvatar()!=null){
					VALUES("avatar","#{avatar}");
				}
				if(comment.getCheck()!=null){
					VALUES("`check`","#{check}");
				}
				if(comment.getDeviceid()!=null){
					VALUES("deviceID","#{deviceid}");
				}
				if(comment.getIp()!=null){
					VALUES("ip","#{ip}");
				}
			}
		}.toString();
	}
	//修改
	public String update(final Comment comment){
		return new SQL(){
			{
				UPDATE("comment");
				if(comment.getParentId()!=null){
					SET("parent_id=#{parentId}");
				}
				if(comment.getActivityId()!=null){
					SET("activity_id=#{activityId}");
				}
				if(comment.getPhotoId()!=null){
					SET("photo_id=#{photoId}");
				}
				if(comment.getContent()!=null){
					SET("content=#{content}");
				}
				if(comment.getUserId()!=null){
					SET("user_id=#{userId}");
				}
				if(comment.getParentUserId()!=null){
					SET("parent_user_id=#{parentUserId}");
				}
				if(comment.getCTime()!=null){
					SET("c_time=#{cTime}");
				}
				if(comment.getNickname()!=null){
					SET("nickname=#{nickname}");
				}
				if(comment.getAvatar()!=null){
					SET("avatar=#{avatar}");
				}
				if(comment.getCheck()!=null){
					SET("`check`=#{check}");
				}
				if(comment.getDeviceid()!=null){
					SET("deviceID=#{deviceid}");
				}
				if(comment.getIp()!=null){
					SET("ip=#{ip}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}
	//查找所有
	public String findAll(){
		return new SQL(){
			{
				SELECT("id,parent_id,activity_id,photo_id,content,user_id,parent_user_id,c_time,nickname,avatar,`check`,deviceID,ip");
				FROM("comment");
			}
		}.toString();
	}

	//根据属性查找（使用user参数）
	public String findByProperty(final Comment comment){
		return new SQL(){
			{
				SELECT("id,parent_id,activity_id,photo_id,content,user_id,parent_user_id,c_time,nickname,avatar,`check`,deviceID,ip");
				FROM("comment");
				if(comment.getId()!=null){
					WHERE("id=#{id}");
				}
				if(comment.getParentId()!=null){
					WHERE("parent_id=#{parentId}");
				}
				if(comment.getActivityId()!=null){
					WHERE("activity_id=#{activityId}");
				}
				if(comment.getPhotoId()!=null){
					WHERE("photo_id=#{photoId}");
				}
				if(comment.getContent()!=null){
					WHERE("content=#{content}");
				}
				if(comment.getUserId()!=null){
					WHERE("user_id=#{userId}");
				}
				if(comment.getParentUserId()!=null){
					WHERE("parent_user_id=#{parentUserId}");
				}
				if(comment.getCTime()!=null){
					WHERE("c_time=#{cTime}");
				}
				if(comment.getNickname()!=null){
					WHERE("nickname=#{nickname}");
				}
				if(comment.getAvatar()!=null){
					WHERE("avatar=#{avatar}");
				}
				if(comment.getCheck()!=null){
					WHERE("`check`=#{check}");
				}
				if(comment.getDeviceid()!=null){
					WHERE("deviceID=#{deviceid}");
				}
				if(comment.getIp()!=null){
					WHERE("ip=#{ip}");
				}

			}
		}.toString();
	}

	//根据属性查找(使用Map参数)
	public String selectByParmer(final Map<String,Object> param){
		return new SQL(){
			{
				SELECT("c.id,c.parent_id,c.activity_id,c.photo_id,c.content,c.user_id,c.parent_user_id,c.c_time,c.nickname,c.avatar,c.`check`,c.deviceID,c.ip,a.title");
				FROM("comment c");
				LEFT_OUTER_JOIN("activity a on a.id = c.activity_id");
				if(param.get("id")!=null&&param.get("id")!=""){
					WHERE("c.id=#{id}");
				}
				if(param.get("parent_id")!=null&&param.get("parent_id")!=""){
					WHERE("parent_id=#{parentId}");
				}
				if(param.get("activityId")!=null&&param.get("activityId")!=""){
					WHERE("c.activity_id=#{activityId}");
				}
				if(param.get("content")!=null&&param.get("content")!=""){
					WHERE("c.content=#{content}");
				}
				if(param.get("userId")!=null&&param.get("userId")!=""){
					WHERE("c.user_id=#{userId}");
				}
				if(param.get("parent_user_id")!=null&&param.get("parent_user_id")!=""){
					WHERE("parent_user_id=#{parentUserId}");
				}
				if(param.get("cTime")!=null&&param.get("cTime")!=""){
					WHERE("c.c_time=#{cTime}");
				}
				if(param.get("nickname")!=null&&param.get("nickname")!=""){
					WHERE("c.nickname like concat('%',#{nickname},'%')");
				}
				if(param.get("avatar")!=null&&param.get("avatar")!=""){
					WHERE("c.avatar=#{avatar}");
				}
				if(param.get("check")!=null&&param.get("check")!=""){
					WHERE("c.`check`=#{check}");
				}
				if(param.get("deviceid")!=null&&param.get("deviceid")!=""){
					WHERE("c.deviceID=#{deviceid}");
				}
				if(param.get("ip")!=null&&param.get("ip")!=""){
					WHERE("c.ip=#{ip}");
				}
				if(param.get("startTime")!=null&&param.get("startTime")!=""){
					WHERE("c.c_time>=#{startTime}");
				}
				if(param.get("endTime")!=null&&param.get("endTime")!=""){
					WHERE("c.c_time<=#{endTime}");
				}
				if(param.get("checkType")!=null&&param.get("checkType")!=""){
					if(param.get("checkType").equals(1))
						WHERE(" c.`check` <> 1 ");
					if(param.get("checkType").equals(0))
						WHERE(" c.`check` = 2 ");
				}
				if(param.get("activityTitle")!=null&&param.get("activityTitle")!=""){
					WHERE("a.title like concat('%',#{activityTitle},'%')");
				}
				if(param.get("orderBy")!=null&&param.get("orderBy")!=""){
					if(param.get("orderBy").equals(1))
						ORDER_BY(" c.c_time desc");
					if(param.get("orderBy").equals(0))
						ORDER_BY(" c.c_time asc");
				}

			}
		}.toString();
	}
	//根据ID查找
	public String selectByPhotoId(@Param("photoId") Integer photoId,Integer checkType){
		return new SQL(){
			{
				SELECT("id,parent_id,activity_id,photo_id,content,user_id,parent_user_id,c_time,nickname,avatar,`check`,deviceID,ip");
				FROM("comment");
				WHERE("photo_id=#{photoId}");
				if(checkType!=null){
					if(checkType.equals(1))
						WHERE(" `check` <> 1 ");
					if(checkType.equals(0))
						WHERE(" `check` = 2 ");
				}
			}
		}.toString();
	}
	//根据ID查找
	public String selectById(Integer id){
		return new SQL(){
			{
				SELECT("id,parent_id,activity_id,photo_id,content,user_id,parent_user_id,c_time,nickname,avatar,`check`,deviceID,ip");
				FROM("comment");
				WHERE("id=#{id}");
			}
		}.toString();
	}

	//根据id删除
	public String deleteById(Map param){
		return new SQL(){
			{
				DELETE_FROM("comment");
				WHERE("id=#{id}");
			}
		}.toString();
	}
}
