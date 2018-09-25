package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.BannerHome;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerHomeMapper extends IBaseMapper<BannerHome>{
	String columns = "id,priority,pic,mobile_pic,title,link,c_time,c_user,u_time,u_user,activity_id";
	@InsertProvider(type = BannerHomeMapperProvider.class,method ="save")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int insertSelective(BannerHome bannerHome);
	
	@UpdateProvider(type = BannerHomeMapperProvider.class,method = "update")
	public int updateByPrimaryKeySelective(BannerHome bannerHome);

	@SelectProvider(type = BannerHomeMapperProvider.class,method = "findAll")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "priority",property = "priority"),
			@Result( column = "pic",property = "pic"),
			@Result( column = "mobile_pic",property = "mobilePic"),
			@Result( column = "title",property = "title"),
			@Result( column = "link",property = "link"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "c_user",property = "cUser"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "u_user",property = "uUser"),
			@Result( column = "activity_id",property = "activityId"),
	})
	public List<BannerHome> findAll();

	@SelectProvider(type = BannerHomeMapperProvider.class,method = "selectById")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "priority",property = "priority"),
			@Result( column = "pic",property = "pic"),
			@Result( column = "mobile_pic",property = "mobilePic"),
			@Result( column = "title",property = "title"),
			@Result( column = "link",property = "link"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "c_user",property = "cUser"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "u_user",property = "uUser"),
			@Result( column = "activity_id",property = "activityId")
	})
	public BannerHome selectByPrimaryKey(Integer id);

	@DeleteProvider(type = BannerHomeMapperProvider.class,method = "deleteById")
	public int deleteByPrimaryKey(Integer id);

	@Update("update banner_home set priority=#{priority} where id = #{id}")
	Integer updatePriorityById(@Param("id") Integer id, @Param("priority") Integer priority);


	@Select(
		"select "+columns+" from banner_home where id = #{id}"
	)
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "priority",property = "priority"),
			@Result( column = "pic",property = "pic"),
			@Result( column = "mobile_pic",property = "mobilePic"),
			@Result( column = "title",property = "title"),
			@Result( column = "link",property = "link"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "c_user",property = "cUser"),
			@Result( column = "u_time",property = "uTime"),
			@Result( column = "u_user",property = "uUser"),
			@Result( column = "activity_id",property = "activityId")
	})
	@Options(useGeneratedKeys = true,keyProperty = "id")
	BannerHome selectById(Integer id);
}
