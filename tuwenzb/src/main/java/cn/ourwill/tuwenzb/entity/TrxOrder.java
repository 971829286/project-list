package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/10 0010 14:24
 * 订单
 * @Version1.0
 */
@Data
public class TrxOrder {
    private Integer id;//
    private String orderNo;//订单号
    private Integer userId;//用户id
    private String username;
    private String mobPhone;
    private String openId;//
    private String transactionId;//支付平台订单
    private String prepayId;//预支付交易会话标识
    private Date prepayIdDueTime;//预支付交易会话标识 有效截至时间
    private Integer fromType;//支付来源（0：微信，1：支付宝）
    private String createIp;//支付用户IP
    private Integer type;//套餐类型（1：包年，2：包时长）
    private Integer price;//价格（分）
    private Integer number;//数量
    private Integer amount;//金额（分）
    private String feeType;//货币类型
    private String bankType;//付款银行
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;//下单时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date finishTime;//完成时间
    private Integer transactionStatus;//交易状态 （0：待支付，1：支付成功，2:支付失败，3：转入退款，4：已关闭，5：已撤销，6：用户支付中）
    private Integer orderStatus;//订单状态 (0:未通知，1：已通知，2：已查询，9：已关闭)
    private String tradeType;//支付类型：JSAPI--公众号支付、NATIVE--原生扫码支付
    private String codeUrl;
    private Integer photoLive;//是否照片直播（0 图文直播  1 照片直播）
    private Integer version;//版本号
    private Integer dealType;//交易类型（0：购买直播，1：打赏红包）
}
