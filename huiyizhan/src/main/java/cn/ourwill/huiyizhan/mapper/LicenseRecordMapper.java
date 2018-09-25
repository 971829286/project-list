package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.LicenseRecord;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/19 0019 12:00
 * @Version1.0
 */
@Repository
public interface LicenseRecordMapper extends IBaseMapper<LicenseRecord> {
    @InsertProvider(type = LicenseRecoreSqlProvider.class,method ="save")
    public Integer save(LicenseRecord licenseRecord);

    @UpdateProvider(type = LicenseRecoreSqlProvider.class,method = "update")
    public Integer update(LicenseRecord licenseRecord);

    @SelectProvider(type = LicenseRecoreSqlProvider.class,method = "findAll")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "license_type",property = "licenseType"),
            @Result( column = "sessions_total",property = "sessionsTotal"),
            @Result( column = "due_date",property = "dueDate"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "payment_type",property = "paymentType"),
            @Result( column = "transaction_date",property = "transactionDate"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "invoice_status",property = "invoiceStatus"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "c_id",property = "cId"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "u_id",property = "uId"),
            @Result( column = "photo_live",property = "photoLive")
    })
    public List<LicenseRecord> findAll(@Param("photoLive") Integer photoLive);

    @SelectProvider(type = LicenseRecoreSqlProvider.class,method = "selectById")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "license_type",property = "licenseType"),
            @Result( column = "sessions_total",property = "sessionsTotal"),
            @Result( column = "due_date",property = "dueDate"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "payment_type",property = "paymentType"),
            @Result( column = "transaction_date",property = "transactionDate"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "invoice_status",property = "invoiceStatus"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "c_id",property = "cId"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "u_id",property = "uId"),
            @Result( column = "photo_live",property = "photoLive"),
    })
    public LicenseRecord getById(Integer id);

    @DeleteProvider(type = LicenseRecoreSqlProvider.class,method = "deleteById")
    public Integer delete(Map param);

    @SelectProvider(type = LicenseRecoreSqlProvider.class,method = "selectByParam")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "license_type",property = "licenseType"),
            @Result( column = "sessions_total",property = "sessionsTotal"),
            @Result( column = "due_date",property = "dueDate"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "payment_type",property = "paymentType"),
            @Result( column = "transaction_date",property = "transactionDate"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "invoice_status",property = "invoiceStatus"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "c_id",property = "cId"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "u_id",property = "uId"),
            @Result( column = "photo_live",property = "photoLive"),
            @Result( column = "express_company",property = "expressCompany"),
            @Result( column = "express_no",property = "expressNo"),
    })
    public List<LicenseRecord> getByParam(Map param);


    //批量删除
    @Delete("<script>" +
            "delete from license_record "+
            "<where> and id in " +
            "<foreach collection=\"ids\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\"> "+
            " #{item}"+
            "</foreach> "+
            "</where> "+
            "</script>" )
    public Integer deleteBatch(@Param("ids") List ids);

    @Select("select id,user_id,license_type,sessions_total,due_date,amount,payment_type,transaction_date,order_no,invoice_status,c_id,c_time,u_id,u_time,photo_live from license_record where user_id = #{userId} and photo_live = #{photoLive}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result( column = "user_id",property = "userId"),
            @Result( column = "license_type",property = "licenseType"),
            @Result( column = "sessions_total",property = "sessionsTotal"),
            @Result( column = "due_date",property = "dueDate"),
            @Result( column = "amount",property = "amount"),
            @Result( column = "payment_type",property = "paymentType"),
            @Result( column = "transaction_date",property = "transactionDate"),
            @Result( column = "order_no",property = "orderNo"),
            @Result( column = "invoice_status",property = "invoiceStatus"),
            @Result( column = "c_time",property = "cTime"),
            @Result( column = "c_id",property = "cId"),
            @Result( column = "u_time",property = "uTime"),
            @Result( column = "u_id",property = "uId"),
            @Result( column = "photo_live",property = "photoLive"),
    })
    List<LicenseRecord> selectByUserId(@Param("userId") Integer UserId, @Param("photoLive") Integer photoLive);

    @Select("<script> " +
            "update license_record " +
            "<set> invoice_status = #{status}</set> "+
            "<where> and id in "+
            "<foreach collection=\"ids\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\"> "+
            " #{item}"+
            "</foreach> "+
            "</where> "+
            "</script>" )
    Integer updateinvoiceStatus(@Param("ids") String[] ids, @Param("status") Integer status);
}
