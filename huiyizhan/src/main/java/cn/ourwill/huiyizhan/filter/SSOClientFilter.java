package cn.ourwill.huiyizhan.filter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 已作废 功能移至AuthenticationAop
 */
//@WebFilter(filterName="SSOClientFilter",urlPatterns="/123123123*")
public class SSOClientFilter implements Filter {

    @Value("${loginServer.willCenter.login}")
    private String loginUrl;
    @Value("${loginServer.willCenter.ticket}")
    private String checkTicket;
	
    public void destroy() {
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String uuid = (String) session.getAttribute("UUID");
        String ticket = request.getParameter("ticket");
        String url = URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");

        //判断用户是否已经登录oa系统
        if (null == uuid) {
        	//1.判断用户是否有认证凭据--ticket（认证中心生成）
            if (null != ticket && !"".equals(ticket)) {
            	try {
            		HttpClient httpClient = new HttpClient();
                    PostMethod postMethod = new PostMethod(checkTicket);
                    //给url添加新的参数
                    postMethod.addParameter("ticket", ticket);
                	//通过httpClient调用SSO Server中的TicektServlet，验证ticket是否有效
                    httpClient.executeMethod(postMethod);
                    //将HTTP方法的响应正文（如果有）返回为String
                    uuid = postMethod.getResponseBodyAsString();
                    //释放此HTTP方法正在使用的连接
                    postMethod.releaseConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //2.判断认证凭据是否有效
                if (null != uuid && !"".equals(uuid)) {
                	//session设置用户名，说明用户登录成功了
//                    System.out.println("登录成功："+uuid);
                    session.setAttribute("UUID", uuid);
                    filterChain.doFilter(request, response);
                } else {
                    response.sendRedirect(loginUrl+"?service=" + url);
                }

            } else {//第一次访问oa系统，需要到sso-server系统验证
                response.sendRedirect(loginUrl+"?service=" + url);
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }


    public void init(FilterConfig arg0) throws ServletException {
    }

    

}