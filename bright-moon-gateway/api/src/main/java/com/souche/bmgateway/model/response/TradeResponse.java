package com.souche.bmgateway.model.response;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zs.
 * Created on 18/7/12.
 */
@Getter
@Setter
@Deprecated
public class TradeResponse extends CommonResponse {
    private static final long serialVersionUID = 194957435001049828L;

    private String memo;

    private String extension;


    @Override
    public String toString() {
        return "TradeResponse{" +
                "memo='" + memo + '\'' +
                ", extension='" + extension + '\'' +
                "} " + super.toString();
    }
}
