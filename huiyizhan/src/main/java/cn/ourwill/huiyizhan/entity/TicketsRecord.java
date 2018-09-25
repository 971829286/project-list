package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.util.StringUtil;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.Date;

import static cn.ourwill.huiyizhan.entity.Config.bucketDomain;

/**
 * 购票记录
 */
@Data
public class TicketsRecord {
    private Integer id;

    private Integer ticketsId;      //会议门票id

    private Integer activityId;     //活动id

    private String ticketsName;     //门票名称

    private Integer ticketsPrice;    //门票价格(分)

    private String price;              //门票价格(元)

    private String confereeName;    //参会者名称

    private String confereePhone;   //参会者电话

    private String confereeEmail;   //参会者邮箱

    private Integer userId;         //用户号

    private Integer orderId;        //订单id

    private String orderNo;         //订单号

    private Integer ticketStatus;   //票状态（0:未生成，1：未签到，2：已签到，3：待审核，4:审核未通过，9：已退票）

    private String backInfo;        //反馈信息

    private String authCode;        //校验码

    private Integer signCode;       //签到码

    private TrxOrder trxOrder;      //订单

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;             //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date signTime;
    //修改时间

    /**
     * 该票信息
     */
    private ActivityTickets activityTickets;


    /**
     * 该票指定的会议
     */
    private Activity activity;
    /**
     * 门票链接
     */
    private String ticketLink;
    /**
     * 下载门票链接拼接了域名
     */
    private String ticketLinkUrl;

    public String getTicketLinkUrl(){
        if (StringUtil.isNotEmpty(this.ticketLink) && this.ticketLink.indexOf("http") < 0) {
            return bucketDomain + this.ticketLink;
        }
        return this.ticketLink;
    }

    public String getPrice() {
        DecimalFormat    df   = new DecimalFormat("######0.00");
        if(this.ticketsPrice == null || this.ticketsPrice == 0){
            return "0.00";
        }else{
            return df.format(this.ticketsPrice / 100.0);
        }
    }
}