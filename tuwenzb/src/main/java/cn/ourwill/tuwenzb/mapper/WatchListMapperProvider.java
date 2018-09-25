package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.WatchList;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class WatchListMapperProvider {
	private String tableName="watch_list";
	private String columns="id,activity_id,user_id";
	//保存
	public String save(final WatchList watchList){
		return new SQL(){
			{
				INSERT_INTO(tableName);
				if(watchList.getActivityId()!=null){
					VALUES("activity_id","#{activityId}");
				}
				if(watchList.getUserId()!=null){
					VALUES("user_id","#{userId}");
				}
			}
		}.toString();
	}
	//修改
	public String update(final WatchList watchList){
		return new SQL(){
			{
				UPDATE(tableName);
				if(watchList.getActivityId()!=null){
					SET("activity_id=#{activityId}");
				}
				if(watchList.getUserId()!=null){
					SET("user_id=#{userId}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}
	//查找所有
	public String findAll(){
		return new SQL(){
			{
				SELECT(columns);
				FROM(tableName);
			}
		}.toString();
	}

	//根据属性查找（使用user参数）
	public String findByProperty(final WatchList watchList){
		return new SQL(){
			{
				SELECT(columns);
				FROM(tableName);
				if(watchList.getId()!=null){
					WHERE("id=#{id}");
				}
				if(watchList.getActivityId()!=null){
					WHERE("activity_id=#{activityId}");
				}
				if(watchList.getUserId()!=null){
					WHERE("user_id=#{userId}");
				}

			}
		}.toString();
	}

	//根据属性查找(使用Map参数)
	public String selectByParmer(final Map<String,Object> param){
		return new SQL(){
			{
				SELECT(columns);
				FROM(tableName);
				if(param.get("id")!=null){
					WHERE("id=#{id}");
				}
				if(param.get("activityId")!=null){
					WHERE("activity_id=#{activityId}");
				}
				if(param.get("userId")!=null){
					WHERE("user_id=#{userId}");
				}
			}
		}.toString();
	}

	//根据ID查找
	public String selectById(Integer id){
		return new SQL(){
			{
				SELECT(columns);
				FROM(tableName);
				WHERE("id=#{id}");
			}
		}.toString();
	}

	//根据id删除
	public String deleteById(Map param){
		return new SQL(){
			{
				DELETE_FROM(tableName);
				if(param.get("activityId")!=null){
					WHERE("activity_id=#{activityId}");
				}
				if(param.get("userId")!=null){
					WHERE("user_id=#{userId}");
				}
			}
		}.toString();
	}

	//根据用户id查找
	public String getWatchList(@Param("userId") Integer userId, @Param("photoLive") Integer photoLive){
		return new SQL(){
			{
				SELECT("w.id,w.activity_id,w.user_id");
				FROM("watch_list w");
				LEFT_OUTER_JOIN("activity a on a.id = w.activity_id");
				WHERE("a.photo_live = #{photoLive}");
				WHERE("(w.user_id=#{userId} or w.user_id = (select bound_id from user where id = #{userId}))");
			}
		}.toString();
	}
}
