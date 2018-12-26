package com.souche.bmgateway.vo;

import com.souche.optimus.common.util.Base64Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 站点信息
 *
 * @author chenwj
 * @since 2018/07/16
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class SiteInfoVO implements Serializable {

    private String siteType;

    private String siteUrl;

    public static String genJsonBase64(List<SiteInfoVO> list) throws JSONException {
        List<JSONObject> arr = new ArrayList<JSONObject>();
        for (SiteInfoVO param : list) {
            JSONObject obj = new JSONObject();
            obj.put("SiteType", param.getSiteType());
            obj.put("SiteUrl", param.getSiteUrl());
            arr.add(obj);
        }
        return Base64Util.encrypt(new JSONArray(arr).toString());
    }

}
