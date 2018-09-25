package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.InvoiceRecords;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 16:05
 * @Version1.0
 */
@Repository
public interface InvoiceRecordsMapper extends IBaseMapper<InvoiceRecords> {
    @InsertProvider(type = InvoiceRecordsMapperProvider.class,method ="save")
    Integer save(InvoiceRecords invoiceRecords);

    @UpdateProvider(type = InvoiceRecordsMapperProvider.class,method = "update")
    Integer update(InvoiceRecords invoiceRecords);

    @SelectProvider(type = InvoiceRecordsMapperProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "apply_time",property = "applyTime"),
            @Result( column = "order_nos",property = "orderNos"),
            @Result( column = "authorization_ids",property = "authorizationIds"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "invoice_amount",property = "invoiceAmount"),
            @Result( column = "invoice_type",property = "invoiceType"),
            @Result( column = "invoice_status",property = "invoiceStatus"),
            @Result( column = "invoice_content_id",property = "invoiceContentId"),
            @Result( column = "invoice_site_id",property = "invoiceSiteId"),
            @Result( column = "receiver",property = "receiver"),
            @Result( column = "phone",property = "phone"),
            @Result( column = "express_company",property = "expressCompany"),
            @Result( column = "express_no",property = "expressNo"),
            @Result( column = "invoice_content",property = "invoiceContentStr"),
            @Result( column = "invoice_site",property = "invoiceSiteStr")
    })
    InvoiceRecords getById(Integer id);

    @SelectProvider(type = InvoiceRecordsMapperProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "apply_time",property = "applyTime"),
            @Result( column = "order_nos",property = "orderNos"),
            @Result( column = "authorization_ids",property = "authorizationIds"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "invoice_amount",property = "invoiceAmount"),
            @Result( column = "invoice_type",property = "invoiceType"),
            @Result( column = "invoice_status",property = "invoiceStatus"),
            @Result( column = "invoice_content_id",property = "invoiceContentId"),
            @Result( column = "invoice_site_id",property = "invoiceSiteId"),
            @Result( column = "receiver",property = "receiver"),
            @Result( column = "phone",property = "phone"),
            @Result( column = "express_company",property = "expressCompany"),
            @Result( column = "express_no",property = "expressNo"),
            @Result( column = "invoice_content",property = "invoiceContentStr"),
            @Result( column = "invoice_site",property = "invoiceSiteStr")
    })
    InvoiceRecords getByIdWithInfo(Integer id);

    @DeleteProvider(type = InvoiceRecordsMapperProvider.class,method = "deleteById")
    Integer delete(Integer id);

    @SelectProvider(type = InvoiceRecordsMapperProvider.class,method = "selectByParams")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "apply_time",property = "applyTime"),
            @Result( column = "order_nos",property = "orderNos"),
            @Result( column = "authorization_ids",property = "authorizationIds"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "invoice_amount",property = "invoiceAmount"),
            @Result( column = "invoice_type",property = "invoiceType"),
            @Result( column = "invoice_status",property = "invoiceStatus"),
            @Result( column = "invoice_content_id",property = "invoiceContentId"),
            @Result( column = "invoice_site_id",property = "invoiceSiteId"),
            @Result( column = "receiver",property = "receiver"),
            @Result( column = "phone",property = "phone"),
            @Result( column = "express_company",property = "expressCompany"),
            @Result( column = "express_no",property = "expressNo"),
            @Result( column = "invoice_content",property = "invoiceContentStr"),
            @Result( column = "invoice_site",property = "invoiceSiteStr")
    })
    List<InvoiceRecords> selectByParams(Map params);

    @Update("update invoice_records set invoice_status = 1 where id = #{id}")
    Integer updateStatus(Integer id);

//    @Select("SELECT sum(amount) FROM `license_record` where id in (#{authorizationIds});")
    @Select("<script>" +
            "SELECT sum(amount) FROM `license_record` "+
            "<where> and id in " +
            "<foreach collection=\"ids\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\"> "+
            " #{item}"+
            "</foreach> "+
            "</where> "+
            "</script>" )
    Double getSumAmount(@Param("ids") List authorizationIds);

    @Select(" SELECT id,apply_time,order_nos,authorization_ids,user_id,invoice_amount,invoice_status,invoice_content_id,invoice_site_id,receiver,phone,express_company,express_no,invoice_type,invoice_content,invoice_site" +
            " FROM invoice_records WHERE user_id=#{userId}" +
            " AND find_in_set(#{authorizationId},authorization_ids)>0")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "apply_time",property = "applyTime"),
            @Result( column = "order_nos",property = "orderNos"),
            @Result( column = "authorization_ids",property = "authorizationIds"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "invoice_amount",property = "invoiceAmount"),
            @Result( column = "invoice_type",property = "invoiceType"),
            @Result( column = "invoice_status",property = "invoiceStatus"),
            @Result( column = "invoice_content_id",property = "invoiceContentId"),
            @Result( column = "invoice_site_id",property = "invoiceSiteId"),
            @Result( column = "receiver",property = "receiver"),
            @Result( column = "phone",property = "phone"),
            @Result( column = "express_company",property = "expressCompany"),
            @Result( column = "express_no",property = "expressNo"),
            @Result( column = "invoice_content",property = "invoiceContentStr"),
            @Result( column = "invoice_site",property = "invoiceSiteStr")
    })
    InvoiceRecords getByAuthorizationIdWithInfo(@Param("authorizationId")Integer authorizationId,@Param("userId") Integer userId);
}
