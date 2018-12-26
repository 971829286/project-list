package com.souche.bmgateway.core.dubbo.api.impl;

import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.service.trade.TradeService;
import com.souche.bmgateway.core.util.ParamsValidate;
import com.souche.bmgateway.core.util.UesService;
import com.souche.bmgateway.dubbo.api.TradeFacade;
import com.souche.bmgateway.model.request.CreateInstantTradeRequest;
import com.souche.bmgateway.model.request.EntryRequest;
import com.souche.bmgateway.model.request.FundoutTradeRequest;
import com.souche.bmgateway.model.request.QueryPayRequest;
import com.souche.bmgateway.model.request.TransferAccountRequest;
import com.souche.bmgateway.model.response.EntryResponse;
import com.souche.bmgateway.model.response.FundoutTradeResponse;
import com.souche.bmgateway.model.response.TradeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zs.
 * Created on 18/7/17.
 */
@Service("tradeFacade")
@Slf4j(topic = "dubbo.impl")
@Deprecated
public class TradeFacadeImpl implements TradeFacade {

    @Resource
    private TradeService tradeService;

    @Resource
    UesService uesService;

    @Resource
    private WalletMemberService walletMemberService;


    @Override
    public TradeResponse createInstantTrade(CreateInstantTradeRequest createInstantTradeRequest) {
        log.info("即时到账交易，请求参数：{}", createInstantTradeRequest);
        ParamsValidate.ParamsValidateResult<CreateInstantTradeRequest> validateResult = ParamsValidate.validate
                (createInstantTradeRequest);
        if (validateResult.hasError()) {
            log.error("即时到账交易参数校验失败:" + validateResult.getMsgError());
            return TradeResponse.createFailResp(new TradeResponse(), ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(),
                    validateResult.getMsgError());
        }

        TradeResponse resp = tradeService.createInstantTrade(createInstantTradeRequest);
        return resp;
    }


    @Override
    public FundoutTradeResponse fundoutTrade(FundoutTradeRequest fundoutRequestDO) {
        log.info("提现，请求参数:{}" + fundoutRequestDO);

        ParamsValidate.ParamsValidateResult<FundoutTradeRequest> validateResult = ParamsValidate.validate
                (fundoutRequestDO);
        if (validateResult.hasError()) {
            log.error("出口请求参数校验失败:" + validateResult.getMsgError());
            return TradeResponse.createFailResp(new FundoutTradeResponse(), ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(),
                    validateResult.getMsgError());
        }
        try{
            String cardNo = fundoutRequestDO.getCardNo();
            if(cardNo.startsWith("P")){
                fundoutRequestDO.setCardNo(uesService.decryptData(cardNo));
            }
        }catch (Exception e){
            log.error(fundoutRequestDO.getPaymentOrderNo()+"卡号解密失败："+fundoutRequestDO.getCardNo());
        }

            //校验提交户名和认证实名是否一致
            //查真实姓名和身份证
//            String bankAccountName = fundoutRequestDO.getBankAccountName();
//            //6真实姓名,1身份证,2手机号
//            String accName = walletMemberService.queryMemberDecipherInfo(fundoutRequestDO.getMemberId(), 6);
//            if ((StringUtil.isEmpty(bankAccountName)) || (!bankAccountName.equals(accName))) {
//                log.error("110", "提现银行卡户名与实名认证姓名不一致");
//                return FundoutTradeResponse.createFailResp(new FundoutTradeResponse(), "110", "提现银行卡户名与实名认证姓名不一致");
//            }
            //出款
            return tradeService.submit(fundoutRequestDO);


    }

    @Override
    public TradeResponse transferAccount(TransferAccountRequest transferAccountRequest) {
        try {
            log.info("转账请求参数：" + transferAccountRequest);
            TradeResponse tradeResponse = tradeService.transferAccount(transferAccountRequest);
            log.info(transferAccountRequest.getOuterTradeNo()+",转账请求返回" + tradeResponse);
            return tradeResponse;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("转账请求异常");
        }

    }

    @Override
    public EntryResponse entry(EntryRequest entryRequest) {
        log.info("登账请求参数：" + entryRequest);
        EntryResponse entryResponse = tradeService.entry(entryRequest);
        log.info(entryRequest.getPaymentVoucherNo()+",登账返回参数：" + entryResponse);
        return entryResponse;
    }

    @Override
    public TradeResponse queryPay(QueryPayRequest queryTrade) {
        try {
            log.info("查询请求参数：" + queryTrade);
            TradeResponse tradeResponse = tradeService.queryTrade(queryTrade);
            log.info(queryTrade.getRequestNo()+",查询返回参数：" + queryTrade);
            return tradeResponse;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("查询请求异常");
        }

    }
}
