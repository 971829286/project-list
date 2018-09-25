package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.InvoiceSite;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 16:44
 * @Version1.0
 */
@Repository
public interface InvoiceSiteMapper extends IBaseMapper<InvoiceSite> {
    @InsertProvider(type = InvoiceSiteSqlProvider.class,method ="save")
    Integer save(InvoiceSite invoiceSite);

    @UpdateProvider(type = InvoiceSiteSqlProvider.class,method = "update")
    Integer update(InvoiceSite invoiceSite);

    @SelectProvider(type = InvoiceSiteSqlProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "receiver",property = "receiver"),
            @Result( column = "phone",property = "phone"),
            @Result( column = "address",property = "address"),
            @Result( column = "is_default",property = "isDefault"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime")
    })
    InvoiceSite getById(Integer id);

    @DeleteProvider(type = InvoiceSiteSqlProvider.class,method = "deleteById")
    Integer delete(Integer id);

    @Update("update invoice_site set is_default = 0 where user_id = #{userId} and is_default = 1")
    void initializeDefault(Integer userId);

    @Update("update invoice_site set is_default = 1 where id = #{id}")
    Integer setDefault(Integer id);

    @SelectProvider(type = InvoiceSiteSqlProvider.class,method = "selectByUserId")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "receiver",property = "receiver"),
            @Result( column = "phone",property = "phone"),
            @Result( column = "address",property = "address"),
            @Result( column = "is_default",property = "isDefault"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime")
    })
    List<InvoiceSite> getByUserId(Integer userId);

    @Select("select count(id) from invoice_site where user_id = #{userId}")
    Integer getCountByUserId(Integer userId);
}
