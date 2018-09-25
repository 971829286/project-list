package cn.ourwill.tuwenzb;

import cn.ourwill.tuwenzb.aop.UnPermissionException;
import cn.ourwill.tuwenzb.utils.ReturnResult;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/24 0024 10:18
 * @Version1.0
 */
@Provider
public class UnPermissionExceptionMapper implements ExceptionMapper<UnPermissionException> {

    @Override
    public Response toResponse(UnPermissionException exception) {
        return Response.status(403).entity(ReturnResult.errorResult("当前用户无权限操作！")).build();
    }
}
