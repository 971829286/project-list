package com.souche.niu.dao.impl;

import com.souche.niu.bean.KvValue;
import com.souche.niu.dao.KvValueDao;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.dao.BasicDao;
import com.souche.optimus.dao.query.OrderParam;
import com.souche.optimus.dao.query.QueryObj;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/13
 **/
@Service("kvValueDao")
public class KvValueDaoImpl implements KvValueDao {

    private static final Logger logger = LoggerFactory.getLogger(KvValueDaoImpl.class);

    @Resource
    private BasicDao basicDao;


    @Override
    public KvValue findById(Integer id) {
        logger.info("数据库中查找KvValue id=[{}]",id);
        KvValue kvValue = this.basicDao.findObjectByPrimaryKey(id, KvValue.class);
        return kvValue;
    }

    @Override
    public List<KvValue> findByGroupId(String groupId) {
        QueryObj obj = new QueryObj(new KvValue());
        obj.addQuery("KvValue.groupId=#{groupId}",groupId);
        List<KvValue> list = this.basicDao.findListByQuery(obj, KvValue.class);
        return list;
    }

    @Override
    public int update(Integer id, KvValue kvValue) {
        if (kvValue == null) {
            logger.info("修改KvVaule失败 参数为空 id=[{}]",id);
            return 0;
        }
        kvValue.setId(id);
        int count = this.basicDao.update(kvValue, KvValue.class, true);
        return count;
    }

    @Override
    public int save(KvValue kvValue) {
        if (kvValue == null) {
            logger.info("KvValue保存失败 参数为空");
            return 0;
        }
        if (StringUtil.isEmpty(kvValue.getGroupId())) {
            logger.info("KvValue 保存失败 groupId为空");
            return 0;
        }
        QueryObj obj = new QueryObj(new KvValue());
        obj.addOrderParam("KvValue.sort",OrderParam.ORDER_DESC);
        obj.setPage(1);
        obj.setPageSize(10);
        kvValue.setSort(100f);
        List<KvValue> list = this.basicDao.findListByQuery(obj, KvValue.class);
        if (CollectionUtils.isNotEmpty(list) && list.get(0).getSort()!=null) {
            kvValue.setSort(list.get(0).getSort()+1);
        }
        int count = this.basicDao.insert(kvValue, KvValue.class);
        return count;
    }

    @Override
    public void deleteById(Integer id) {
        KvValue kv = this.basicDao.findObjectByPrimaryKey(id, KvValue.class);
        if (kv == null) {
            logger.info("当前ID未查找到KvValue id=[{}]",id);
            throw new OptimusExceptionBase("当前ID未查找到KvValue id=" + id);
        }
        try {
            this.basicDao.realDeleteByPrimaryKey(id, KvValue.class);
            logger.info("删除kvValue成功 ID=[{}]", id);
        } catch (OptimusExceptionBase e) {
            logger.info("删除kvValue成功 ID=[{}]",id);
            throw e;
        }

    }
}
