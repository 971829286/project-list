package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.WatchList;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import javax.ws.rs.DELETE;
import java.util.List;
import java.util.Map;

@Repository
public interface WatchListMapper extends IBaseMapper<WatchList>{
	@InsertProvider(type = WatchListMapperProvider.class,method ="save")
	public Integer save(WatchList watchList);
	
	@UpdateProvider(type = WatchListMapperProvider.class,method = "update")
	public Integer update(WatchList watchList);

	@SelectProvider(type = WatchListMapperProvider.class,method = "findAll")
	@Results({
			@Result(id = true, column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "activity_id",property = "activity",one = @One(
					select="cn.ourwill.tuwenzb.mapper.ActivityMapper.getById",
					fetchType= FetchType.EAGER
			))
	})
	public List<WatchList> findAll();

	@SelectProvider(type = WatchListMapperProvider.class,method = "selectById")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "activity_id",property = "activity",one = @One(
			select="cn.ourwill.tuwenzb.mapper.ActivityMapper.getById",
			fetchType= FetchType.EAGER
	))
	})
	public WatchList getById(Integer id);

	@DeleteProvider(type = WatchListMapperProvider.class,method = "deleteById")
	public Integer delete(Map param);

	//根据用户id查找
	@SelectProvider(type = WatchListMapperProvider.class,method = "getWatchList")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "activity_id",property = "activityId"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "activity_id",property = "activity",one = @One(
					select="cn.ourwill.tuwenzb.mapper.ActivityMapper.getById",
					fetchType= FetchType.EAGER
			))
	})
    List<WatchList> getWatchList(@Param("userId") Integer userId,@Param("photoLive") Integer photoLive);

	@Select("select count(0) from watch_list w left join activity a on a.id = w.activity_id where a.photo_live = #{photoLive} and (w.user_id=#{userId} or w.user_id = (select bound_id from user where id = #{userId}))")
	Integer selectCountByUserId(@Param("userId") Integer userId,@Param("photoLive") Integer photoLive);

	//按用户id和活动id查找
	@Select("select id as id,activity_id as activityId,user_id as userId from watch_list where activity_id = #{activityId} and user_id = #{userId}")
	List<WatchList> selectByActivityAndUser(@Param("activityId") Integer activityId,@Param("userId") Integer userId);

}
