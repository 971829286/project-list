package com.souche.bmgateway.core.domain;

import java.util.Date;

public class TradeSceneConfDO {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.ID
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.TRADE_TYPE
     *
     * @mbggenerated
     */
    private String tradeType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.BIZ_PRODUCT_CODE
     *
     * @mbggenerated
     */
    private String bizProductCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.PRODUCT_CODE
     *
     * @mbggenerated
     */
    private String productCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.PRODUCT_DESC
     *
     * @mbggenerated
     */
    private String productDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.BIZ_CODE
     *
     * @mbggenerated
     */
    private String bizCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.BIZ_DESC
     *
     * @mbggenerated
     */
    private String bizDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.FEE_CODE
     *
     * @mbggenerated
     */
    private String feeCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.FEE_DESC
     *
     * @mbggenerated
     */
    private String feeDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.BUYER_ID
     *
     * @mbggenerated
     */
    private String buyerId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.BUYER_NAME
     *
     * @mbggenerated
     */
    private String buyerName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.BUYER_ACCOUNT_TYPE
     *
     * @mbggenerated
     */
    private String buyerAccountType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.BUYER_ACCOUNT_IDENTITY
     *
     * @mbggenerated
     */
    private String buyerAccountIdentity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.SELLER_ID
     *
     * @mbggenerated
     */
    private String sellerId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.SELLER_NAME
     *
     * @mbggenerated
     */
    private String sellerName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.SELLER_ACCOUNT_TYPE
     *
     * @mbggenerated
     */
    private String sellerAccountType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.SELLER_ACCOUNT_IDENTITY
     *
     * @mbggenerated
     */
    private String sellerAccountIdentity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.MEMO
     *
     * @mbggenerated
     */
    private String memo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.EXTENSION
     *
     * @mbggenerated
     */
    private String extension;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.GMT_CREATED
     *
     * @mbggenerated
     */
    private Date gmtCreated;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trade_scene_conf.GMT_MODIFIED
     *
     * @mbggenerated
     */
    private Date gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.ID
     *
     * @return the value of t_trade_scene_conf.ID
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.ID
     *
     * @param id the value for t_trade_scene_conf.ID
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.TRADE_TYPE
     *
     * @return the value of t_trade_scene_conf.TRADE_TYPE
     *
     * @mbggenerated
     */
    public String getTradeType() {
        return tradeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.TRADE_TYPE
     *
     * @param tradeType the value for t_trade_scene_conf.TRADE_TYPE
     *
     * @mbggenerated
     */
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.BIZ_PRODUCT_CODE
     *
     * @return the value of t_trade_scene_conf.BIZ_PRODUCT_CODE
     *
     * @mbggenerated
     */
    public String getBizProductCode() {
        return bizProductCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.BIZ_PRODUCT_CODE
     *
     * @param bizProductCode the value for t_trade_scene_conf.BIZ_PRODUCT_CODE
     *
     * @mbggenerated
     */
    public void setBizProductCode(String bizProductCode) {
        this.bizProductCode = bizProductCode == null ? null : bizProductCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.PRODUCT_CODE
     *
     * @return the value of t_trade_scene_conf.PRODUCT_CODE
     *
     * @mbggenerated
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.PRODUCT_CODE
     *
     * @param productCode the value for t_trade_scene_conf.PRODUCT_CODE
     *
     * @mbggenerated
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.PRODUCT_DESC
     *
     * @return the value of t_trade_scene_conf.PRODUCT_DESC
     *
     * @mbggenerated
     */
    public String getProductDesc() {
        return productDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.PRODUCT_DESC
     *
     * @param productDesc the value for t_trade_scene_conf.PRODUCT_DESC
     *
     * @mbggenerated
     */
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc == null ? null : productDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.BIZ_CODE
     *
     * @return the value of t_trade_scene_conf.BIZ_CODE
     *
     * @mbggenerated
     */
    public String getBizCode() {
        return bizCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.BIZ_CODE
     *
     * @param bizCode the value for t_trade_scene_conf.BIZ_CODE
     *
     * @mbggenerated
     */
    public void setBizCode(String bizCode) {
        this.bizCode = bizCode == null ? null : bizCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.BIZ_DESC
     *
     * @return the value of t_trade_scene_conf.BIZ_DESC
     *
     * @mbggenerated
     */
    public String getBizDesc() {
        return bizDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.BIZ_DESC
     *
     * @param bizDesc the value for t_trade_scene_conf.BIZ_DESC
     *
     * @mbggenerated
     */
    public void setBizDesc(String bizDesc) {
        this.bizDesc = bizDesc == null ? null : bizDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.FEE_CODE
     *
     * @return the value of t_trade_scene_conf.FEE_CODE
     *
     * @mbggenerated
     */
    public String getFeeCode() {
        return feeCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.FEE_CODE
     *
     * @param feeCode the value for t_trade_scene_conf.FEE_CODE
     *
     * @mbggenerated
     */
    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode == null ? null : feeCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.FEE_DESC
     *
     * @return the value of t_trade_scene_conf.FEE_DESC
     *
     * @mbggenerated
     */
    public String getFeeDesc() {
        return feeDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.FEE_DESC
     *
     * @param feeDesc the value for t_trade_scene_conf.FEE_DESC
     *
     * @mbggenerated
     */
    public void setFeeDesc(String feeDesc) {
        this.feeDesc = feeDesc == null ? null : feeDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.BUYER_ID
     *
     * @return the value of t_trade_scene_conf.BUYER_ID
     *
     * @mbggenerated
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.BUYER_ID
     *
     * @param buyerId the value for t_trade_scene_conf.BUYER_ID
     *
     * @mbggenerated
     */
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId == null ? null : buyerId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.BUYER_NAME
     *
     * @return the value of t_trade_scene_conf.BUYER_NAME
     *
     * @mbggenerated
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.BUYER_NAME
     *
     * @param buyerName the value for t_trade_scene_conf.BUYER_NAME
     *
     * @mbggenerated
     */
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName == null ? null : buyerName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.BUYER_ACCOUNT_TYPE
     *
     * @return the value of t_trade_scene_conf.BUYER_ACCOUNT_TYPE
     *
     * @mbggenerated
     */
    public String getBuyerAccountType() {
        return buyerAccountType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.BUYER_ACCOUNT_TYPE
     *
     * @param buyerAccountType the value for t_trade_scene_conf.BUYER_ACCOUNT_TYPE
     *
     * @mbggenerated
     */
    public void setBuyerAccountType(String buyerAccountType) {
        this.buyerAccountType = buyerAccountType == null ? null : buyerAccountType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.BUYER_ACCOUNT_IDENTITY
     *
     * @return the value of t_trade_scene_conf.BUYER_ACCOUNT_IDENTITY
     *
     * @mbggenerated
     */
    public String getBuyerAccountIdentity() {
        return buyerAccountIdentity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.BUYER_ACCOUNT_IDENTITY
     *
     * @param buyerAccountIdentity the value for t_trade_scene_conf.BUYER_ACCOUNT_IDENTITY
     *
     * @mbggenerated
     */
    public void setBuyerAccountIdentity(String buyerAccountIdentity) {
        this.buyerAccountIdentity = buyerAccountIdentity == null ? null : buyerAccountIdentity.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.SELLER_ID
     *
     * @return the value of t_trade_scene_conf.SELLER_ID
     *
     * @mbggenerated
     */
    public String getSellerId() {
        return sellerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.SELLER_ID
     *
     * @param sellerId the value for t_trade_scene_conf.SELLER_ID
     *
     * @mbggenerated
     */
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId == null ? null : sellerId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.SELLER_NAME
     *
     * @return the value of t_trade_scene_conf.SELLER_NAME
     *
     * @mbggenerated
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.SELLER_NAME
     *
     * @param sellerName the value for t_trade_scene_conf.SELLER_NAME
     *
     * @mbggenerated
     */
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName == null ? null : sellerName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.SELLER_ACCOUNT_TYPE
     *
     * @return the value of t_trade_scene_conf.SELLER_ACCOUNT_TYPE
     *
     * @mbggenerated
     */
    public String getSellerAccountType() {
        return sellerAccountType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.SELLER_ACCOUNT_TYPE
     *
     * @param sellerAccountType the value for t_trade_scene_conf.SELLER_ACCOUNT_TYPE
     *
     * @mbggenerated
     */
    public void setSellerAccountType(String sellerAccountType) {
        this.sellerAccountType = sellerAccountType == null ? null : sellerAccountType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.SELLER_ACCOUNT_IDENTITY
     *
     * @return the value of t_trade_scene_conf.SELLER_ACCOUNT_IDENTITY
     *
     * @mbggenerated
     */
    public String getSellerAccountIdentity() {
        return sellerAccountIdentity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.SELLER_ACCOUNT_IDENTITY
     *
     * @param sellerAccountIdentity the value for t_trade_scene_conf.SELLER_ACCOUNT_IDENTITY
     *
     * @mbggenerated
     */
    public void setSellerAccountIdentity(String sellerAccountIdentity) {
        this.sellerAccountIdentity = sellerAccountIdentity == null ? null : sellerAccountIdentity.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.MEMO
     *
     * @return the value of t_trade_scene_conf.MEMO
     *
     * @mbggenerated
     */
    public String getMemo() {
        return memo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.MEMO
     *
     * @param memo the value for t_trade_scene_conf.MEMO
     *
     * @mbggenerated
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.EXTENSION
     *
     * @return the value of t_trade_scene_conf.EXTENSION
     *
     * @mbggenerated
     */
    public String getExtension() {
        return extension;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.EXTENSION
     *
     * @param extension the value for t_trade_scene_conf.EXTENSION
     *
     * @mbggenerated
     */
    public void setExtension(String extension) {
        this.extension = extension == null ? null : extension.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.GMT_CREATED
     *
     * @return the value of t_trade_scene_conf.GMT_CREATED
     *
     * @mbggenerated
     */
    public Date getGmtCreated() {
        return gmtCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.GMT_CREATED
     *
     * @param gmtCreated the value for t_trade_scene_conf.GMT_CREATED
     *
     * @mbggenerated
     */
    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trade_scene_conf.GMT_MODIFIED
     *
     * @return the value of t_trade_scene_conf.GMT_MODIFIED
     *
     * @mbggenerated
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trade_scene_conf.GMT_MODIFIED
     *
     * @param gmtModified the value for t_trade_scene_conf.GMT_MODIFIED
     *
     * @mbggenerated
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}