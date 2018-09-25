package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.SystemConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/10 0010 17:31
 * @Version1.0
 */
@Repository
public interface SystemConfigMapper {
    @Select("select id,discount_switch,discount_start,discount_end from system_config where active=1 and photo_live = #{photoLive} limit 1")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "discount_switch",property = "discountSwitch"),
            @Result( column = "discount_start",property = "discountStart"),
            @Result( column = "discount_end",property = "discountEnd")
    })
    SystemConfig getConfig(@Param("photoLive") Integer photoLive);
}
