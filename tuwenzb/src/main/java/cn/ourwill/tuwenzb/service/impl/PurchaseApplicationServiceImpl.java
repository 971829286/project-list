package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.mapper.PurchaseApplicationMapper;
import cn.ourwill.tuwenzb.service.IPurchaseApplicationService;
import cn.ourwill.tuwenzb.entity.PurchaseApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PurchaseApplicationServiceImpl extends BaseServiceImpl<PurchaseApplication> implements IPurchaseApplicationService {

    @Autowired
    private PurchaseApplicationMapper purchaseApplicationMapper;
    @Override
    public Integer updatePhoneByUserId(PurchaseApplication purchaseApplication) {
        return purchaseApplicationMapper.updatePhoneByUserId(purchaseApplication);
    }

    @Override
    public List<PurchaseApplication> selectByParams(Map params) {
        return purchaseApplicationMapper.selectByParams(params);
    }
}
