package cn.ourwill.huiyizhan.weChat.pojo;

import lombok.Data;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/20 0020 17:34
 * @Version1.0
 */
@Data
public class MessageDataList {

    private String value;

    private String color;

    public MessageDataList(String value) {
        this.value = value;
    }
}
