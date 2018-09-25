package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.LightMap;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/26 11:35
 * @Description
 */
@Repository
public interface LightMapMapper {
    @Select("select id,logourl,shareurl,`name`,backimgurl,textimgurl,`desc`,date,sponsor,album,albumdesc,qrcodeurl from meeting where id = #{id}")
    @Results({
            @Result( id = true,column = "id",property = "id"),
            @Result( column = "logourl",property = "logoUrl"),
            @Result( column = "shareurl",property = "shareUrl"),
            @Result( column = "name",property = "name"),
            @Result( column = "backimgurl",property = "backImgUrl"),
            @Result( column = "textimgurl",property = "textImgUrl"),
            @Result( column = "desc",property = "desc"),
            @Result( column = "date",property = "date"),
            @Result( column = "sponsor",property = "sponsor"),
            @Result( column = "album",property = "album"),
            @Result( column = "albumdesc",property = "albumDesc"),
            @Result( column = "qrcodeurl",property = "qrCodeUrl"),
    })
    LightMap getById(@Param("id")Integer id);
}
