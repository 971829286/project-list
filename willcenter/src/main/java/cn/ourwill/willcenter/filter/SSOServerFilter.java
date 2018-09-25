package cn.ourwill.willcenter.filter;

import cn.ourwill.willcenter.entity.User;
import cn.ourwill.willcenter.utils.GlobalUtils;
import cn.ourwill.willcenter.utils.RedisUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;


/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/16 0016 10:53
 * @Version1.0
 */

@WebFilter(filterName="SSOServerFilter",urlPatterns={"/login","/checkLogin"})
public class SSOServerFilter extends HttpServlet implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String service = request.getParameter("service");
        Cookie[] cookies = request.getCookies();
        String cookieId = "";
        //判断用户是否已经登录认证中心
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if ("sso".equals(cookie.getName())) {
                    cookieId = cookie.getValue();
                    break;
                }
            }
        }

        //实现一处登录处处登录
        User user = (User) RedisUtils.get("cookie:" + cookieId);
        String logout = null;
        if (user != null)
            logout = (String) RedisUtils.get("logout:" + user.getUUID());
//        JSONObject jsonObject= JSONObject.fromObject(userString);
//        User user=(User)JSONObject.toBean(jsonObject, User.class);
        if (StringUtils.isNotEmpty(service) && null != user && logout == null) {
            String ticket = GlobalUtils.generateTicket(user);
//            HttpClient httpClient = new HttpClient();
//            GetMethod get = new GetMethod(service);
//            get.addRequestHeader("ticket", ticket);
//            httpClient
            StringBuilder url = new StringBuilder();
            url.append(service);
            if (0 <= service.indexOf("?")) {
                url.append("&");
            } else {
                url.append("?");
            }
            //返回给用户一个认证的凭据--ticket
            url.append("ticket=").append(ticket);
//            response.setHeader("ticket", ticket);
            if(request.getRequestURI().contains("checkLogin")){
                request.getSession().setAttribute("isLogin",url.toString());
                filterChain.doFilter(servletRequest, servletResponse);
            }else {
                response.sendRedirect(url.toString());
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
