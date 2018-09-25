package cn.ourwill.tuwenzb.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.LicenseRecord;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IActivityService;
import cn.ourwill.tuwenzb.service.ILicenseRecordService;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.utils.*;
import cn.ourwill.tuwenzb.weixin.pojo.UserInfoReturn;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.pagehelper.PageHelper;
import cn.ourwill.tuwenzb.entity.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/user")
public class UserController {
	@Autowired
	private IUserService userService;

	@Autowired
    private IActivityService activityService;

	@Autowired
	private ILicenseRecordService licenseRecordService;

	private static final Logger log = LogManager.getLogger(UserController.class);

//	@Value("${user.discounts.day}")
//	private String discountDay;

	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map save(User user){
		try {
			user.setCTime(new Date());
			if(userService.save(user)>0){
				return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
			}
			return ReturnResult.errorResult(ReturnType.ADD_ERROR);
		}catch (Exception e){
			log.error("UserController.save",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}
	@Path("/{id}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map update(@PathParam("id") Integer id, User user,@Context HttpServletRequest request){
		try {
			Integer userId = GlobalUtils.getUserId(request);
			user.setId(id);
			user.setUTime(new Date());
			user.setUId(userId);
			if(userService.update(user)>0) {
				return ReturnResult.successResult("data",userService.getById(id),ReturnType.UPDATE_SUCCESS);
			}
			return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
		}catch (Exception e) {
			log.error("UserController.update",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	@Path("/updateActiveUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map updateActiveUser(User user,@Context HttpServletRequest request){
		try {
			Integer userId = GlobalUtils.getUserId(request);
			user.setId(userId);
			user.setUTime(new Date());
			user.setUId(userId);
			if(userService.update(user)>0) {
				return ReturnResult.successResult("data",userService.getById(userId),ReturnType.UPDATE_SUCCESS);
			}
			return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
		}catch (Exception e) {
			log.error("UserController.updateActiveUser",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	@POST
	@Path("/changePWD/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Access(level = 1)
	public Map changePWDManager(@Context HttpServletRequest request,@PathParam("userId") Integer userId, Map params){
		try{
//			Integer userInfo = GlobalUtils.getUserId(request);
//			User user = userService.getById(userInfo);
//			if(user.getLevel()!=1){
//				return ReturnResult.errorResult("当前用户为非管理员用户，无权修改");
//			}
			if(params==null) return ReturnResult.errorResult("请填写修改信息!");
			String password = (String) params.get("password");
			String confirmPasswore = (String) params.get("confirmPassword");
			if(StringUtils.isEmpty(password)){
				return ReturnResult.errorResult("密码为空!");
			}
			if(!password.equals(confirmPasswore)){
				return ReturnResult.errorResult("两次密码不一致!");
			}
			String salt = GlobalUtils.getRandomString(6);
			String newPawwword = GlobalUtils.getMD5(password+salt);
			if(userService.changePWD(userId,newPawwword,salt)>0){
				return ReturnResult.successResult("修改密码成功");
			}
			return ReturnResult.errorResult("修改密码失败，请稍后再试");
		}catch (Exception e){
			log.error("UserController.changePWDManager",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	@POST
	@Path("/changePWD")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map changePWD(@Context HttpServletRequest request,Map params){
		try{
			if(params==null) return ReturnResult.errorResult("请填写修改信息!");
			String oldPassword = (String) params.get("oldPassword");
			String password = (String) params.get("password");
			String confirmPasswore = (String) params.get("confirmPassword");
			if(StringUtils.isEmpty(oldPassword)){
				return ReturnResult.errorResult("旧密码为空!");
			}
			if(StringUtils.isEmpty(password)){
				return ReturnResult.errorResult("密码为空!");
			}
			if(!password.equals(confirmPasswore)){
				return ReturnResult.errorResult("两次密码不一致!");
			}
			Integer userId = GlobalUtils.getUserId(request);
			User user = userService.getById(userId);
			if(!user.getPassword().equals(GlobalUtils.getMD5(oldPassword+user.getSalt()))) {
				return ReturnResult.errorResult("旧密码不正确!");
			}
			String salt = GlobalUtils.getRandomString(6);
			String newPawwword = GlobalUtils.getMD5(password+salt);
			if(userService.changePWD(userId,newPawwword,salt)>0){
				return ReturnResult.successResult("修改密码成功");
			}
			return ReturnResult.errorResult("修改密码失败，请稍后再试");
		}catch (Exception e){
			log.error("UserController.changePWD",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	@POST
	@Path("/resetPWD")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map resetPWD(@Context HttpServletRequest request,Map params){
		try{
			if(params==null) return ReturnResult.errorResult("请填写修改信息!");

			String password = (String) params.get("password");
			String mobPhone = (String) params.get("mobPhone");
			Integer smsCode = (Integer) params.get("smsCode");
			if(StringUtils.isEmpty(password)){
				return ReturnResult.errorResult("密码为空!");
			}
			User user = userService.setectByMobPhone(mobPhone);
			if(user==null||StringUtils.isEmpty(mobPhone)||!mobPhone.equals(user.getMobPhone())){
				return ReturnResult.errorResult("手机号不正确！");
			}
			Integer RealSmsCode = (Integer) RedisUtils.get("smsCode"+mobPhone);
			if(smsCode==null||RealSmsCode==null||!smsCode.equals(RealSmsCode)){
				return ReturnResult.errorResult("验证码输入有误！");
			}

			String salt = GlobalUtils.getRandomString(6);
			String newPawwword = GlobalUtils.getMD5(password+salt);
			if(userService.changePWD(user.getId(),newPawwword,salt)>0){
				return ReturnResult.successResult("修改密码成功");
			}
			return ReturnResult.errorResult("修改密码失败，请稍后再试");
		}catch (Exception e){
			log.error("UserController.resetPWD",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	@Path("/{id}")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Access(level = 1)
	public Map getById(@PathParam("id") Integer id,@Context HttpServletRequest request){
		try {
			User user = userService.getById(id);
			if(user!=null){
				return ReturnResult.successResult("data",user,ReturnType.GET_SUCCESS);
			}
			return ReturnResult.errorResult(ReturnType.GET_ERROR);
		}catch (Exception e){
			log.error("UserController.getById",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map<String, Object> findByParam(@QueryParam("pageNum") Integer pageNum, @QueryParam("pageSize") Integer pageSize, Map params){
	    //开始分页,pageNum:页码   pageSize:每页显示数量
		if(pageNum==null){
			pageNum=1;
		}
		if(pageSize==null){
			pageSize=10;
		}
		if(params==null) params = new HashMap();
		PageHelper.startPage(pageNum,pageSize);
		PageHelper.orderBy(" id desc");
		params.put("userfromType",1);
		PageInfo page = new PageInfo<User>(userService.selectByParams(params));
		return ReturnResult.successResult("data",page,ReturnType.GET_SUCCESS);
	}

	@Path("/{id}")
	@DELETE
	@Access
	public Integer delete(@PathParam("id") Integer id){
		return userService.delete(id);
	}

	//用于微信端获取用户数据
	@Path("/getUserinfo")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map<String, Object> getUserinfo(@Context HttpServletRequest request, @Context HttpServletResponse response,@QueryParam("photoLive")@DefaultValue("0") Integer photoLive){
		try {
			Integer userId = GlobalUtils.getUserId(request);
			User user = userService.getById(userId);
//			List<LicenseRecord> licenseRecords = licenseRecordService.selectByUserId(userId);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//			Date date = sdf.parse(discountDay);
//			if(licenseRecords!=null&&licenseRecords.size()>0){
				user.setDiscounts(false);
//			}else if(user.getCTime().before(date)){
//				user.setDiscounts(false);
//			}else{
//				user.setDiscounts(true);
//				user.setDiscountsType(4);
//			}
			if(photoLive.equals(1)){
				userService.getCounts(user,photoLive);
			}
			if (user != null) {
				return ReturnResult.successResult("data", user, ReturnType.GET_SUCCESS);
			}
			return ReturnResult.errorResult(ReturnType.GET_ERROR);
		}catch (Exception e){
			log.error("UserController.getUserinfo",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//用于微信端获取用户数据(测试用)
	@Path("/getUserinfoTest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> getUserinfotest(@Context HttpServletRequest request, @Context HttpServletResponse response){
		try {
			User user = userService.selectByUsername("test");
			if (user != null) {
				UserInfoReturn userInfoReturn = new UserInfoReturn();
				GlobalUtils.setSessionValue(request, "userId", user.getId());
				return ReturnResult.successResult("data",user,ReturnType.GET_SUCCESS);
			}
			return ReturnResult.errorResult(ReturnType.GET_ERROR);
		}catch (Exception e){
			log.error("UserController.getUserinfoTest",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

    //获取用户活动列表
    @Path("/getActivity")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	@Access
    public Map getActivity(@Context HttpServletRequest request,@QueryParam("page") Integer page,@QueryParam("pageNum")@DefaultValue("1") Integer pageNum,
						   @QueryParam("pageSize")@DefaultValue("10") Integer pageSize,@QueryParam("orderBy") Integer orderBy,@DefaultValue("0") @QueryParam("photoLive") Integer photoLive){
        try {
			//从Session中获取用户id
			Integer userId = GlobalUtils.getUserId(request);
			if (userId != null) {
				if(orderBy!=null&&orderBy==0) {
					PageHelper.orderBy(" c_time desc");
				}else if(orderBy!=null&&orderBy==1){
					PageHelper.orderBy(" c_time asc");
				}else{
					PageHelper.orderBy(" priority desc");
				}
				if(page!=null&&page==1) {
					PageHelper.startPage(pageNum,pageSize);
					PageInfo<Activity> pages = new PageInfo<>(activityService.selectByUserId(userId,photoLive));
					return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
				}
				List<Activity> activityList = activityService.selectByUserId(userId,photoLive);
				return ReturnResult.successResult("data",activityList,ReturnType.GET_SUCCESS);
			}
			return ReturnResult.errorResult(ReturnType.GET_ERROR);
		}catch (Exception e){
			log.error("UserController.getActivity",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
    }

	//用户登录(电脑端)
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map login(@Context HttpServletRequest request,Map params){

		//图形验证码使用一次后，无论正确与否，从session中清除

		String verifyCodeSession = (String) GlobalUtils.getSessionValue(request, "verifyCode");
		if(StringUtils.isNotEmpty(verifyCodeSession)){
			GlobalUtils.removeSessionValue(request, "verifyCode");
		}

		try {
			if(params==null) return ReturnResult.errorResult("请填写登录信息!");
			String username = (String) params.get("username");
			String password = (String) params.get("password");
			if(StringUtils.isEmpty(username)){
				return ReturnResult.errorResult("用户名为空!");
			}
			if(StringUtils.isEmpty(password)){
				return ReturnResult.errorResult("密码为空!");
			}
			String verifyCode = (String) params.get("verifyCode");
			if(StringUtils.isEmpty(verifyCode)){
				return ReturnResult.errorResult("验证码为空!");
			}
			if(StringUtils.isNotEmpty(verifyCode) && !verifyCode.equals(verifyCodeSession)){
				return ReturnResult.errorResult("验证码错误!");
			}
			User user=userService.selectByUsernameOrPhone(username);
			if(user!=null){
				if(user.getPassword().equals(GlobalUtils.getMD5(password+user.getSalt()))) {
					GlobalUtils.setSessionValue(request,"userId",user.getId());
					GlobalUtils.setSessionValue(request, "userLevel", user.getLevel());
					log.info("登录 session："+ request.getSession().getId());
					userService.updateLicenseType(user.getId());
					return ReturnResult.successResult("data",user,"登录成功!");
				}
			}
			return ReturnResult.errorResult("用户名或密码错误!");
		}catch (Exception e){
			log.error("UserController.login",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//用户登录(电脑端)
	@Path("/loginByApp")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map loginByApp(@Context HttpServletRequest request,Map params){
		try {
			if(params==null) return ReturnResult.errorResult("请填写登录信息!");
			String username = (String) params.get("username");
			String password = (String) params.get("password");
			if(StringUtils.isEmpty(username)){
				return ReturnResult.errorResult("用户名为空!");
			}
			if(StringUtils.isEmpty(password)) {
				return ReturnResult.errorResult("密码为空!");
			}
			User user=userService.selectByUsernameOrPhone(username);
			if(user!=null){
				if(user.getPassword().equals(GlobalUtils.getMD5(password+user.getSalt()))) {
					String apptoken = GlobalUtils.generateUUID();
					Map<String,Integer> userInfo = new HashMap<>();
					userInfo.put("userId",user.getId());
					userInfo.put("userLevel",user.getLevel());
					RedisUtils.set("APPTOKEN:"+apptoken,userInfo,30,TimeUnit.DAYS);
					user.setAppToken(apptoken);
					GlobalUtils.setSessionValue(request,"userId",user.getId());
					GlobalUtils.setSessionValue(request, "userLevel", user.getLevel());
					log.info("登录 session："+ request.getSession().getId());
					userService.updateLicenseType(user.getId());
					return ReturnResult.successResult("data",user,"登录成功!");
				}
			}
			return ReturnResult.errorResult("用户名或密码错误!");
		}catch (Exception e){
			log.error("UserController.login",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//用户退出(电脑端)
	@Path("/logout")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map logout(@Context HttpServletRequest request){
		try {
			log.info("登出 session："+ request.getSession().getId());
			request.getSession().invalidate();
			return ReturnResult.successResult("退出成功!");
		}catch (Exception e){
			log.error("UserController.logout",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//添加用户
	@Path("/addUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Access(level=1)
	public Map addUser(@Context HttpServletRequest request,Map params){
		try {
			Integer userInfo = GlobalUtils.getUserId(request);
			User userLogin = userService.getById(userInfo);
			if(userLogin.getLevel()!=1){
				return ReturnResult.errorResult("当前用户为非管理员用户，无权创建");
			}
			if(params==null) return ReturnResult.errorResult("请填写登录信息!");
			String username = (String) params.get("username");
			String password = (String) params.get("password");
			String confirmPasswore = (String) params.get("confirmPassword");
			String mobPhone = (String) params.get("mobPhone");
			if(StringUtils.isEmpty(username)){
				return ReturnResult.errorResult("用户名为空!");
			}
			if(StringUtils.isEmpty(password)){
				return ReturnResult.errorResult("密码为空!");
			}
			if(!password.equals(confirmPasswore)){
				return ReturnResult.errorResult("两次密码不一致!");
			}
			if(userService.selectByUsername(username)!=null){
				return ReturnResult.errorResult("用户名已存在！");
			}
			if(userService.setectByMobPhone(mobPhone)!=null){
				return ReturnResult.errorResult("手机号已被注册！");
			}
			String salt = GlobalUtils.getRandomString(6);
			Class<User> userBean = User.class;
			User user = userBean.newInstance();
			BeanUtils.populate(user,params);
			String newPawwword = GlobalUtils.getMD5(password+salt);
			user.setPassword(newPawwword);
			user.setSalt(salt);
			user.setUserfromType(1);
			user.setCTime(new Date());
			if(userService.save(user)>0){
				return ReturnResult.successResult("注册成功!");
			}
			return ReturnResult.errorResult("注册失败！");
		}catch (Exception e){
			log.error("UserController.register",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//用户注册
	@Path("/registerByWeChat")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map registerByWeChat(@Context HttpServletRequest request,Map params){
		try {
			if(params==null) return ReturnResult.errorResult("请填写登录信息!");
			String username = (String) params.get("username");
			String password = (String) params.get("password");
			String mobPhone = (String) params.get("mobPhone");
			Integer smsCode = Integer.valueOf((String) params.get("smsCode"));
			if(StringUtils.isEmpty(username)){
				return ReturnResult.errorResult("用户名为空!");
			}
			if(StringUtils.isEmpty(password)){
				return ReturnResult.errorResult("密码为空!");
			}
			Integer RealSmsCode = (Integer) RedisUtils.get("smsCode"+mobPhone);
			if(smsCode==null||RealSmsCode==null||!smsCode.equals(RealSmsCode)){
				return ReturnResult.errorResult("验证码输入有误！");
			}
			//判断是否已绑定
			Integer wxUserId = GlobalUtils.getUserId(request);
			User wxUser = userService.getById(wxUserId);
			if(wxUser.getBoundId()!=null){
				return ReturnResult.errorResult("当前微信用户已绑定账号！");
			}
			//判断是否已有用户
			if(userService.selectByUsername(username)!=null){
				return ReturnResult.errorResult("用户名已存在！");
			}
			if(userService.setectByMobPhone(mobPhone)!=null){
				return ReturnResult.errorResult("手机号已被注册！");
			}

			String salt = GlobalUtils.getRandomString(6);
			Class<User> userBean = User.class;
			User normaluser = userBean.newInstance();
			BeanUtils.populate(normaluser,params);
			String newPassword = GlobalUtils.getMD5(password+salt);
			normaluser.setPassword(newPassword);
			normaluser.setSalt(salt);
			normaluser.setUserfromType(1);
			normaluser.setCTime(new Date());
			normaluser.setLevel(0);
			//复制openId
			normaluser.setWechatNum(wxUser.getWechatNum());
			if (StringUtils.isEmpty(normaluser.getNickname())) {
				normaluser.setNickname(wxUser.getNickname());
			}
			if (StringUtils.isEmpty(normaluser.getAvatar())) {
				normaluser.setAvatar(wxUser.getAvatar());
			}
			normaluser.setBoundId(wxUser.getId());
			if(userService.save(normaluser)>0){
				wxUser.setBoundId(normaluser.getId());
				userService.update(wxUser);
				GlobalUtils.setSessionValue(request, "userId", normaluser.getId());
				GlobalUtils.setSessionValue(request, "userLevel", normaluser.getLevel());
				return ReturnResult.successResult("注册成功!");
			}
			return ReturnResult.errorResult("注册失败！");
		}catch (Exception e){
			log.error("UserController.registerByWeChat",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//用户注册（pc）
	@Path("/register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map register(@Context HttpServletRequest request,Map params){
		try {
			if(params==null) return ReturnResult.errorResult("请填写登录信息!");
			String username = (String) params.get("username");
			String password = (String) params.get("password");
			String mobPhone = (String) params.get("mobPhone");
			Integer smsCode = (Integer) params.get("smsCode");
			String channel = (String) params.get("channel");
			if(StringUtils.isEmpty(username)){
				return ReturnResult.errorResult("用户名为空!");
			}
			if(StringUtils.isEmpty(password)){
				return ReturnResult.errorResult("密码为空!");
			}
			Integer RealSmsCode = (Integer) RedisUtils.get("smsCode"+mobPhone);
			if(smsCode==null||RealSmsCode==null||!smsCode.equals(RealSmsCode)){
				return ReturnResult.errorResult("验证码输入有误！");
			}
			//判断是否已有用户
			if(userService.selectByUsername(username)!=null){
				return ReturnResult.errorResult("用户名已存在！");
			}
			if(userService.setectByMobPhone(mobPhone)!=null){
				return ReturnResult.errorResult("手机号已被注册！");
			}

			String salt = GlobalUtils.getRandomString(6);
			Class<User> userBean = User.class;
			User normaluser = userBean.newInstance();
			BeanUtils.populate(normaluser,params);
			String newPassword = GlobalUtils.getMD5(password+salt);
			normaluser.setPassword(newPassword);
			normaluser.setSalt(salt);
			normaluser.setUserfromType(1);
			normaluser.setCTime(new Date());
			normaluser.setLevel(0);
			normaluser.setChannel(channel);
			if(userService.save(normaluser)>0){
				GlobalUtils.setSessionValue(request, "userId", normaluser.getId());
				GlobalUtils.setSessionValue(request, "userLevel", normaluser.getLevel());
				return ReturnResult.successResult("注册成功!");
			}
			return ReturnResult.errorResult("注册失败！");
		}catch (Exception e){
			log.error("UserController.register",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}


	/**
	 * 微信用户绑定
	 * @param request
	 */
	@POST
	@Path("/binding")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map binding(@Context HttpServletRequest request,Map params){
		try {
			//获取当前登录用户
			Integer userId = GlobalUtils.getUserId(request);
			String verifyCode = (String) params.get("verifycode");
			if(!GlobalUtils.checkVerifyCode(request,verifyCode)){
				return ReturnResult.errorResult("验证码错误!");
			}
			User wxuser = userService.getById(userId);
			if(wxuser.getUserfromType()==1){
				if(wxuser.getBoundId()==null) {
					return ReturnResult.errorResult("当前登录用户非微信用户!");
				}else{
					return ReturnResult.errorResult("当前登录用户已绑定!");
				}
			}
			if (params == null) return ReturnResult.errorResult("请填写绑定账户信息!");
			String username = (String) params.get("username");
			String password = (String) params.get("password");
			if (StringUtils.isEmpty(username)) {
				return ReturnResult.errorResult("用户名为空!");
			}
			if (StringUtils.isEmpty(password)) {
				return ReturnResult.errorResult("密码为空!");
			}
			User normaluser = userService.selectByUsernameOrPhone(username);
			if (normaluser != null) {
				if (normaluser.getPassword().equals(GlobalUtils.getMD5(password + normaluser.getSalt()))) {
					if (normaluser.getBoundId()!=null){
						return ReturnResult.errorResult("该账户已绑定微信!");
					}
					//复制openId
					normaluser.setWechatNum(wxuser.getWechatNum());
					if (StringUtils.isEmpty(normaluser.getNickname())) {
						normaluser.setNickname(wxuser.getNickname());
					}
					if (StringUtils.isEmpty(normaluser.getAvatar())) {
						normaluser.setAvatar(wxuser.getAvatar());
					}
					normaluser.setBoundId(wxuser.getId());
					wxuser.setBoundId(normaluser.getId());
					userService.update(wxuser);
					userService.update(normaluser);
					GlobalUtils.setSessionValue(request, "userId", normaluser.getId());
					GlobalUtils.setSessionValue(request, "userLevel", normaluser.getLevel());
					return ReturnResult.successResult("绑定成功!");
				}
			}
			return ReturnResult.errorResult("用户名或密码错误!");
		}catch (Exception e){
			log.error("UserController.binding",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//发送手机验证码
	@Path("/sendMessage")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map sendMessage(@Context HttpServletRequest request, @Context HttpServletResponse response,@QueryParam("mobPhone") String mobPhone){
		try {
			int mobile_code = (int)((Math.random()*9+1)*100000);
			String content = "验证码为："+mobile_code+"，请在10分钟内输入。为了保护您的账号安全，请勿把验证码提供给别人。";
			if(StringUtils.isEmpty(mobPhone))return ReturnResult.errorResult("请填写手机号！");
			boolean isSend = SmsUtil.send(mobPhone,content);
			if(!isSend){
				return ReturnResult.errorResult("发送失败！");
			}
			log.info("smsCode:"+mobile_code);
			RedisUtils.set("smsCode"+mobPhone,mobile_code,10, TimeUnit.MINUTES);
//			GlobalUtils.setSessionValue(request,"smsCode",mobile_code);
			return ReturnResult.successResult("发送成功!");
		}catch (Exception e){
			e.printStackTrace();
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	/**
	 * 微信用户解除绑定
	 * @param request
	 */
	@POST
	@Path("/unbinding/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Access(level = 1)
	public Map unbinding(@Context HttpServletRequest request,@PathParam("userId") Integer userId){
		try {
			User user = userService.getById(userId);
			if(user.getBoundId()==null){
				return ReturnResult.errorResult("该用户未绑定微信！");
			}
			User wxUser = userService.getById(user.getBoundId());
			userService.unBinding(user);
			GlobalUtils.setSessionValue(request, "userId", wxUser.getId());
			GlobalUtils.setSessionValue(request, "userLevel", wxUser.getLevel());
			return ReturnResult.successResult("解除绑定成功！");
		}catch (Exception e){
			log.error("UserController.unbinding",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}
	/**
	 * 微信用户解除绑定
	 * @param request
	 */
	@GET
	@Path("/unbinding")
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map unbinding(@Context HttpServletRequest request){
		try {
			Integer userId = GlobalUtils.getUserId(request);
			User user = userService.getById(userId);
			Integer wxUserId = user.getBoundId();
			if(user.getBoundId()==null){
				return ReturnResult.errorResult("该用户未绑定微信！");
			}
			//log.info("+++++++++++++++++++++++++++++++解除绑定：用户："+user.getNickname()+"++++++++++++++++++++++++++++++++++++++++++");
			userService.unBinding(user);
			GlobalUtils.setSessionValue(request, "userId", wxUserId);
			return ReturnResult.successResult("解除绑定成功！");
		}catch (Exception e){
			log.error("UserController.unbinding",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}
	//验证码
	@Path("/verifyCode")
	@GET
	public void getVerifyCodeImg(@Context HttpServletRequest request, @Context HttpServletResponse response){
		try {
			VerifyCodeUtils verifyCodeUtils = new VerifyCodeUtils(180,80,4,40);
			log.info("verifyCode:"+verifyCodeUtils.getCodeText().toUpperCase());
			GlobalUtils.setSessionValue(request,"verifyCode",verifyCodeUtils.getCodeText().toUpperCase());
//			request.getSession().setAttribute("verifyCode", verifyCodeUtils.getCodeText());
			verifyCodeUtils.writeOut(response.getOutputStream());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	//验证码
	@Path("/checkVerifyCode")
	@GET
	public Response VerifyCode(@Context HttpServletRequest request,@QueryParam("verifyCode") String verifyCode){
		//检查验证码是否错误
//		System.out.println(GlobalUtils.getSessionValue(request,"verifyCode"));
		if(!verifyCode.trim().toUpperCase().equals(GlobalUtils.getSessionValue(request,"verifyCode"))){
			return Response.ok(false).build();
		}
		return Response.ok(true).build();
	}

	//校验当前手机
	@Path("/checkOldPhone")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map checkOldPhone(@Context HttpServletRequest request,@QueryParam("oldPhone") String oldPhone,@QueryParam("smsCode") Integer smsCode){
		try {
			User user = userService.getById(GlobalUtils.getUserId(request));
			if(StringUtils.isEmpty(oldPhone)||!oldPhone.equals(user.getMobPhone())){
				return ReturnResult.errorResult("原手机号输入有误！");
			}
			Integer RealSmsCode = (Integer) RedisUtils.get("smsCode"+oldPhone);
			if(smsCode==null||RealSmsCode==null||!smsCode.equals(RealSmsCode)){
				return ReturnResult.errorResult("验证码输入有误！");
			}
			GlobalUtils.setSessionValue(request,"isCheckOldPhone","true");
			return ReturnResult.successResult("验证通过！");
		}catch (Exception e){
			log.error("userController.checkOldPhone",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//修改绑定手机
	@Path("/updatePhone")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map updatePhone(@Context HttpServletRequest request,@QueryParam("newPhone") String newPhone,@QueryParam("smsCode") Integer smsCode){
		try {
			String isCheck = (String) GlobalUtils.getSessionValue(request,"isCheckOldPhone");
//			if(StringUtils.isEmpty(isCheck)||!isCheck.equals("true")){
//				return ReturnResult.errorResult("请验证原手机号！");
//			}
			if(StringUtils.isEmpty(newPhone)){
				return ReturnResult.errorResult("请输入手机号！");
			}
			Integer RealSmsCode = (Integer) RedisUtils.get("smsCode"+newPhone);
			if(smsCode==null||RealSmsCode==null||!smsCode.equals(RealSmsCode)){
				return ReturnResult.errorResult("验证码输入有误！");
			}
			if(userService.setectByMobPhone(newPhone)!=null){
				return ReturnResult.errorResult("手机号已被注册！");
			}
			Integer userId = GlobalUtils.getUserId(request);
			if(userService.updatePhone(newPhone,userId)>0) {
				return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
			}
			return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
		}catch (Exception e){
			log.error("userController.checkOldPhone",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//获取指定用户活动列表（管理）
	@Path("/getActivity/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Access(level = 1)
	public Map getActivityByUserId(@Context HttpServletRequest request,@PathParam("userId") Integer userId,@QueryParam("pageNum")@DefaultValue("1") Integer pageNum,
						   @QueryParam("pageSize")@DefaultValue("10") Integer pageSize,@QueryParam("photoLive")@DefaultValue("0") Integer photoLive){
		try {
			PageHelper.startPage(pageNum,pageSize);
			PageInfo<Activity> pages = new PageInfo<>(activityService.selectByUserId(userId,photoLive));
			return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
		}catch (Exception e){
			log.error("UserController.getActivityByUserId",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//获取指定用户授权列表（管理）
	@Path("/getAuthorize/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Access(level = 1)
	public Map getAuthorizeByUserId(@Context HttpServletRequest request,@PathParam("userId") Integer userId,@QueryParam("pageNum")@DefaultValue("1") Integer pageNum,
						   @QueryParam("pageSize")@DefaultValue("10") Integer pageSize,@QueryParam("photoLive")@DefaultValue("0") Integer photoLive){
		try {
			PageHelper.startPage(pageNum,pageSize);
			PageInfo<LicenseRecord> pages = new PageInfo<>(licenseRecordService.selectByUserId(userId,photoLive));
			return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
		}catch (Exception e){
			log.error("UserController.getAuthorizeByUserId",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//获取当前登陆人参与发布的活动
	@Path("/getImpowerActivity")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map getImpowerActivity(@Context HttpServletRequest request,@QueryParam("pageNum")@DefaultValue("1") Integer pageNum,
									@QueryParam("pageSize")@DefaultValue("10") Integer pageSize,@QueryParam("photoLive")@DefaultValue("0") Integer photoLive){
		try {
			Integer userId = GlobalUtils.getUserId(request);
			PageHelper.startPage(pageNum,pageSize);
			PageInfo<Activity> pages = new PageInfo<>(activityService.selectByImpower(userId, photoLive));
			return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
		}catch (Exception e){
			log.error("UserController.getImpowerActivity",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	//获取当前登陆人参与发布的活动AppPhoto
	@Path("/getImpowerActivityApp")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Access
	public Map getImpowerActivityApp(@Context HttpServletRequest request,@QueryParam("pageNum")@DefaultValue("1") Integer pageNum,
								  @QueryParam("pageSize")@DefaultValue("10") Integer pageSize,@QueryParam("photoLive")@DefaultValue("0") Integer photoLive){
		try {
			Integer userId = GlobalUtils.getUserId(request);
			PageHelper.startPage(pageNum,pageSize);
			PageInfo<Activity> pages = new PageInfo<>(activityService.selectByImpowerApp(userId, photoLive));
			return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
		}catch (Exception e){
			log.error("UserController.getImpowerActivity",e);
			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
		}
	}

	/**
	 * 设置用户为代理商
	 * @param request
	 * @param agentId 代理商用户id
	 * @return
	 */
//	@POST
//	@Path("/agent/{agentId}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Access(level = 1)
//	public Map setAgentUser(@Context HttpServletRequest request,@PathParam("agentId") Integer agentId){
//		try {
//			Integer userId = GlobalUtils.getUserId(request);
//
//			return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
//		}catch (Exception e){
//			log.error("UserController.setAgentUser",e);
//			return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//		}
//	}
}
