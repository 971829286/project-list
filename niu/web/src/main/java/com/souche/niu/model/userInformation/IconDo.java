package com.souche.niu.model.userInformation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *图标
 */
public class IconDo implements Serializable {

    private String allutilprotocol;

    private String allutilname;

    private List<IconUtilDo> util ;

    public String getAllutilprotocol() {
        return allutilprotocol;
    }

    public void setAllutilprotocol(String allutilprotocol) {
        this.allutilprotocol = allutilprotocol;
    }

    public String getAllutilname() {
        return allutilname;
    }

    public void setAllutilname(String allutilname) {
        this.allutilname = allutilname;
    }

    public List<IconUtilDo> getUtil() {
        return util;
    }

    public void setUtil(List<IconUtilDo> util) {
        this.util = util;
    }

    @Override
    public String toString() {
        return "IconDo{" +
                "allutilprotocol='" + allutilprotocol + '\'' +
                ", allutilname='" + allutilname + '\'' +
                ", util=" + util +
                '}';
    }
}
