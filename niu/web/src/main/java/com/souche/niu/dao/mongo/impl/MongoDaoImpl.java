package com.souche.niu.dao.mongo.impl;

import com.souche.niu.dao.mongo.MongoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.util.Collection;

/**
 * mongo 操作类
 * Created by sid on 2018/9/7.
 */
@Service
public class MongoDaoImpl implements MongoDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Object obj, String collectionName) {
        mongoTemplate.save(obj, collectionName);
    }

    @Override
    public <T> T findOne(Query query, Class<T> entityClass, String collectionName) {
        return mongoTemplate.findOne(query,entityClass,collectionName);
    }

    @Override
    public void addToSet(String id, String key, Object value, String collectionName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        if(value instanceof Collection) {
            for(Object v : (Collection<?>)value) {
                update.addToSet(key, v);
                mongoTemplate.updateFirst(query, update, collectionName);
            }
        } else {
            update.addToSet(key, value);
            mongoTemplate.updateFirst(query, update, collectionName);
        }
    }
}
