package com.souche.bmgateway.core.witness.aspect;

import com.alibaba.fastjson.JSON;
import com.netfinworks.ma.service.base.model.CompanyInfo;
import com.netfinworks.ma.service.request.BankAccInfoRequest;
import com.netfinworks.ma.service.request.BankAccRemoveRequest;
import com.netfinworks.ma.service.request.PersonalMemberInfoRequest;
import com.netfinworks.ma.service.response.IntegratedPersonalResponse;
import com.souche.bmgateway.core.manager.weijin.CurrentOperationEnvironment;
import com.souche.bmgateway.core.witness.WitnessMemberDataDealService;
import com.souche.bmgateway.model.request.CreateEnterpriseMemberRequest;
import com.souche.bmgateway.model.request.member.CreatePersonalMemberRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 会员见证切面类
 *
 * @author luobing 20018/11/27 选择manager来做见证
 *
 */
@Component
@Aspect
@Slf4j(topic = "witness")
public class MemberWitnessAspectUtil {

	@Resource
	private WitnessMemberDataDealService witnessMemberDataDealService;

	@Pointcut("@annotation(com.souche.bmgateway.core.witness.aspect.MemberWitness)")
	public void memberWitness() {}

	@Around("memberWitness()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		if (!CurrentOperationEnvironment.isNeedWitness()) {
			return joinPoint.proceed();
		}

		log.info("会员见证网关请求参数：" + JSON.toJSONString(joinPoint.getArgs()));
		Object response = null;
		try {
			response = joinPoint.proceed();
			return response;
		} catch (Throwable throwable) {
			log.error("请求异常", throwable);
			throw throwable;
		} finally {
			try {
				doMemberWitnessDataDeal(joinPoint.getArgs(), response);
			} catch (Exception e) {
				log.error("会员数据同步失败", e);
			}
		}
	}

	private void doMemberWitnessDataDeal(Object[] object, Object response) {
		if (object[0] instanceof String) {
			// 同步见证服务--开通账户
			witnessMemberDataDealService.witnessAccountOpen((String) object[0], (long) object[1]);
		} else if (object[0] instanceof CreateEnterpriseMemberRequest) {
			// 同步见证服务-创建企业会员信息
			witnessMemberDataDealService.witnessCreateEnterpriseMember((CreateEnterpriseMemberRequest) object[0]);
		} else if (object[0] instanceof CompanyInfo) {
			// 同步见证服务-修改企业会员信息
			witnessMemberDataDealService.witnessModifyEnterpriseInfo((CompanyInfo) object[0]);
		} else if (object[0] instanceof BankAccInfoRequest) {
			// 同步见证服务-绑定银行卡
			witnessMemberDataDealService.witnessCreateBankCard((BankAccInfoRequest) object[0]);
		} else if (object[0] instanceof BankAccRemoveRequest) {
			// 同步见证服务-解绑银行卡
			witnessMemberDataDealService.witnessUnbindBankCard((BankAccRemoveRequest) object[0]);
		} else if (object[0] instanceof CreatePersonalMemberRequest) {
			// 同步见证服务-创建个人会员
			IntegratedPersonalResponse personalResponse = (IntegratedPersonalResponse) response;
			witnessMemberDataDealService.witnessCreatePersonalMember((CreatePersonalMemberRequest) object[0],
					personalResponse.getMemberId());
		} else if (object[0] instanceof PersonalMemberInfoRequest) {
			// 同步见证服务-修改个人会员信息
			witnessMemberDataDealService.witnessModifyPersonalInfo((PersonalMemberInfoRequest) object[0]);
		}
	}

}
