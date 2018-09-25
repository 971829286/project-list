package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.AppVersion;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVersionMapper  {
        @Select("select * from app_version order by version_code desc limit 1")
        @Results({
                @Result(id = true, column = "id", property = "id"),
                @Result(column = "version_code", property = "versionCode"),
                @Result(column = "version_name", property = "versionName"),
                @Result(column = "introduce", property = "introduce"),
                @Result(column = "download_url", property = "downloadUrl"),
                @Result(column = "c_time", property = "cTime")
        })
        AppVersion findTheLast();
}
