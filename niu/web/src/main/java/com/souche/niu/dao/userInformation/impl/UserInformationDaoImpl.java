package com.souche.niu.dao.userInformation.impl;

import com.souche.niu.dao.userInformation.UserInformationDao;
import com.souche.niu.model.userInformation.SubscribeDo;
import com.souche.niu.model.userInformation.TbKvValueDo;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.dao.BasicDao;
import com.souche.optimus.dao.query.QueryObj;
import com.souche.optimus.exception.OptimusCrossShopException;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userInformation")
public class UserInformationDaoImpl implements UserInformationDao {

    private static final Logger logger = LoggerFactory.getLogger(UserInformationDaoImpl.class);

    @Autowired
    private BasicDao basicDao;

    /**
     * 查询用户的订阅信息
     * @param userID 用户id
     * @return list
     */
    public List<SubscribeDo> queryByUserID(String userID) {
        try{
            logger.info("查询订阅信息，参数userID={}",userID);
            QueryObj queryObj = new QueryObj(new SubscribeDo());
            queryObj.addQuery("SubscribeDo.userID=#{userID}", userID);
            queryObj.addQuery("SubscribeDo.deletedAt is null",null);
            List<SubscribeDo> list = basicDao.findListByQuery(queryObj, SubscribeDo.class);
            logger.info("查询订阅信息，返回结果为list={}",list);
            return list;
        }catch (Exception e){
            logger.error("查询订阅信息异常e=",e);
            return null;
        }
    }

    @Override
    public List<TbKvValueDo> getKvList(String groupId) {
        try{
            logger.info("查询kv_value,参数为groupId={}",groupId);
            String sql = "SELECT * from `kv_value` where group_id=#{groupId} ";
            Map<String,Object> map = new HashMap<>();
            map.put("groupId",groupId);
            List<TbKvValueDo> list = basicDao.selectListBySql(sql, map, TbKvValueDo.class);
            logger.info("查询kv_value,返回结果为list={}",list);
            return list;
        }catch (Exception e){
            logger.error("查询kv_value异常e=",e);
            return null;
        }
    }

    @Override
    public Integer countSubscribeByUserId(String userId) {
        try {
            logger.info("根据用户ID统计订阅数 userid:{}",userId);
            QueryObj queryObj = new QueryObj(new SubscribeDo());
            queryObj.addQuery("SubscribeDo.userID=#{userID}", userId);
            queryObj.addQuery("SubscribeDo.deletedAt is null");
            int count=basicDao.countByQuery(queryObj);
            logger.info("根据用户ID统计订阅数 userid:{},count:{}",userId,count);
            return count;
        } catch (OptimusExceptionBase e) {
            logger.error("统计用户订阅数失败 userID：{}，errMsg：{}",userId,e.toString());
            throw e;
        }
    }
}
