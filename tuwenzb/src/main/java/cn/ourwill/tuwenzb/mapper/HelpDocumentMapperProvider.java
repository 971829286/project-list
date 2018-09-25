package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.HelpDocument;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @author wanghao
 *
 */
public class HelpDocumentMapperProvider {

    //保存
    public String save(final HelpDocument helpDocument){
        return new SQL(){
            {
                INSERT_INTO("help_document");
                    if(helpDocument.getDocumentTitle()!=null){
                        VALUES("document_title","#{documentTitle}");
                    }
                    if(helpDocument.getDocumentContent()!=null){
                        VALUES("document_content","#{documentContent}");
                    }
                    if(helpDocument.getUpdateTime()!=null) {
                        VALUES("update_time", "#{updateTime}");
                    }if(helpDocument.getUploadTime()!=null) {
                        VALUES("upload_time", "#{uploadTime}");
                    }if(helpDocument.getManagerId()!=null) {
                        VALUES("manager_id", "#{managerId}");
                    }
            }
        }.toString();
    }

    //修改
    public String update(final HelpDocument helpDocument){
        return new SQL(){
            {
                UPDATE("help_document");
                if(helpDocument.getDocumentTitle()!=null) {
                    SET("document_title=#{documentTitle}");
                }
                if(helpDocument.getDocumentContent()!=null) {
                    SET("document_content=#{documentContent}");
                }
                if(helpDocument.getUpdateTime()!=null) {
                    SET("update_time=#{updateTime}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //查找所有
    public String findAll(){
        return new SQL(){
            {
                SELECT("id,document_title,document_content,update_time,upload_time,manager_id");
                FROM("help_document");
            }
        }.toString();
    }

    //根据ID查找
    public String selectById(Integer id){
        return new SQL(){
            {
                SELECT("id,document_title,document_content,update_time,upload_time,manager_id");
                FROM("help_document");
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //根据id删除
    public String deleteById(Map param){
        return new SQL(){
            {
                DELETE_FROM("help_document");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
