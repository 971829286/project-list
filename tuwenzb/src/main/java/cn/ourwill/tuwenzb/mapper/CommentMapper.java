package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.Comment;
import cn.ourwill.tuwenzb.entity.CommentWithActivity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentMapper extends IBaseMapper<Comment>{

	@Insert({"<script>" +
			"insert into comment(activity_id,content,c_time,nickname,avatar,`check`) "+
			"values"+
			"<foreach collection=\"list\" index=\"index\" item=\"comment\"  separator=\",\" > "+
			" (#{comment.activityId},#{comment.content},#{comment.cTime},#{comment.nickname},#{comment.avatar},#{comment.check})"+
			"</foreach> "+
			"</script>"})
	public Integer volumeIncrease(List<Comment> list);

	@InsertProvider(type = CommentMapperProvider.class,method ="save")
	public Integer save(Comment comment);
	
	@UpdateProvider(type = CommentMapperProvider.class,method = "update")
	public Integer update(Comment comment);

	@SelectProvider(type = CommentMapperProvider.class,method = "findAll")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "parent_id",property = "parentId"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "photo_id",property = "photoId"),
			@Result( column = "content",property = "content"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "parent_user_id",property = "parentUserId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "check",property = "check"),
			@Result( column = "deviceID",property = "deviceid"),
			@Result( column = "ip",property = "ip")
	})
	public List<Comment> findAll();

	@SelectProvider(type = CommentMapperProvider.class,method = "selectById")
	@Results({
			@Result(id = true, column = "id",property = "id"),
			@Result( column = "parent_id",property = "parentId"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "photo_id",property = "photoId"),
			@Result( column = "content",property = "content"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "parent_user_id",property = "parentUserId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "check",property = "check"),
			@Result( column = "deviceID",property = "deviceid"),
			@Result( column = "ip",property = "ip")
	})
	public Comment getById(Integer id);

	@DeleteProvider(type = CommentMapperProvider.class,method = "deleteById")
	public Integer delete(Map param);

	@SelectProvider(type = CommentMapperProvider.class,method = "selectByParmer")
	@Results({
			@Result(id = true, column = "id",property = "id"),
			@Result( column = "parent_id",property = "parentId"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "photo_id",property = "photoId"),
			@Result( column = "content",property = "content"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "parent_user_id",property = "parentUserId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "check",property = "check"),
			@Result( column = "deviceID",property = "deviceid"),
			@Result( column = "ip",property = "ip"),
			@Result( column = "title",property = "activityTitle"),
			@Result( column = "photo_id",property = "activityPhoto",
				one = @One(
						select="cn.ourwill.tuwenzb.mapper.ActivityPhotoMapper.getById"
				)
			),
			@Result( column = "parent_id",property = "parentComment",
				one = @One(
					select="cn.ourwill.tuwenzb.mapper.CommentMapper.getById"
				)
			)
	})
	public List<Comment> getByParamWithBack(Map param);

	@SelectProvider(type = CommentMapperProvider.class,method = "selectByParmer")
	@Results({
			@Result(id = true, column = "id",property = "id"),
			@Result( column = "parent_id",property = "parentId"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "photo_id",property = "photoId"),
			@Result( column = "content",property = "content"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "parent_user_id",property = "parentUserId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "check",property = "check"),
			@Result( column = "deviceID",property = "deviceid"),
			@Result( column = "ip",property = "ip"),
			@Result( column = "title",property = "activityTitle")
	})
	public List<Comment> getByParam(Map param);

	@Select("select c.id,c.parent_id,c.activity_id,c.photo_id,c.content,c.user_id,c.parent_user_id,c.c_time,c.nickname,c.avatar,c.check,c.deviceID,c.ip,a.title,a.banner,a.photo_live from comment c " +
			"left join activity a on a.id=c.activity_id where (c.user_id = #{userId} or c.user_id = (select bound_id from user where id = #{userId}) " +
			"or c.parent_user_id = #{userId} or c.parent_user_id = (select bound_id from user where id = #{userId})) and c.check <> 1 and a.photo_live = #{photoLive} order by c.c_time desc ")
	@Results({
			@Result(id = true, column = "id",property = "id"),
			@Result( column = "parent_id",property = "parentId"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "photo_id",property = "photoId"),
			@Result( column = "content",property = "content"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "parent_user_id",property = "parentUserId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "check",property = "check"),
			@Result( column = "deviceID",property = "deviceid"),
			@Result( column = "ip",property = "ip"),
			@Result( column = "title",property = "activityTitle"),
			@Result( column = "banner",property = "activityBanner"),
			@Result( column = "photo_live",property = "photoLive"),
			@Result( column = "photo_id",property = "activityPhoto",
					one = @One(
							select="cn.ourwill.tuwenzb.mapper.ActivityPhotoMapper.getById"
					)
			),
			@Result( column = "parent_id",property = "parentComment",
					one = @One(
							select="cn.ourwill.tuwenzb.mapper.CommentMapper.getById"
					)
			)
	})
	public List<CommentWithActivity> getByUserId(@Param("userId") Integer userId,@Param("photoLive") Integer photoLive);

	@Select("select c.id,c.parent_id,c.activity_id,c.photo_id,c.content,c.user_id,c.parent_user_id,c.c_time,c.nickname,c.avatar,c.check,c.deviceID,c.ip,a.title,a.banner,a.photo_live from comment c " +
			"left join activity a on a.id=c.activity_id where " +
			"(c.parent_user_id = #{userId} or c.parent_user_id = (select bound_id from user where id = #{userId})) and c.check <> 1 and a.photo_live = #{photoLive} order by c.c_time desc ")
	@Results({
			@Result(id = true, column = "id",property = "id"),
			@Result( column = "parent_id",property = "parentId"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "photo_id",property = "photoId"),
			@Result( column = "content",property = "content"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "parent_user_id",property = "parentUserId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "check",property = "check"),
			@Result( column = "deviceID",property = "deviceid"),
			@Result( column = "ip",property = "ip"),
			@Result( column = "title",property = "activityTitle"),
			@Result( column = "banner",property = "activityBanner"),
			@Result( column = "photo_live",property = "photoLive"),
			@Result( column = "photo_id",property = "activityPhoto",
					one = @One(
							select="cn.ourwill.tuwenzb.mapper.ActivityPhotoMapper.getById"
					)
			),
			@Result( column = "parent_id",property = "parentComment",
					one = @One(
							select="cn.ourwill.tuwenzb.mapper.CommentMapper.getById"
					)
			)
	})
	List<CommentWithActivity> getBackByUserId(@Param("userId") Integer userId,@Param("photoLive") Integer photoLive);

	@Select("select count(0) from comment c " +
			"left join activity a on a.id=c.activity_id where (c.user_id = #{userId} or c.user_id = (select bound_id from user where id = #{userId}) " +
			"or c.parent_user_id = #{userId} or c.parent_user_id = (select bound_id from user where id = #{userId})) and c.check <> 1 and a.photo_live = #{photoLive}")
	Integer selectCountByUserId(@Param("userId") Integer userId,@Param("photoLive") Integer photoLive);

	@Update("<script>" +
			"update comment set `check` = #{check} "+
			"<where> and id in " +
			"<foreach collection=\"ids\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\"> "+
			" #{item}"+
			"</foreach> "+
			"</where> "+
			"</script>" )
	public Integer updateCheckBatch(@Param("ids") List ids,@Param("check") Integer check);

	@Select("<script> select count(1) as count from comment "+
			"<where> and activity_id = #{activityId} "+
			"<if test=\"checkType==1\"> <![CDATA[and `check` <> 1]]> </if> "+
			"<if test=\"checkType==0\"> and `check` = 2 </if> "+
			"</where> </script>")
	public Integer selectCountByCheck(@Param("activityId") Integer activityId,@Param("checkType") Integer checkType);

	@SelectProvider(type = CommentMapperProvider.class,method = "selectByPhotoId")
	@Results({
			@Result(id = true, column = "id",property = "id"),
			@Result( column = "parent_id",property = "parentId"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "photo_id",property = "photoId"),
			@Result( column = "content",property = "content"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "parent_user_id",property = "parentUserId"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "nickname",property = "nickname"),
			@Result( column = "avatar",property = "avatar"),
			@Result( column = "check",property = "check"),
			@Result( column = "deviceID",property = "deviceid"),
			@Result( column = "ip",property = "ip"),
			@Result( column = "title",property = "activityTitle")
	})
	List<Comment> getByPhotoId(@Param("photoId")Integer photoId,Integer checkType);

}
