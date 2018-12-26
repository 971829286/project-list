package com.souche.bmgateway.core.dto.response;

import java.math.BigDecimal;

public class SumDataDTO {

    private String businessDate;

    private String type;

    private BigDecimal totalAmount;

    private Integer totalCount;

    private BigDecimal abnormalAmount;

    private Integer abnormalCount;

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getAbnormalAmount() {
        return abnormalAmount;
    }

    public void setAbnormalAmount(BigDecimal abnormalAmount) {
        this.abnormalAmount = abnormalAmount;
    }

    public Integer getAbnormalCount() {
        return abnormalCount;
    }

    public void setAbnormalCount(Integer abnormalCount) {
        this.abnormalCount = abnormalCount;
    }
}
