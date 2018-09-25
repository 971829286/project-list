package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import cn.ourwill.tuwenzb.weixin.Utils.HttpURlUtils;
import cn.ourwill.tuwenzb.weixin.Utils.PageAuthorizeUtils;
import cn.ourwill.tuwenzb.weixin.Utils.PastUtil;
import cn.ourwill.tuwenzb.weixin.pojo.TokenReturn;
import cn.ourwill.tuwenzb.weixin.pojo.UserInfo;
import cn.ourwill.tuwenzb.weixin.pojo.UserInfoReturn;
import cn.ourwill.tuwenzb.weixin.pojo.UserTokenReturn;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

//
@Component
@Path("/")
public class IndexController {

	//微信开发者ID
	@Value("${weixin.appid}")
	private String appid;

	//开发者密码
	@Value("${weixin.appsecret}")
	private String secret;

	//获取access_token填写client_credential
	@Value("${weixin.grant_type}")
	private String grant_type;

	//授权后重定向的回调链接地址
	@Value("${weixin.redirect_uri}")
	private String redirect_uri;

	//静默授权后重定向的回调链接地址
	@Value("${weixin.redirect_uri_OPENID}")
	private String redirect_uri_OPENID;

	//应用授权作用域
	@Value("${weixin.scope}")
	private String scope;

	//网站应用ID
	@Value("${weixin.webappid}")
	private String webappid;

	//网站应用开发密码
	@Value("${weixin.webappsecret}")
	private String webappsecret;

	@Value("${weixin.index.path}")
	private String indexPath;

	@Value("${weixin.photo.index.path}")
	private String photoIndexPath;

	@Value("${system.domain}")
	private String domian;

	//ticket获取方式
	@Value("${weixin.accesstoken.mode}")
	private String mode;

	@Value("${weixin.accesstoken.opening}")
	private boolean opening;
	//是否是网站
	public boolean isWeb=false;

	@Autowired
	private IUserService userService;

	private static final Logger log = LogManager.getLogger(IndexController.class);

