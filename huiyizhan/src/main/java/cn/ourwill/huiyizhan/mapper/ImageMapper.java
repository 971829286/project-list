package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.Image;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-12 10:39
 **/
@Repository
public interface ImageMapper extends IBaseMapper {
    String column = "id,type,pic";
    @Insert({
            "insert into img ("+column+")",
            "values (#{id,jdbcType=INTEGER}, #{type,jdbcType=INTEGER}, #{pic,jdbcType=VARCHAR})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertSelective(Image image);

    @Update({
            "update img set type = #{type,jdbcType = INTEGER},pic = #{pic,jdbcType = VARCHAR} where id = #{id,jdbcType = INTEGER}"
    })
     int updateByPrimaryKeySelective(Image image);

    @Select({
            "select "+column+" from img where id = #{id,jdbcType = INTEGER}"
    })
     Image selectByPrimaryKey(Integer id);

    @Select({
            "select "+column+" from img"
    })
     List<Image> findAll();
}
