package com.souche.bmgateway.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SettleTranSumInfo {

    private Integer totalNum;

    private Integer payNum;

    private Integer successNum;

    private Integer failNum;

    private Integer accountErrorNum;

    private Integer handingNum;

    private Integer initailNum;
}
