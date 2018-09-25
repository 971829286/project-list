package cn.ourwill.huiyizhan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/27 17:19
 * @Description
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://172.168.1.160:3000","http://172.168.1.157:3000","http://hyz.ourwill.cn","http://localhost:5081","http://127.0.0.1:3000","http://localhost:3000","http://huiyizhan.ourwill.cn:5081","http://huiyizhan.ourwill.cn")
                .allowCredentials(true)
                .allowedMethods("GET", "POST","PUT","DELETE","OPTIONS","PATCH")
                .maxAge(3600);
    }
}
