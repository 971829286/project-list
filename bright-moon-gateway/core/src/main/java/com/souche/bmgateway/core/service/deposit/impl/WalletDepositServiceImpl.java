package com.souche.bmgateway.core.service.deposit.impl;

import com.netfinworks.deposit.api.domain.DepositResponse;
import com.netfinworks.deposit.api.domain.ResponseResult;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.manager.weijin.DepositManager;
import com.souche.bmgateway.core.service.deposit.WalletDepositService;
import com.souche.bmgateway.core.service.dto.WalletDepositDTO;
import com.souche.bmgateway.core.service.voucher.impl.VoucherServiceImpl;
import com.souche.bmgateway.model.response.trade.WalletDepositResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author luobing. Created on 18/11/19.
 */
@Service("walletDepositService")
@Slf4j(topic = "service")
public class WalletDepositServiceImpl implements WalletDepositService {

	@Resource
	private DepositManager     depositManager;

	@Autowired
	private VoucherServiceImpl voucherService;


	@Override
	public WalletDepositResponse walletDeposit(WalletDepositDTO walletDepositDTO) {
		log.info("充值，请求参数：{}", walletDepositDTO);
		try {
			//交易凭证落地
			voucherService.setTradeVoucher(walletDepositDTO);
			// 支付凭证落地
			voucherService.setPaymentVoucher(walletDepositDTO);
			DepositResponse depositResponse = depositManager.deposit(WalletDepositConvert.convert(walletDepositDTO));
			return buildDepositResponse(depositResponse);
		} catch (Exception e) {
			log.info("充值失败：请求参数{},异常：{}", walletDepositDTO, e);
			return WalletDepositResponse.createFailResp(new WalletDepositResponse(), ErrorCodeEnums
					.MANAGER_SERVICE_ERROR.getCode(), "充值异常：" + e.getMessage());
		}
	}

	/**
	 * 构建充值返回结果
	 *
	 * @param depositResponse
	 * @return
	 */
	public WalletDepositResponse buildDepositResponse(DepositResponse depositResponse) {
		WalletDepositResponse walletDepositResponse = new WalletDepositResponse();
		ResponseResult respResult = depositResponse.getRespResult();
		if (null != respResult) {
			walletDepositResponse.setSuccess(respResult.isSuccess());
			walletDepositResponse.setCode(respResult.getErrorCode());
			walletDepositResponse.setMsg(respResult.getResultMessage());
		}
		walletDepositResponse.setExtension(depositResponse.getExtension());
		walletDepositResponse.setFormContent(depositResponse.getFormContent());
		walletDepositResponse.setInstOrderNo(depositResponse.getInstOrderNo());
		walletDepositResponse.setVoucherNo(depositResponse.getVoucherNo());
		return walletDepositResponse;
	}
}
