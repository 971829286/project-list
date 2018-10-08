package com.souche.niu.dao.mongo;

import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by sid on 2018/9/8.
 */
public interface MongoDao {


    void save(Object obj, String collectionName);


    <T> T findOne(Query query, Class<T> entityClass, String collectionName);


    void addToSet(String id, String key, Object value, String collectionName);
}
