package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: huangbin
 * @Description: 分账结果枚举类
 * @Date: Created in 2018/07/12
 * @Modified By:
 */
@Getter
@AllArgsConstructor
public enum ShareResult {

    SUCCESS("SUCCESS","S","分账成功"),
    OTHER("OTHER","O","未知分账结果"),
    FAIL("FAIL","F","分账失败");

    private final String code;
    private final String nitifyStatus;
    private final String msg;

    /**
     * 根据code得到分账结果
     * @param code
     * @return
     */
    public static ShareResult getShareResultByCode(String code){

        for (ShareResult shareResult : ShareResult.values()) {
            if (shareResult.getCode().equals(code)) {
                return shareResult;
            }
        }
        return OTHER;
    }

}
