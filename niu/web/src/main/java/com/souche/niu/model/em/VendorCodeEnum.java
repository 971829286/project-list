package com.souche.niu.model.em;

/**
 * Created by sid on 2018/9/8.
 */
public enum VendorCodeEnum {

    CJD("CJD",1, "车鉴定"),
    DSLL("DSLL",2,"大圣来了"),
    CBS("CBS",3, "查博士"),
    CHE300("CHE300",4,"车300"),
    DSC("DSC",5,"大搜车"),
    JINGZHENGU("JINGZHENGU",6,"精真估"),
    CHEHU("CHEHU",7,"车虎"),
    MYNW("MYNW",8,"蚂蚁女王"),
    BX4139("BX4139",9,"保险理赔4139"),
    CACHEBX("CACHEBX",10,"保险理赔缓存"),
    JCXM("JCXM",11, "鉴车小妹"),
    XY("XY",12,"祥云保险"),
    CACHEWB("CACHEWB",13,"维保缓存"),
    CACHEDBWB("CACHEDBWB",14,"维保缓存定期"),
    JUHE("JUHE",15,"聚合数据"),
    CACHEWZ("CACHEWZ",16,"违章数据缓存");

    private String code;

    private int index;

    private String description;

    VendorCodeEnum(String code,int index,String description) {
        this.code = code;
        this.description = description;
        this.index=index;
    }

    public static VendorCodeEnum getByCode(String code) {
        for (VendorCodeEnum ls : VendorCodeEnum.values()) {
            if (ls.code.equalsIgnoreCase(code)) {
                return ls;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public int getIndex() {
        return index;
    }
}
