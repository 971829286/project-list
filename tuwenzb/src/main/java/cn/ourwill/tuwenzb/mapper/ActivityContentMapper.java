package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityContent;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ActivityContentMapper extends IBaseMapper<ActivityContent>{

	@InsertProvider(type = ActivityContentMapperProvider.class,method ="save")
	public Integer save(ActivityContent activityContent);
	
	@UpdateProvider(type = ActivityContentMapperProvider.class,method = "update")
	public Integer update(ActivityContent activityContent);

	@SelectProvider(type = ActivityContentMapperProvider.class,method = "findAll")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "content",property = "content"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "img",property = "img"),
			@Result( column = "video",property = "video"),
			@Result( column = "video_link",property = "videoLink"),
			@Result(column = "stick_sign",property = "stickSign")
	})
	public List<ActivityContent> findAll();

	@SelectProvider(type = ActivityContentMapperProvider.class,method = "selectById")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "content",property = "content"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "img",property = "img"),
			@Result( column = "video",property = "video"),
			@Result( column = "video_link",property = "videoLink"),
			@Result(column = "stick_sign",property = "stickSign")
	})
	public ActivityContent getById(Integer id);

	@DeleteProvider(type = ActivityContentMapperProvider.class,method = "deleteById")
	public Integer delete(Map param);

	//获取最新直播内容
	@SelectProvider(type = ActivityContentMapperProvider.class,method = "getRecentActivity")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "content",property = "content"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "img",property = "img"),
			@Result( column = "video",property = "video"),
			@Result( column = "video_link",property = "videoLink"),
			@Result( column = "stick_sign",property = "stickSign")
	})
	public List<ActivityContent> getRecentActivity(@Param("activityId") Integer activityId,@Param("date") String date,@Param("timeOrder") String timeOrder);


	//获取最新直播内容不含标签
	@SelectProvider(type = ActivityContentMapperProvider.class,method = "getRecentActivityNoTags")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "content",property = "content"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "img",property = "img"),
			@Result( column = "video",property = "video"),
			@Result( column = "video_link",property = "videoLink"),
			@Result( column = "stick_sign",property = "stickSign")
	})
	public List<ActivityContent> getRecentActivityNoTags(@Param("activityId") Integer activityId);

	@Select("select img from activity_content where activity_id = #{activityId} and img <> '[]' order by c_time desc")
	List<String> getAllImgByActivityId(Integer activityId);

	@Select("select count(1) from activity_content where activity_id = #{activityId}")
    Integer getContentCount(Integer activityId);

	@Select("select count(1) from activity_content where activity_id = #{activityId}")
    Integer getCountNum(Integer activityId);

	@Update("update activity_content set stick_sign = #{stickSign}, u_time = SYSDATE() where id = #{id}")
	Integer stickActivityContent (@Param("id") Integer id,@Param("stickSign") Integer stickSign);

	@Select("select MAX(stick_sign)+1 from   \n" +
			"(select activity_id from activity_content where id= #{id})a LEFT JOIN activity_content b on a.activity_id = b.activity_id")
	Integer getMaxSticksign(@Param("id") Integer id);

	@Select("select  count(1) from (select activity_id from activity_content where id= #{id}) a LEFT JOIN activity_content b on a.activity_id = b.activity_id WHERE stick_sign <> 0")
	Integer getStickNum(@Param("id") Integer id);

	@Select("select  count(1) from activity_content where activity_id = #{activityId} and ( c_time > #{time} or u_time >#{time})")
	Integer getNewContentNum(@Param("activityId") Integer activityId, @Param("time")Date time);

	@SelectProvider(type = ActivityContentMapperProvider.class,method = "getRecentConcentByTime")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "content",property = "content"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "img",property = "img"),
			@Result( column = "video",property = "video"),
			@Result( column = "video_link",property = "videoLink"),
			@Result( column = "stick_sign",property = "stickSign")
	})
	List<ActivityContent> getRecentConcentByTime(@Param("activityId") Integer activityId, @Param("date") Date date, @Param("timeOrder") Integer timeOrder,@Param("contentNum") Integer contentNum);

	@Select("select max(timet) from (\n" +
			"select  u_time AS timet from activity_content where activity_id = #{activityId}\n" +
			"UNION\n" +
			"select  c_time AS timet from activity_content where activity_id = #{activityId})tt ")
	String getMaxTime(@Param("activityId") Integer activityId);
}
