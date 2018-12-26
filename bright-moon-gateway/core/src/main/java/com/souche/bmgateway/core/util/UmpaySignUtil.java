package com.souche.bmgateway.core.util;

import com.umpay.api.util.CipherUtil;
import com.umpay.api.util.SignUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author zs.
 *         Created on 18/8/4.
 */
public class UmpaySignUtil {

    public static String signRSA(String content, String merId) throws Exception {
        return SignUtil.sign(content, merId);
    }


    public static boolean verify(String content, String sign) {

        return SignUtil.verify(sign, content);
    }

    public static String encrypt(String content) throws Exception {
        return CipherUtil.Encrypt(content);
    }


    public static String toParamStr(boolean sort, Map<String, String> map, String enc) throws
            UnsupportedEncodingException {
        StringBuilder buff = new StringBuilder();
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        map.entrySet();
        Collections.sort(infoIds, (o1, o2) -> (o1.getKey()).toString().compareTo(o2.getKey()));

        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> item = infoIds.get(i);
            if (item.getKey() != "") {
                String key = item.getKey();
                String val = item.getValue();
                if (val == null) {
                    continue;
                }
                if (enc != null && enc.length() > 0) {
                    val = URLEncoder.encode(val, enc);

                }
                buff.append(key).append("=").append(val).append("&");
            }
        }

        if (buff.length() > 0) {
            return buff.substring(0, buff.length() - 1);
        }

        return buff.toString();

    }

    public static Map<String, String> toMap(Map<String, String> map) {
        Map<String, String> mMap = new HashMap<String, String>();
        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry<String, String> entry : set) {
            mMap.put(entry.getKey(), entry.getValue());
        }
        return mMap;
    }
}
