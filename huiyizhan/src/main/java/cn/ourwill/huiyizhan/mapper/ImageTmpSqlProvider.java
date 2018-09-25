package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.ImageTmp;
import org.apache.ibatis.jdbc.SQL;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-06-12 10:55
 **/
public class ImageTmpSqlProvider {

    String column = "id,table_name,field_name,entity_id,url_old,url_new,flag";

    //修改
    public String update(final ImageTmp imageTmp){

        return new SQL(){
            {
                UPDATE("image_tmp");
                if(imageTmp.getTableName()!=null){
                    SET("table_name=#{tableName}");
                }
                if(imageTmp.getFieldName()!= null){
                    SET("field_name=#{fieldName}");
                }
                if(imageTmp.getEntityId()!=null){
                    SET("entity_id=#{entityId}");
                }
                if(imageTmp.getUrlOld()!=null){
                    SET("url_old=#{urlOld}");
                }
                if (imageTmp.getUrlNew()!= null){
                    SET("url_new=#{urlNew}");
                }
                if (imageTmp.getFlag()!=null){
                    SET("flag=#{flag}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
    //

//    public String selectImageTmp(@Param("tableName") String tableName, @Param("fieldName") String fieldName){
//        return new SQL(){
//            {
//                SELECT(column);
//                FROM("image_tmp");
//                WHERE("table_name = #{tableName}");
//                WHERE("field_name = #{fieldName}");
//            }
//        }.toString();
//    }
}
