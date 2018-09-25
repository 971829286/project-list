package com.souche.niu.model;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description：金额配置表 此表只包含一条记录
 *
 * @remark: Created by wujingtao in 2018/9/12
 **/
public class MoneyCfgDto implements Serializable {

    private Integer id;
    private BigDecimal noCertification;
    private BigDecimal certification;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getNoCertification() {
        return noCertification;
    }

    public void setNoCertification(BigDecimal noCertification) {
        this.noCertification = noCertification;
    }

    public BigDecimal getCertification() {
        return certification;
    }

    public void setCertification(BigDecimal certification) {
        this.certification = certification;
    }
}
