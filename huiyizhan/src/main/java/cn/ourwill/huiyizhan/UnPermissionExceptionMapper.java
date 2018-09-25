package cn.ourwill.huiyizhan;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/24 0024 10:18
 * @Version1.0
 */
@ControllerAdvice
public class UnPermissionExceptionMapper{

    @ExceptionHandler(value=UnPermissionException.class)
    @ResponseBody
    public Map toResponse(UnPermissionException exception,HttpServletResponse response) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletResponse response =  attributes.getResponse();
        response.setStatus(403);
        return ReturnResult.errorResult("当前用户无权限操作！");
    }
}
