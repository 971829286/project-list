package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.BannerHome;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BannerHomeMapper extends IBaseMapper<BannerHome>{
	@InsertProvider(type = BannerHomeMapperProvider.class,method ="save")
	public Integer save(BannerHome bannerHome);
	
	@UpdateProvider(type = BannerHomeMapperProvider.class,method = "update")
	public Integer update(BannerHome bannerHome);

	@SelectProvider(type = BannerHomeMapperProvider.class,method = "findAll")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "priority",property = "priority"),
			@Result( column = "pic",property = "pic"),
			@Result( column = "title",property = "title"),
			@Result( column = "link",property = "link"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "c_user",property = "cUser"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "u_user",property = "uUser"),
			@Result( column = "photo_live",property = "photoLive")
	})
	public List<BannerHome> findAll(@Param("photoLive") Integer photoLive);

	@SelectProvider(type = BannerHomeMapperProvider.class,method = "selectById")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "priority",property = "priority"),
			@Result( column = "pic",property = "pic"),
			@Result( column = "title",property = "title"),
			@Result( column = "link",property = "link"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "c_user",property = "cUser"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "u_user",property = "uUser"),
			@Result( column = "photo_live",property = "photoLive")
	})
	public BannerHome getById(Integer id);

	@DeleteProvider(type = BannerHomeMapperProvider.class,method = "deleteById")
	public Integer delete(Integer id);

	@Update("update banner_home set priority=#{priority} where id = #{id}")
	Integer updatePriorityById(@Param("id")Integer id,@Param("priority") Integer priority);
}
