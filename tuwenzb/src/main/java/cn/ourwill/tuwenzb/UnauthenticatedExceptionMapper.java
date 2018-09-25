package cn.ourwill.tuwenzb;

import cn.ourwill.tuwenzb.aop.UnauthenticatedException;
import cn.ourwill.tuwenzb.utils.ResultEnum;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/13 0013 18:08
 * @Version1.0
 */
@Provider
public class UnauthenticatedExceptionMapper implements ExceptionMapper<UnauthenticatedException> {

    @Override
    public Response toResponse(UnauthenticatedException exception) {
        return Response.status(200).entity(ReturnResult.errorResult(ResultEnum.RELOGIN,"当前用户未登录！")).build();
    }
}
