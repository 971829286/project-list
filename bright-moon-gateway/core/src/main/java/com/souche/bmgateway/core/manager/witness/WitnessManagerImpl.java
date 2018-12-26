package com.souche.bmgateway.core.manager.witness;

import com.netfinworks.dts.api.enums.WitnessIdentityEnum;
import com.netfinworks.dts.api.models.WitnessMemberInfoResponse;
import com.netfinworks.dts.api.service.WitnessMemberInfoService;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zs.
 * Created on 2018-12-14.
 */
@Service("witnessManager")
@Slf4j(topic = "manager")
public class WitnessManagerImpl implements WitnessManager {

    @Resource
    private WitnessMemberInfoService witnessMemberInfoService;

    @Override
    public WitnessMemberInfoResponse queryWitnessMemberInfo(String partnerId, String memberId) throws ManagerException {
        try {
            log.info("查询用户见证信息，请求参数：{}", memberId);
            long beginTime = System.currentTimeMillis();
            // TODO dts需要提供partnerId 查询
            WitnessMemberInfoResponse witnessMemberInfoResponse =
                    witnessMemberInfoService.queryWitnessMemberInfo(WitnessIdentityEnum.MEMBER_ID, memberId);
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("查询用户见证信息， 耗时:{} (ms); 响应结果:{} ", consumeTime, witnessMemberInfoResponse);
            if (!witnessMemberInfoResponse.isSuccess()) {
                throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "查询见证信息失败" + witnessMemberInfoResponse.getMsg());
            }
            return witnessMemberInfoResponse;
        } catch (ManagerException e) {
            if (e instanceof ManagerException) {
                throw e;
            }
            throw new ManagerException(e, ErrorCodeEnums.SYSTEM_ERROR);
        }
    }
}
