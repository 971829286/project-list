package com.souche.bmgateway.core.service.interceptor;

import com.souche.bmgateway.core.manager.weijin.CurrentOperationEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zs.
 * Created on 2018-12-18.
 */
@Component("servicePartnerInterceptor")
@Slf4j(topic = "service")
public class ServicePartnerInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        log.info("执行拦截器开始：ServicePartnerInterceptor设置partnerId...");
        Object arguments = methodInvocation.getArguments()[0];
        Method method = arguments.getClass().getMethod("getPartnerId", null);
        String partnerId = (String) method.invoke(arguments, null);
        CurrentOperationEnvironment.setEnv(partnerId);
        log.info("执行拦截器结束：ServicePartnerInterceptor设置partnerId..." + partnerId);
        return methodInvocation.proceed();
    }
}
