package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.Feedback;
import org.apache.ibatis.jdbc.SQL;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-06-05 14:26
 **/
public class FeedBackMapperProvider {


    String columns = "id,name,phone,remark,user_id";

    public String save(final Feedback feedback){

        return new SQL(){
            {
                INSERT_INTO("feedback");
                if (feedback.getName()!=null){
                    VALUES("name","#{name}");
                }
                if (feedback.getPhone()!=null){
                    VALUES("phone","#{phone}");
                }
                if (feedback.getRemark()!=null){
                    VALUES("remark","#{remark}");
                }
                if (feedback.getUserId()!=null){
                    VALUES("user_id","#{userId}");
                }
            }
        }.toString();
    }

}
