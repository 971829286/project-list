package cn.ourwill.willcenter.weixin.pojo;

import java.io.Serializable;

/**
 * 　ClassName:WeixinErrorReturn
 * Description：
 * User:hasee
 * CreatedDate:2017/7/1 17:30
 */

public class WeixinErrorReturn  implements Serializable {
    private String errcode;
    private String errmsg;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}