package cn.ourwill.huiyizhan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/24 0024 10:37
 * @Version1.0
 */
@EnableSwagger2
@Configuration
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("cn.ourwill.huiyizhan.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
//                .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
                .title("韦尔科技-会议站 API 说明")
                .termsOfServiceUrl("http://huiyizhan.com/")
                //创建人
                .contact("linjixuan")
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }
}
