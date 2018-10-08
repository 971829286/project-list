package com.souche.niu.dto;

import java.io.Serializable;

/**
 * DTO
 *
 * @since 2018-09-10
 * @author ZhangHui
 */
public class MaintenanceRecordDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 3462670256576004656L;

    private String phone;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
