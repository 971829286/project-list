package cn.ourwill.tuwenzb.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 15:51
 * 发票内容
 * @Version1.0
 */
@Data
public class InvoiceContent {
    private Integer id; //
    private Integer userId; //'用户id',
    private Integer invoiceType; //'发票类型 0：普通  1：专用',
    private Integer titleType; //'发票抬头类型 0：个人  1：企业',
    private String invoiceTitle; //'发票抬头',
    private String registrationNum; //'纳税人识别号',
    private String registrationSite; //'注册地址',
    private String registrationPhone; //'注册电话',
    private String bank; //'开户银行',
    private String bankAccount; //'银行账号',
    private Integer isDefault; //'是否默认 0 不默认  1 默认',
    private Date cTime; //'创建时间',
    private Date uTime; //'更新时间',
}
