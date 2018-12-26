package com.souche.bmgateway.model.request.trade;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zs
 */
@Setter
@Getter
@ToString(callSuper = true)
public class TradeQueryRequest extends CommonBaseRequest {

    private static final long serialVersionUID = -8555641534682449788L;

    /**商户网站请求号**/
    @NotBlank
    private String outerOrderNo;
}
