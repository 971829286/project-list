package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.InvoiceContent;
import cn.ourwill.tuwenzb.entity.InvoiceSite;
import cn.ourwill.tuwenzb.mapper.InvoiceSiteMapper;
import cn.ourwill.tuwenzb.service.IInvoiceSiteService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 18:56
 * @Version1.0
 */
@Service
public class invoiceSiteServiceImpl extends BaseServiceImpl<InvoiceSite> implements IInvoiceSiteService {

    @Autowired
    private InvoiceSiteMapper invoiceSiteMapper;

    @Override
    @Transactional
    public Integer save(InvoiceSite invoiceSite) {
        //设置默认
        invoiceSite.setCTime(new Date());
        if(invoiceSite.getIsDefault().equals(1)){
            invoiceSiteMapper.initializeDefault(invoiceSite.getUserId());
        }
        if(invoiceSiteMapper.save(invoiceSite)>0){
            return 1;
        }
        return -1;
    }

    @Override
    @Transactional
    public Integer update(InvoiceSite invoiceSite){
        //设置默认
        invoiceSite.setUTime(new Date());
        if(invoiceSite.getIsDefault()!=null&&invoiceSite.getIsDefault().equals(1)){
            invoiceSiteMapper.initializeDefault(invoiceSite.getUserId());
        }
        if(invoiceSiteMapper.update(invoiceSite)>0){
            return 1;
        }
        return -1;
    }

    @Override
    public Integer getCountByUserId(Integer userId) {
        return invoiceSiteMapper.getCountByUserId(userId);
    }

    @Override
    public List<InvoiceSite> getByUserId(Integer userId) throws JSONException {
        List<InvoiceSite> reList = invoiceSiteMapper.getByUserId(userId);
//        for (InvoiceSite site : reList) {
//            JSONObject jsonObject = new JSONObject(site.getAddress());
//            String province = jsonObject.getString("province");
//            String city = jsonObject.getString("city").equals("市辖区")?"":jsonObject.getString("city");
//            String district = jsonObject.getString("district");
//            String street = jsonObject.getString("street");
//            site.setAddress(GlobalUtils.getAddressStr(site.getAddress()));
//        }
        return reList;
    }

    @Override
    @Transactional
    public Integer setDefault(Integer id, Integer userId) {
        invoiceSiteMapper.initializeDefault(userId);
        return invoiceSiteMapper.setDefault(id);
    }
}
