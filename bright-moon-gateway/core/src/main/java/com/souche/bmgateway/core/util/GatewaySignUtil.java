package com.souche.bmgateway.core.util;

import com.souche.optimus.common.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 网关接口签名工具类
 *
 * @author sirk
 */
public class GatewaySignUtil {

    public static final String CHAR_ENCODING = "UTF-8";

    /**
     * MD5签名
     *
     * @param map
     * @param md5Key
     * @return
     */
    public static String signMD5(Map<String, String> map, String md5Key) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String sign = "";
        if (StringUtils.isNotEmpty(md5Key)) {
            sign = MD5Utils.md5(stringBuilder.substring(0, stringBuilder.length()) + md5Key);
        }
        return sign;
    }

    /**
     * 校验MD5签名
     *
     * @param content
     * @param sign
     * @param md5Key
     * @return
     */
    public static boolean checkMD5Sign(String content, String sign, String md5Key) {
        if (sign.equals(content + md5Key)) {
            return true;
        }
        return false;
    }


    /**
     * RSA签名
     *
     * @param map
     * @return
     */
    public static String signRSA(Map<String, String> map, String privateKey) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String sign = "";
        if (StringUtils.isNotEmpty(privateKey)) {
            sign = RSAUtil.sign(stringBuilder.substring(0, stringBuilder.length() - 1), privateKey, CHAR_ENCODING);
        }
        return sign;
    }


    /**
     * 校验RSA签名
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     */
    public static boolean checkRSASign(String content, String sign, String publicKey) {
        return RSAUtil.verify(content, sign, publicKey, CHAR_ENCODING);
    }

}
