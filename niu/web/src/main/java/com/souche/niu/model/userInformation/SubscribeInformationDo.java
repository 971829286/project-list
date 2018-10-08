package com.souche.niu.model.userInformation;


import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class SubscribeInformationDo implements Serializable {

    //是否有订阅  0：未定阅，1：订阅
    private int subscribe;
    //订阅数
    private int totleNum;
    //车辆信息
    private List<CarInfoDo> car_list;

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public int getTotleNum() {
        return totleNum;
    }

    public void setTotleNum(int totleNum) {
        this.totleNum = totleNum;
    }

    public List<CarInfoDo> getCar_list() {
        return car_list;
    }

    public void setCar_list(List<CarInfoDo> car_list) {
        this.car_list = car_list;
    }

    @Override
    public String toString() {
        return "SubscribeInformationDo{" +
                "subscribe=" + subscribe +
                ", totleNum=" + totleNum +
                ", car_list=" + car_list +
                '}';
    }
}
