package cn.ourwill.huiyizhan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/24 0024 12:08
 * @Version1.0
 */
@Controller
@ApiIgnore
public class SwaggerApi {
    /**
     * 跳转登录
     * @param
     * @return
     */

    @RequestMapping("/swaggerApi")
    public String toApi(){
        return "/api";
//        return "swagger-ui";
    }

    @RequestMapping("/doc")
    public String toApi2(){
        return "/doc";
    }
}
