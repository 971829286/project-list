package com.souche.bmgateway.vo;

import com.souche.bmgateway.core.enums.FeeTypeEnum;
import com.souche.bmgateway.core.enums.PayChannelEnum;
import com.souche.optimus.common.util.Base64Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 手续费
 *
 * @author chenwj
 * @since 2018/7/16
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class FeeVO {

    private PayChannelEnum channelType;
    private FeeTypeEnum feeType;
    private String feeValue;

    public static String genJsonBase64(List<FeeVO> list) throws JSONException {
        List<JSONObject> arr = new ArrayList<JSONObject>();
        for (FeeVO param : list) {
            JSONObject obj = new JSONObject();
            obj.put("ChannelType", param.channelType.getChnCode());
            obj.put("FeeType", param.feeType.getFeeCode());
            obj.put("FeeValue", param.feeValue);
            arr.add(obj);
        }
        return Base64Util.encrypt(new JSONArray(arr).toString());
    }

}
