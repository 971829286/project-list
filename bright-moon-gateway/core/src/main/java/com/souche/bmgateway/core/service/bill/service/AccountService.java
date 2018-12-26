package com.souche.bmgateway.core.service.bill.service;

import com.souche.bmgateway.core.domain.ShopInfo;

public interface AccountService {

    ShopInfo queryAccountNoByShopCode(String shopCode);
}
