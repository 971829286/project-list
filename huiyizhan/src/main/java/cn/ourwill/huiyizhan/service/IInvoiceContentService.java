package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.InvoiceContent;

import java.util.List;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 18:28
 * @Version1.0
 */
public interface IInvoiceContentService extends IBaseService<InvoiceContent>{
    @Override
    Integer save(InvoiceContent invoiceContent);

    @Override
    Integer update(InvoiceContent invoiceContent);

    Integer getCountByUserId(Integer userId);

    List<InvoiceContent> getByUserId(Integer userId);

    Integer setDefault(Integer id, Integer userId);
}