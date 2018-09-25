package cn.ourwill.willcenter.controller;


import cn.ourwill.willcenter.entity.User;
import cn.ourwill.willcenter.mapper.UserMapper;
import cn.ourwill.willcenter.service.IUserService;
import cn.ourwill.willcenter.utils.GlobalUtils;
import cn.ourwill.willcenter.utils.RedisUtils;
import cn.ourwill.willcenter.utils.ReturnResult;
import cn.ourwill.willcenter.utils.ReturnType;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Jinniu.xu @ourwill.com.cn
 * @Time 2018年4月9日15:26:42
 * @Version1.0
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 按照uuid获取用户信息
     * @param uuid
     * @return
     */
    @PostMapping("/getUserInfoByUUID")
    @ResponseBody
    public Map getUserInfo(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return ReturnResult.errorResult("uuid为空");
        }
        User user = userService.getUserByUUID(uuid);
        if (user == null) {
            return ReturnResult.errorResult("该用户不存在");
        }
        return ReturnResult.successResult("data", user, ReturnType.GET_SUCCESS);
    }


    /**
     * 添加用户
     * @param param
     * @return
     */
    @PostMapping("/addUser")
    @ResponseBody
    public Map addUser(@RequestBody Map<String, String> param) throws Exception {
        String username = param.get("username").toString();
        String password = param.get("password").toString();
        String mobPhone = param.get("mobPhone").toString();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(mobPhone)) {
            return ReturnResult.errorResult("信息不完整");
        }
        if (userService.selectByUsername(username) != null) {
            return ReturnResult.errorResult("用户名已存在！");
        }
        if (userService.setectByMobPhone(mobPhone) != null) {
            return ReturnResult.errorResult("手机号已被注册！");
        }
