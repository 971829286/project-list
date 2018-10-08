package com.souche.niu.model.userInformation;

import java.io.Serializable;

public class CarShopDo implements Serializable {

    //在售
    private int onSaleNum;
    //已售
    private int saledNum;
    //新访客
    private int newVisitor;


    public int getOnSaleNum() {
        return onSaleNum;
    }

    public void setOnSaleNum(int onSaleNum) {
        this.onSaleNum = onSaleNum;
    }

    public int getSaledNum() {
        return saledNum;
    }

    public void setSaledNum(int saledNum) {
        this.saledNum = saledNum;
    }

    public int getNewVisitor() {
        return newVisitor;
    }

    public void setNewVisitor(int newVisitor) {
        this.newVisitor = newVisitor;
    }

    @Override
    public String toString() {
        return "CarShopDo{" +
                "onSaleNum=" + onSaleNum +
                ", saledNum=" + saledNum +
                ", newVisitor=" + newVisitor +
                '}';
    }
}
