package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 15:44
 * 发票申请记录
 * @Version1.0
 */
@Data
public class InvoiceRecords {
    private Integer id; //'主键id'
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date applyTime;  //'申请时间',
    private String orderNos; //'订单号',
    private String authorizationIds;//授权记录id
    private Integer userId;  //'申请用户id',
    private Double invoiceAmount;  //金额,
    private Integer invoiceType;  //'发票类型：0 普票  1 专票',
    private Integer invoiceStatus;  //'开票状态（0未开票，1已开票）',
    private Integer invoiceContentId;  //'发票内容id',
    private Integer invoiceSiteId;  //'发票地址id',
    private String invoiceContentStr; //发票信息json
    private String invoiceSiteStr; //发票地址json

    private String receiver;//收件人 联系人
    private String phone; //联系电话
    private String address; //地址
    private String expressCompany;//快递公司
    private String expressNo;//快递单号

    private InvoiceContent invoiceContent;
    private InvoiceSite invoiceSite;

    //订单列表
    private List<LicenseRecord> licenseRecords;

    public InvoiceContent getInvoiceContent() {
        Gson gson = new Gson();
        invoiceContent = gson.fromJson(invoiceContentStr,InvoiceContent.class);
        return invoiceContent;
    }

    public InvoiceSite getInvoiceSite() {
        Gson gson = new Gson();
        invoiceSite = gson.fromJson(invoiceSiteStr,InvoiceSite.class);
        return invoiceSite;
    }
}
