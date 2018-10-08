package com.souche.niu.model.workTable;

import com.souche.optimus.dao.annotation.SqlTable;

import java.io.Serializable;
import java.sql.Date;

@SqlTable("user_coin")
public class UserCoinDo implements Serializable {

    private String phone;
    private int coinCount;
    private int status;
    private int commomBitStatus;
    private String extStr;
    private Date dateCreate;
    private Date dateUpdate;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCommomBitStatus() {
        return commomBitStatus;
    }

    public void setCommomBitStatus(int commomBitStatus) {
        this.commomBitStatus = commomBitStatus;
    }

    public String getExtStr() {
        return extStr;
    }

    public void setExtStr(String extStr) {
        this.extStr = extStr;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @Override
    public String toString() {
        return "UserCoinDo{" +
                "phone='" + phone + '\'' +
                ", coinCount=" + coinCount +
                ", status=" + status +
                ", commomBitStatus=" + commomBitStatus +
                ", extStr='" + extStr + '\'' +
                ", dateCreate=" + dateCreate +
                ", dateUpdate=" + dateUpdate +
                '}';
    }
}
