package cn.ourwill.huiyizhan;

import cn.ourwill.huiyizhan.aop.UnauthenticatedException;
import cn.ourwill.huiyizhan.aop.UnauthenticatedRedirectException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/13 0013 18:08
 * @Version1.0
 */
@ControllerAdvice
public class UnauthenticatedRedirectExceptionMapper {

    @ExceptionHandler(value=UnauthenticatedRedirectException.class)
    public String toResponse(UnauthenticatedRedirectException exception) {
//        return Response.status(200).entity(ReturnResult.errorResult(ResultEnum.RELOGIN,"当前用户未登录！")).build();
        return "redirect:"+exception.getMessage();
    }
}
