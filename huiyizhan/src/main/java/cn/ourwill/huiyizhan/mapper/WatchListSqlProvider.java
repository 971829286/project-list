package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.WatchList;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class WatchListSqlProvider {
	private String tableName="watch_list";
	private String columns="id,activity_id,user_id,watch_date";
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
				if(watchList.getWatchDate()!=null){
					VALUES("watch_date","#{watchDate}");
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
				if(watchList.getWatchDate()!=null){
					SET("watch_date = #{watchDate}");
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
				if(watchList.getWatchDate()!=null){
					WHERE("watch_date=#{watchDate}");
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
				if(param.get("watchDate")!=null){
					WHERE("watch_date=#{watchDate}");
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
    public String deleteById(WatchList watchList){
        return new SQL(){
            {
                DELETE_FROM(tableName);
                if(watchList.getActivityId()!=null){
                    WHERE("activity_id=#{activityId}");
                }
                if(watchList.getUserId()!=null){
                    WHERE("user_id=#{userId}");
                }
            }
        }.toString();
    }

	//根据用户id查找
	public String getWatchList(@Param("userId") Integer userId){
		return new SQL(){
			{
				SELECT("w.id,w.activity_id,w.user_id");
				FROM("watch_list w");
				LEFT_OUTER_JOIN("activity a on a.id = w.activity_id");
				WHERE("w.user_id = #{userId}");
			}
		}.toString();
	}

	/**
	 * 根据用户id查找 当前用户的粉丝的信息
	 *
	 * @param userId
	 * @return
	 */

	private String activityInfo =   " a1.activity_title, a1.activity_type, a1.start_time, a1.end_time, a1.activity_address, " +
            "a1.activity_banner_mobile, a1.activity_banner, a1.is_open, a1.is_online, a1.schedule_status, "+
					"a1.guest_status, a1.partner_status, a1.contact_status, a1.c_time, a1.u_time, a1.u_id, a1.activity_description ,a1.issue_status,a1.banner_type ,";

	private String userInfo = "u.level,u.nickname,u.username,u.avatar,u.mob_phone,u.tel_phone,u.email,u.qq,u.company,u.address,u.version,u.info,u.personalized_signature";

	public String getActivityDynamic(@Param("userId") Integer userId) {
		return new SQL() {
			{
				SELECT(activityInfo + userInfo + ", w.user_id,w.activity_id,w.watch_date");
				FROM("watch_list w");
				LEFT_OUTER_JOIN(" activity a1 ON     a1.id  =   w.activity_id ");
				LEFT_OUTER_JOIN("activity a2 ON  a2.id = w.activity_id  ");
				LEFT_OUTER_JOIN("user u ON u.id  = w.user_id");
				WHERE("w.activity_id  in ( select id from activity  WHERE user_id = #{userId} ) " + "OR  w.user_id = #{userId} ");
				GROUP_BY("w.watch_date desc ,w.user_id,w.activity_id ");
			}
		}.toString();
	}
}
