package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.entity.Vote;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class VoteMapperProvider {
	private String colums="";
	private String simpleColums="";
	//保存
	public String save(final Vote vote){
		return new SQL(){
			{
				INSERT_INTO("vote");
				if(vote.getActivityId() != null){
					VALUES("activity_id", "#{activityId}");
				}
				if(vote.getUserId()!=null){
					VALUES("user_id", "#{userId}");
				}
				if(StringUtils.isNotBlank(vote.getTitle())){
					VALUES("title", "#{title}");
				}
				if(StringUtils.isNotBlank(vote.getIntroduction())){
					VALUES("introduction", "#{introduction}");
				}
				if(vote.getStartTime()!=null){
					VALUES("start_time", "#{startTime}");
				}
				if(vote.getEndTime()!=null){
					VALUES("end_time", "#{endTime}");
				}
				if(vote.getDateType()!=null){
					VALUES("date_type", "#{dateType}");
				}
				if(vote.getIsRadio()!=null){
					VALUES("is_radio","#{isRadio}");
				}
				if(vote.getIsVisible()!=null){
					VALUES("is_visible", "#{isVisible}");
				}
				if(vote.getStatus()!=null){
					VALUES("status", "#{status}");
				}
			}
		}.toString();
	}

	//根据activityId查找
	public String selectByActivityId(Integer activityId){
		return new SQL(){
			{
				SELECT("id,activity_id,user_id,title,introduction,start_time,end_time,is_radio,is_visible,status,date_type");
				FROM("vote");
				if(activityId!=null){
					WHERE("activity_id=#{activityId}");
				}
			}
		}.toString();
	}

	public String selectById(Integer voteId){
		return new SQL(){
			{
				SELECT("id,activity_id,user_id,title,introduction,start_time,end_time,is_radio,is_visible,status,date_type");
				FROM("vote");
				WHERE("id=#{voteId}");
			}
		}.toString();
	}

	//修改
	public String updateVote(final Vote vote){
		return new SQL(){
			{
				UPDATE("vote");
				if(vote.getActivityId() != null){
					SET("activity_id=#{activityId}");
				}
				if(vote.getUserId()!=null){
					SET("user_id=#{userId}");
				}
				if(StringUtils.isNotBlank(vote.getTitle())){
					SET("title=#{title}");
				}
				if(StringUtils.isNotBlank(vote.getIntroduction())){
					SET("introduction=#{introduction}");
				}
				if(vote.getStartTime()!=null){
					SET("start_time=#{startTime}");
				}
				if(vote.getEndTime()!=null){
					SET("end_time=#{endTime}");
				}
				if(vote.getDateType()!=null){
					SET("date_type=#{dateType}");
				}
				if(vote.getIsRadio()!=null){
					SET("is_radio=#{isRadio}");
				}
				if(vote.getIsVisible()!=null){
					SET("is_visible=#{isVisible}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}

	//关闭投票
	public String updateStatusById(final Integer voteId){
		return new SQL(){
			{
				UPDATE("vote");
				SET("status=-1");
				WHERE("id=#{voteId}");
			}
		}.toString();
	}

}
