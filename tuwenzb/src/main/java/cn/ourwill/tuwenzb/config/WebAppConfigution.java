package cn.ourwill.tuwenzb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/**
 * classpath路径：locations={"classpath:application-test1.xml","classpath:application-test2.xml"}
 * file路径： locations = {"file:d:/test/application-bean1.xml"};
 */
//配置资源文件
@Configuration
@ImportResource(locations={"classpath:*.xml"})
public class WebAppConfigution extends WebMvcConfigurerAdapter{
	//添加资源文件
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//资源访问路径
		String pathPatterns="/assets/**";
		//本地资源位置(src/main/resources目录代表的是classpath)
		String resourceLocations="classpath:/assets/";
		registry.addResourceHandler(pathPatterns).addResourceLocations(resourceLocations);
		super.addResourceHandlers(registry);
	}
	//添加拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
	}
}
