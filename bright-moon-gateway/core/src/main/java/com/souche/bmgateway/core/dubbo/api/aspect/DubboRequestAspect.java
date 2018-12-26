package com.souche.bmgateway.core.dubbo.api.aspect;

import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.core.util.ParamsValidate;
import com.souche.bmgateway.core.domain.TradeSceneConfDO;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.service.config.TradeSceneConfService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author zs.
 *         Created on 18/11/28.
 */
@Component
@Aspect
@Slf4j(topic = "dubbo.aspect")
public class DubboRequestAspect {

    @Resource
    private TradeSceneConfService tradeSceneConfService;

    @Pointcut("@annotation(com.souche.bmgateway.core.dubbo.api.aspect.DubboService)")
    public void dubboAspect() {
    }

    @Around("dubboAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            long beginTime = System.currentTimeMillis();

            //请求日志打印
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            Method method = joinPoint.getTarget().getClass().getMethod(methodName, joinPoint.getArgs()[0].getClass());
            DubboService annotation = method.getAnnotation(DubboService.class);
            log.info("请求dubbo服务: {} - {}, {}请求参数：{}, 请求时间:{} (ms)", className, methodName, annotation.desc(),
                    joinPoint.getArgs()[0], beginTime);

            //请求参数校验
            Class<?> responseClazz = method.getReturnType();
            Method createRespMethod = responseClazz.getMethod("createFailResp", CommonResponse.class, String.class, String.class);
            ParamsValidate.ParamsValidateResult<Object> validateResult = ParamsValidate.validate(joinPoint.getArgs()[0]);
            if (validateResult.hasError()) {
                log.error(annotation.desc() + ErrorCodeEnums.ILLEGAL_ARGUMENT.getMessage() + validateResult.getMsgError());
                return createRespMethod.invoke(null, responseClazz.newInstance(),
                        ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(),
                        ErrorCodeEnums.ILLEGAL_ARGUMENT.getMessage() + validateResult.getMsgError());
            }

            //检查产品码是否是已配置的，未配置产品码则不允许交易
            if (annotation.checkProductCode()) {
                String bizProductCode = (String) joinPoint.getArgs()[0].getClass().getMethod("getBizProductCode")
                        .invoke(joinPoint.getArgs()[0]);
                TradeSceneConfDO tradeSceneConfDO = tradeSceneConfService.queryTradeConf(bizProductCode);
                if (tradeSceneConfDO == null) {
                    return createRespMethod.invoke(null, responseClazz.newInstance(),
                            ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(),
                            ErrorCodeEnums.ILLEGAL_ARGUMENT.getMessage() + "未找到有效的业务产品码" + bizProductCode);
                }
            }

            //服务调用
            Object proceed = joinPoint.proceed();

            //响应日志打印
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("请求dubbo服务: {} - {}, {}响应结果:{}, 耗时:{} (ms)", className, methodName, annotation.desc(),
                    proceed, consumeTime);

            return proceed;
        } catch (Throwable throwable) {
            log.error("调用dubbo服务异常" + ReflectionToStringBuilder.toString(joinPoint.getArgs()[0]) + throwable);
            return CommonResponse.createFailResp(ErrorCodeEnums.SYSTEM_ERROR.getCode(), ErrorCodeEnums
                    .ILLEGAL_ARGUMENT.getMessage());
        }
    }
}
