package com.souche.niu.model.workTable;

public class ShopMemberDo {

    public static final int ROLE_BOSS = 1;
    public static final int ROLE_EMPLOYEE = 2;
    public static final int STATUS_TOREVIEW = 0;
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_REJECT = 2;
    private int id;
    private String shopCode;
    private String userAccount;
    private String remarkName;
    private Integer role;
    private Integer status;
    private String roleName;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShopMemberDo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "ShopMemberDo{" +
                "id=" + id +
                ", shopCode='" + shopCode + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", remarkName='" + remarkName + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