	@GET
	@Path("live{var:.*}")
	public Response index(@QueryParam("code") String code,@QueryParam("state")String state,@Context HttpServletRequest request, @Context HttpServletResponse response) {
		try {
			//判断session
			Integer userId = GlobalUtils.getUserId(request);
			String Url = null;
			String uri = request.getRequestURI();
			if(uri.contains("/live/news/")){
				Url = domian + request.getRequestURI().replace("live/news","live/?#/news");
				request.getSession().setAttribute("redirectUrl",Url);
			}else if(uri.contains("/live/home/")){
				Url = domian + uri.replace("live/home","live/?#/home");
				request.getSession().setAttribute("redirectUrl",Url);
			}else{
				Url = domian + uri;
			}
//			if(shareId!=null) {
//				shareUrl = domian + request.getRequestURI() + "/?#/news/" + shareId;
			//如果不为空则直接跳转
			if(userId!=null) {
				if (uri.contains("/live/news/")||uri.contains("/live/home/")) {
					log.info("重定向+++++++++++++++++++++");
					URI targetURIForRedirection = new URI(Url);
					return Response.temporaryRedirect(targetURIForRedirection).build();
				}
				return goIndex(request, response);
			}
			//判断是否是回调请求
			if(code!=null) {
				String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
				if (StringUtils.isEmpty(redirectUrl)){
					redirectUrl = domian + request.getRequestURI();
				}
//				log.info("beforeGetcode:"+URLDecoder.decode(redirectUrl,"UTF-8"));
				return getCode(code, state, URLDecoder.decode(redirectUrl,"UTF-8"), 0, request, response);
			}
			//判断是否来自微信浏览器
//			request.getRequestURL()
//			log.info("shareUrl:"+shareUrl);
			log.info("Url:"+Url);
			log.info("UrlEncoder:"+URLEncoder.encode(Url,"UTF-8"));
			if (request.getHeader("user-agent").indexOf("MicroMessenger") > 0) {
				//获取用户的openid
				String loginURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri=" + URLEncoder.encode(Url,"UTF-8") + "&response_type=code&scope=" + scope + "#wechat_redirect";
				log.info("loginUrl:"+ loginURL);
				response.sendRedirect(loginURL);
//				HttpURlUtils.getUrlResponse(loginURL);
//				return;
			}else if(uri.contains("/live/news/")||uri.contains("/live/home/")){
				log.info("非微信重定向+++++++++++++++++++++");
				URI targetURIForRedirection = new URI(Url);
				return Response.temporaryRedirect(targetURIForRedirection).build();
			}
			//不是来自微信
			//进入首页
			return goIndex(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			//进入首页
			return goIndex(request, response);
		}
	}

	@GET
	@Path("photolive{var:.*}")
	public Response photeIndex(@QueryParam("code") String code,@QueryParam("state")String state,@Context HttpServletRequest request, @Context HttpServletResponse response) {
		try {
			//判断session
			Integer userId = GlobalUtils.getUserId(request);
			//url 增加路由
            String Url = null;
            String uri = request.getRequestURI();
            if(uri.contains("/photolive/news/")){
                Url = domian + uri.replace("photolive/news","photolive/?#/news");
				request.getSession().setAttribute("redirectUrl",Url);
            }else if(uri.contains("/photolive/home/")){
				Url = domian + uri.replace("photolive/home","photolive/?#/home");
				request.getSession().setAttribute("redirectUrl",Url);
			}else {
                Url = domian + uri;
            }
			//如果不为空则直接跳转
			if(userId!=null){
				if (uri.contains("/photolive/news/")||uri.contains("/photolive/home/")) {
					log.info("重定向+++++++++++++++++++++");
					URI targetURIForRedirection = new URI(Url);
					return Response.temporaryRedirect(targetURIForRedirection).build();
				}
				return goPhoneIndex(request,response);
			}
			//判断是否是回调请求
			if(code!=null){
				String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
				if (StringUtils.isEmpty(redirectUrl)){
					redirectUrl = domian + request.getRequestURI();
				}
				return getCode(code,state,URLDecoder.decode(redirectUrl,"UTF-8"),1,request,response);
			}
			//判断是否来自微信浏览器
			if (request.getHeader("user-agent").indexOf("MicroMessenger") > 0) {
				//获取用户的openid
				String loginURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri=" + URLEncoder.encode(Url,"UTF-8") + "&response_type=code&scope=" + scope + "#wechat_redirect";
				log.info("loginUrl:"+ loginURL);
				response.sendRedirect(loginURL);
//				HttpURlUtils.getUrlResponse(loginURL);
//				return;
			}else if(uri.contains("/photolive/news/")||uri.contains("/photolive/home/")){
                log.info("非微信重定向+++++++++++++++++++++");
                URI targetURIForRedirection = new URI(Url);
                return Response.temporaryRedirect(targetURIForRedirection).build();
            }
			//不是来自微信
			//进入首页
			return goPhoneIndex(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			//进入首页
			return goPhoneIndex(request, response);
		}
	}

	/**
	 * 微信签到活动
	 * @param code
	 * @param state
	 * @param request
	 * @param response
	 * @return
	 */
	@GET
	@Path("hudong{var:.*}")
	public Response hudongIndex(@QueryParam("code") String code,@QueryParam("state")String state,@Context HttpServletRequest request, @Context HttpServletResponse response) {
		try {
			//判断session
			Integer userId = GlobalUtils.getUserId(request);
			//如果不为空则直接跳转
			if(userId!=null){
				return goHudongIndex(request,response);
			}
			//判断是否是回调请求
			if(code!=null){
				return getCode(code,state,null,2,request,response);
			}
			//判断是否来自微信浏览器
			String Url = domian+request.getRequestURI();
			if (request.getHeader("user-agent").indexOf("MicroMessenger") > 0) {
				//获取用户的openid
				String loginURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri=" + Url + "&response_type=code&scope=" + scope + "#wechat_redirect";
				log.info("loginUrl:"+ loginURL);
				response.sendRedirect(loginURL);
//				HttpURlUtils.getUrlResponse(loginURL);
//				return;
			}
			//不是来自微信
			//进入首页
			return goHudongIndex(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			//进入首页
			return goHudongIndex(request, response);
		}
	}

	@Path("index")
	@GET
	public String toIndex(@QueryParam("state") String state) {
		String userName = "index";
		return "welcome," + state;
	}

	//进入照片首页
	public Response goPhoneIndex(HttpServletRequest request, HttpServletResponse response) {
		try {
			//进入首页
////			String indexUrl = request.getContextPath() + "/dev/user.html";
//			String indexUrl = "http://live.tuwenzhibo.com/dev/user.html";
//			log.info("indexUrl:"+indexUrl);
////			request.getRequestDispatcher("http://dev.tuwenzhibo.com/user.html").forward(request,response);
//			response.sendRedirect(indexUrl);
			// Load your file content as byte.
			InputStream ins = new FileInputStream(photoIndexPath);
			byte[] fileContent = IOUtils.toByteArray(ins);
			return Response.ok(fileContent, MediaType.TEXT_HTML_TYPE).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//进入首页
	public Response goIndex(HttpServletRequest request, HttpServletResponse response) {
		try {
			//进入首页
////			String indexUrl = request.getContextPath() + "/dev/user.html";
//			String indexUrl = "http://live.tuwenzhibo.com/dev/user.html";
//			log.info("indexUrl:"+indexUrl);
////			request.getRequestDispatcher("http://dev.tuwenzhibo.com/user.html").forward(request,response);
//			response.sendRedirect(indexUrl);
			// Load your file content as byte.
			InputStream ins = new FileInputStream(indexPath);
			byte[] fileContent = IOUtils.toByteArray(ins);
			return Response.ok(fileContent, MediaType.TEXT_HTML_TYPE).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//进入弹幕发送
	public Response goHudongIndex(HttpServletRequest request, HttpServletResponse response) {
		try {
			//进入首页
////			String indexUrl = request.getContextPath() + "/dev/user.html";
//			String indexUrl = "http://live.tuwenzhibo.com/dev/user.html";
//			log.info("indexUrl:"+indexUrl);
////			request.getRequestDispatcher("http://dev.tuwenzhibo.com/user.html").forward(request,response);
//			response.sendRedirect(indexUrl);
			// Load your file content as byte.
			InputStream ins = new FileInputStream("/home/signPage/index.html");
			byte[] fileContent = IOUtils.toByteArray(ins);
			return Response.ok(fileContent, MediaType.TEXT_HTML_TYPE).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

//	//用于获取openid
//	@Path("/openid")
//	@GET
//	public void getOpenid(@QueryParam("openid") String openid, @QueryParam("refresh_token") String refresh_token, @Context HttpServletRequest request, @Context HttpServletResponse response) {
//		try {
//			if (refresh_token == null) {
//				User user = userService.selectByWechatNum(openid);
//				if (user != null) {
//					//refresh_token有效期是30天，30天(d)=2592000000毫秒(ms)-1小时
//					if (System.currentTimeMillis() - user.getRefreshTokenCTime().getTime() < 2591996400L) {
//						log.info("+++++++++126:1:"+System.currentTimeMillis()+"2:"+user.getRefreshTokenCTime());
//						//refresh_token有效
//						//使用refresh_token和openid获得用户AccessToken
//						UserTokenReturn userTokenReturn = PageAuthorizeUtils.refreshUserToken(user.getRefreshToken());
//						UserInfoReturn userInfoReturn = PageAuthorizeUtils.getUserInfo(userTokenReturn);
//						//将用户信息存入session
//						GlobalUtils.setSessionValue(request,"userId",user.getId());
//						//进入首页
//						goIndex(request, response);
//					} else { //refresh_token失效，重新进入授权页，并存储授权后的refresh_token
//						log.info("+++++++++136");
//						toAutonrizePage(request, response);
//					}
//				} else { //如果用户不存在，重新进入授权页，创建用户
//					log.info("+++++++++140");
//					toAutonrizePage(request, response);
//				}
//			} else { //存储refresh_token
//				log.info("+++++++++144");
//				User user = userService.selectByWechatNum(openid);
//				if (user != null) {
//					user.setRefreshToken(refresh_token);
//					user.setRefreshTokenCTime(new Date());
//					userService.updateRefreshToken(user);
//				} else {
//					//创建用户
//					user = new User();
//					//用户来源类型：0 微信  1 普通用户
//					user.setUserfromType(0);
//					user.setCTime(new Date());
//					user.setWechatNum(openid);
//					user.setRefreshToken(refresh_token);
//					user.setRefreshTokenCTime(new Date());
//					userService.save(user);
//				}
//				//使用refresh_token和openid获得用户AccessToken
//				UserTokenReturn userTokenReturn = PageAuthorizeUtils.refreshUserToken(user.getRefreshToken());
//				UserInfoReturn userInfoReturn = PageAuthorizeUtils.getUserInfo(userTokenReturn);
//				//将用户信息存入session
//				GlobalUtils.setSessionValue(request,"userId",user.getId());
//				//进入首页
//				goIndex(request, response);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			//进入首页
//			goIndex(request, response);
//		}
//	}

	//用于获取code
	@Path("/code")
	@GET
	public Response getCode(@QueryParam("code") String code,@QueryParam("state")String state,@QueryParam("redirectUrl") String redirectUrl,@QueryParam("photoLive") Integer photoLive,@Context HttpServletRequest request, @Context HttpServletResponse response) throws UnsupportedEncodingException {
		//通过code换取网页授权access_token
		//appid:公众号的唯一标识
		//secret:公众号的appsecret
		//code:填写第一步获取的code参数
		//grant_type:填写为authorization_code
		System.out.printf("code："+code);
		if(isWeb){
			appid=webappid;
		}
		System.out.printf("isWeb："+isWeb);
		if(state!=null&&state.equals("webapp")){
			appid=webappid;
		}
		System.out.printf("appid:"+appid);
		String getURL="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		System.out.printf("getURL:"+getURL);
		ObjectMapper mapper=new ObjectMapper();
		UserTokenReturn tokenReturn= null;
		try {
			String returnStr=HttpURlUtils.getUrlResponse(getURL);
			System.out.printf("returnStr:"+returnStr);
			if(returnStr.indexOf("errcode")<=0) {
				tokenReturn = mapper.readValue(returnStr, UserTokenReturn.class);
				UserInfoReturn userInfoReturn = null;
				List<User> users = userService.selectByWechatNum(tokenReturn.getOpenid());
				User user = new User();
				if(users.size()>1){
					for (User entity : users) {
						if(entity.getId().equals(32)){
							toAutonrizePage(request,response,redirectUrl);
						}
						if(entity.getUserfromType()==1) {
							user = entity;
//							if(StringUtils.isEmpty(user.getUnionId())) {
//								syncUnionId(tokenReturn, user);//刷新unionId
//								userService.updateUnionId(user.getId(), user.getUnionId());
//							}
							userService.updateLicenseType(user.getId());
							log.info("已有绑定微信用户：" + user.getWechatNum()+",存入session");
						}
//						if(entity.getUserfromType()==1){
//							userService.updateUnionId(entity.getId(),tokenReturn.);
//						}
					}
				}else if (users.size()==1){
					user = users.get(0);
					log.info("refresh_token有效");
//						tokenReturn = PageAuthorizeUtils.refreshUserToken(tokenReturn.getRefresh_token());
//					userInfoReturn = PageAuthorizeUtils.getUserInfo(tokenReturn);
//					user.setRefreshToken(tokenReturn.getRefresh_token());
//					user.setRefreshTokenCTime(new Date());
//					userService.updateRefreshToken(user);
					log.info("已有微信用户：" + user.getWechatNum()+",存入session");
//					syncUnionId(tokenReturn,user);//刷新unionId
//					if(user.get)
				} else {
					userInfoReturn = PageAuthorizeUtils.getUserInfo(tokenReturn);
//					if(userInfoReturn==null) {
//						log.info("该用户未授权，重新授权");
//						toAutonrizePage(request,response,redirectUrl);
//                        return null;
//					}
					if(GlobalUtils.getSessionValue(request,"test")==null) {
						GlobalUtils.setSessionValue(request,"test","have");
						log.info("该用户未授权，重新授权");
						toAutonrizePage(request,response,redirectUrl);
						return null;
					}
					//创建用户
					user = new User();
					//用户来源类型：0 微信  1 普通用户
					user.setUserfromType(0);
					user.setCTime(new Date());
					user.setAvatar(userInfoReturn.getHeadimgurl());
					user.setNickname(userInfoReturn.getNickname());
					user.setWechatNum(tokenReturn.getOpenid());
					user.setRefreshToken(tokenReturn.getRefresh_token());
					user.setRefreshTokenCTime(new Date());
					user.setLevel(0);
//					syncUnionId(tokenReturn,user);//刷新unionId
					log.info("创建用户："+user.getWechatNum());
					userService.save(user);
				}
				log.info("456");
				GlobalUtils.setSessionValue(request,"userId",user.getId());
				GlobalUtils.setSessionValue(request,"userLevel",user.getLevel());
				log.info("789");
			}else{
				log.info("+++++++++URI:"+request.getRequestURI());
				response.sendRedirect(domian+request.getRequestURI());
			}
			//进入首页
			log.info("===前");
			if(photoLive!=null&&photoLive.equals(1)){
				log.info("to photoIndex");
				return goPhoneIndex(request,response);
			}
			if(photoLive!=null&&photoLive.equals(2)){
				log.info("to hudongIndex");
				return goHudongIndex(request,response);
			}
			return goIndex(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			//进入首页
			return goIndex(request, response);
		}
	}

	//更新unionId
	private void syncUnionId(UserTokenReturn tokenReturn, User user) throws Exception {
		if(tokenReturn!=null && StringUtils.isNotEmpty(tokenReturn.getUnionid())){
			user.setUnionId(tokenReturn.getUnionid());
			return;
		}
		UserInfoReturn userInfoReturn = PageAuthorizeUtils.getUserInfoByOpenId(user.getWechatNum());
		if(userInfoReturn!=null&&StringUtils.isNotEmpty(userInfoReturn.getUnionid())){
			user.setUnionId(userInfoReturn.getUnionid());
		}
	}

	//进入授权页面
	public void toAutonrizePage(HttpServletRequest request, HttpServletResponse response,String redirectUrl) {
		try {
			//如果没有授权(首次登陆)，进入授权页面
			//redirect_uri:授权后重定向的回调链接地址
//			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
			//appid:公众号的唯一标识
			//response_type:返回类型，请填写code
			//state:重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
			//#wechat_redirect:无论直接打开还是做页面302重定向时候，必须带此参数
            log.info("重新授权");
            if (StringUtils.isEmpty(redirectUrl))
				redirectUrl = domian+request.getRequestURI();
            log.info("++++++++++++++++++++++++++++redirectUrl："+URLEncoder.encode(redirectUrl,"UTF-8")+"++++++++++++++++++++++++");
			String loginURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri=" + URLEncoder.encode(redirectUrl,"UTF-8") + "&response_type=code&scope=" + "snsapi_userinfo" + "#wechat_redirect";
			//如果用户同意授权，页面将跳转至 redirect_uri/?code=CODE&state=STATE。
			//code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
			response.sendRedirect(loginURL);
		} catch (Exception e) {
			e.printStackTrace();
			//进入首页
			goIndex(request, response);
		}
	}

	@Path("/wxConfig")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getWxConfig(@QueryParam("url") String url){
	    try {
            String ticket = PageAuthorizeUtils.getJsApiTicket();
            if(ticket==null){
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            }
            Map remap = PastUtil.sign(ticket, URLDecoder.decode(url, "utf-8"));
            remap.put("appId",appid);
            return ReturnResult.successResult("data",remap, ReturnType.GET_SUCCESS);
        }catch (Exception e){
	        log.error("IndexController.getWxConfig",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @Path("/getTicket")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map getTicket(){
		try {
			if(!opening){
				return ReturnResult.errorResult("本服务不对外提供此接口！");
			}
			String ticket = PageAuthorizeUtils.getJsApiTicket();
			if(ticket!=null) {
				return ReturnResult.successResult("ticket", ticket, ReturnType.GET_SUCCESS);
			}else{
				return ReturnResult.errorResult(ReturnType.GET_ERROR);
			}
		} catch (Exception e) {
			log.error("IndexController.getTicket",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	@Path("/getAccessToken")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map getAccessToken(){
		try {
			if(!opening){
				return ReturnResult.errorResult("本服务不对外提供此接口！");
			}
			String accessToken = PageAuthorizeUtils.getAccess_token(false);
			if(accessToken!=null) {
				return ReturnResult.successResult("accessToken", accessToken, ReturnType.GET_SUCCESS);
			}else{
				return ReturnResult.errorResult(ReturnType.GET_ERROR);
			}
		} catch (Exception e) {
			log.error("IndexController.getAccessToken",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	@Path("/getAccessToken/true")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Access(level = 1)
	public Map getAccessTokenTrue(){
		try {
			if(!opening){
				return ReturnResult.errorResult("本服务不对外提供此接口！");
			}
			String accessToken = PageAuthorizeUtils.getAccess_token(true);
			if(accessToken!=null) {
				return ReturnResult.successResult("accessToken", accessToken, ReturnType.GET_SUCCESS);
			}else{
				return ReturnResult.errorResult(ReturnType.GET_ERROR);
			}
		} catch (Exception e) {
			log.error("IndexController.getAccessToken",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//微信扫码登录
	//用于获取code
	@Path("/qrconnect")
	@GET
	public void getCode(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		try {
			String getURl = "https://open.weixin.qq.com/connect/qrconnect?appid="+webappid+"&redirect_uri="+redirect_uri+"&response_type=code&scope="+"snsapi_login"+"#wechat_redirect";
			isWeb=true;
			response.sendRedirect(getURl);
		}catch (Exception e){
			e.printStackTrace();
			//进入首页
			goIndex(request, response);
		}
	}


//	//将用户信息存入session
//	public void saveToSession(HttpServletRequest request, HttpServletResponse response, UserInfoReturn userInfoReturn,User user) {
//		log.info("存入session");
//		UserInfo userInfo = connectUserInfoReturn_User(userInfoReturn,user);
//		GlobalUtils.setSessionValue(request,"userAllInfo",userInfo);
//		GlobalUtils.setSessionValue(request,"userinfo", userInfoReturn);
//	}
//
//	//通过反射把UserInfoReturn的信息组合成UserInfo
//	private UserInfo connectUserInfoReturn_User(UserInfoReturn userInfoReturn,User user){
//		UserInfo userInfo=new UserInfo();
//		Field[] userInfoFields=userInfo.getClass().getFields();
//		Field[] userInfoReturnFields=userInfoReturn.getClass().getDeclaredFields();
//        Field[] userFields=user.getClass().getDeclaredFields();
//		try {
//			for (int i = 0; i < userInfoFields.length; i++) {
//				for (int j = 0; j < userInfoReturnFields.length; j++) {
//					if (userInfoFields[i].getName().equals(userInfoReturnFields[j].getName())) {
//						userInfoFields[i].setAccessible(true);
//						userInfoFields[i].set(userInfo, userInfoReturnFields[j].get(userInfoReturn));
//					}
//				}
//                for (int m = 0; m < userFields.length; m++) {
//                    if (userInfoFields[i].getName().equals(userFields[m].getName())) {
//                        userInfoFields[i].setAccessible(true);
//                        userInfoFields[i].set(userInfo, userFields[m].get(user));
//                    }
//                }
//			}
//			return userInfo;
//		}catch (Exception e){
//			e.printStackTrace();
//			return null;
//		}
//	}
}
