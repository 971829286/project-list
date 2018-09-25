package cn.ourwill.huiyizhan.weChat.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author feng.Liu @ourwill.com.cn
 * @Time 2018/4/25
 * @Version1.0
 */
@Data
public class QRCodeParam implements Serializable {
    //网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
    private String access_token;
    //该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
    private Integer expire_seconds;
    //二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值
    private String action_name;
    //二维码详细信息
    private String action_info;
    //场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
    private Integer scene_id;
    //场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
    private String scene_str;
}
