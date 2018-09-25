package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private Integer activityId;//活动id, 门票订单用
    private String username;
    private String mobPhone;
    private String openId;//
    private String transactionId;//支付平台订单
    private String prepayId;//预支付交易会话标识
    private Date prepayIdDueTime;//预支付交易会话标识 有效截至时间
    private Integer fromType;//支付来源（0：微信，1：支付宝）
    private String createIp;//支付用户IP
    private Integer type;//套餐类型（0:免费，1收费，只针对门票订单）
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
    private Integer version;//版本号
    private List<TicketsRecord> ticketsRecordList;//门票记录列表（下单参数用）
    private Integer sysType;//0：授权订单，1：门票订单
    private String buyer;//买家姓名
    private String buyerPhone;//买家电话
    private String buyerEmail;//买家邮箱
    private List<TicketsRecord> ticketsRecords;//订单门票
    private String QRCodeTicketUrl;//带参数二维码url
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date overTime;//二维码失效时间

    private int isCheckView;//是否显示需要审核页面 0：否  1：是   用于下单返回前端的标志位

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) {
            return false;
        }
        TrxOrder trxOrder = (TrxOrder) o;
        return this.getId() == trxOrder.getId();
    }
    @Override
    public int hashCode() {
        return Objects.hash(id,orderNo,userId);
    }
}
