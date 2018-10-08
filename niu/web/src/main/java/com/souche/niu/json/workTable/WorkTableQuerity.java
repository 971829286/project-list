package com.souche.niu.json.workTable;

import com.souche.niu.manager.workTable.WorkTableManager;
import com.souche.niu.model.workTable.*;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.core.annotation.Header;
import com.souche.optimus.core.annotation.View;
import com.souche.sso.client2.AuthNHolder;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车牛工作台
 */
@View(value = "WorkTableQuerity", desc = "车牛工作台")
public class WorkTableQuerity {

    private static final Logger logger = LoggerFactory.getLogger(WorkTableQuerity.class);

    @Autowired
    private WorkTableManager workTableManager;

    /**
     * 车牛工作台获取所有数据
     * @param token 用户token
     * @return  WorkTableDo
     */
    @ApiOperation("获取车牛工作台返回数据")
    public Map<String,Object> getAllInformation(@Header(value = "_security_token", desc = "用户登录的token",required = true) String token){
        try{

            logger.info("======车牛工作台查询参数token={}",token);
            String shopCode = AuthNHolder.shopCode();
            String userPhone = AuthNHolder.userPhone();
            if (StringUtils.isEmpty(shopCode)){
                shopCode = userPhone;
            }
            String appName = OptimusConfig.getValue("appName");

            WorkTableDo workTableDo = new WorkTableDo();
            //用户信息
            UserDo user = workTableManager.getUser(userPhone,appName,shopCode);
            //车店信息
            ShopMemberDo shop = workTableManager.getShop(token,userPhone);
            //会员账单，我的金币，优惠券
            InformationDo information = workTableManager.getInformation(token,userPhone,shopCode);
            //车行资料，交易管理等
            List<GroupDo> groupList = workTableManager.getGroupList(token,shopCode);

            workTableDo.setGroup(groupList);
            workTableDo.setInformation(information);
            workTableDo.setShop(shop);
            workTableDo.setUser(user);

            Map<String,Object> map = new HashMap<>();
            map.put("data",workTableDo);

            logger.info("======车牛工作台返回数据 map={}",map);

            return map;

        }catch (Exception e){
            logger.error("车牛工作台接口异常",e);
            return null;
        }

    }

}
