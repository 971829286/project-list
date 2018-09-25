package com.souche.niu.dao;

import com.souche.niu.bean.KvValue;

import java.util.List;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/
public interface KvValueDao {

    KvValue findById(Integer id);

    List<KvValue> findByGroupId(String groupId);

    int update(Integer id, KvValue kvValue);

    int save(KvValue kvValue);

    void deleteById(Integer id);

}
