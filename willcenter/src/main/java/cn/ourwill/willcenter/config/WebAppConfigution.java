package cn.ourwill.willcenter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/**
 * classpath路径：locations={"classpath:application-test1.xml","classpath:application-test2.xml"}
 * file路径： locations = {"file:d:/test/application-bean1.xml"};
 */
//配置资源文件
@Configuration
public class WebAppConfigution extends WebMvcConfigurerAdapter{
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://127.0.0.1:3000","http://localhost:3000","http://huiyizhan.ourwill.cn:5081","http://huiyizhan.ourwill.cn","http://login.ourwill.cn")
				.allowCredentials(true)
				.allowedMethods("GET", "POST","PUT","DELETE","OPTIONS","PATCH")
				.maxAge(3600);
	}
}
