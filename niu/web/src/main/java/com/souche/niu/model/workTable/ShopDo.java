package com.souche.niu.model.workTable;

import java.io.Serializable;

public class ShopDo implements Serializable {

    private String shopCode;
    private Integer role;
    private String roleName;
    private String name;

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ShopDo{" +
                "shopCode='" + shopCode + '\'' +
                ", role=" + role +
                ", roleName='" + roleName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
