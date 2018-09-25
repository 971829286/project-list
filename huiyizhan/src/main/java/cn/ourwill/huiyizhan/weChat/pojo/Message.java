package cn.ourwill.huiyizhan.weChat.pojo;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/20 0020 17:34
 * 模板信息
 * @Version1.0
 */
import lombok.Data;

import java.util.List;

@Data
public class Message {

    //是 否  表示是否为必须数据
    private String touser;//是  接收者openid

    private String template_id;//是  模板ID

    private String url;//否	模板跳转链接

    private String miniprogram;//否 跳小程序所需数据，不需跳小程序可不用传该数据

    private String appid;//是 所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系）

    private String pagepath;//是 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）

    private MessageData data;//是 模板数据

}
