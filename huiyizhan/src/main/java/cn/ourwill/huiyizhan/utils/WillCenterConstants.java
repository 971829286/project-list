package cn.ourwill.huiyizhan.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/5/4 11:59
 * @Description 用户中心接口
 */
@Component
public class WillCenterConstants {
    private static WillCenterConstants INSTANCE = new WillCenterConstants();
    private static String willCenterDomain;

    @Value("${willCenter.domain}")
    public void setWillCenterDomain(String willCenterDomain) {
        this.willCenterDomain = willCenterDomain;
    }

    public static WillCenterConstants getInstance(){
        return INSTANCE;
    }

    public WillCenterConstants() {
    }

    public String getLoginUrl() {
        return this.willCenterDomain+"/login";
    }

    public String getCheckLoginUrl() {
        return this.willCenterDomain+"/checkLogin";
    }

    public String getRegisterUrl() {
        return this.willCenterDomain+"/register";
    }

    public String getTicketUrl() {
        return this.willCenterDomain+"/ticket";
    }

    public String getTestLoginUrl() {
        return this.willCenterDomain+"/tests/testLogin";
    }

    public String getLogoutUrl() {
        return this.willCenterDomain+"/logout";
    }

    public String getGetUserInfoByUUIDUrl() {
        return this.willCenterDomain+"/api/user/getUserInfoByUUID";
    }

    public String getAddUserUrl() {
        return this.willCenterDomain+"/api/user/addUser";
    }

    public String getEditUserInfoUrl() {
        return this.willCenterDomain+"/api/user/editUserInfo";
    }

    public String getChangePasswordUrl() {
        return this.willCenterDomain+"/api/user/editUserPassword";
    }

    public String getChangeMobPhoneUrl() {
        return this.willCenterDomain+"/api/user/editUserMobPhone";
    }

    public String getSendMessageUrl() {
        return this.willCenterDomain+"/sendMessage";
    }

    public String addUnionIdById() {
        return this.willCenterDomain+"/api/user/addUnionIdById";
    }

    public String deleteUnionIdById() {
        return this.willCenterDomain+"/api/user/deleteUnionIdById";
    }

    public String getUserByUnionId() {
        return this.willCenterDomain+"/api/user/getUserByUnionId";
    }
    public String getUserByEmailOrPhone(){
        return this.willCenterDomain+"/api/user/getUserByEmailOrPhone";
    }
    public String getEditEmail(){
        return this.willCenterDomain+"/api/user/editEmail";
    }

    public String getSingleUpload(){
        return this.willCenterDomain + "/api/upload/receive/single";
    }
//    public String getWeChatLoginUrl() {
//        return this.willCenterDomain+"/mobile/WeChatLogin";
//    }


    //    #willCenter server
//    loginServer.willCenter.login=http://login.ourwill.cn/login
//    loginServer.willCenter.register=http://login.ourwill.cn/register
//    loginServer.willCenter.ticket=http://login.ourwill.cn/ticket
//    loginServer.willCenter.testLogin=http://login.ourwill.cn/tests/testLogin
//    loginServer.willCenter.logout=http://login.ourwill.cn/logout
//
//    WillCenter.API.User.getUserInfoByUUID=http://login.ourwill.cn/api/user/getUserInfoByUUID
//    WillCenter.API.User.addUser=http://login.ourwill.cn/api/user/addUser
//    WillCenter.API.User.editUserInfo=http://login.ourwill.cn/api/user/editUserInfo
//    WillCenter.API.User.ChangePassword=http://login.ourwill.cn/api/user/editUserPassword
//    WillCenter.API.User.ChangeMobPhone=http://login.ourwill.cn/api/user/editUserMobPhone
//    WillCenter.API.User.sendMessage=http://login.ourwill.cn/sendMessage
}
