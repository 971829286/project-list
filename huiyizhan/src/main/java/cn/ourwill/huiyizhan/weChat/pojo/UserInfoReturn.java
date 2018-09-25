package cn.ourwill.huiyizhan.weChat.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/20 0020 17:34
 * @Version1.0
 */

@Data
public class UserInfoReturn  implements Serializable {

    //用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
    public String subscribe;
    //用户的唯一标识
    public String openid;
    //用户昵称
    public String nickname;
    //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    public String sex;
    //语言
    public String language;
    //用户个人资料填写的省份
    public String province;
    //普通用户个人资料填写的城市
    public String city;
    //国家，如中国为CN
    public String country;
    //用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
    public String headimgurl;
    //用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
    public String[] privilege;
    //只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
    public String unionid;
    //用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    public long subscribe_time;
    //公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
    public String remark;
    //用户所在的分组ID（兼容旧的用户分组接口）
    public String groupid;
    //用户被打上的标签ID列表
    public String[] tagid_list;
    //返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，
    // ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENEPROFILE LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，
    // ADD_SCENE_OTHERS 其他
    public String subscribe_scene;
    //二维码扫码场景（开发者自定义）
    public String qr_scene;
    //二维码扫码场景描述（开发者自定义）
    public String qr_scene_str;
}

