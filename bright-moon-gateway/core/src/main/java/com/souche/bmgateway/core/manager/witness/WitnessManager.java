package com.souche.bmgateway.core.manager.witness;

import com.netfinworks.dts.api.models.WitnessMemberInfoResponse;
import com.souche.bmgateway.core.exception.ManagerException;

/**
 * @author zs.
 * Created on 2018-12-14.
 */
public interface WitnessManager {

    /**
     * 查询交易见证信息
     *
     * @param memberId
     * @return
     */
    WitnessMemberInfoResponse queryWitnessMemberInfo(String partnerId, String memberId) throws ManagerException;
}
