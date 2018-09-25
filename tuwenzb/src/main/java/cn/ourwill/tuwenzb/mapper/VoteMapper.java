package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityType;
import cn.ourwill.tuwenzb.entity.Vote;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VoteMapper extends IBaseMapper<Vote>{
	@InsertProvider(type = VoteMapperProvider.class,method ="save")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public Integer save(Vote vote);

	@SelectProvider(type = VoteMapperProvider.class,method = "selectByActivityId")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "title",property = "title"),
			@Result( column = "introduction",property = "introduction"),
			@Result( column = "start_time",property = "startTime"),
			@Result( column = "end_time",property = "endTime"),
			@Result( column = "is_radio",property = "isRadio"),
			@Result( column = "is_visible",property = "isVisible"),
			@Result( column = "status",property = "status"),
			@Result( column = "date_type",property = "dateType")
	})
	public List<Vote> getVoteList(Integer activityId);

	@Delete("<script> " +
			"delete from vote " +
			"where  id in "+
			"<foreach collection=\"voteIds\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\" > "+
			"#{item}"+
			"</foreach> " +
			"</script>" )
	public Integer deleteVotes(@Param("voteIds")List<Integer> voteIds);

	@UpdateProvider(type = VoteMapperProvider.class,method = "updateVote")
	Integer updateVote(Vote vote);

	@SelectProvider(type = VoteMapperProvider.class,method = "selectById")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "title",property = "title"),
			@Result( column = "introduction",property = "introduction"),
			@Result( column = "start_time",property = "startTime"),
			@Result( column = "end_time",property = "endTime"),
			@Result( column = "is_radio",property = "isRadio"),
			@Result( column = "is_visible",property = "isVisible"),
			@Result( column = "status",property = "status"),
			@Result( column = "date_type",property = "dateType")
	})
	Vote getById(Integer voteId);

	@UpdateProvider(type = VoteMapperProvider.class,method = "updateStatusById")
	Integer closedById(Integer voteId);

	@Select("select count(1) from vote where activity_id = #{activityId} and status = 1")
    int getVoteCount(Integer activityId);
}
