package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.InvoiceRecords;
import cn.ourwill.tuwenzb.entity.InvoiceSite;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 18:55
 * @Version1.0
 */
public interface IInvoiceSiteService extends IBaseService<InvoiceSite>{
    @Override
    Integer save(InvoiceSite invoiceSite);
    @Override
    Integer update(InvoiceSite invoiceSite);

    Integer getCountByUserId(Integer userId);

    List<InvoiceSite> getByUserId(Integer userId) throws JSONException;

    Integer setDefault(Integer id, Integer userId);
}
