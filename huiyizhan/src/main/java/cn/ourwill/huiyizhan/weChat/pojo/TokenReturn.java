package cn.ourwill.huiyizhan.weChat.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/20 0020 17:34
 * @Version1.0
 */

@Data
public class TokenReturn  implements Serializable {
    //获取到的凭证
    private	String access_token;
    //凭证有效时间，单位：秒
    private	Integer expires_in;
    //用户刷新access_token
    private String refresh_token ;
    //授权用户唯一标识
    private String openid;
    //用户授权的作用域，使用逗号（,）分隔
    private String scope;
    //当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
    private String unionid;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid(){return openid; }

    public void setOpenid(String openid) { this.openid = openid; }

    public String getScope() { return scope; }

    public void setScope(String scope) { this.scope = scope; }

    public String getUnionid() { return unionid; }

    public void setUnionid(String unionid) { this.unionid = unionid; }
}