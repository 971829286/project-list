package com.souche.bmgateway.core.util;

import com.souche.optimus.common.util.HashUtil;

import java.util.UUID;

public class UUIDUtil {

    private final long now = 41478183418239l;

    //基于随机数的UUID
    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getNameUUID(byte[] name) {
        return UUID.nameUUIDFromBytes(name).toString();
    }

    /**
     * 生成包含时间的UUID
     */
    public static String getTimeID() {
        return com.souche.optimus.common.util.UUIDUtil.getID() + System.currentTimeMillis();
    }

    public static void main(String args[]) {
        byte[] b = {1,2};
        System.out.println(getRandomUUID());
        System.out.println( getNameUUID( b));
    }
}
