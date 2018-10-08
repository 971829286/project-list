package com.souche.niu.spi;

import com.souche.user.hessian.model.UserObject;
import org.apache.http.Header;

import java.util.List;
import java.util.Map;

public interface MsgPushSPI {

    /**
     * @Description:
     * @Param1: msgPushUrl
     * @Param2: params
     * @Param3: headers
     * @return: java.lang.String
     * @Author: malin
     * @Date: 2018/9/28
     */
    String msgPushFunction(String msgPushUrl, Map<String, String> params,
                           List<Header> headers);

    /**
     * @Description:
     * @Param1: params
     * @Param2: appName
     * @return: com.souche.user.hessian.model.UserObject
     * @Author: malin
     * @Date: 2018/9/28
     */
    UserObject findByQuery(String params, String appName);
}
