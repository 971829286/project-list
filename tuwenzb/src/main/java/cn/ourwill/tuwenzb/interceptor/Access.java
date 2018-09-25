package cn.ourwill.tuwenzb.interceptor;

import java.lang.annotation.*;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/12 0012 18:56
 * @Version1.0
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Access {

    int level() default 0;

}

