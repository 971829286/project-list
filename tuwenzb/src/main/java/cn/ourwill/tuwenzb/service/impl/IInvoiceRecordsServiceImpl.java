package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.InvoiceContent;
import cn.ourwill.tuwenzb.entity.InvoiceRecords;
import cn.ourwill.tuwenzb.entity.InvoiceSite;
import cn.ourwill.tuwenzb.mapper.InvoiceContentMapper;
import cn.ourwill.tuwenzb.mapper.InvoiceRecordsMapper;
import cn.ourwill.tuwenzb.mapper.InvoiceSiteMapper;
import cn.ourwill.tuwenzb.mapper.LicenseRecordMapper;
import cn.ourwill.tuwenzb.service.IInvoiceRecordsService;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/20 0020 17:22
 * @Version1.0
 */
@Service
public class IInvoiceRecordsServiceImpl extends BaseServiceImpl<InvoiceRecords> implements IInvoiceRecordsService{
    private static final Logger log = LogManager.getLogger(IInvoiceRecordsServiceImpl.class);
    @Autowired
    InvoiceRecordsMapper invoiceRecordsMapper;

    @Autowired
    LicenseRecordMapper licenseRecordMapper;

    @Autowired
    InvoiceContentMapper invoiceContentMapper;

    @Autowired
    InvoiceSiteMapper invoiceSiteMapper;

    @Override
    @Transactional
    public Integer save(InvoiceRecords invoiceRecords){
        Gson gson = new Gson();
        InvoiceContent invoiceContent = invoiceContentMapper.getById(invoiceRecords.getInvoiceContentId());
        InvoiceSite invoiceSite = invoiceSiteMapper.getById(invoiceRecords.getInvoiceSiteId());
        invoiceRecords.setInvoiceContentStr(gson.toJson(invoiceContent));
        invoiceRecords.setInvoiceSiteStr(gson.toJson(invoiceSite));
        invoiceRecords.setReceiver(invoiceSite.getReceiver());
        invoiceRecords.setPhone(invoiceSite.getPhone());
        invoiceRecords.setInvoiceType(invoiceContent.getInvoiceType());
        //计算金额
        invoiceRecords.setApplyTime(new Date());
        String authorizationIds = invoiceRecords.getAuthorizationIds();
        List ids = Arrays.asList(authorizationIds.split(","));
        Double sumAmount = invoiceRecordsMapper.getSumAmount(ids);
        invoiceRecords.setInvoiceAmount(sumAmount);
        Integer count = invoiceRecordsMapper.save(invoiceRecords);
        //更新状态
        if(count>0){
            licenseRecordMapper.updateinvoiceStatus(invoiceRecords.getAuthorizationIds().split(","),2);
        }
        return count;
    }

    @Override
    @Transactional
    public Map updateStatus(String ids,String expressCompany, String expressNo) {
        if(ids==null){
            return ReturnResult.successResult("请选择！");
        }
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            InvoiceRecords invoiceRecords = invoiceRecordsMapper.getById(Integer.valueOf(id));
            //添加快递信息
            if(expressCompany!=null)
                invoiceRecords.setExpressCompany(expressCompany);
            if(expressCompany!=null)
                invoiceRecords.setExpressNo(expressNo);
            if(expressCompany!=null || expressNo!=null)
                invoiceRecordsMapper.update(invoiceRecords);
            //更新记录状态
            Integer count = invoiceRecordsMapper.updateStatus(Integer.valueOf(id));
            //更新授权记录状态
            if(count>0){
                licenseRecordMapper.updateinvoiceStatus(invoiceRecords.getAuthorizationIds().split(","),1);
            }
        }
        return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
    }

    @Override
    public List<InvoiceRecords> selectByParams(Map params) throws JSONException {
        List<InvoiceRecords> reList = invoiceRecordsMapper.selectByParams(params);
//        for (InvoiceRecords records : reList) {
//            records.setAddress(getAddressStr(records.getAddress()));
//        }
        return reList;
    }

    @Override
    public InvoiceRecords selectById(Integer id) throws JSONException {
        InvoiceRecords invoiceRecords = invoiceRecordsMapper.getByIdWithInfo(id);
//        invoiceRecords.setAddress(getAddressStr(invoiceRecords.getInvoiceSite().getAddress()));
        return invoiceRecords;
    }

    @Override
    public InvoiceRecords selectByAuthorizationId(Integer authorizationId, Integer userId) {
        InvoiceRecords invoiceRecords = invoiceRecordsMapper.getByAuthorizationIdWithInfo(authorizationId,userId);
        return invoiceRecords;
    }
}
