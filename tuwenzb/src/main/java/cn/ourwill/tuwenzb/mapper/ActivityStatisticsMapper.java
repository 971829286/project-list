package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityStatistics;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ActivityStatisticsMapper extends IBaseMapper<ActivityStatistics>{
	@InsertProvider(type = ActivityStatisticsMapperProvider.class,method ="save")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public Integer save(ActivityStatistics activityStatistics);
	
	@UpdateProvider(type = ActivityStatisticsMapperProvider.class,method = "update")
	public Integer update(ActivityStatistics activityStatistics);

	@SelectProvider(type = ActivityStatisticsMapperProvider.class,method = "findAll")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "participants_num",property = "participantsNum"),
			@Result( column = "comment_num",property = "commentNum"),
			@Result( column = "like_num",property = "likeNum"),
			@Result( column = "share_num",property = "shareNum"),

			@Result( column = "real_participants_num",property = "realParticipantsNum"),
			@Result( column = "real_comment_num",property = "realCommentNum"),
			@Result( column = "real_share_num",property = "realShareNum"),
			@Result( column = "real_like_num",property = "realLikeNum")
	})
	public List<ActivityStatistics> findAll();

	@SelectProvider(type = ActivityStatisticsMapperProvider.class,method = "selectById")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "participants_num",property = "participantsNum"),
			@Result( column = "comment_num",property = "commentNum"),
			@Result( column = "like_num",property = "likeNum"),
			@Result( column = "share_num",property = "shareNum"),

			@Result( column = "real_participants_num",property = "realParticipantsNum"),
			@Result( column = "real_comment_num",property = "realCommentNum"),
			@Result( column = "real_share_num",property = "realShareNum"),
			@Result( column = "real_like_num",property = "realLikeNum")
	})
	public ActivityStatistics getById(Integer id);

	//根据ActivityId查找
	@SelectProvider(type = ActivityStatisticsMapperProvider.class,method = "findByActivityId")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "participants_num",property = "participantsNum"),
			@Result( column = "comment_num",property = "commentNum"),
			@Result( column = "like_num",property = "likeNum"),
			@Result( column = "share_num",property = "shareNum"),

			@Result( column = "real_participants_num",property = "realParticipantsNum"),
			@Result( column = "real_comment_num",property = "realCommentNum"),
			@Result( column = "real_share_num",property = "realShareNum"),
			@Result( column = "real_like_num",property = "realLikeNum")
	})
	public ActivityStatistics findByActivityId(Integer activityId);

	@DeleteProvider(type = ActivityStatisticsMapperProvider.class,method = "deleteById")
	public Integer delete(Map param);

	@DeleteProvider(type = ActivityStatisticsMapperProvider.class,method = "deleteByActivityId")
	public Integer deleteActivityId(Map param);

	//直播点赞
	@UpdateProvider(type = ActivityStatisticsMapperProvider.class,method = "addLikeNumber")
	public Integer addLikeNumber(Integer activityId);

	//增加评论数
	@UpdateProvider(type = ActivityStatisticsMapperProvider.class,method = "addCommentNumber")
	public Integer addCommentNumber(Integer activityId);

	//减少评论数
	@UpdateProvider(type = ActivityStatisticsMapperProvider.class,method = "minusCommentNumber")
	public Integer minusCommentNumber(Integer activityId);

	//增加参与人数
	@UpdateProvider(type = ActivityStatisticsMapperProvider.class,method = "addParticipantsNumber")
	public Integer addParticipantsNumber(Integer activityId);

	@Update("<script>" +
			"INSERT INTO activity_statistics(id,activity_id,participants_num,like_num,comment_num,share_num,real_like_num," +
			"real_comment_num,real_share_num,real_participants_num) "+
			"VALUES " +
			"<foreach collection=\"statistics\" index=\"index\" item=\"item\" separator=\",\"> "+
			"(#{item.id},#{item.activityId},#{item.participantsNum},#{item.likeNum},#{item.commentNum},#{item.shareNum}," +
			"#{item.realLikeNum},#{item.realCommentNum},#{item.realShareNum},#{item.realParticipantsNum})"+
			"</foreach> "+
			"ON DUPLICATE KEY UPDATE "+
			"participants_num = VALUES(participants_num), "+
			"like_num = VALUES(like_num), "+
			"comment_num = VALUES(comment_num)," +
			"share_num = VALUES(share_num),"+
			"real_comment_num = VALUES(real_comment_num),"+
			"real_share_num = VALUES(real_share_num),"+
			"real_participants_num = VALUES(real_participants_num),"+
			"real_like_num = VALUES(real_like_num)"+
			"</script>" )
	public Integer batchUpdate(@Param("statistics") List statistics);

	@Select("select activity_id from activity_statistics")
	@Results({
			@Result( column = "activity_id",property = "activityId")
	})
    List<Integer> findAllId();

	//增加分享数
	@UpdateProvider(type = ActivityStatisticsMapperProvider.class,method = "addShareNumber")
	Integer addShareNumber(Integer activityId);
}
