package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.BannerHome;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class BannerHomeMapperProvider {
	//保存
	public String save(final BannerHome bannerHome){
		return new SQL(){
			{
				INSERT_INTO("banner_home");
				if(bannerHome.getPic()!=null){
					VALUES("pic","#{pic}");
				}
				if(bannerHome.getMobilePic()!=null){
					VALUES("mobile_pic","#{mobilePic}");
				}
				if(bannerHome.getPriority()!=null){
					VALUES("priority","#{priority}");
				}
				if(bannerHome.getTitle()!=null){
					VALUES("title","#{title}");
				}
				if(bannerHome.getLink()!=null){
					VALUES("link","#{link}");
				}
				if(bannerHome.getCTime()!=null){
					VALUES("c_time","#{cTime}");
				}
				if(bannerHome.getCUser()!=null){
					VALUES("c_user","#{cUser}");
				}
				if(bannerHome.getUTime()!=null){
					VALUES("u_time","#{uTime}");
				}
				if(bannerHome.getUUser()!=null){
					VALUES("u_user","#{uUser}");
				}
				if(bannerHome.getActivityId()!=null){
					VALUES("activity_id","#{activityId}");
				}

			}
		}.toString();
	}
	//修改
	public String update(final BannerHome bannerHome){
		return new SQL(){
			{
				UPDATE("banner_home");
				if(bannerHome.getPriority()!=null){
					SET("priority=#{priority}");
				}
				if(bannerHome.getPic()!=null){
					SET("pic=#{pic}");
				}
				if(bannerHome.getMobilePic()!=null){
					SET("mobile_pic=#{mobilePic}");
				}
				if(bannerHome.getTitle()!=null){
					SET("title=#{title}");
				}
				if(bannerHome.getLink()!=null){
					SET("link=#{link}");
				}
				if(bannerHome.getCTime()!=null){
					SET("c_time=#{cTime}");
				}
				if(bannerHome.getCUser()!=null){
					SET("c_user=#{cUser}");
				}
				if(bannerHome.getUTime()!=null){
					SET("u_time=#{uTime}");
				}
				if(bannerHome.getUUser()!=null){
					SET("u_user=#{uUser}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}
	//查找所有
	public String findAll(){
		return new SQL(){
			{
				SELECT("id,priority,pic,mobile_pic,title,link,c_time,c_user,u_time,u_user,activity_id");
				FROM("banner_home");
				ORDER_BY("priority desc");
			}
		}.toString();
	}

	//根据属性查找（使用user参数）
	public String findByProperty(final BannerHome bannerHome){
		return new SQL(){
			{
				SELECT("id,priority,pic,mobile_pic,title,link,c_time,c_user,u_time,u_user");
				FROM("banner_home");
				if(bannerHome.getId()!=null){
					WHERE("id=#{id}");
				}
				if(bannerHome.getPriority()!=null){
					WHERE("priority=#{priority}");
				}
				if(bannerHome.getPic()!=null){
					WHERE("pic=#{pic}");
				}
				if(bannerHome.getTitle()!=null){
					WHERE("title=#{title}");
				}
				if(bannerHome.getLink()!=null){
					WHERE("link=#{link}");
				}
				if(bannerHome.getCTime()!=null){
					WHERE("c_time=#{cTime}");
				}
				if(bannerHome.getCUser()!=null){
					WHERE("c_user=#{cUser}");
				}
				if(bannerHome.getUTime()!=null){
					WHERE("u_time=#{uTime}");
				}
				if(bannerHome.getUUser()!=null){
					WHERE("u_user=#{uUser}");
				}

			}
		}.toString();
	}

	//根据属性查找(使用Map参数)
	public String selectByParmer(final Map<String,Object> param){
		return new SQL(){
			{
				SELECT("id,pic,mobile_pic,title,link,c_time,c_user,u_time,u_user");
				FROM("banner_home");
				if(param.get("id")!=null){
					WHERE("id=#{id}");
				}
				if(param.get("priority")!=null){
					WHERE("priority=#{priority}");
				}
				if(param.get("pic")!=null){
					WHERE("pic=#{pic}");
				}
				if(param.get("title")!=null){
					WHERE("title=#{title}");
				}
				if(param.get("link")!=null){
					WHERE("link=#{link}");
				}
				if(param.get("cTime")!=null){
					WHERE("c_time=#{cTime}");
				}
				if(param.get("cUser")!=null){
					WHERE("c_user=#{cUser}");
				}
				if(param.get("uTime")!=null){
					WHERE("u_time=#{uTime}");
				}
				if(param.get("uUser")!=null){
					WHERE("u_user=#{uUser}");
				}
				if(param.get("activityId")!=null){
					WHERE("activity_id=#{activityId}");
				}
			}
		}.toString();
	}

	//根据ID查找
	public String selectById(Integer id){
		return new SQL(){
			{
				SELECT("id,priority,pic,mobile_pic,title,link,c_time,c_user,u_time,u_user,activity_id");
				FROM("banner_home");
				WHERE("id=#{id}");
			}
		}.toString();
	}

	//根据id删除
	public String deleteById(Integer id){
		return new SQL(){
			{
				DELETE_FROM("banner_home");
				WHERE("id=#{id}");
			}
		}.toString();
	}
}
