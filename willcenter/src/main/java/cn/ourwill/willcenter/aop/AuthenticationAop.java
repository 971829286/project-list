package cn.ourwill.willcenter.aop;

import cn.ourwill.willcenter.interceptor.Access;
import cn.ourwill.willcenter.utils.GlobalUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/13 0013 15:48
 * @Version1.0
 */
@Component
@Aspect
public class AuthenticationAop {
    private static final Logger log = LogManager.getLogger(AuthenticationAop.class);
    @Pointcut("execution(* cn.ourwill.willcenter.controller..*.*(..))")
    public void executeService(){

    }

    /**
     * 前置通知，方法调用前被调用
     * @param joinPoint
     */
    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request =  sra.getRequest();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 获取出方法上的Access注解
        Access access = method.getAnnotation(Access.class);
        if (access != null) {
            Integer userId = GlobalUtils.getUserId(request);
            Integer userLevel = (Integer) GlobalUtils.getSessionValue(request,"userLevel");
            log.info("AOP+++++userid:"+userId);
            log.info("AOP+++++userLevel:"+userLevel);
            if (userId == null || userLevel == null) {
                throw new UnauthenticatedException();
            }else if(access.level() == 1&&userLevel!=1){
                throw new UnPermissionException();
            }
        }
    }
}
