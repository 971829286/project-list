package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface ActivityMapper extends IBaseMapper<Activity> {
    @InsertProvider(type = ActivityMapperProvider.class, method = "save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer save(Activity activity);

    @UpdateProvider(type = ActivityMapperProvider.class, method = "update")
    public Integer update(Activity activity);

    @SelectProvider(type = ActivityMapperProvider.class, method = "findAll")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "title", property = "title"),
            @Result(column = "introduction", property = "introduction"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "site", property = "site"),
            @Result(column = "organizer", property = "organizer"),
            @Result(column = "publisher", property = "publisher"),
            @Result(column = "generalize_status", property = "generalizeStatus"),
            @Result(column = "generalize_name", property = "generalizeName"),
            @Result(column = "generalize_link", property = "generalizeLink"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "banner", property = "banner"),
            @Result(column = "watermark", property = "watermark"),
            @Result(column = "water_complete", property = "waterComplete"),
            @Result(column = "water_config", property = "waterConfig"),
            @Result(column = "check_type", property = "checkType"),
            @Result(column = "status", property = "status"),
            @Result(column = "is_impower", property = "isImpower"),
            @Result(column = "qr_code", property = "qrCode"),
            @Result(column = "screen", property = "screen"),
            @Result(column = "docx", property = "docx"),
            @Result(column = "classical", property = "classical"),
            @Result(column = "model", property = "model"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "u_time", property = "uTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "type_name", property = "typeName"),
            @Result(column = "icon_switch", property = "iconSwitch"),
            @Result(column = "is_auto_publish", property = "isAutoPublish"),
            @Result(column = "switch_password", property = "switchPassword"),
            @Result(column = "password", property = "password"),
            @Result(column = "photo_live", property = "photoLive"),
            @Result(column = "face_set_token", property = "faceSetToken"),
            @Result(column = "is_open_face_search", property = "isOpenFaceSearch"),
            @Result(column = "iframe", property = "iframe")
//			@Result( column = "id",property = "activityStatistics",
//			one = @One(
//					select="cn.ourwill.tuwenzb.mapper.ActivityStatisticsMapper.findByActivityId",
//					fetchType= FetchType.EAGER
//			)
//		)
    })
    public List<Activity> findAll();

    @SelectProvider(type = ActivityMapperProvider.class, method = "selectById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "title", property = "title"),
            @Result(column = "introduction", property = "introduction"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "site", property = "site"),
            @Result(column = "organizer", property = "organizer"),
            @Result(column = "publisher", property = "publisher"),
            @Result(column = "generalize_status", property = "generalizeStatus"),
            @Result(column = "generalize_name", property = "generalizeName"),
            @Result(column = "generalize_link", property = "generalizeLink"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "banner", property = "banner"),
            @Result(column = "watermark", property = "watermark"),
            @Result(column = "water_complete", property = "waterComplete"),
            @Result(column = "water_config", property = "waterConfig"),
            @Result(column = "check_type", property = "checkType"),
            @Result(column = "status", property = "status"),
            @Result(column = "is_impower", property = "isImpower"),
            @Result(column = "qr_code", property = "qrCode"),
            @Result(column = "screen", property = "screen"),
            @Result(column = "docx", property = "docx"),
            @Result(column = "classical", property = "classical"),
            @Result(column = "model", property = "model"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "u_time", property = "uTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "icon_switch", property = "iconSwitch"),
            @Result(column = "is_auto_publish", property = "isAutoPublish"),
            @Result(column = "switch_password", property = "switchPassword"),
            @Result(column = "password", property = "password"),
            @Result(column = "photo_live", property = "photoLive"),
            @Result(column = "face_set_token", property = "faceSetToken"),
            @Result(column = "is_open_face_search", property = "isOpenFaceSearch"),
            @Result(column = "iframe", property = "iframe"),
            @Result(column = "expect_like_num", property = "expectLikeNum"),
            @Result(column = "expect_participate_num", property = "expectParticipateNum")
    })
    public Activity getById(Integer id);

    @SelectProvider(type = ActivityMapperProvider.class, method = "selectByParmer")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "title", property = "title"),
            @Result(column = "introduction", property = "introduction"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "site", property = "site"),
            @Result(column = "organizer", property = "organizer"),
            @Result(column = "publisher", property = "publisher"),
            @Result(column = "generalize_status", property = "generalizeStatus"),
            @Result(column = "generalize_name", property = "generalizeName"),
            @Result(column = "generalize_link", property = "generalizeLink"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "banner", property = "banner"),
            @Result(column = "watermark", property = "watermark"),
            @Result(column = "water_complete", property = "waterComplete"),
            @Result(column = "water_config", property = "waterConfig"),
            @Result(column = "check_type", property = "checkType"),
            @Result(column = "status", property = "status"),
            @Result(column = "is_impower", property = "isImpower"),
            @Result(column = "qr_code", property = "qrCode"),
            @Result(column = "screen", property = "screen"),
            @Result(column = "docx", property = "docx"),
            @Result(column = "classical", property = "classical"),
            @Result(column = "model", property = "model"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "u_time", property = "uTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "type_name", property = "typeName"),
            @Result(column = "statuss", property = "statuss"),
            @Result(column = "icon_switch", property = "iconSwitch"),
            @Result(column = "is_auto_publish", property = "isAutoPublish"),
            @Result(column = "switch_password", property = "switchPassword"),
            @Result(column = "password", property = "password"),
            @Result(column = "photo_live", property = "photoLive"),
            @Result(column = "face_set_token", property = "faceSetToken"),
            @Result(column = "is_open_face_search", property = "isOpenFaceSearch"),
            @Result(column = "iframe", property = "iframe")
    })
    public List<Activity> selectByParam(Map map);

    @DeleteProvider(type = ActivityMapperProvider.class, method = "deleteById")
    public Integer delete(Map param);

    //根据用户id查找活动
    @SelectProvider(type = ActivityMapperProvider.class, method = "selectByUserId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "title", property = "title"),
            @Result(column = "introduction", property = "introduction"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "site", property = "site"),
            @Result(column = "organizer", property = "organizer"),
            @Result(column = "publisher", property = "publisher"),
            @Result(column = "generalize_status", property = "generalizeStatus"),
            @Result(column = "generalize_name", property = "generalizeName"),
            @Result(column = "generalize_link", property = "generalizeLink"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "banner", property = "banner"),
            @Result(column = "watermark", property = "watermark"),
            @Result(column = "water_complete", property = "waterComplete"),
            @Result(column = "water_config", property = "waterConfig"),
            @Result(column = "check_type", property = "checkType"),
            @Result(column = "status", property = "status"),
            @Result(column = "is_impower", property = "isImpower"),
            @Result(column = "qr_code", property = "qrCode"),
            @Result(column = "screen", property = "screen"),
            @Result(column = "docx", property = "docx"),
            @Result(column = "classical", property = "classical"),
            @Result(column = "model", property = "model"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "u_time", property = "uTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "icon_switch", property = "iconSwitch"),
            @Result(column = "is_auto_publish", property = "isAutoPublish"),
            @Result(column = "switch_password", property = "switchPassword"),
            @Result(column = "password", property = "password"),
            @Result(column = "photo_live", property = "photoLive"),
            @Result(column = "iframe", property = "iframe"),
            @Result(column = "face_set_token", property = "faceSetToken"),
            @Result(column = "is_open_face_search", property = "isOpenFaceSearch")
//			@Result( column = "id",property = "activityStatistics",
//					one = @One(
//							select="cn.ourwill.tuwenzb.mapper.ActivityStatisticsMapper.findByActivityId",
//							fetchType= FetchType.EAGER
//					)
//			)
    })
    public List<Activity> selectByUserId(@Param("userId") Integer userId, @Param("photoLive") Integer photoLive);


    @Select("select count(0) from activity where user_id=#{userId} and photo_live = #{photoLive} and status <> 3")
    public Integer selectCountByUserId(@Param("userId") Integer userId, @Param("photoLive") Integer photoLive);

    //更新状态
    @Update("update activity a set a.status=#{status} where a.id=#{activityId} and a.status in (0,1)")
    public Integer updateStatus(@Param("status") Integer status, @Param("activityId") Integer activityId);

    //所有的列名
    public String columns = "id,user_id,priority,title,introduction," +
            "start_time,end_time,site,organizer,publisher,generalize_status,generalize_name,generalize_link," +
            "phone,email,banner,watermark,water_complete,water_config,check_type,status,is_impower,qr_code,model," +
            "c_time,u_time,type,show_start_time,show_end_time,icon_switch,switch_password,password,photo_live,face_set_token,is_open_face_search,iframe";

    @Select("select activity_union.* from (select " + columns + " from activity where status = #{status} union all select " + columns + " from activity_old where status = #{status}) as activity_union")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "title", property = "title"),
            @Result(column = "introduction", property = "introduction"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "site", property = "site"),
            @Result(column = "organizer", property = "organizer"),
            @Result(column = "publisher", property = "publisher"),
            @Result(column = "generalize_status", property = "generalizeStatus"),
            @Result(column = "generalize_name", property = "generalizeName"),
            @Result(column = "generalize_link", property = "generalizeLink"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "banner", property = "banner"),
            @Result(column = "watermark", property = "watermark"),
            @Result(column = "water_complete", property = "waterComplete"),
            @Result(column = "water_config", property = "waterConfig"),
            @Result(column = "check_type", property = "checkType"),
            @Result(column = "status", property = "status"),
            @Result(column = "is_impower", property = "isImpower"),
            @Result(column = "qr_code", property = "qrCode"),
            @Result(column = "model", property = "model"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "u_time", property = "uTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "icon_switch", property = "iconSwitch"),
            @Result(column = "switch_password", property = "switchPassword"),
            @Result(column = "password", property = "password"),
            @Result(column = "photo_live", property = "photoLive"),
            @Result(column = "face_set_token", property = "faceSetToken"),
            @Result(column = "is_open_face_search", property = "isOpenFaceSearch"),
            @Result(column = "iframe", property = "iframe")
    })
    List<Activity> selectWithOld(Map param);

    @Select("select " + columns + " from activity_old where id = #{id}")
    Activity getByIdWithOld(Integer id);

