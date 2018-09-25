package cn.ourwill.tuwenzb.weixin.pojo;

import lombok.Data;

@Data
public class MessageDataList {

    private String value;

    private String color;

    public MessageDataList(String value) {
        this.value = value;
    }
}
