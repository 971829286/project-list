package com.souche.bmgateway.core.domain;

import java.util.Date;

public class OrderShareNotifyDetail {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.isv_org_id
     *
     * @mbggenerated
     */
    private String isvOrgId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.merchant_id
     *
     * @mbggenerated
     */
    private String merchantId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.relate_order_no
     *
     * @mbggenerated
     */
    private String relateOrderNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.out_trade_no
     *
     * @mbggenerated
     */
    private String outTradeNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.share_order_no
     *
     * @mbggenerated
     */
    private String shareOrderNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.share_date
     *
     * @mbggenerated
     */
    private String shareDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.status
     *
     * @mbggenerated
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.total_amount
     *
     * @mbggenerated
     */
    private String totalAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.currency
     *
     * @mbggenerated
     */
    private String currency;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.error_code
     *
     * @mbggenerated
     */
    private String errorCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.error_desc
     *
     * @mbggenerated
     */
    private String errorDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.ext_info
     *
     * @mbggenerated
     */
    private String extInfo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.memo
     *
     * @mbggenerated
     */
    private String memo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.gmt_create
     *
     * @mbggenerated
     */
    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_share_notify_detail.gmt_modified
     *
     * @mbggenerated
     */
    private Date gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.id
     *
     * @return the value of t_order_share_notify_detail.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.id
     *
     * @param id the value for t_order_share_notify_detail.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.isv_org_id
     *
     * @return the value of t_order_share_notify_detail.isv_org_id
     *
     * @mbggenerated
     */
    public String getIsvOrgId() {
        return isvOrgId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.isv_org_id
     *
     * @param isvOrgId the value for t_order_share_notify_detail.isv_org_id
     *
     * @mbggenerated
     */
    public void setIsvOrgId(String isvOrgId) {
        this.isvOrgId = isvOrgId == null ? null : isvOrgId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.merchant_id
     *
     * @return the value of t_order_share_notify_detail.merchant_id
     *
     * @mbggenerated
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.merchant_id
     *
     * @param merchantId the value for t_order_share_notify_detail.merchant_id
     *
     * @mbggenerated
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.relate_order_no
     *
     * @return the value of t_order_share_notify_detail.relate_order_no
     *
     * @mbggenerated
     */
    public String getRelateOrderNo() {
        return relateOrderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.relate_order_no
     *
     * @param relateOrderNo the value for t_order_share_notify_detail.relate_order_no
     *
     * @mbggenerated
     */
    public void setRelateOrderNo(String relateOrderNo) {
        this.relateOrderNo = relateOrderNo == null ? null : relateOrderNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.out_trade_no
     *
     * @return the value of t_order_share_notify_detail.out_trade_no
     *
     * @mbggenerated
     */
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.out_trade_no
     *
     * @param outTradeNo the value for t_order_share_notify_detail.out_trade_no
     *
     * @mbggenerated
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.share_order_no
     *
     * @return the value of t_order_share_notify_detail.share_order_no
     *
     * @mbggenerated
     */
    public String getShareOrderNo() {
        return shareOrderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.share_order_no
     *
     * @param shareOrderNo the value for t_order_share_notify_detail.share_order_no
     *
     * @mbggenerated
     */
    public void setShareOrderNo(String shareOrderNo) {
        this.shareOrderNo = shareOrderNo == null ? null : shareOrderNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.share_date
     *
     * @return the value of t_order_share_notify_detail.share_date
     *
     * @mbggenerated
     */
    public String getShareDate() {
        return shareDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.share_date
     *
     * @param shareDate the value for t_order_share_notify_detail.share_date
     *
     * @mbggenerated
     */
    public void setShareDate(String shareDate) {
        this.shareDate = shareDate == null ? null : shareDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.status
     *
     * @return the value of t_order_share_notify_detail.status
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.status
     *
     * @param status the value for t_order_share_notify_detail.status
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.total_amount
     *
     * @return the value of t_order_share_notify_detail.total_amount
     *
     * @mbggenerated
     */
    public String getTotalAmount() {
        return totalAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.total_amount
     *
     * @param totalAmount the value for t_order_share_notify_detail.total_amount
     *
     * @mbggenerated
     */
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount == null ? null : totalAmount.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.currency
     *
     * @return the value of t_order_share_notify_detail.currency
     *
     * @mbggenerated
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.currency
     *
     * @param currency the value for t_order_share_notify_detail.currency
     *
     * @mbggenerated
     */
    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.error_code
     *
     * @return the value of t_order_share_notify_detail.error_code
     *
     * @mbggenerated
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.error_code
     *
     * @param errorCode the value for t_order_share_notify_detail.error_code
     *
     * @mbggenerated
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode == null ? null : errorCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.error_desc
     *
     * @return the value of t_order_share_notify_detail.error_desc
     *
     * @mbggenerated
     */
    public String getErrorDesc() {
        return errorDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.error_desc
     *
     * @param errorDesc the value for t_order_share_notify_detail.error_desc
     *
     * @mbggenerated
     */
    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc == null ? null : errorDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.ext_info
     *
     * @return the value of t_order_share_notify_detail.ext_info
     *
     * @mbggenerated
     */
    public String getExtInfo() {
        return extInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.ext_info
     *
     * @param extInfo the value for t_order_share_notify_detail.ext_info
     *
     * @mbggenerated
     */
    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo == null ? null : extInfo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.memo
     *
     * @return the value of t_order_share_notify_detail.memo
     *
     * @mbggenerated
     */
    public String getMemo() {
        return memo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.memo
     *
     * @param memo the value for t_order_share_notify_detail.memo
     *
     * @mbggenerated
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.gmt_create
     *
     * @return the value of t_order_share_notify_detail.gmt_create
     *
     * @mbggenerated
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.gmt_create
     *
     * @param gmtCreate the value for t_order_share_notify_detail.gmt_create
     *
     * @mbggenerated
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_share_notify_detail.gmt_modified
     *
     * @return the value of t_order_share_notify_detail.gmt_modified
     *
     * @mbggenerated
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_share_notify_detail.gmt_modified
     *
     * @param gmtModified the value for t_order_share_notify_detail.gmt_modified
     *
     * @mbggenerated
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}