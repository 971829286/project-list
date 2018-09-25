package cn.ourwill.tuwenzb.aop;

import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.weixin.pojo.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
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
import java.util.HashMap;
import java.util.Map;


/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/13 0013 15:48
 * @Version1.0
 */
@Component
@Aspect
public class AuthenticationAop {
    private static final Logger log = LogManager.getLogger(AuthenticationAop.class);
    @Pointcut("execution(* cn.ourwill.tuwenzb.controller..*.*(..))")
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
            String appToken = request.getParameter("appToken");
            log.info("AOP+++++userid:"+userId);
            log.info("AOP+++++userLevel:"+userLevel);
            if (userId == null || userLevel == null) {
                if(StringUtils.isNotEmpty(appToken)){
                    HashMap<String,Integer> userInfo = (HashMap<String,Integer>)RedisUtils.get("APPTOKEN:"+appToken);
                    if(userInfo!=null&& userInfo.get("userId")!=null && userInfo.get("userLevel")!=null){
                        GlobalUtils.setSessionValue(request,"userId",userInfo.get("userId"));
                        GlobalUtils.setSessionValue(request, "userLevel", userInfo.get("userLevel"));
                    }else{
                        throw new UnauthenticatedException();
                    }
                }else {
                    throw new UnauthenticatedException();
                }
            }else if(access.level() == 1&&userLevel!=1){
                throw new UnPermissionException();
            }
        }
    }
}
