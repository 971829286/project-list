package cn.ourwill.tuwenzb.weixin.pojo;

import java.io.Serializable;

/**
 * 　ClassName:TokenReturn
 * Description：
 * User:hasee
 * CreatedDate:2017/7/1 17:30
 */

public class TokenReturn  implements Serializable {
    //获取到的凭证
    private	String access_token;
    //凭证有效时间，单位：秒
    private	Integer expires_in;

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
}