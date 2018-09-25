package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.Feedback;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;


/**
 * @Author zhaoqing
 * @Time 2018-06-05 14:26
 * @Version1.0
 */
@Repository
public interface FeedBackMapper {

    String columns = "id,name,phone,remark,user_id";

    @InsertProvider(type = FeedBackMapperProvider.class ,method = "save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer save(Feedback feedback);

}
