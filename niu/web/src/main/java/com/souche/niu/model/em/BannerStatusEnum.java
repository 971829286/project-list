package com.souche.niu.model.em;

/**
 * Banenr状态枚举类
 *      未上架:0
 *      已上架:1
 *      已下架:2
 * @author XuJinNiu
 * @since 2018-09-11
 */
public enum BannerStatusEnum {
    NO_ADDED("未上架",0),
    ADDED("已上架",1),
    REMOVED("已下架",2);

    private String name;
    private Integer index;
    BannerStatusEnum(String name, Integer index){
        this.name = name;
        this.index = index;
    }
    public static String getName(int index){
        for(BannerStatusEnum b : BannerStatusEnum.values()){
            if(b.getIndex() == index){
                return b.name;
            }
        }
        return null;
    }
    //get & set
    public String getName(){
        return name;
    }

    public int getIndex(){
        return index;
    }
}
