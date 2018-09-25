package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.Contribute;
import cn.ourwill.tuwenzb.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *@Author zhaoqing
 * @Time 2018/6/20 14:14
 * @Version1.0
 */

@Repository
public interface ContributeMapper extends IBaseMapper<Contribute> {

    String columns ="id,user_id,f_user_id,name,sex,mob_phone,email,address,work_title,pic_url,check_status,sub_time,update_time,check_time,feedback";

    @InsertProvider(type = ContributeMapperProvider.class ,method = "save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer save(Contribute contribute);

    @UpdateProvider(type = ContributeMapperProvider.class ,method = "update")
    Integer update(Contribute contribute);

    @SelectProvider(type = ContributeMapperProvider.class,method = "selectById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "f_user_id", property = "fUserId"),
            @Result(column = "name", property = "name"),
            @Result(column = "mob_phone", property = "mobPhone"),
            @Result(column = "email", property = "email"),
            @Result(column = "address", property = "address"),
            @Result(column = "work_title", property = "workTitle"),
            @Result(column = "pic_url", property = "picUrl"),
            @Result(column = "check_status", property = "checkStatus"),
            @Result(column = "sub_time",property = "subTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "check_time",property = "checkTime"),
            @Result(column = "feedback",property = "feedback")
    })
    public Contribute getById(Integer id);

    @Select("select "+columns+" from  contribute where  user_id = #{userId}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "f_user_id", property = "fUserId"),
            @Result(column = "name", property = "name"),
            @Result(column = "mob_phone", property = "mobPhone"),
            @Result(column = "email", property = "email"),
            @Result(column = "address", property = "address"),
            @Result(column = "work_title", property = "workTitle"),
            @Result(column = "pic_url", property = "picUrl"),
            @Result(column = "check_status", property = "checkStatus"),
            @Result(column = "sub_time",property = "subTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "check_time",property = "checkTime"),
            @Result(column = "feedback",property = "feedback")
    })
    List<Contribute> selectByUserId(Integer userId);

    @Select("select "+columns+" from  contribute where  user_id = #{userId} limit 1")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "f_user_id", property = "fUserId"),
            @Result(column = "name", property = "name"),
            @Result(column = "mob_phone", property = "mobPhone"),
            @Result(column = "email", property = "email"),
            @Result(column = "address", property = "address"),
            @Result(column = "work_title", property = "workTitle"),
            @Result(column = "pic_url", property = "picUrl"),
            @Result(column = "check_status", property = "checkStatus"),
            @Result(column = "sub_time",property = "subTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "check_time",property = "checkTime"),
            @Result(column = "feedback",property = "feedback")
    })
    Contribute selectOneByUserId(@Param("userId") Integer userId);

    @SelectProvider(type = ContributeMapperProvider.class, method = "selectByParam")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "f_user_id", property = "fUserId"),
            @Result(column = "name", property = "name"),
            @Result(column = "mob_phone", property = "mobPhone"),
            @Result(column = "email", property = "email"),
            @Result(column = "address", property = "address"),
            @Result(column = "work_title", property = "workTitle"),
            @Result(column = "pic_url", property = "picUrl"),
            @Result(column = "check_status", property = "checkStatus"),
            @Result(column = "sub_time",property = "subTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "check_time",property = "checkTime"),
            @Result(column = "feedback",property = "feedback")
    })
    List<Contribute> getContributeList(Map map);

    @Select("select "+columns+" from  contribute")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "f_user_id", property = "fUserId"),
            @Result(column = "name", property = "name"),
            @Result(column = "mob_phone", property = "mobPhone"),
            @Result(column = "email", property = "email"),
            @Result(column = "address", property = "address"),
            @Result(column = "work_title", property = "workTitle"),
            @Result(column = "pic_url", property = "picUrl"),
            @Result(column = "check_status", property = "checkStatus"),
            @Result(column = "sub_time",property = "subTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "check_time",property = "checkTime"),
            @Result(column = "feedback",property = "feedback")
    })
    List<Contribute> findAll();

    @Update("update contribute set check_status = #{status},check_time = #{checkTime},feedback = #{feedback} where id = #{id}")
    Integer updateStatus(@Param("id") Integer id, @Param("status") Integer status, @Param("feedback") String feedback, @Param("checkTime")Date checkTime);

    @SelectProvider(type = ContributeMapperProvider.class, method = "selectUserByParamNew")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "f_user_id", property = "fUserId"),
            @Result(column = "name", property = "name"),
            @Result(column = "mob_phone", property = "mobPhone"),
            @Result(column = "email", property = "email"),
            @Result(column = "address", property = "address"),
            @Result(column = "work_title", property = "workTitle"),
            @Result(column = "pic_url", property = "picUrl"),
            @Result(column = "check_status", property = "checkStatus"),
            @Result(column = "sub_time",property = "subTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "check_time",property = "checkTime"),
            @Result(column = "feedback",property = "feedback")
    })
    List<Contribute> getContributeUserList(Map map);

    @SelectProvider(type = ContributeMapperProvider.class, method = "selectUserByParamNew")
    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "f_user_id", property = "fUserId"),
            @Result(column = "name", property = "name"),
            @Result(column = "mob_phone", property = "mobPhone"),
            @Result(column = "email", property = "email"),
            @Result(column = "address", property = "address"),
            @Result(column = "sub_time",property = "subTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "check_time",property = "checkTime"),
            @Result(column = "feedback",property = "feedback")
    })
    List<Contribute> getContributeUserListAll(Map map);

    @Select("select id,user_id,f_user_id,work_title,pic_url,check_status from contribute where user_id = #{userId}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "f_user_id", property = "fUserId"),
            @Result(column = "work_title", property = "workTitle"),
            @Result(column = "pic_url", property = "picUrl"),
            @Result(column = "check_status", property = "checkStatus")
    })
    List<Contribute> getUrlByUserId(@Param("userId") Integer userId);

    @Select("select id,user_id,f_user_id,work_title,pic_url,check_status from contribute where user_id = #{userId} and check_status = #{status}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "f_user_id", property = "fUserId"),
            @Result(column = "work_title", property = "workTitle"),
            @Result(column = "pic_url", property = "picUrl"),
            @Result(column = "check_status", property = "checkStatus")
    })
    List<Contribute> getUrlByUserIdStatus(@Param("userId") Integer userId,@Param("status") Integer status);

    @Select("select u.userFrom_type,u.username,u.nickname,u.wechat_num,u.bound_id,u.level from contribute c inner join user u on c.user_id = u.id where c.id = #{id}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "userFrom_type",property = "userfromType"),
            @Result( column = "username",property = "username"),
            @Result( column = "nickname",property = "nickname"),
            @Result( column = "wechat_num",property = "wechatNum"),
            @Result( column = "bound_id",property = "boundId"),
            @Result( column = "level",property = "level")
    })
    User getUserByContributeId(@Param("id") Integer id);


    @Update({"<script>" +
            "update  contribute set check_status = #{checkStatus},feedback = #{feedback},check_time=#{checkTime}",
            " where id in " +
                    "<foreach collection=\"list\" index=\"index\" item=\"item\"   open=\"(\"  separator=\",\"  close=\")\">  \n" +
                    "#{item} \n" +
                    "</foreach> " +
                    "</script>"})
    Integer batchCheck(@Param("checkStatus") Integer checkStatus,@Param("list") List<Integer> list,@Param("feedback") String feedback,@Param("checkTime")Date checkTime);

    @Select("select count(id) from contribute where f_user_id = #{userId}")
    Integer getPromotionNum(@Param("userId") Integer userId);
}
