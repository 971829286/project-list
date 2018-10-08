package com.souche.niu.spi;

import com.souche.niu.model.workTable.ShopMemberDo;
import com.souche.shop.model.Shop;
import com.souche.shop.model.ShopAudit;
import com.souche.shop.model.ShopMember;
import com.souche.user.hessian.model.Result;
import com.souche.user.hessian.model.UserObject;

import java.util.Map;

/**
 * 车牛工作台查询接口
 * create by zzh
 */
public interface WorkTableSPI {

    ShopMemberDo getShopMember(String userAccount);

    UserObject getUser(String phone,String appName);

    Shop getShopInfo(String shopCode);

    ShopAudit getShopAuditByCode(String shopCode);

    Result findUserByToken(String token);

    Double getWalletBalance();

    Double getTicketsNumber(String userId);
}
