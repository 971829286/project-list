package cn.ourwill.willcenter.mapper;

import cn.ourwill.willcenter.entity.FdfsImage;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FdfsImageMapper {

    String column = "id,url,upload_time,flag";

    @InsertProvider(type = FdfsImageSqlProvider.class, method = "save")
    Integer save(FdfsImage fdfsImage);

    @UpdateProvider(type = FdfsImageSqlProvider.class,method = "update")
    Integer update(FdfsImage fdfsImage);

    @Insert("insert into fdfs_image (url) values (#{url,jdbcType=VARCHAR})")
    Integer insert(@Param("url") String url);

    @Select("select "+column+" from fdfs_image where url = #{url}")
    FdfsImage selectByUrl(@Param("url") String url);

    @Select("select "+column+" from fdfs_image where flag = #{flag}")
    List<FdfsImage> selectByFlag(@Param("flag") Integer flag);

    @Delete("delete fdfs_image where url = #{url}")
    Integer deleteFdfsImage(@Param("url") String url);

}
