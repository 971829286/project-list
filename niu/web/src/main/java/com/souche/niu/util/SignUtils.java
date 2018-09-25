package com.souche.niu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SignUtils
 *
 * @author ZhangHui
 * @since 2018-09-10
 */
public class SignUtils {

    /**
     * encrypt
     *
     * @author ZhangHui
     * @since 2018-09-10
     */
    public static String encrypt(String str) {
        StringBuilder signStr = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            // 不能直接这样转换，这样转换前面可能会丢失字符串前面的0，比如 str=189488798102 时
            // signStr = new BigInteger(1, md.digest()).toString(16);
            for (byte b : md.digest()) {
                // 10进制转16进制，x 表示以十六进制形式输出，02 表示不足两位前面补0输出
                signStr.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signStr.toString();
    }

}
