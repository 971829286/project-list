package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.InvoiceContent;
import cn.ourwill.huiyizhan.mapper.InvoiceContentMapper;
import cn.ourwill.huiyizhan.service.IInvoiceContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 18:37
 * @Version1.0
 */
@Service
public class InvoiceContentServiceImpl extends BaseServiceImpl<InvoiceContent> implements IInvoiceContentService{

    @Autowired
    private InvoiceContentMapper invoiceContentMapper;

    @Override
    @Transactional
    public Integer save(InvoiceContent invoiceContent) {
        invoiceContent.setCTime(new Date());
        //设置默认
        if(invoiceContent.getIsDefault().equals(1)){
            invoiceContentMapper.initializeDefault(invoiceContent.getUserId());
        }
        if(invoiceContentMapper.save(invoiceContent)>0){
            return 1;
        }
        return -1;
    }

    @Override
    @Transactional
    public Integer update(InvoiceContent invoiceContent){
        invoiceContent.setUTime(new Date());
        //设置默认
        if(invoiceContent.getIsDefault()!=null&&invoiceContent.getIsDefault().equals(1)){

            invoiceContentMapper.initializeDefault(invoiceContent.getUserId());
        }
        if(invoiceContentMapper.update(invoiceContent)>0){
            return 1;
        }
        return -1;
    }

    @Override
    public Integer getCountByUserId(Integer userId) {
        return invoiceContentMapper.getCountByUserId(userId);
    }

    @Override
    public List<InvoiceContent> getByUserId(Integer userId) {
        return invoiceContentMapper.getByUserId(userId);
    }

    @Override
    @Transactional
    public Integer setDefault(Integer id,Integer userId) {
        invoiceContentMapper.initializeDefault(userId);
        return invoiceContentMapper.setDefault(id);
    }
}
