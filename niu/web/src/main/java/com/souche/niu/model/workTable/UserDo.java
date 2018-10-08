package com.souche.niu.model.workTable;

import java.io.Serializable;

public class UserDo implements Serializable {

    private String shopCode;
    private String authenticate;
    private String name;
    private String userAccount;
    private String avatar;
    private String userId;

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(String authenticate) {
        this.authenticate = authenticate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserDo{" +
                "shopCode='" + shopCode + '\'' +
                ", authenticate='" + authenticate + '\'' +
                ", name='" + name + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