//        User user = new User();
        Class<User> userBean = User.class;
        User user = userBean.newInstance();
        BeanUtils.populate(user,param);
        String uuid = GlobalUtils.generateUUID();
        user.setUUID(uuid);
        String salt = GlobalUtils.getRandomString(6);
        String newPassword = GlobalUtils.getMD5(password + salt);
        user.setPassword(newPassword);
        user.setMobPhone(mobPhone);
        user.setCTime(new Date());
        user.setUsername(username);
        user.setSalt(salt);
        int count = userService.save(user);
        if (count > 0) {
            return ReturnResult.successResult("data", user, ReturnType.ADD_SUCCESS);
        } else {
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        }
    }

    /**
     * 编辑用户信息
     *
     * @param param
     * @return
     */
    @PostMapping("/editUserInfo")
    @ResponseBody
    public Map editUserInfo(@RequestBody Map param) {
        try {
            String uuid = (String) param.get("uuid");
            String nickname = (String) param.get("nickname");
            String mobPhone = (String) param.get("mobPhone");
            String avatar = (String) param.get("avatar");
            String info = (String) param.get("info");
            if (StringUtils.isEmpty(uuid)) {
                return ReturnResult.errorResult("用户uuid为空");
            }
            User user = new User();
//        User user = userService.getUserByUUID(uuid);
//        if(user == null){
//            return ReturnResult.errorResult("用户不存在");
//        }
            user.setUUID(uuid);
            if (StringUtils.isNotEmpty(mobPhone)) {
                if (userService.setectByMobPhone(mobPhone) != null) {
                    return ReturnResult.errorResult("手机号已被注册！");
                }
                user.setMobPhone(mobPhone);
            }
//            if (StringUtils.isNotEmpty(nickname)) {
            user.setNickname(nickname);
//            }
            if (StringUtils.isNotEmpty(avatar)) {
                user.setAvatar(avatar);
            }
//            if (StringUtils.isNotEmpty(info)) {
            user.setInfo(info);
//            }
            Integer flag = userService.updateByUuid(user);
            if (flag != null && flag == 1) {
                user = userService.getUserByUUID(uuid);
                return ReturnResult.successResult("data", user, ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (Exception e){
            log.info("UserController.editUserInfo",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 修改用户密码
     * @param param
     * @return
     */
    @PostMapping("/editUserPassword")
    @ResponseBody
    public Map editUserPassword(@RequestBody Map<String, String> param) {
        String isVerify = param.get("isVerify");
        String uuid = param.get("uuid");
        String password = param.get("password");
        String oldPassword = param.get("oldPassword");
        User user = userService.getUserByUUID(uuid);
        if (StringUtils.isEmpty(isVerify) || isVerify.equals("true")) {
            //校验原密码
            if (oldPassword == null || user == null || !user.getPassword().equals(GlobalUtils.getMD5(oldPassword + user.getSalt()))) {
                return ReturnResult.errorResult("验证失败！");
            }
        }
        if (StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(uuid)) {
            String salt = GlobalUtils.getRandomString(6);
            String newPassword = GlobalUtils.getMD5(password + salt);
            if (userService.changePWDByUuid(uuid, newPassword, salt) > 0) {
                return ReturnResult.successResult("data", user, ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } else {
            return ReturnResult.errorResult("参数无效!");
        }
    }

    /**
     * 重置用户密码-忘记密码
     * @param param
     * @return
     */
    @PostMapping("/resetPassword")
    @ResponseBody
    public Map resetPassword(@RequestBody Map<String, String> param) {
        String password = param.get("password");
        String mobPhone = param.get("mobPhone");
        User user = userService.setectByMobPhone(mobPhone);
        if(user==null||StringUtils.isEmpty(mobPhone)){
            return ReturnResult.errorResult("手机号不正确！");
        }
        if (StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(mobPhone)) {
            String salt = GlobalUtils.getRandomString(6);
            String newPassword = GlobalUtils.getMD5(password + salt);
            if (userService.changePWDByUuid(user.getUUID(), newPassword, salt) > 0) {
                return ReturnResult.successResult("data", user, ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } else {
            return ReturnResult.errorResult("参数无效!");
        }
    }

    /**
     * 修改手机号-待完善
     * @return
     */
    @PostMapping("/editUserMobPhone")
    @ResponseBody
    public Map editUserMobPhone(@RequestBody Map<String, String> param) {
        String mobPhone = param.get("mobPhone");
        String uuid = param.get("uuid");
        String smsCode = param.get("smsCode");
        if (StringUtils.isEmpty(uuid)) {
            return ReturnResult.errorResult("uuid无效");
        }
        if (StringUtils.isEmpty(mobPhone)) {
            return ReturnResult.errorResult("请输入手机号！");
        }
        String RealSmsCode = (String) RedisUtils.get("smsCode:" + mobPhone);
        if (smsCode == null || RealSmsCode == null || !smsCode.equals(RealSmsCode)) {
            return ReturnResult.errorResult("验证码输入有误！");
        }
        if (userService.setectByMobPhone(mobPhone) != null) {
            return ReturnResult.errorResult("手机号已被注册！");
        }
        User user = userService.getUserByUUID(uuid);
        if (user != null) {
            userService.updatePhone(mobPhone, user.getId());
            user.setMobPhone(mobPhone);
            return ReturnResult.successResult("data", user, ReturnType.GET_SUCCESS);
        } else {
            return ReturnResult.errorResult("uuid无效");
        }
    }

    /**
     * 修改UnionId为用户微信的unionid吗，微信绑定
     *
     * @param param
     * @return
     */
    @PostMapping("/addUnionIdById")
    @ResponseBody
    public Map addUnionIdById(@RequestBody Map<String, String> param) {

        try {
            String unionId = param.get("unionid");
            String uuid = param.get("uuid");
            //校验unionId是否存在
            User user = userService.selectByUnionId(unionId);
            if (user == null) {//不存在，进行绑定
                Integer flag = userService.changeUnionIdByUuid(unionId, uuid);
                if (flag > 0) {
                    return ReturnResult.successResult("data", uuid, ReturnType.GET_SUCCESS);
                }
                return ReturnResult.errorResult("绑定失败！！！");
            } else {
                return ReturnResult.errorResult("该微信用户已绑定！！！");
            }
        } catch (Exception e) {
            log.error("e:", e);
            return ReturnResult.errorResult("绑定失败！！！");
        }
    }

    /**
     * 修改UnionId为空，解除微信绑定
     *
     * @param param
     * @return
     */
    @PostMapping("/deleteUnionIdById")
    @ResponseBody
    public Map deleteUnionIdById(@RequestBody Map<String, String> param) {

        try {
            String unionId = param.get("unionid");
            String uuid = param.get("uuid");
            Integer flag = userService.changeUnionIdByUuid(unionId, uuid);
            if (flag > 0) {
                return ReturnResult.successResult("data", uuid, ReturnType.GET_SUCCESS);
            }
            return ReturnResult.errorResult("解绑失败！！！");
        } catch (Exception e) {
            return ReturnResult.errorResult("解绑失败！！！");
        }
    }

    @GetMapping("/getUserByUnionId")
    @ResponseBody
    public Map getUserByUnionId(String unionId) {
        User user = userService.selectByUnionId(unionId);
        return ReturnResult.successResult("data",user,ReturnType.GET_SUCCESS);
    }

    @PostMapping("/getUserByEmailOrPhone")
    public User isBingDing(@RequestBody Map map) {
        String email = (String) map.get("email");
        String mobPhone = (String) map.get("mobPhone");

        if (StringUtils.isNotEmpty(email)) {
            User user = userService.selectByEmailOrPhone(email);
            if (user == null){
                return null;
            }else{
                return user;
            }
        }else if(StringUtils.isNotEmpty(mobPhone)){
            User user = userService.selectByEmailOrPhone(mobPhone);
            if(user == null)
                return null;
            else
                return user;
        }
        return null;
    }

    @PostMapping("/editEmail")
    public User editEmail(@RequestBody Map map){
        String email = (String) map.get("email");
        String uuid = (String) map.get("uuid");
        if(StringUtils.isEmpty(uuid)){
            return null;
        }else{
            User user = userService.getUserByUUID(uuid);
            if(user != null){
                user.setEmail(email);
                Integer update = userService.updateEmail(user.getId(),email);
                if(update > 0)
                    user.setVersion(user.getVersion()+1);
                    return user;
            }
        }
        return null;
    }

    /**
     * 通过用户密码绑定微信（图文直播微信端使用）
     * @param userMap
     * @return
     */
    @PostMapping("/binding")
    @ResponseBody
    public Map binding(@RequestBody Map<String,String> userMap){
        try {
            //通过手机号或者邮箱判断该用户是否已经注册
            String username = userMap.get("username");
            String password = userMap.get("password");
            String unionId = userMap.get("unionid");
            if (StringUtils.isEmpty(username)) {
                return ReturnResult.errorResult("手机号或用户名为空！");
            }
            if (StringUtils.isEmpty(password)) {
                return ReturnResult.errorResult("密码为空！");
            }
            if (StringUtils.isEmpty(unionId)) {
                return ReturnResult.errorResult("用户标示为空！");
            }
            User user = userService.selectByUsernameOrPhone(username);
            if (user != null && user.getPassword().equals(GlobalUtils.getMD5(password + user.getSalt()))) {
                if (StringUtils.isEmpty(user.getUnionid())) {
                    //更新unionid
                    userService.changeUnionIdByUuid(unionId, user.getUUID());
                    return ReturnResult.successResult("data",user,"绑定成功！");
                }
                return ReturnResult.errorResult("该账户已经绑定，请先解除绑定！");
            }
            return ReturnResult.errorResult("用户名或密码错误！");
        } catch (Exception e){
            log.error("userController.binding",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 通过uuid解绑微信
     * @param userMap
     * @return
     */
    @PostMapping("/unbinding")
    @ResponseBody
    public Map unBinding(@RequestBody Map<String,String> userMap){
        //通过手机号或者邮箱判断该用户是否已经注册
        String uuid = userMap.get("uuid");
        if (StringUtils.isEmpty(uuid)){
            return ReturnResult.errorResult("用户标示为空！");
        }
        User user = userService.getUserByUUID(uuid);
        if(user!=null){
            //更新unionid为null
            userService.changeUnionIdByUuid(null,user.getUUID());
            return ReturnResult.successResult("解除绑定成功！");
        }
        return ReturnResult.errorResult("用户不存在！");

    }

    @GetMapping("/generalUuid")
    @ResponseBody
    public int generalUUID(){
        List<User> users = userService.selectWithOutUuid();
        users.stream().forEach(e->e.setUUID(GlobalUtils.generateUUID()));
        return userService.batchUpdateUUID(users);
    }

    /**
     * 校验用户名密码是否正确
     */
    @PostMapping("/verifyLogin")
    @ResponseBody
    public Map verifyLogin(HttpServletRequest request, @RequestBody Map<String,String> params){
        try{
            String username = params.get("username");
            String password = params.get("password");
            if(StringUtils.isEmpty(username)){
                return ReturnResult.errorResult("用户名为空!");
            }
            if(StringUtils.isEmpty(password)) {
                return ReturnResult.errorResult("密码为空!");
            }
            User user = userService.selectByUsernameOrPhone(username);
            if(user != null && user.getPassword().equals(GlobalUtils.getMD5(password + user.getSalt()))){
                return ReturnResult.successResult("data",user,ReturnType.GET_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.GET_ERROR);
        } catch (Exception e){
            log.error("UserController.verifyLogin",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}