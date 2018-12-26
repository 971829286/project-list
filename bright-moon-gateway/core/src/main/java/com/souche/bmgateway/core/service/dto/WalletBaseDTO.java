package com.souche.bmgateway.core.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 调用维金的serviceDTO 必须要继承此类
 *
 * @author zs. Created on 2018-12-18.
 */
@Setter
@Getter
@ToString
public class WalletBaseDTO implements Serializable {

	/*** 合作方id ***/
	private String partnerId;

	/*** 接口名称 ***/
	private String service;

}
