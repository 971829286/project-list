package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.WatchListPeople;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class WatchListPeopleSqlProvider {

    private String tableName = "watch_list_people";
    private String columns = "id,watch_user_id,user_id,watch_date";

    //保存
    public String save(final WatchListPeople watchListPeople) {
        return new SQL() {
            {
                INSERT_INTO(tableName);
                if (watchListPeople.getWatchUserId() != null) {
                    VALUES("watch_user_id", "#{watchUserId}");
                }
                if (watchListPeople.getUserId() != null) {
                    VALUES("user_id", "#{userId}");
                }
                if (watchListPeople.getWatchDate() != null) {
                    VALUES("watch_date", "#{watchDate}");
                }
            }
        }.toString();
    }

    //修改
    public String update(final WatchListPeople watchListPeople) {
        return new SQL() {
            {
                UPDATE(tableName);
                if (watchListPeople.getWatchUserId() != null) {
                    SET("watch_user_id=#{watchUserId}");
                }
                if (watchListPeople.getUserId() != null) {
                    SET("user_id=#{userId}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //查找所有
    public String findAll() {
        return new SQL() {
            {
                SELECT(columns);
                FROM(tableName);
            }
        }.toString();
    }

    //根据属性查找（使用user参数）
    public String findByProperty(final WatchListPeople watchListPeople) {
        return new SQL() {
            {
                SELECT(columns);
                FROM(tableName);
                if (watchListPeople.getId() != null) {
                    WHERE("id=#{id}");
                }
                if (watchListPeople.getWatchUserId() != null) {
                    WHERE("watch_user_id=#{watchUserId}");
                }
                if (watchListPeople.getUserId() != null) {
                    WHERE("user_id=#{userId}");
                }

            }
        }.toString();
    }

    //根据属性查找(使用Map参数)
    public String selectByParmer(final Map<String, Object> param) {
        return new SQL() {
            {
                SELECT(columns);
                FROM(tableName);
                if (param.get("id") != null) {
                    WHERE("id=#{id}");
                }
                if (param.get("watchUserId") != null) {
                    WHERE("watch_user_id=#{watchUserId}");
                }
                if (param.get("userId") != null) {
                    WHERE("user_id=#{userId}");
                }
            }
        }.toString();
    }

    //根据ID查找
    public String selectById(Integer id) {
        return new SQL() {
            {
                SELECT(columns);
                FROM(tableName);
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //根据id删除
    public String deleteById(WatchListPeople watchListPeople) {
        return new SQL() {
            {
                DELETE_FROM(tableName);
                if (watchListPeople.getWatchUserId() != null) {
                    WHERE("watch_user_id=#{watchUserId}");
                }
                if (watchListPeople.getUserId() != null) {
                    WHERE("user_id=#{userId}");
                }
            }
        }.toString();
    }

    //根据用户id查找
    public String getWatchAll(@Param("id") Integer id) {
        return new SQL() {
            {
                SELECT("u.id,u.level,u.nickname");
                FROM("user u");
                WHERE("u.id = #{id}");
            }
        }.toString();
    }

    /**
     * 根据用户id查找 当前用户的关注人的信息
     *
     *
     * id,uuid,level,nickname,username,avatar,mob_phone,tel_phone,email,qq,company,address,version,info
     * @param id
     * @return
     */

    public String getWatchPeopleInfo(@Param("id") Integer id) {
        return new SQL() {
            {
                SELECT("u.id,u.uuid,u.level,u.nickname,u.username,u.avatar,u.info,personalized_signature");
                FROM("user u");
                WHERE("u.id  in (select watch_user_id from watch_list_people where user_id = #{id})");
            }
        }.toString();
    }

    /**
     * 根据用户id查找 当前用户的粉丝的信息
     *
     * @param id
     * @return
     */
    public String getFansInfo(@Param("id") Integer id) {
        return new SQL() {
            {
                SELECT("u.id,u.uuid,u.level,u.nickname,u.username,u.avatar,u.info,personalized_signature");
                FROM("user u");
                WHERE("u.id  in (select user_id from watch_list_people where watch_user_id = #{id})");
            }
        }.toString();
    }


    /**
     * 根据用户id查找 当前用户的粉丝的信息
     *
     * @param userId
     * @return
     */

    private String[] userInfo = "id,level,nickname,username,avatar,mob_phone,tel_phone,email,qq,company,address,version,info,personalized_signature".split(",");


    /**
     * 获取 需要查询的 用户的列的  case when
     */
    public String getNeededUserColumn() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < userInfo.length; i++) {
            str.append("(case \n" +
                    "\t\t\twhen u.id = #{userId} then u2." + userInfo[i] + "\n" +
                    "\t\t\telse u." + userInfo[i] + " end\n" +
                    "\t) as " + userInfo[i] + ","
            );
        }
        return str.toString();
    }


    public String getPeopleDynamicBase(@Param("userId") Integer userId) {
        return new SQL() {
            {
                SELECT(getNeededUserColumn() + " w.user_id,w.watch_user_id,w.watch_date");
                FROM("watch_list_people w");
                LEFT_OUTER_JOIN(" user u ON w.user_id = u.id");
                LEFT_OUTER_JOIN(" user u2 ON w.watch_user_id = u2.id");
                WHERE("w.user_id = #{userId} OR w.watch_user_id = #{userId}");
                GROUP_BY("w.watch_date desc ,w.user_id,w.watch_user_id ");
            }
        }.toString();
    }
}