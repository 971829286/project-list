package com.souche.bmgateway.core.repo.impl;

import com.souche.bmgateway.core.dao.ShopInfoMapper;
import com.souche.bmgateway.core.domain.ShopInfo;
import com.souche.bmgateway.core.repo.ShopInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ShopInfoRepositoryImpl implements ShopInfoRepository {

    @Autowired
    ShopInfoMapper shopInfoMapper;

    @Override
    public int save(ShopInfo shopInfo) {
        return shopInfoMapper.save(shopInfo);
    }
}
