package com.souche.niu.json.userInformation;

import com.souche.niu.manager.userInformation.UserInformationManager;
import com.souche.niu.model.userInformation.OpenScreenDO;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.core.annotation.Header;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.web.Result;
import com.souche.sso.client2.AuthNHolder;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页信息
 */
@View(value = "UserInformationQuery", desc = "首页信息")
public class UserInformationQuery {

    private static final Logger logger = LoggerFactory.getLogger(UserInformationQuery.class);

    @Autowired
    private UserInformationManager userInformationManager;

    @ApiOperation("获取所有信息")
    public Map<String,Object> getHomeInformation(@Header(value = "_security_token",desc = "用户登录的token",required = true) String token){

        try {
            logger.info("首页查询参数token={}",token);

            String userPhone = AuthNHolder.userPhone();
            String userId = AuthNHolder.userId();
            String shopCode = AuthNHolder.shopCode();
            if (StringUtils.isEmpty(shopCode)){
                shopCode = userPhone;
            }

            Map<String,Object> map = new HashMap<String,Object>();
            Map<String,Object> dataMap = new HashMap<>();
            map.put("banner",userInformationManager.getBannerlist());
            map.put("subscribe",userInformationManager.querySubscribe(userId));
            map.put("myCarShop",userInformationManager.getMyCarShop(userId,shopCode));
            map.put("iconList",userInformationManager.getIconList());
            map.put("activityScreen",userInformationManager.getActivityScreen());
            dataMap.put("data",map);
            logger.info("首页返回结果map={}",dataMap);

            return dataMap;
        }catch (Exception e){
            logger.error("收车页面查询异常e=",e);
            return null;
        }
    }

    /**
     * 获取开屏信息
     * @param token
     * @return
     */
    @ApiOperation("获取开屏信息")
    public Result<Map<String, Object>> openScreenQuery(@Header(value = "_security_token",desc = "用户登录的token", required=true) String token){

        try {
            logger.info("开屏信息查询，参数为token={}",token);
            String groupId = OptimusConfig.getValue("open.screen.group.id");
            Result<Map<String, Object>> result = new Result<>();
            OpenScreenDO openScreen = userInformationManager.getOpenScreen(groupId);
            Map<String, Object> map = new HashMap<>();
            map.put("openScreen",openScreen);
            result.setCode("200");
            result.setMsg("开屏信息查询成功");
            result.setSuccess(true);
            result.setData(map);
            return result;
        }catch (Exception e){
            logger.error("获取开屏信息异常e=",e);
            return null;
        }
    }
}
