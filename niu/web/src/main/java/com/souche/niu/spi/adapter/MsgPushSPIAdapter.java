package com.souche.niu.spi.adapter;

import com.souche.niu.spi.MsgPushSPI;
import com.souche.optimus.common.util.http.HttpClientPostUtil;
import com.souche.user.hessian.api.UserAPI;
import com.souche.user.hessian.model.UserObject;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: niu
 * @ClassName: MsgPushSPI
 * @description: 消息推送调用接口
 * @author: malin
 * @create: 2018-09-28 09:58
 **/
@Component
public class MsgPushSPIAdapter implements MsgPushSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgPushSPIAdapter.class);

    @Autowired
    private UserAPI userAPI;

    /**
     * @Description:
     * @Param1: msgPushUrl
     * @Param2: params
     * @Param3: headers
     * @return: java.lang.String
     * @Author: malin
     * @Date: 2018/9/28
     */
    @Override
    public String msgPushFunction(String msgPushUrl, Map<String, String> params,
                                  List<Header> headers) {
        try {
            LOGGER.info("消息推送调用接口 msgPushFunction,调用参数 msgPushUrl={},params={},headers={}",
                    msgPushUrl, params, headers);
            String result = HttpClientPostUtil.postUrl(msgPushUrl, params, headers);
            return result;
        } catch (Exception e) {
            LOGGER.error("消息推送调用url接口异常", e);
            return null;
        }
    }

    /**
     * @Description:
     * @Param1: params
     * @Param2: appName
     * @return: com.souche.user.hessian.model.UserObject
     * @Author: malin
     * @Date: 2018/9/28
     */
    @Override
    public UserObject findByQuery(String params, String appName) {
        LOGGER.info("查询用户ID方法名 findByQuery params={},appName={}", params, appName);
        try {
            UserObject userObject = userAPI.findOne(params, appName);
            return userObject;
        } catch (Exception e) {
            LOGGER.error("查询User异常", e);
        }
        return null;
    }

}
