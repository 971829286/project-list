package cn.ourwill.huiyizhan;

import cn.ourwill.huiyizhan.aop.UnauthenticatedException;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.WillCenterConstants;
import com.sun.jndi.cosnaming.ExceptionMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/13 0013 18:08
 * @Version1.0
 */
@ControllerAdvice
public class UnauthenticatedExceptionMapper{
    @ExceptionHandler(value=UnauthenticatedException.class)
    @ResponseBody
    public Map toResponse(UnauthenticatedException exception,HttpServletResponse response) {
//        return Response.status(200).entity(ReturnResult.errorResult(ResultEnum.RELOGIN,"当前用户未登录！")).build();
        response.setStatus(401);
        return ReturnResult.errorResult("loginUrl",WillCenterConstants.getInstance().getLoginUrl(),"当前用户未登录！");
//        return "redirect:"+exception.getMessage();
    }
}
