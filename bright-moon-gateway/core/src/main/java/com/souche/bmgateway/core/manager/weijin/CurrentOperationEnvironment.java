package com.souche.bmgateway.core.manager.weijin;

import com.netfinworks.common.domain.Extension;
import com.netfinworks.common.domain.OperationEnvironment;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.enums.PartnerEnums;
import org.apache.commons.lang.StringUtils;

/**
 * 调用维金环境类
 *
 * @author zs.
 *         Created on 18/7/19.
 */
public class CurrentOperationEnvironment {

    private final static ThreadLocal<OperationEnvironment> operationEnvironment = new ThreadLocal<OperationEnvironment>(){
        @Override
        protected OperationEnvironment initialValue() {
            OperationEnvironment operationEnvironment = new OperationEnvironment();
            operationEnvironment.setClientId("bmgateway");
            return operationEnvironment;
        }
    };

    public static OperationEnvironment getOperationEnvironment() {
        return operationEnvironment.get();
    }

    /**
     * 设置请求维金的地址
     *
     * @param partnerId
     */
    public static void setEnv(String partnerId) {
        Extension extension = operationEnvironment.get().getExtension() != null
                ? operationEnvironment.get().getExtension()
                : new Extension();
        extension.add(Constants.PARTNER_ID, partnerId);
        operationEnvironment.get().setExtension(extension);
    }

    public static boolean isNeedWitness(){
        Extension extension = operationEnvironment.get().getExtension();
        if (extension == null || StringUtils.isBlank(extension.getValue(Constants.PARTNER_ID))) {
            return false;
        }
        return PartnerEnums.isNeedWitness(extension.getValue(Constants.PARTNER_ID));
    }

}
