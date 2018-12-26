package com.souche.bmgateway.core.manager.umpay;

import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.model.AuthRealNameResponse;

/**
 * @author zs.
 *         Created on 18/8/4.
 */
public interface AuthRealNameManager {

    /**
     * 联动实名认证卡号和姓名是否正确
     *
     * @param orderId
     * @param realName
     * @param idCard
     * @return
     * @throws ManagerException
     */
    AuthRealNameResponse realNameAuth(String orderId, String realName, String idCard) throws ManagerException;

}
