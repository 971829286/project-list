package com.souche.bmgateway.core.dubbo.api.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于对请求参数进行校验
 *
 * @author zs.
 *         Created on 18/11/28.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DubboService {

    /*** 描述接口用途 ***/
    String desc() default "";

    /*** 是否检查bizProductCode ***/
    boolean checkProductCode() default false;
}
