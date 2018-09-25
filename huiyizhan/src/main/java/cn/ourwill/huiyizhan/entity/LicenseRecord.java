package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/19 0019 11:30
 * @Version1.0
 */
@Data
public class LicenseRecord {
    private Integer id;//主键

    private Integer userId;//用户ID

    private String username;//用户ID对应的用户名

    private Integer licenseType;//授权类型（1:包年  2:包时长  9:永久）
    private Integer sessionsTotal;//总场次

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date dueDate;//授权截至日期
    private String amount;//金额
    private String paymentType;//付款方式

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date transactionDate;//交易日期

    private String orderNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime; //创建时间
    private Integer cId;//创建人

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uTime;//更新时间
    private Integer uId;//更新人
    private Integer photoLive;//是否照片直播（0 图文直播  1 照片直播）

    private Integer invoiceStatus;//开票状态（0：未开，1：已开，2：已申请）
    private String expressCompany;//快递公司
    private String expressNo;//快递单号
}
