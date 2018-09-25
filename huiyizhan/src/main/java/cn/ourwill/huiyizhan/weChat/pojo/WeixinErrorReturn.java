package cn.ourwill.huiyizhan.weChat.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/20 0020 17:34
 * @Version1.0
 */

@Data
public class WeixinErrorReturn  implements Serializable {
    private String errcode;
    private String errmsg;
}