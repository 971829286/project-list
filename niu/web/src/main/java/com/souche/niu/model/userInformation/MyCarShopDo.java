package com.souche.niu.model.userInformation;

import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

public class MyCarShopDo implements Serializable {

    //标志  0：没有数据（在售和已售都为0)    1：有数据
    private int hasCarShopData;
    private List carShopData;

    public int getHasCarShopData() {
        return hasCarShopData;
    }

    public void setHasCarShopData(int hasCarShopData) {
        this.hasCarShopData = hasCarShopData;
    }

    public List getCarShopData() {
        return carShopData;
    }

    public void setCarShopData(List carShopData) {
        this.carShopData = carShopData;
    }

    @Override
    public String toString() {
        return "MyCarShopDo{" +
                "hasCarShopData=" + hasCarShopData +
                ", carShopData=" + carShopData +
                '}';
    }
}
