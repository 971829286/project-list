package com.souche.niu.model.userInformation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CarInfoDo implements Serializable {

    private String carId;
    private String android_protocol;
    private String protocol;
    private String modeName;
    private int  date;
    private String emission;
    private String mileage;
    private String price;
    private String mainPicture;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getAndroid_protocol() {
        return android_protocol;
    }

    public void setAndroid_protocol(String android_protocol) {
        this.android_protocol = android_protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getEmission() {
        return emission;
    }

    public void setEmission(String emission) {
        this.emission = emission;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    @Override
    public String toString() {
        return "CarInfoDo{" +
                "carId='" + carId + '\'' +
                ", android_protocol='" + android_protocol + '\'' +
                ", protocol='" + protocol + '\'' +
                ", modeName='" + modeName + '\'' +
                ", date=" + date +
                ", emission='" + emission + '\'' +
                ", mileage='" + mileage + '\'' +
                ", price='" + price + '\'' +
                ", mainPicture='" + mainPicture + '\'' +
                '}';
    }
}
