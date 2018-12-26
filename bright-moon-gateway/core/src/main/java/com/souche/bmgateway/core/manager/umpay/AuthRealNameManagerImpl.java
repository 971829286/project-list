package com.souche.bmgateway.core.manager.umpay;

import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.model.AuthRealNameResponse;
import com.souche.bmgateway.core.util.HttpClientUtil;
import com.souche.bmgateway.core.util.UmpaySignUtil;
import com.souche.optimus.common.config.OptimusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zs.
 *         Created on 18/8/4.
 */
@Service("AuthRealNameManager")
@Slf4j(topic = "manager")
public class AuthRealNameManagerImpl implements AuthRealNameManager {

    private static final String MER_ID = OptimusConfig.getValue("umpay.merId");
    private static final String URL = OptimusConfig.getValue("umpay.url");
    private static final Pattern pattern = Pattern.compile("CONTENT=\"(.*)\"");

    @Override
    public AuthRealNameResponse realNameAuth(String orderId, String realName, String idCard) throws ManagerException {
        AuthRealNameResponse authRealNameResponse = new AuthRealNameResponse();
        try {
            log.info("调用联动实名认证，请求参数：{}", orderId + "|" + realName + "|" + idCard);
            TreeMap<String, String> params = buildUmpayRequestParams(orderId, realName, idCard);
            long beginTime = System.currentTimeMillis();
            Map<String, String> response = parseResponse(HttpClientUtil.postUrlParams(URL, params));
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("调用联动实名认证，耗时:{} (ms); 响应结果:{} ", consumeTime, response);

            //验证签名
            String returnSign = new String(response.get("sign").getBytes(), "utf-8");
            response.remove("sign");
            response.remove("sign_type");
            String returnParamStr = UmpaySignUtil.toParamStr(true, response, null);
            if (!UmpaySignUtil.verify(returnParamStr, returnSign)) {
                log.error("联动验签失败" + returnParamStr);
                throw new ManagerException(ErrorCodeEnums.UMPAY_AUTH_ERROR, "联动验签失败");
            }
            authRealNameResponse.setSuccess(Constants.UMPAY_SUCCESS_CODE.equals(response.get("ret_code")));
            authRealNameResponse.setCode(response.get("ret_code"));
            authRealNameResponse.setMsg(response.get("ret_msg"));
        } catch (Exception e) {
            if (e instanceof ManagerException) {
                throw (ManagerException) e;
            }
            log.error("调用联动实名认证异常：请求参数：{}, e: {}", realName + "|" + idCard, e);
            throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR);
        }
        return authRealNameResponse;
    }

    /**
     * 构造联动实名认证请求参数
     *
     * @param realName
     * @param idCard
     * @return
     * @throws Exception
     */
    private TreeMap<String, String> buildUmpayRequestParams(String orderId, String realName, String idCard)
            throws Exception {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("service", Constants.SERVICE);
        params.put("mer_id", MER_ID);
        params.put("charset", Constants.CHARSET);
        params.put("version", Constants.VERSION);
        params.put("order_id", orderId);
        params.put("auth_type", Constants.AUTH_TYPE);
        params.put("auth_mode", Constants.AUTH_MODE);
        params.put("account_name", UmpaySignUtil.encrypt(realName));
        params.put("identity_code", UmpaySignUtil.encrypt(idCard));
        String paramStr = UmpaySignUtil.toParamStr(true, params, null);
        String sign = UmpaySignUtil.signRSA(paramStr, MER_ID);
        params.put("sign", sign);
        params.put("sign_type", Constants.SIGN_TYPE);
        return params;
    }

    /**
     * 解析联动返回请求
     *
     * @param httpRes
     * @return
     */
    public static Map<String, String> parseResponse(String httpRes) throws ManagerException {
        Map<String, String> map = new HashMap<String, String>(128);
        Matcher matcher = pattern.matcher(httpRes);
        String res = "";
        if (matcher.find()) {
            res = matcher.group(1);
        } else {
            throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR, "联动报文解析失败");
        }
        String[] strArr = res.split("&");
        for (String string : strArr) {
            String[] array = string.split("=", 2);
            if (array.length != 2) {
                map.put(array[0], null);
                continue;
            }
            map.put(array[0], array[1]);
        }
        return map;
    }
}