//	@Select("select a.id from activity a \n" +
//			"left join user u on u.id = a.user_id\n" +
//			"where u.userFrom_type=0 and a.`status` = 4")
//	List<Integer> getDeleteIds();

    @Select("SELECT activity_id FROM activity_statistics where participants_num is null and like_num is null and comment_num is null")
    List<Integer> getDeleteIds();


    @Select("SELECT * FROM activity_type")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "type", property = "type"),
            @Result(column = "type_name", property = "typeName")
    })
    List<ActivityType> getAllType();

    @Select("SELECT DISTINCT(DATE_FORMAT(a.c_time,'%Y-%m-%d')) from activity_content a where a.activity_id = #{activityId}")
    List<String> getAllDate(Integer activityId);

    @Select("select a.id,a.user_id,a.priority,a.title,a.introduction, " +
            "a.start_time,a.end_time,a.site,a.organizer,a.publisher,a.generalize_status, " +
            "a.generalize_name,a.generalize_link,a.phone,a.email,a.banner,a.watermark,a.water_complete,water_config,a.check_type,a.status, " +
            "a.is_impower,a.qr_code,a.screen,a.docx,a.classical,a.model,a.c_time,a.u_time,a.type,a.show_start_time,a.show_end_time," +
            "t.type_name,a.icon_switch,a.is_auto_publish,a.switch_password,a.password,a.photo_live,a.face_set_token,a.is_open_face_search,a.iframe " +
            "FROM activity a " +
            "left join activity_type t on a.type = t.type " +
            "where a.status = 1 and photo_live = 1 " +
            "and a.start_time>NOW()")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "title", property = "title"),
            @Result(column = "introduction", property = "introduction"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "site", property = "site"),
            @Result(column = "organizer", property = "organizer"),
            @Result(column = "publisher", property = "publisher"),
            @Result(column = "generalize_status", property = "generalizeStatus"),
            @Result(column = "generalize_name", property = "generalizeName"),
            @Result(column = "generalize_link", property = "generalizeLink"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "banner", property = "banner"),
            @Result(column = "watermark", property = "watermark"),
            @Result(column = "water_complete", property = "waterComplete"),
            @Result(column = "water_config", property = "waterConfig"),
            @Result(column = "check_type", property = "checkType"),
            @Result(column = "status", property = "status"),
            @Result(column = "is_impower", property = "isImpower"),
            @Result(column = "qr_code", property = "qrCode"),
            @Result(column = "screen", property = "screen"),
            @Result(column = "docx", property = "docx"),
            @Result(column = "classical", property = "classical"),
            @Result(column = "model", property = "model"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "u_time", property = "uTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "type_name", property = "typeName"),
            @Result(column = "statuss", property = "statuss"),
            @Result(column = "icon_switch", property = "iconSwitch"),
            @Result(column = "is_auto_publish", property = "isAutoPublish"),
            @Result(column = "switch_password", property = "switchPassword"),
            @Result(column = "password", property = "password"),
            @Result(column = "photo_live", property = "photoLive"),
            @Result(column = "face_set_token", property = "faceSetToken"),
            @Result(column = "is_open_face_search", property = "isOpenFaceSearch"),
            @Result(column = "iframe", property = "iframe")
    })
    List<Activity> selectAdvance();

    @Select("SELECT a.id,a.user_id,a.priority,a.title,a.introduction, " +
            "a.start_time,a.end_time,a.site,a.organizer,a.publisher,a.generalize_status, " +
            "a.generalize_name,a.generalize_link,a.phone,a.email,a.banner,a.watermark,water_config,a.water_complete,a.check_type,a.status, " +
            "a.is_impower,a.qr_code,a.screen,a.docx,a.classical,a.model,a.c_time,a.u_time,a.type,a.show_start_time,a.show_end_time,a.icon_switch," +
            "a.is_auto_publish,a.switch_password,a.password,a.photo_live,a.face_set_token,a.is_open_face_search,a.iframe  " +
            "FROM activity a left join activity_album ab on a.id = ab.activity_id where ab.id = #{albumId}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "title", property = "title"),
            @Result(column = "introduction", property = "introduction"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "site", property = "site"),
            @Result(column = "organizer", property = "organizer"),
            @Result(column = "publisher", property = "publisher"),
            @Result(column = "generalize_status", property = "generalizeStatus"),
            @Result(column = "generalize_name", property = "generalizeName"),
            @Result(column = "generalize_link", property = "generalizeLink"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "banner", property = "banner"),
            @Result(column = "watermark", property = "watermark"),
            @Result(column = "water_complete", property = "waterComplete"),
            @Result(column = "water_config", property = "waterConfig"),
            @Result(column = "check_type", property = "checkType"),
            @Result(column = "status", property = "status"),
            @Result(column = "is_impower", property = "isImpower"),
            @Result(column = "qr_code", property = "qrCode"),
            @Result(column = "screen", property = "screen"),
            @Result(column = "docx", property = "docx"),
            @Result(column = "classical", property = "classical"),
            @Result(column = "model", property = "model"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "u_time", property = "uTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "type_name", property = "typeName"),
            @Result(column = "statuss", property = "statuss"),
            @Result(column = "icon_switch", property = "iconSwitch"),
            @Result(column = "is_auto_publish", property = "isAutoPublish"),
            @Result(column = "switch_password", property = "switchPassword"),
            @Result(column = "password", property = "password"),
            @Result(column = "photo_live", property = "photoLive"),
            @Result(column = "face_set_token", property = "faceSetToken"),
            @Result(column = "is_open_face_search", property = "isOpenFaceSearch"),
            @Result(column = "iframe", property = "iframe")
    })
    Activity selectByAlbumId(@Param("albumId") Integer albumId);

    @Select("select distinct a.id,a.user_id,a.priority,a.title,a.introduction, " +
            "a.start_time,a.end_time,a.site,a.organizer,a.publisher,a.generalize_status, " +
            "a.generalize_name,a.generalize_link,a.phone,a.email,a.banner,a.watermark,a.water_complete,water_config,a.check_type,a.status, " +
            "a.is_impower,a.qr_code,a.screen,a.docx,a.classical,a.model,a.c_time,a.u_time,a.type,a.show_start_time,a.show_end_time," +
            "a.icon_switch,a.is_auto_publish,a.switch_password,a.password,a.photo_live,a.face_set_token,a.is_open_face_search,a.iframe " +
            "from activity a left join activity_impower ai on a.id = ai.activity_id " +
            "where ai.user_id = #{userId} and a.photo_live = #{photoLive} and a.status = 1 and ai.status = 1")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "title", property = "title"),
            @Result(column = "introduction", property = "introduction"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "site", property = "site"),
            @Result(column = "organizer", property = "organizer"),
            @Result(column = "publisher", property = "publisher"),
            @Result(column = "generalize_status", property = "generalizeStatus"),
            @Result(column = "generalize_name", property = "generalizeName"),
            @Result(column = "generalize_link", property = "generalizeLink"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "banner", property = "banner"),
            @Result(column = "watermark", property = "watermark"),
            @Result(column = "water_complete", property = "waterComplete"),
            @Result(column = "water_config", property = "waterConfig"),
            @Result(column = "check_type", property = "checkType"),
            @Result(column = "status", property = "status"),
            @Result(column = "is_impower", property = "isImpower"),
            @Result(column = "qr_code", property = "qrCode"),
            @Result(column = "screen", property = "screen"),
            @Result(column = "docx", property = "docx"),
            @Result(column = "classical", property = "classical"),
            @Result(column = "model", property = "model"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "u_time", property = "uTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "type_name", property = "typeName"),
            @Result(column = "statuss", property = "statuss"),
            @Result(column = "icon_switch", property = "iconSwitch"),
            @Result(column = "is_auto_publish", property = "isAutoPublish"),
            @Result(column = "switch_password", property = "switchPassword"),
            @Result(column = "password", property = "password"),
            @Result(column = "photo_live", property = "photoLive"),
            @Result(column = "face_set_token", property = "faceSetToken"),
            @Result(column = "is_open_face_search", property = "isOpenFaceSearch"),
            @Result(column = "iframe", property = "iframe")
    })
    List<Activity> selectByImpower(@Param("userId") Integer userId, @Param("photoLive") Integer photoLive);

    @Select("select a.id,a.user_id,a.priority,a.title,a.introduction, " +
            "a.start_time,a.end_time,a.site,a.organizer,a.publisher,a.generalize_status, " +
            "a.generalize_name,a.generalize_link,a.phone,a.email,a.banner,a.watermark,a.water_complete,water_config," +
            "a.check_type,a.status, " +
            "a.is_impower,a.qr_code,a.screen,a.docx,a.classical,a.model,a.c_time,a.u_time,a.type,a.show_start_time,a.show_end_time," +
            "a.icon_switch,a.is_auto_publish,a.switch_password,a.password,a.photo_live,a.face_set_token,a.is_open_face_search,a.iframe " +
            "from activity a " +
            "left join activity_album aa on a.id = aa.activity_id " +
            "left join activity_photo ap on ap.album_id = aa.id " +
            "where ap.id = #{photoId}")

    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "title", property = "title"),
            @Result(column = "introduction", property = "introduction"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "site", property = "site"),
            @Result(column = "organizer", property = "organizer"),
            @Result(column = "publisher", property = "publisher"),
            @Result(column = "generalize_status", property = "generalizeStatus"),
            @Result(column = "generalize_name", property = "generalizeName"),
            @Result(column = "generalize_link", property = "generalizeLink"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "banner", property = "banner"),
            @Result(column = "watermark", property = "watermark"),
            @Result(column = "water_complete", property = "waterComplete"),
            @Result(column = "water_config", property = "waterConfig"),
            @Result(column = "check_type", property = "checkType"),
            @Result(column = "status", property = "status"),
            @Result(column = "is_impower", property = "isImpower"),
            @Result(column = "qr_code", property = "qrCode"),
            @Result(column = "screen", property = "screen"),
            @Result(column = "docx", property = "docx"),
            @Result(column = "classical", property = "classical"),
            @Result(column = "model", property = "model"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "u_time", property = "uTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "type_name", property = "typeName"),
            @Result(column = "statuss", property = "statuss"),
            @Result(column = "icon_switch", property = "iconSwitch"),
            @Result(column = "is_auto_publish", property = "isAutoPublish"),
            @Result(column = "switch_password", property = "switchPassword"),
            @Result(column = "password", property = "password"),
            @Result(column = "photo_live", property = "photoLive"),
            @Result(column = "face_set_token", property = "faceSetToken"),
            @Result(column = "is_open_face_search", property = "isOpenFaceSearch"),
            @Result(column = "iframe", property = "iframe")
    })
    Activity getByPhotoId(@Param("photoId") Integer photoId);

    @Update("update activity set is_auto_publish = #{status} where id = #{activityId}")
    Integer updateAutoPublish(@Param("activityId") Integer activityId, @Param("status") Integer status);

    @Select("select distinct a.id,a.user_id,a.priority,a.title,a.introduction, " +
            "a.start_time,a.end_time,a.site,a.organizer,a.publisher,a.generalize_status, " +
            "a.generalize_name,a.generalize_link,a.phone,a.email,a.banner,a.watermark,a.water_complete,water_config,a.check_type,a.status, " +
            "a.is_impower,a.qr_code,a.screen,a.docx,a.classical,a.model,a.c_time," +
            "a.u_time,a.type,a.show_start_time,a.show_end_time,a.icon_switch," +
            "a.is_auto_publish,a.switch_password,a.password,a.photo_live,a.face_set_token,a.is_open_face_search,a.iframe " +
            "from activity a left join activity_impower ai on a.id = ai.activity_id " +
            "where ai.user_id = #{userId} and a.photo_live = #{photoLive} and a.status = 1 and ai.status = 1 and ai.type <> #{type}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "title", property = "title"),
            @Result(column = "introduction", property = "introduction"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "site", property = "site"),
            @Result(column = "organizer", property = "organizer"),
            @Result(column = "publisher", property = "publisher"),
            @Result(column = "generalize_status", property = "generalizeStatus"),
            @Result(column = "generalize_name", property = "generalizeName"),
            @Result(column = "generalize_link", property = "generalizeLink"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "banner", property = "banner"),
            @Result(column = "watermark", property = "watermark"),
            @Result(column = "water_complete", property = "waterComplete"),
            @Result(column = "water_config", property = "waterConfig"),
            @Result(column = "check_type", property = "checkType"),
            @Result(column = "status", property = "status"),
            @Result(column = "is_impower", property = "isImpower"),
            @Result(column = "qr_code", property = "qrCode"),
            @Result(column = "screen", property = "screen"),
            @Result(column = "docx", property = "docx"),
            @Result(column = "classical", property = "classical"),
            @Result(column = "model", property = "model"),
            @Result(column = "c_time", property = "cTime"),
            @Result(column = "u_time", property = "uTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "type_name", property = "typeName"),
            @Result(column = "statuss", property = "statuss"),
            @Result(column = "icon_switch", property = "iconSwitch"),
            @Result(column = "is_auto_publish", property = "isAutoPublish"),
            @Result(column = "switch_password", property = "switchPassword"),
            @Result(column = "password", property = "password"),
            @Result(column = "photo_live", property = "photoLive"),
            @Result(column = "face_set_token", property = "faceSetToken"),
            @Result(column = "is_open_face_search", property = "isOpenFaceSearch"),
            @Result(column = "iframe", property = "iframe")
    })
    List<Activity> selectByImpowerPhotoPC(@Param("userId") Integer userId, @Param("photoLive") Integer photoLive, @Param("type") Integer type);

    @Update("update activity set  display_album_id = #{displayAlbumId}  where id = #{id} ")
    Integer pDateDisplayAlbum(@Param("id") Integer id, @Param("displayAlbumId") Integer displayAlbumId);

    @Update("update activity set is_open_face_search = #{isOpenFaceSearch} where id = #{id}")
    Integer setActivityFaceSearch(@Param("isOpenFaceSearch") Integer isOpenFaceSearch,@Param("id") Integer id);

    @Select("select id,start_time,end_time,expect_like_num,expect_participate_num from activity where start_time <= #{now} and end_time >= #{now} and status = 1 order by start_time desc")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "expect_like_num", property = "expectLikeNum"),
            @Result(column = "expect_participate_num", property = "expectParticipateNum"),
    })
    //获取正在进行中的会议
    List<Activity> getActivityIng(@Param("now")LocalDateTime now);
}
