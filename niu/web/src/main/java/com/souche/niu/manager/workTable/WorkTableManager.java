package com.souche.niu.manager.workTable;

import com.souche.niu.model.workTable.*;
import com.souche.user.hessian.model.UserObject;

import java.util.List;

public interface WorkTableManager {

    List<GroupDo> getGroupList(String token,String shopCode);

    UserDo getUser(String userPhone, String appName,String shopCode);

    ShopMemberDo getShop(String token, String userAccount);

    InformationDo getInformation(String token, String userPhone,String shopCode);

}
