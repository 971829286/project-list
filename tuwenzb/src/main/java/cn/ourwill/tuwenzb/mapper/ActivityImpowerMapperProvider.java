package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.ActivityImpower;
import org.apache.ibatis.jdbc.SQL;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/23 0023 14:51
 * @Version1.0
 */
public class ActivityImpowerMapperProvider {

    //保存
    public String save(final ActivityImpower activityImpower){
        return new SQL(){
            {
                INSERT_INTO("activity_impower");
                if(activityImpower.getActivityId()!=null){
                    VALUES("activity_id","#{activityId}");
                }
                if(activityImpower.getAlbumId()!=null){
                    VALUES("album_id","#{albumId}");
                }
                if(activityImpower.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
                if(activityImpower.getType()!=null){
                    VALUES("type","#{type}");
                }
                if(activityImpower.getStatus()!=null){
                    VALUES("status","#{status}");
                }
                if(activityImpower.getCTime()!=null){
                    VALUES("c_time","#{cTime}");
                }
            }
        }.toString();
    }
}
