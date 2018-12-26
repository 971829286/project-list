package com.souche.bmgateway.core.service.fos.impl;

import com.netfinworks.fos.service.facade.request.FundoutRequest;
import com.netfinworks.fos.service.facade.response.FundoutResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.manager.weijin.FosManager;
import com.souche.bmgateway.core.service.dto.WalletFundoutDTO;
import com.souche.bmgateway.core.service.fos.WalletFundoutService;
import com.souche.bmgateway.core.service.voucher.impl.VoucherServiceImpl;
import com.souche.bmgateway.core.util.UesService;
import com.souche.bmgateway.enums.ResponseCode;
import com.souche.bmgateway.model.response.trade.FundoutTradeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author luobing. Created on 18/11/19.
 */
@Service("walletFundoutService")
@Slf4j(topic = "service")
public class WalletFundoutServiceImpl implements WalletFundoutService {

	@Resource
	private FosManager fosManager;

	@Resource
	private UesService uesService;

	@Autowired
	private VoucherServiceImpl voucherService;


	@Override
	public FundoutTradeResponse walletFundout(WalletFundoutDTO wallentFundoutDTO) {
		log.info("提现，请求参数：{}", wallentFundoutDTO);
		FundoutResponse response = null;
		try {
			voucherService.setWalletFundOutVoucher(wallentFundoutDTO);
			voucherService.setPaymentVoucher(wallentFundoutDTO);
			FundoutRequest request = WalletFundoutConvertUtil.convertFundoutRequest(wallentFundoutDTO);
			// 传的卡号都为明文
			String encryptCardNo = uesService.encryptData(wallentFundoutDTO.getCardNo());
			wallentFundoutDTO.setCardNo(encryptCardNo);
			response = fosManager.submit(request);
		} catch (Exception e) {
			log.error("提现失败:请求参数:{},异常:{}", wallentFundoutDTO, e);
			return FundoutTradeResponse.createFailResp(new FundoutTradeResponse(),
					ErrorCodeEnums.MANAGER_SERVICE_ERROR.getCode(), "提现异常" + e.getMessage());
		}
		return buildFundoutTradeResult(response);

	}

	private FundoutTradeResponse buildFundoutTradeResult(FundoutResponse response) {
		FundoutTradeResponse fundoutTradeResponse = new FundoutTradeResponse();
		fundoutTradeResponse.setMsg(response.getReturnMessage());
		fundoutTradeResponse.setCode(response.getReturnCode());
		if (ResponseCode.APPLY_SUCCESS.getCode().equalsIgnoreCase(response.getReturnCode())) {
			fundoutTradeResponse.setSuccess(true);
		} else {
			fundoutTradeResponse.setSuccess(false);
		}
		return fundoutTradeResponse;

	}

}
