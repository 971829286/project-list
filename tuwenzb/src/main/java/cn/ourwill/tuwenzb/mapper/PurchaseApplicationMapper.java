package cn.ourwill.tuwenzb.mapper;

import cn.ourwill.tuwenzb.entity.PurchaseApplication;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PurchaseApplicationMapper extends IBaseMapper<PurchaseApplication>{
	@InsertProvider(type = PurchaseApplicationMapperProvider.class,method ="save")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public Integer save(PurchaseApplication purchaseApplication);
	
	@UpdateProvider(type = PurchaseApplicationMapperProvider.class,method = "update")
	public Integer update(PurchaseApplication purchaseApplication);

	@SelectProvider(type = PurchaseApplicationMapperProvider.class,method = "findAll")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "phone",property = "phone"),
			@Result( column = "contacts",property = "contacts"),
			@Result( column = "wechat",property = "weChat"),
			@Result( column = "email",property = "email"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "status",property = "status"),
			@Result( column = "u_time",property = "uTime")
	})
	public List<PurchaseApplication> findAll();

	@SelectProvider(type = PurchaseApplicationMapperProvider.class,method = "selectById")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "phone",property = "phone"),
			@Result( column = "contacts",property = "contacts"),
			@Result( column = "wechat",property = "weChat"),
			@Result( column = "email",property = "email"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "status",property = "status"),
			@Result( column = "u_time",property = "uTime")
	})
	public PurchaseApplication getById(Integer id);

	@DeleteProvider(type = PurchaseApplicationMapperProvider.class,method = "deleteById")
	public Integer delete(Map param);

	@UpdateProvider(type = PurchaseApplicationMapperProvider.class,method = "updatePhoneByUserId")
    public Integer updatePhoneByUserId(PurchaseApplication purchaseApplication);

	@SelectProvider(type = PurchaseApplicationMapperProvider.class,method = "selectByParmer")
	@Results({
			@Result(id = true,column = "id",property = "id"),
			@Result( column = "user_id",property = "userId"),
			@Result( column = "license_type",property = "licenseType"),
			@Result( column = "phone",property = "phone"),
			@Result( column = "contacts",property = "contacts"),
			@Result( column = "wechat",property = "weChat"),
			@Result( column = "email",property = "email"),
			@Result( column = "c_time",property = "cTime"),
			@Result( column = "status",property = "status"),
			@Result( column = "u_time",property = "uTime")
	})
    List<PurchaseApplication> selectByParams(Map params);
}
