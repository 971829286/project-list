package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 合作方机构枚举
 *
 * @author zs.
 * Created on 2018-12-18.
 */
@Getter
@AllArgsConstructor
public enum PartnerEnums {

    SOU_CHE("188888888888", true, "大搜车"),
    YI_CHENG_PAI("188888888888", true, "易成拍"),
    CHE_YI_PAI("188888888888", true, "车易拍"),
    SOUCHE_CHE_INNER("188888888888", false, "大搜车内部户");
    
    /*** 合作方id ***/
    private String partnerId;
    
    /*** 是否进交易见证 ***/
    private boolean isNeedWitness;

    /*** 描述 ***/
    private String desc;

    public static boolean isNeedWitness(String partnerId){
        for (PartnerEnums partnerEnums : PartnerEnums.values()) {
            if (partnerEnums.getPartnerId().equals(partnerId)) {
                return partnerEnums.isNeedWitness;
            }
        }
        return false;
    }
}
