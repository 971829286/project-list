package cn.ourwill.huiyizhan.entity;

import lombok.Data;

//地址bean
@Data
public class Address {
    //{"province":"上海市","city":"黄浦区","address":"awdawd"}
    private String province;
    private String city;
    private String address;

    @Override
    public String toString() {
        String res = "";
        if(province != null){
            res += province;
        }
        if(city != null){
            res += city;
        }
        if(address != null){
            res += address;
        }
        return  res;
    }
}
