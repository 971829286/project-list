package com.souche.bmgateway.core.service.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.netfinworks.common.lang.StringUtil;
import com.souche.bmgateway.core.domain.ShopInfo;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.enums.CardAttrEnums;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.manager.shop.ShopManager;
import com.souche.bmgateway.core.service.bill.service.AccountService;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.util.UesService;
import com.souche.bmgateway.model.request.QueryBankAccountRequest;
import com.souche.bmgateway.model.response.BankAccountListResponse;
import com.souche.bmgateway.model.response.BankAccountRecordInfo;
import com.souche.optimus.common.util.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j(topic = "service")
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private ShopManager shopManager;

    @Resource
    private WalletMemberService walletMemberService;

    @Resource
    private UesService uesService;

    @Override
    public ShopInfo queryAccountNoByShopCode(String shopCode) {
        try {
            HttpBaseResponse response = shopManager.queryShopInfo(shopCode);
            if (!response.isSuccess()) {
                throw Exceptions.fault(ErrorCodeEnums.INVOKE_HTTP_ERROR.getCode(), "<查询商户信息>调用HTTP接口失败");
            }

            String shopAuthResp = response.getData().get("shopAuth").toString();
            log.info("<查询商户信息> 响应->{}", shopAuthResp);
            JSONObject json = JSONObject.parseObject(JSONObject.parseObject(shopAuthResp).getString("data"));
            JSONObject shopJson = json.getJSONObject("shop");
            if(shopJson == null) {
                throw Exceptions.fault(ErrorCodeEnums.RET_OBJECT_IS_NULL.getCode(), "<查询商户信息>查询商户信息失败");
            }
            String memberId = shopJson.getString("walletAccountId");
            String shopName = shopJson.getString("name");
            if (StringUtil.isBlank(memberId)) {
                throw Exceptions.fault(ErrorCodeEnums.RET_PARAM_IS_NULL.getCode(), "<查询商户信息>钱包ID为空");
            }
            BankAccountRecordInfo recordInfo = getAccountByMemberId(memberId);
            return buildShopInfo(shopCode, shopName, recordInfo);
        } catch (ManagerException e) {
            throw Exceptions.fault(ErrorCodeEnums.INVOKE_HTTP_ERROR.getCode(), "<查询企业信息>调用HTTP接口失败");
        }
    }

    private BankAccountRecordInfo getAccountByMemberId(String memberId) {
        QueryBankAccountRequest queryBankAccountRequest = new QueryBankAccountRequest();
        queryBankAccountRequest.setMemberId(memberId);
        queryBankAccountRequest.setCardAttribute(CardAttrEnums.ENTERPRISE.getCode());
        BankAccountListResponse bankAccountResponse = walletMemberService.queryBankAccountList(queryBankAccountRequest);
        BankAccountRecordInfo accountRecordInfo = bankAccountResponse.getBankAccountRecordInfoList().get(0);
        return accountRecordInfo;

    }

    private ShopInfo buildShopInfo(String shopCode, String shopName, BankAccountRecordInfo bankAccountInfo) {
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setShopCode(shopCode);
        shopInfo.setShopName(shopName);
        shopInfo.setAccountNo(decryptAccount(bankAccountInfo));
        shopInfo.setAccountName(bankAccountInfo.getRealName());
        shopInfo.setBankName(bankAccountInfo.getBankName());
        shopInfo.setBankCode(bankAccountInfo.getBankCode());
        return shopInfo;
    }

    private String decryptAccount(BankAccountRecordInfo bankAccountInfo) {
        String accountNo = null;
        if(!StringUtil.isBlank(bankAccountInfo.getExtention())) {
            try {
                JSONObject jsonAcctNo = JSONObject.parseObject(bankAccountInfo.getExtention());
                accountNo = uesService.getDataByTicket(jsonAcctNo.getString(Constants.BANK_ACCOUNT_NO));
                if (StringUtil.isBlank(accountNo)) {
                    log.error("<查询绑卡信息>银行卡号为空, bankAccountInfo={}", bankAccountInfo);
                }
            } catch (Exception e) {
                throw Exceptions.fault(ErrorCodeEnums.SYSTEM_ERROR.getCode(), "<查询绑卡信息>获取银行卡号失败");
            }
        }

        log.info("<查询绑卡信息>银行卡号解密后->{}", accountNo);
        return accountNo;
    }
}
