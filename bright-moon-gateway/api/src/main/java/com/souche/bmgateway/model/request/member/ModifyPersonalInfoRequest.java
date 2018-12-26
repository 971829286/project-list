package com.souche.bmgateway.model.request.member;

import com.souche.bmgateway.model.request.CommonBaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * @author luobing 2018/11/29
 *
 */
@Setter
@Getter
@ToString(callSuper = true)
public class ModifyPersonalInfoRequest extends CommonBaseRequest {

	/*** 会员编号 ***/
	@NotBlank
	private String memberId;

	/*** 会员名称 ***/
	private String memberName; 

	/*** 身份证号 ***/
	private String idCardNo;

}
