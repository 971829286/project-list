package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author chenwj
 * @since 2018/8/9
 */
@Getter
@Setter
public class CreateAccountResponse extends CommonResponse implements Serializable {

    /**
     * 账户id
     */
    private String accountId;

}
