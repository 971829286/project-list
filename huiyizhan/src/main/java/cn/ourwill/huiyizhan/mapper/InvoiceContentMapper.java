package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.InvoiceContent;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 16:43
 * @Version1.0
 */
@Repository
public interface InvoiceContentMapper extends IBaseMapper<InvoiceContent> {
    @InsertProvider(type = InvoiceContentSqlProvider.class,method ="save")
    Integer save(InvoiceContent invoiceContent);

    @UpdateProvider(type = InvoiceContentSqlProvider.class,method = "update")
    Integer update(InvoiceContent invoiceContent);

    @SelectProvider(type = InvoiceContentSqlProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "invoice_type",property = "invoiceType"),
            @Result( column = "title_type",property = "titleType"),
            @Result( column = "invoice_title",property = "invoiceTitle"),
            @Result( column = "registration_num",property = "registrationNum"),
            @Result( column = "registration_site",property = "registrationSite"),
            @Result( column = "registration_phone",property = "registrationPhone"),
            @Result( column = "bank",property = "bank"),
            @Result( column = "bank_account",property = "bankAccount"),
            @Result( column = "is_default",property = "isDefault"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
    })
    InvoiceContent getById(Integer id);

    @DeleteProvider(type = InvoiceContentSqlProvider.class,method = "deleteById")
    Integer delete(Integer id);

    @Update("update invoice_content set is_default = 0 where user_id = #{userId} and is_default = 1")
    void initializeDefault(Integer userId);

    @Update("update invoice_content set is_default = 1 where id = #{id}")
    Integer setDefault(Integer id);

    @SelectProvider(type=InvoiceContentSqlProvider.class,method = "selectByUserId")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "invoice_type",property = "invoiceType"),
            @Result( column = "title_type",property = "titleType"),
            @Result( column = "invoice_title",property = "invoiceTitle"),
            @Result( column = "registration_num",property = "registrationNum"),
            @Result( column = "registration_site",property = "registrationSite"),
            @Result( column = "registration_phone",property = "registrationPhone"),
            @Result( column = "bank",property = "bank"),
            @Result( column = "bank_account",property = "bankAccount"),
            @Result( column = "is_default",property = "isDefault"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "u_time",property = "uTime"),
    })
    List<InvoiceContent> getByUserId(Integer userId);

    @Select("select count(id) from invoice_content where user_id = #{userId}")
    Integer getCountByUserId(Integer userId);
}
