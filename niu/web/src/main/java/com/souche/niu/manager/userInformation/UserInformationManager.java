package com.souche.niu.manager.userInformation;

import com.souche.niu.model.userInformation.*;

import java.util.List;

public interface UserInformationManager {

    SubscribeInformationDo querySubscribe(String userId);

    IconDo getIconList();

    List<BannerDo> getBannerlist();

    MyCarShopDo getMyCarShop(String userId,String shopCode);

    ActivityScreenDo getActivityScreen();

    OpenScreenDO getOpenScreen(String groupId);



}
