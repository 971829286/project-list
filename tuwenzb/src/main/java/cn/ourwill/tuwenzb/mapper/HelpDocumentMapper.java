package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.HelpDocument;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpDocumentMapper extends IBaseMapper<HelpDocument> {

    //保存插入
    @InsertProvider(type = HelpDocumentMapperProvider.class,method ="save")
    public Integer save(HelpDocument helpDocument);

    //修改
    @UpdateProvider(type = HelpDocumentMapperProvider.class,method = "update")
    public Integer update(HelpDocument helpDocument);

    //查找所有
    @SelectProvider(type = HelpDocumentMapperProvider.class,method = "findAll")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "document_title",property = "documentTitle"),
            @Result( column = "document_content",property = "documentContent"),
            @Result( column = "update_time",property = "updateTime"),
            @Result( column = "upload_time",property = "uploadTime"),
            @Result( column = "manager_id",property = "managerId")
    })
    public List<HelpDocument> findAll();

    //根据ID查找
    @SelectProvider(type = HelpDocumentMapperProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "document_title",property = "documentTitle"),
            @Result( column = "document_content",property = "documentContent"),
            @Result( column = "update_time",property = "updateTime"),
            @Result( column = "upload_time",property = "uploadTime"),
            @Result( column = "manager_id",property = "managerId")
    })
    public HelpDocument getById(Integer id);

    //根据ID删除多个帮助文档
    @Delete("<script>" +
            "delete from help_document "+
            "<where> and id in " +
            "<foreach collection=\"ids\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\"> "+
            " #{item}"+
            "</foreach> "+
            "</where> "+
            "</script>" )
    public Integer deleteBatch(@Param("ids") List ids);

}
