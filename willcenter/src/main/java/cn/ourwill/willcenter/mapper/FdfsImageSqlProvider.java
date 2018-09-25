package cn.ourwill.willcenter.mapper;

import cn.ourwill.willcenter.entity.FdfsImage;
import org.apache.ibatis.jdbc.SQL;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-06-13 14:04
 **/
public class FdfsImageSqlProvider {

    public String save (final FdfsImage fdfsImage){
        return new SQL(){
            {
                INSERT_INTO("fdfs_image");
                if(fdfsImage.getUrl()!=null){
                    VALUES("url","#{url}");
                }
                if(fdfsImage.getFlag()!=null){
                    VALUES("flag","#{flag}");
                }
                if(fdfsImage.getUploadTime()!=null){
                    VALUES("upload_time","#{uploadTime}");
                }
            }
        }.toString();
    }

    public String update (final FdfsImage fdfsImage){
        return new SQL(){
            {
                UPDATE("fdfs_image");
                if(fdfsImage.getUrl()!=null){
                    SET("url = #{url}");
                }
                if (fdfsImage.getFlag()!=null){
                    SET("flag = #{flag}");
                }
                if(fdfsImage.getUploadTime()!=null){
                    SET("upload_time = #{uploadTime}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
