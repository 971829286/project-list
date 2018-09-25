package cn.ourwill.willcenter.weixin.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/20 0020 17:51
 * @Version1.0
 */
@Data
public class JsTicket {
    private Integer errcode;
    private String errmsg;
    private String jsTicket;
    private Date dueDate;
    private Integer expireIn;
}
