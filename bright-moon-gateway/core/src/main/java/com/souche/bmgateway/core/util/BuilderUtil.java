package com.souche.bmgateway.core.util;

import com.souche.optimus.common.config.OptimusConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author zs
 */
public class BuilderUtil {

    public final static String MD5KEY = OptimusConfig.getValue("gateway.md5key");
    public final static String RSA_PRIVATE_KEY = OptimusConfig.getValue("gateway.privatekey");

    public static Map<String, String> build(Object paramsBean) {
        TreeMap<String, String> params = ObjectUtil.convertObjToMap(paramsBean);
        String signType = StringUtils.isEmpty(params.get("sign_type")) ? "MD5" : params.get("sign_type");
        String sign = "";
        params.remove("sign_type");
        params.remove("sign");
        if (signType.equalsIgnoreCase("MD5")) {
            sign = GatewaySignUtil.signMD5(params, MD5KEY);

        }
        if (signType.equalsIgnoreCase("RSA")) {
            sign = GatewaySignUtil.signRSA(params, RSA_PRIVATE_KEY);
        }
        params.put("sign", sign);
        params.put("sign_type", signType);
        return paramFilter(params);
    }

    /**
     * 参数过滤
     *
     * @param sArray
     * @return
     */
    public static Map<String, String> paramFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>(64);
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("")) {
                continue;
            }
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            result.put(key, value);
        }
        return result;
    }
}
