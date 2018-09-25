package cn.ourwill.huiyizhan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityTickets {
    private Integer id;

    private Integer activityId;     //会议活动id

    private String ticketName;      //门票名称

    private Integer ticketPrice;    //单价

    private String ticketExplain;   //门票说明

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date startTime;         //开始售票时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cutTime;           //截至售票时间

    private Integer isFree;         //是否免费票（0，否； 1， 是）

    private Integer isPublishSell;  //发布活动后立即售票（0，否； 1， 是）

    private Integer isCheck;        //是否审核

    private Integer sellStatus;     //是否可售（0，否；1，是）

    private Integer singleLimits;   //单次数量限制（0 为无限制）

    private Integer totalNumber;    //总票数量（0为 无限制）

    private Integer stockNumber;    //库存数量

    private Integer rank; //排序

    private Integer userId;         //创建人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date cTime;             //创建时间

    private Integer uId;            //修改人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date uTime;             //修改时间

    //statistics use
    private Integer noCheckNumber; //待审核数
    private Integer soldNumber;//已售出数量
    private Integer noPayNumber;//待支付数量
//    private Integer totalStockNumber;//全部票数总的剩余量
}