package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ImageTmp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageTmpMapper extends IBaseMapper<ImageTmpMapper> {


    String column = "id,table_name,field_name,entity_id,url_old,url_new,flag";

    @UpdateProvider(type = ImageTmpSqlProvider.class,method = "update")
    Integer update(ImageTmp imageTmp);

    //@SelectProvider(type=ImageTmpSqlProvider.class,method = "selectImageTmp")
    @Select("select "+column+" from image_tmp where table_name = #{tableName} and field_name = #{fieldName} and url_new  is NULL")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "table_name",property = "tableName"),
            @Result(column = "field_name",property = "fieldName"),
            @Result(column = "entity_id",property = "entityId"),
            @Result(column = "url_old",property = "urlOld"),
            @Result(column = "url_new",property = "urlNew"),
            @Result(column = "flag",property = "flag")
    })
    List<ImageTmp> selectImageTmp(@Param("tableName") String tableName,@Param("fieldName") String fieldName);


}
