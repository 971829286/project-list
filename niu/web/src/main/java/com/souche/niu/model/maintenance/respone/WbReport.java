package com.souche.niu.model.maintenance.respone;

import java.io.Serializable;

/**
 * Created by sid on 2018/9/4.
 */
public class WbReport implements Serializable {

    private static final long serialVersionUID = 4182418376665635228L;


    //公里数
    private int kmValue;

    //维保类型
    private String repairType;

    //保养内容
    private String repairContent;

    //保养日期
    private String repairDate;

    //材料
    private String materal;


    public int getKmValue() {
        return kmValue;
    }

    public void setKmValue(int kmValue) {
        this.kmValue = kmValue;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getRepairContent() {
        return repairContent;
    }

    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }

    public String getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }

    public String getMateral() {
        return materal;
    }

    public void setMateral(String materal) {
        this.materal = materal;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("kmValue=").append(this.kmValue).append(",");
        sb.append("repairType=").append(this.repairType).append(",");
        sb.append("repairContent=").append(this.repairContent).append(",");
        sb.append("repairDate=").append(this.repairDate).append(",");
        sb.append("materal=").append(this.materal);
        return sb.toString();
    }
}
