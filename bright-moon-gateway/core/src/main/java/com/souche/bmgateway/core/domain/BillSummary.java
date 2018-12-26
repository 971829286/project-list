package com.souche.bmgateway.core.domain;

import java.math.BigDecimal;
import java.util.Date;

public class BillSummary {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.business_date
     *
     * @mbggenerated
     */
    private String businessDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.batch_id
     *
     * @mbggenerated
     */
    private String batchId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.shop_code
     *
     * @mbggenerated
     */
    private String shopCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.shop_name
     *
     * @mbggenerated
     */
    private String shopName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.account_no
     *
     * @mbggenerated
     */
    private String accountNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.account_name
     *
     * @mbggenerated
     */
    private String accountName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.bank_code
     *
     * @mbggenerated
     */
    private String bankCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.bank_name
     *
     * @mbggenerated
     */
    private String bankName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.biz_type
     *
     * @mbggenerated
     */
    private String bizType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.currency
     *
     * @mbggenerated
     */
    private String currency;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.total_amount
     *
     * @mbggenerated
     */
    private BigDecimal totalAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.total_fee
     *
     * @mbggenerated
     */
    private BigDecimal totalFee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.trade_status
     *
     * @mbggenerated
     */
    private Integer tradeStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.retry_time
     *
     * @mbggenerated
     */
    private Integer retryTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.gmt_create
     *
     * @mbggenerated
     */

    private String remark;

    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_bill_summary.gmt_modify
     *
     * @mbggenerated
     */
    private Date gmtModify;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.id
     *
     * @return the value of t_bill_summary.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.id
     *
     * @param id the value for t_bill_summary.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.business_date
     *
     * @return the value of t_bill_summary.business_date
     *
     * @mbggenerated
     */
    public String getBusinessDate() {
        return businessDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.business_date
     *
     * @param businessDate the value for t_bill_summary.business_date
     *
     * @mbggenerated
     */
    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate == null ? null : businessDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.batch_id
     *
     * @return the value of t_bill_summary.batch_id
     *
     * @mbggenerated
     */
    public String getBatchId() {
        return batchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.batch_id
     *
     * @param batchId the value for t_bill_summary.batch_id
     *
     * @mbggenerated
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId == null ? null : batchId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.shop_code
     *
     * @return the value of t_bill_summary.shop_code
     *
     * @mbggenerated
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.shop_code
     *
     * @param shopCode the value for t_bill_summary.shop_code
     *
     * @mbggenerated
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.shop_name
     *
     * @return the value of t_bill_summary.shop_name
     *
     * @mbggenerated
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.shop_name
     *
     * @param shopName the value for t_bill_summary.shop_name
     *
     * @mbggenerated
     */
    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.account_no
     *
     * @return the value of t_bill_summary.account_no
     *
     * @mbggenerated
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.account_no
     *
     * @param accountNo the value for t_bill_summary.account_no
     *
     * @mbggenerated
     */
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.account_name
     *
     * @return the value of t_bill_summary.account_name
     *
     * @mbggenerated
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.account_name
     *
     * @param accountName the value for t_bill_summary.account_name
     *
     * @mbggenerated
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.bank_code
     *
     * @return the value of t_bill_summary.bank_code
     *
     * @mbggenerated
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.bank_code
     *
     * @param bankCode the value for t_bill_summary.bank_code
     *
     * @mbggenerated
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.bank_name
     *
     * @return the value of t_bill_summary.bank_name
     *
     * @mbggenerated
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.bank_name
     *
     * @param bankName the value for t_bill_summary.bank_name
     *
     * @mbggenerated
     */
    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.biz_type
     *
     * @return the value of t_bill_summary.biz_type
     *
     * @mbggenerated
     */
    public String getBizType() {
        return bizType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.biz_type
     *
     * @param bizType the value for t_bill_summary.biz_type
     *
     * @mbggenerated
     */
    public void setBizType(String bizType) {
        this.bizType = bizType == null ? null : bizType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.currency
     *
     * @return the value of t_bill_summary.currency
     *
     * @mbggenerated
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.currency
     *
     * @param currency the value for t_bill_summary.currency
     *
     * @mbggenerated
     */
    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.total_amount
     *
     * @return the value of t_bill_summary.total_amount
     *
     * @mbggenerated
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.total_amount
     *
     * @param totalAmount the value for t_bill_summary.total_amount
     *
     * @mbggenerated
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.total_fee
     *
     * @return the value of t_bill_summary.total_fee
     *
     * @mbggenerated
     */
    public BigDecimal getTotalFee() {
        return totalFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.total_fee
     *
     * @param totalFee the value for t_bill_summary.total_fee
     *
     * @mbggenerated
     */
    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.trade_status
     *
     * @return the value of t_bill_summary.trade_status
     *
     * @mbggenerated
     */
    public Integer getTradeStatus() {
        return tradeStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.trade_status
     *
     * @param tradeStatus the value for t_bill_summary.trade_status
     *
     * @mbggenerated
     */
    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.retry_time
     *
     * @return the value of t_bill_summary.retry_time
     *
     * @mbggenerated
     */
    public Integer getRetryTime() {
        return retryTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.retry_time
     *
     * @param retryTime the value for t_bill_summary.retry_time
     *
     * @mbggenerated
     */
    public void setRetryTime(Integer retryTime) {
        this.retryTime = retryTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.gmt_create
     *
     * @return the value of t_bill_summary.gmt_create
     *
     * @mbggenerated
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.gmt_create
     *
     * @param gmtCreate the value for t_bill_summary.gmt_create
     *
     * @mbggenerated
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_bill_summary.gmt_modify
     *
     * @return the value of t_bill_summary.gmt_modify
     *
     * @mbggenerated
     */
    public Date getGmtModify() {
        return gmtModify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_bill_summary.gmt_modify
     *
     * @param gmtModify the value for t_bill_summary.gmt_modify
     *
     * @mbggenerated
     */
    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}