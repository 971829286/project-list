package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-05-22 18:49
 **/
@Data
public class FundDetail extends Config implements Serializable {
    private Integer id;//主键id
    private String orderNo;//订单号
    private Integer userId;//用户id
    private Integer activityId;
    private Integer amount;//金额
    private Integer fund_status;//资金状态(0：未到账，1：已到账)
    private Integer type;//类型(0：打赏，1：提现)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;//下单时间·
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date finishTime;//完成时间
    private Integer payUser;//打赏人
    private String payWeChatNum;//打赏人微信
    private String avatar;
    private String nickname;
    private String avatarUrl;
    /**
     * 上次剩余金额
     */
    private Integer lastBalance;
    /**
     * 当前剩余金额
     */
    private Integer nowBalance;
    public String getAvatarUrl() {
        if(StringUtils.isEmpty(avatar)){
            return bucketDomain + defaultAvator;
        }
        if(StringUtils.isNotEmpty(avatar)&&avatar.indexOf("http")<0) {
            return bucketDomain + this.avatar;
        }
        return avatar;
    }
}
