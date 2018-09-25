package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.PriceType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/22 0022 15:35
 * @Version1.0
 */
@Repository
public interface PriceTypeMapper extends IBaseMapper<PriceType>{
    @Select("select id,type,description,price,number,photo_live from price_type where type = #{type}")
    @Results({
            @Result( id = true,column = "id",property = "id"),
            @Result( column = "type",property = "type"),
            @Result( column = "description",property = "description"),
            @Result( column = "price",property = "price"),
            @Result( column = "number",property = "number"),
            @Result( column = "photo_live",property = "photoLive")
    })
    PriceType getByType(Integer type);

    @Select("select id,type,description,price,number,photo_live from price_type where photo_live = #{photoLive}")
    @Results({
            @Result( id = true,column = "id",property = "id"),
            @Result( column = "type",property = "type"),
            @Result( column = "description",property = "description"),
            @Result( column = "price",property = "price"),
            @Result( column = "number",property = "number"),
            @Result( column = "photo_live",property = "photoLive")
    })
    List<PriceType> findAll(@Param("photoLive") Integer photoLive);
}
