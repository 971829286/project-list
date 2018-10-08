package com.souche.niu.model.workTable;

import java.util.List;

public class WorkTableDo {

    private UserDo user;

    private ShopMemberDo shop;

    private InformationDo information;

    private List<GroupDo> group;

    public UserDo getUser() {
        return user;
    }

    public void setUser(UserDo user) {
        this.user = user;
    }

    public ShopMemberDo getShop() {
        return shop;
    }

    public void setShop(ShopMemberDo shop) {
        this.shop = shop;
    }

    public InformationDo getInformation() {
        return information;
    }

    public void setInformation(InformationDo information) {
        this.information = information;
    }

    public List<GroupDo> getGroup() {
        return group;
    }

    public void setGroup(List<GroupDo> group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "WorkTableDo{" +
                "user=" + user +
                ", shop=" + shop +
                ", information=" + information +
                ", group=" + group +
                '}';
    }
}
