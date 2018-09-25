package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-05-24 17:06
 **/
@Data
public class WithdrawalOrder {

    /**
     * 主键id
     */
    private Integer id;
    /**
     * //商户订单号
     */
    private String partnerTradeNo;
    /**
     * 提现人
     */
    private Integer userId;
    /**
     * //微信用户openid
     */
    private String openId;
    /**
     * 收款人姓名
     */
    private String userName;
    /**
     *请求提现金额
     */
    private Integer requestAmount;
    /**
     *服务费
     */
    private Integer serviceCharge;
    /**
     *实际提现金额
     */
    private Integer practicalAmount;
    /**
     *用户ip
     */
    private String createIp;
    /**
     *微信订单号
     */
    private String paymentNo;
    /**
     *微信支付成功时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date paymentTime;
    /**转账时间
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date  transferTime;
    /**
     *转账状态 （0：失败，1：成功）
     */
    private Integer transferStatus;
    /**
     *失败原因
     */
    private String failedReason;
    /**
     *付款描述
     */
    private String description;
}
