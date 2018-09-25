package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.InvoiceRecords;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/20 0020 17:21
 * @Version1.0
 */
public interface IInvoiceRecordsService extends IBaseService<InvoiceRecords>{
    @Override
    Integer save(InvoiceRecords invoiceRecords);

    Map updateStatus(String ids, String expressCompany, String expressNo);

    List<InvoiceRecords> selectByParams(Map params) throws JSONException;

    InvoiceRecords selectById(Integer id) throws JSONException;

    InvoiceRecords selectByAuthorizationId(Integer authorizationId, Integer userId);
}
