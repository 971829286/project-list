package com.souche.niu.dao.workTable.impl;

import com.souche.niu.dao.workTable.WorkTableDao;
import com.souche.niu.model.workTable.UserCoinDo;
import com.souche.optimus.dao.BasicDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("workTableDao")
public class WorkTableDaoImpl implements WorkTableDao {

    private static final Logger logger = LoggerFactory.getLogger(WorkTableDaoImpl.class);

    @Autowired
    private BasicDao basicDao;

    /**
     *用户金币
     * @param phone
     * @return
     */
    public UserCoinDo getCoin(String phone) {
        try{
            logger.info("查询用户的的金币信息，参数为phone={}",phone);
            UserCoinDo sample = new UserCoinDo();
            sample.setPhone(phone);
            UserCoinDo userCoin = basicDao.findObjectBySample(sample, UserCoinDo.class);
            logger.info("查询用户的的金币信息，返回结果为={}",userCoin);
            return userCoin;
        }catch (Exception e){
            logger.error("查询用户的的金币信息异常e=",e);
            return null;
        }
    }

}
