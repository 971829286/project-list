package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityStatistics;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class ActivityStatisticsMapperProvider {
	//保存
	public String save(final ActivityStatistics activityStatistics){
		return new SQL(){
			{
				INSERT_INTO("activity_statistics");
				if(activityStatistics.getActivityId()!=null){
					VALUES("activity_id","#{activityId}");
				}
				if(activityStatistics.getParticipantsNum()!=null){
					VALUES("participants_num","#{participantsNum}");
				}
				if(activityStatistics.getLikeNum()!=null){
					VALUES("like_num","#{likeNum}");
				}
				if(activityStatistics.getCommentNum()!=null){
					VALUES("comment_num","#{commentNum}");
				}
				if(activityStatistics.getShareNum()!=null){
					VALUES("share_num","#{shareNum}");
				}
			}
		}.toString();
	}
	//修改
	public String update(final ActivityStatistics activityStatistics){
		return new SQL(){
			{
				UPDATE("activity_statistics");
				if(activityStatistics.getActivityId()!=null){
					SET("activity_id=#{activityId}");
				}
				if(activityStatistics.getParticipantsNum()!=null){
					SET("participants_num=#{participantsNum}");
				}
				if(activityStatistics.getLikeNum()!=null){
					SET("like_num=#{likeNum}");
				}
				if(activityStatistics.getCommentNum()!=null){
					SET("comment_num=#{commentNum}");
				}
				if(activityStatistics.getCommentNum()!=null){
					SET("share_num=#{shareNum}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}
	//查找所有
	public String findAll(){
		return new SQL(){
			{
				SELECT("id,activity_id,participants_num,like_num,comment_num,share_num");
				FROM("activity_statistics");
			}
		}.toString();
	}

	//根据属性查找（使用user参数）
	public String findByProperty(final ActivityStatistics activityStatistics){
		return new SQL(){
			{
				SELECT("id,activity_id,participants_num,like_num,comment_num,share_num");
				FROM("activity_statistics");
				if(activityStatistics.getId()!=null){
					WHERE("id=#{id}");
				}
				if(activityStatistics.getActivityId()!=null){
					WHERE("activity_id=#{activityId}");
				}
				if(activityStatistics.getParticipantsNum()!=null){
					WHERE("participants_num=#{participantsNum}");
				}
				if(activityStatistics.getLikeNum()!=null){
					WHERE("like_num=#{likeNum}");
				}
				if(activityStatistics.getCommentNum()!=null){
					WHERE("comment_num=#{commentNum}");
				}
				if(activityStatistics.getShareNum()!=null){
					WHERE("share_num=#{shareNum}");
				}
			}
		}.toString();
	}

	//根据属性查找(使用Map参数)
	public String selectByParmer(final Map<String,Object> param){
		return new SQL(){
			{
				SELECT("id,activity_id,participants_num,like_num,share_num");
				FROM("activity_statistics");
				if(param.get("id")!=null){
					WHERE("id=#{id}");
				}
				if(param.get("activityId")!=null){
					WHERE("activity_id=#{activityId}");
				}
				if(param.get("participantsNum")!=null){
					WHERE("participants_num=#{participantsNum}");
				}
				if(param.get("likeNum")!=null){
					WHERE("like_num=#{likeNum}");
				}
				if(param.get("commentNum")!=null){
					WHERE("comment_num=#{commentNum}");
				}
				if(param.get("shareNum")!=null){
					WHERE("share_num=#{shareNum}");
				}
			}
		}.toString();
	}

	//根据ID查找
	public String selectById(Integer id){
		return new SQL(){
			{
				SELECT("id,activity_id,participants_num,like_num,comment_num,share_num");
				FROM("activity_statistics");
				WHERE("id=#{id}");
			}
		}.toString();
	}
	//根据活动ID查找
	public String findByActivityId(Integer activityId){
		return new SQL(){
			{
				SELECT("id,activity_id,participants_num,like_num,comment_num,share_num");
				FROM("activity_statistics");
				WHERE("activity_id=#{activityId}");
			}
		}.toString();
	}

	//根据id删除
	public String deleteById(Map param){
		return new SQL(){
			{
				DELETE_FROM("activity_statistics");
				WHERE("id=#{id}");
			}
		}.toString();
	}

	//根据activityId删除
	public String deleteByActivityId(Map param){
		return new SQL(){
			{
				DELETE_FROM("activity_statistics");
				WHERE("activity_id=#{activityId}");
			}
		}.toString();
	}

	//直播点赞
	public String addLikeNumber(Integer activityId){
		return new SQL(){
			{
				UPDATE("activity_statistics");
					SET("like_num=like_num+1");
				WHERE("activity_id=#{activityId}");
			}
		}.toString();
	}

	//增加评论数
	public String addCommentNumber(Integer activityId){
		return new SQL(){
			{
				UPDATE("activity_statistics");
				SET("comment_num=comment_num+1");
				WHERE("activity_id=#{activityId}");
			}
		}.toString();
	}
	//减少评论数
	public String minusCommentNumber(Integer activityId){
		return new SQL(){
			{
				UPDATE("activity_statistics");
				SET("comment_num=comment_num-1");
				WHERE("activity_id=#{activityId}");
			}
		}.toString();
	}

	//增加参与人数
	public String addParticipantsNumber(Integer activityId){
		return new SQL(){
			{
				UPDATE("activity_statistics");
				SET("participants_num=participants_num+1");
				WHERE("activity_id=#{activityId}");
			}
		}.toString();
	}

	//增加分享数

	//直播点赞
	public String addShareNumber(Integer activityId){
		return new SQL(){
			{
				UPDATE("activity_statistics");
				SET("share_num=share_num+1");
				WHERE("activity_id=#{activityId}");
			}
		}.toString();
	}
}
