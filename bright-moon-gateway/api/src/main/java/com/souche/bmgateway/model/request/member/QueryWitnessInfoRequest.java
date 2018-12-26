package com.souche.bmgateway.model.request.member;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zs.
 * Created on 2018-12-14.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class QueryWitnessInfoRequest extends CommonBaseRequest {

    /*** 钱包id ***/
    @NotBlank
    private String memberId;
}
