package cn.ourwill.tuwenzb.config;

import cn.ourwill.tuwenzb.UnPermissionExceptionMapper;
import cn.ourwill.tuwenzb.UnauthenticatedExceptionMapper;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import javax.ws.rs.ext.ExceptionMapper;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/6/30 0030 18:01
 * @Version1.0
 */
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(RequestContextFilter.class);
        register(MultiPartFeature.class);
        register(UnauthenticatedExceptionMapper.class);
        register(UnPermissionExceptionMapper.class);
        //配置restful package.
        packages("cn.ourwill.tuwenzb.controller");
        //表单字段多提交失败时使用
        //property(MessageProperties.XML_SECURITY_DISABLE,Boolean.TRUE);
    }
}
