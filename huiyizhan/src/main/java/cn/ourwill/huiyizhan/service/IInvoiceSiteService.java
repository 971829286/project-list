package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.InvoiceSite;
import org.json.JSONException;

import java.util.List;

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
