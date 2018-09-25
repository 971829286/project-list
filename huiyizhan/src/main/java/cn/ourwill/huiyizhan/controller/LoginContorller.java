package cn.ourwill.huiyizhan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/24 0024 15:32
 * @Version1.0
 */
@RestController
@RequestMapping("/auth")
@ApiIgnore
public class LoginContorller {

    @RequestMapping(value = "/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "http://huiyizhan.ourwill.cn/swaggerApi";
        response.sendRedirect("http://login.ourwill.cn:5080/login?service=" + url);
    }
}
