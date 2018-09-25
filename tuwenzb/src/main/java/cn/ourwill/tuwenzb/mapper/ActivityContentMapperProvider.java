package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityContent;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.Map;

public class ActivityContentMapperProvider {
	//保存
	public String save(final ActivityContent activityContent){
		return new SQL(){
			{
				INSERT_INTO("activity_content");
				if(activityContent.getActivityId()!=null){
					VALUES("activity_id","#{activityId}");
				}
				if(activityContent.getContent()!=null){
					VALUES("content","#{content}");
				}
				if(activityContent.getCTime()!=null){
					VALUES("c_time","#{cTime}");
				}
				if(activityContent.getUTime()!=null){
					VALUES("u_time","#{uTime}");
				}
				if(activityContent.getImg()!=null){
					VALUES("img","#{img}");
				}
				if(activityContent.getVideo()!=null){
					VALUES("video","#{video}");
				}
				if(activityContent.getVideoLink()!=null){
					VALUES("video_link","#{videoLink}");
				}
				if(activityContent.getStickSign()!=null){
					VALUES("stick_sign","#{stickSign}");
				}
			}
		}.toString();
	}
	//修改
	public String update(final ActivityContent activityContent){
		return new SQL(){
			{
				UPDATE("activity_content");
				if(activityContent.getActivityId()!=null){
					SET("activity_id=#{activityId}");
				}
				if(activityContent.getContent()!=null){
					SET("content=#{content}");
				}
				if(activityContent.getCTime()!=null){
					SET("c_time=#{cTime}");
				}
				if(activityContent.getUTime()!=null){
					SET("u_time=#{uTime}");
				}
				if(activityContent.getImg()!=null){
					SET("img=#{img}");
				}
				if(activityContent.getVideo()!=null){
					SET("video=#{video}");
				}
				if(activityContent.getVideoLink()!=null){
					SET("video_link=#{videoLink}");
				}
				if(activityContent.getStickSign()!=null){
					SET("stick_sign=#{stickSign}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}
	//查找所有
	public String findAll(){
		return new SQL(){
			{
				SELECT("id,activity_id,content,c_time,u_time,img,video,video_link,stick_sign");
				FROM("activity_content");
			}
		}.toString();
	}

	//根据属性查找（使用user参数）
	public String findByProperty(final ActivityContent activityContent){
		return new SQL(){
			{
				SELECT("id,activity_id,content,c_time,u_time,img,video,video_link,stick_sign");
				FROM("activity_content");
				if(activityContent.getId()!=null){
					WHERE("id=#{id}");
				}
				if(activityContent.getActivityId()!=null){
					WHERE("activity_id=#{activityId}");
				}
				if(activityContent.getContent()!=null){
					WHERE("content=#{content}");
				}
				if(activityContent.getCTime()!=null){
					WHERE("c_time=#{cTime}");
				}
				if(activityContent.getUTime()!=null){
					WHERE("u_time=#{uTime}");
				}
				if(activityContent.getImg()!=null){
					WHERE("img=#{img}");
				}
				if(activityContent.getVideo()!=null){
					WHERE("video=#{video}");
				}
				if(activityContent.getVideoLink()!=null){
					WHERE("video_link=#{videoLink}");
				}
				if(activityContent.getStickSign()!=null){
					WHERE("stick_sign=#{stickSign}");
				}

			}
		}.toString();
	}

	//根据属性查找(使用Map参数)
	public String selectByParmer(final Map<String,Object> param){
		return new SQL(){
			{
				SELECT("id,activity_id,content,c_time,u_time,img,video,video_link,stick_sign");
				FROM("activity_content");
				if(param.get("id")!=null){
					WHERE("id=#{id}");
				}
				if(param.get("activityId")!=null){
					WHERE("activity_id=#{activityId}");
				}
				if(param.get("content")!=null){
					WHERE("content=#{content}");
				}
				if(param.get("cTime")!=null){
					WHERE("c_time=#{cTime}");
				}
				if(param.get("uTime")!=null){
					WHERE("u_time=#{uTime}");
				}
				if(param.get("img")!=null){
					WHERE("img=#{img}");
				}
				if(param.get("video")!=null){
					WHERE("video=#{video}");
				}
				if(param.get("videoLink")!=null){
					WHERE("video_link=#{videoLink}");
				}
				if(param.get("stickSign")!=null){
					WHERE("stick_sign=#{stickSign}");
				}

			}
		}.toString();
	}

	//根据ID查找
	public String selectById(Integer id){
		return new SQL(){
			{
				SELECT("id,activity_id,content,c_time,u_time,img,video,video_link,stick_sign");
				FROM("activity_content");
				WHERE("id=#{id}");
			}
		}.toString();
	}

	//根据id删除
	public String deleteById(Map param){
		return new SQL(){
			{
				DELETE_FROM("activity_content");
				WHERE("id=#{id}");
			}
		}.toString();
	}
	//获取最新直播内容
	public String  getRecentActivity(@Param("activityId") Integer activityId, @Param("date") String date, @Param("timeOrder") String timeOrder){
		return new SQL(){
			{
				SELECT("id,activity_id,content,c_time,u_time,img,video,video_link,stick_sign");
				FROM("activity_content");
				WHERE("activity_id=#{activityId}");
				if(StringUtils.isNotEmpty(date)){
					WHERE("DATEDIFF(#{date},c_time)= 0");
				}
				ORDER_BY(" stick_sign DESC");
				if(timeOrder!=null&&!"".equals(timeOrder)){
					if (timeOrder.equals("ASC"))
						ORDER_BY("c_time ASC");
					else if(timeOrder.equals("DESC"))
						ORDER_BY("c_time DESC");
				}
			}
		}.toString();
	};

	//获取最新直播内容没有标签
	public String  getRecentActivityNoTags(@Param("activityId") Integer activityId){
		return new SQL(){
			{
				SELECT("id,activity_id,fnStripTags(content) content ,c_time,u_time,img,video,video_link,stick_sign");
				FROM("activity_content");
				WHERE("activity_id=#{activityId}");
				ORDER_BY(" stick_sign DESC");
				ORDER_BY("c_time DESC");
			}
		}.toString();
	};

	public String getRecentConcentByTime(@Param("activityId") Integer activityId, @Param("date") Date date, @Param("timeOrder") Integer timeOrder,@Param("contentNum") Integer contentNum){
		return new SQL(){
			{
				SELECT("id,activity_id,content,c_time,u_time,img,video,video_link,stick_sign");
				FROM("activity_content");
				WHERE("activity_id=#{activityId}");
				if(date != null){
                    if (timeOrder == 0){
                        WHERE("stick_sign = 0");
                        WHERE("c_time < #{date}");
                        ORDER_BY(" stick_sign DESC");
                        ORDER_BY(" c_time DESC");
                    }else {
                        WHERE("stick_sign = 0");
                        WHERE("c_time > #{date}");
                        ORDER_BY(" stick_sign DESC");
                        ORDER_BY(" c_time ASC");
                    }
                }else {
                    if (timeOrder == 0){
                        ORDER_BY(" stick_sign DESC");
                        ORDER_BY(" c_time DESC");
                    }else {
                        ORDER_BY(" stick_sign DESC");
                        ORDER_BY(" c_time ASC");
                    }

                }

			}
		}+" limit "+contentNum.toString();
	};


}
