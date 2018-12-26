package com.souche.bmgateway.core.manager.weijin;

import com.google.common.base.Splitter;
import com.netfinworks.common.domain.Extension;
import com.netfinworks.common.domain.OperationEnvironment;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.optimus.common.config.OptimusConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * 用于根据不同的partnerId请求不同的维金系统
 *
 * @author zs.
 * Created on 2018-12-17.
 */
@Component("weijinCXFOutInterceptor")
@Slf4j(topic = "manager")
public class WeijinCXFOutInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

    private String weijinAddressMap = OptimusConfig.getValue("weijin.address");

    public WeijinCXFOutInterceptor() {
        super(Phase.PREPARE_SEND);
    }

    @Override
    public void handleMessage(SoapMessage soapMessage) throws Fault {
        Extension extension = getCurrentOperationEnvironment(soapMessage).getExtension();
        if (extension != null && StringUtils.isNotBlank(extension.getValue(Constants.PARTNER_ID))) {
            setWeijinAddress(soapMessage, extension);
        }
    }

    /**
     * 根据partnerId设置最终请求的维金地址
     *
     * @param soapMap
     * @param extension
     */
    private void setWeijinAddress(HashMap<String, Object> soapMap, Extension extension) {
        String partnerId = extension.getValue(Constants.PARTNER_ID);
        List<String> weijinAddressList = Splitter.on(";").trimResults().splitToList(weijinAddressMap);
        if (weijinAddressList != null && !weijinAddressList.isEmpty()) {
            for (String weijinAddress : weijinAddressList) {
                List<String> strings = Splitter.on("@").trimResults().splitToList(weijinAddress);
                if (partnerId.equals(strings.get(0))) {
                    log.info("请求webService，根据partnerId设置调用地址:{}", strings.get(1));
                    String originAddress = (String) soapMap.get("org.apache.cxf.message.Message.ENDPOINT_ADDRESS");
                    String address = originAddress.replaceFirst("^((http://)|(https://))?([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}(/)", strings.get(1));
                    soapMap.put("org.apache.cxf.message.Message.ENDPOINT_ADDRESS", address);
                }
            }
        }
    }

    /**
     * 获取请求参数环境信息
     *
     * @param soapMessage
     * @return
     */
    private OperationEnvironment getCurrentOperationEnvironment(SoapMessage soapMessage) {
        List<Object> params = soapMessage.getContent(List.class);
        Method[] methods = params.get(0).getClass().getMethods();
        for (Method method : methods) {
            if (method.getReturnType().isAssignableFrom(OperationEnvironment.class)) {
                try {
                    return (OperationEnvironment) method.invoke(params.get(0));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("反射获取请求参数异常, {}", e);
                }
            }
        }
        return new OperationEnvironment();
    }
}
