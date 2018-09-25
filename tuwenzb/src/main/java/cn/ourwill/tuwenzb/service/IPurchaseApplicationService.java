package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.PurchaseApplication;

import java.util.List;
import java.util.Map;

/**
 * 　ClassName:IUserService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:51
 */

public interface IPurchaseApplicationService extends IBaseService<PurchaseApplication>{
    public Integer updatePhoneByUserId(PurchaseApplication purchaseApplication);

    List<PurchaseApplication> selectByParams(Map params);
}
