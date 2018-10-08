package com.souche.niu.model.userInformation;

public class FriendsCars {

    private String protocol;

    private String new_car_count;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getNew_car_count() {
        return new_car_count;
    }

    public void setNew_car_count(String new_car_count) {
        this.new_car_count = new_car_count;
    }

    @Override
    public String toString() {
        return "FriendsCars{" +
                "protocol='" + protocol + '\'' +
                ", new_car_count='" + new_car_count + '\'' +
                '}';
    }
}
