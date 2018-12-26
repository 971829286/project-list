package com.souche.bmgateway.core.util;

import com.alibaba.fastjson.JSON;
import com.netfinworks.common.lang.StringUtil;
import com.netfinworks.common.util.money.Money;
import com.netfinworks.voucher.service.facade.domain.access.GatewayAccessInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.PaymentVoucherInfo;
import com.netfinworks.voucher.service.facade.domain.voucher.SimpleOrderVoucherInfo;
import com.souche.bmgateway.core.manager.model.trade.PaymentToCardReq;
import com.souche.bmgateway.core.manager.model.trade.RequestBase;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Date;

public class VoucherHelper {

    
    /**
     * 不可实例化
     */
    private VoucherHelper() {

    }

    /**
     * 基本参数转换
     * @param request
     * @return
     * @throws Exception
     */
    private static GatewayAccessInfo baseFrom(RequestBase base)  {

        GatewayAccessInfo accessInfo = new GatewayAccessInfo();
        accessInfo.setInputCharset(base.getInputCharset());
        accessInfo.setMemo(base.getMemo());
        accessInfo.setPartnerId(base.getPartnerId());
        accessInfo.setReturnUrl(base.getReturnUrl());
        accessInfo.setService(base.getService());
        accessInfo.setSign(base.getSign());
        accessInfo.setSignType(base.getSignType());

        return accessInfo;
    }


    /**
     * 转付款到卡对象
     * @param
     * @return
     * @throws Exception
     */
    public static SimpleOrderVoucherInfo paymentToCardReqTradeFrom(PaymentToCardReq req)
                                                                              {
        SimpleOrderVoucherInfo simpleInfo = new SimpleOrderVoucherInfo();
        GatewayAccessInfo accessInfo = baseFrom(req);
        accessInfo.setNotifyUrl(req.getNotifyUrl());

        simpleInfo.setAccess(JSON.toJSONString(accessInfo));
        simpleInfo.setAccessType(GatewayAccessInfo.accessType());

        simpleInfo.setProductCode(req.getProductCode());
        simpleInfo.setSource(req.getPartnerId());
        simpleInfo.setSourceVoucherNo(req.getOuterTradeNo());
        simpleInfo.setAmount(convertMoney(req.getPayableAmount()));
        simpleInfo.setPayerId(req.getMemberId());
        simpleInfo.setRequestTime(new Date());
        return simpleInfo;
    }


     /**
     * 转付款到卡对象
     * @param
     * @return
     * @throws Exception
     */
    public static PaymentVoucherInfo payFromByPaymentToCardReq(PaymentToCardReq req)  {
        PaymentVoucherInfo payInfo = new PaymentVoucherInfo();
        GatewayAccessInfo accessInfo = baseFrom(req);
        accessInfo.setNotifyUrl(req.getNotifyUrl());

        payInfo.setAccess(JSON.toJSONString(accessInfo));
        payInfo.setAccessType(GatewayAccessInfo.accessType());
        req.setSign(" ");
        payInfo.setPaymentDetail(JSON.toJSONString(req));
        payInfo.setProductCode(req.getProductCode());
        payInfo.setSource(req.getPartnerId());
        payInfo.setSourceVoucherNo(req.getOuterTradeNo());

        return payInfo;
    }

    public static Money convertMoney(String moneyStr) {
        Money result = new Money();
        if (StringUtil.isEmpty(moneyStr)) {
            return result;
        } else {
            try {
                result = new Money(moneyStr);
            } catch (NumberFormatException e) {
                // 金额格式错误，有字母等
                throw new RuntimeException("金额格式错误，有字母等");
            }
            return result;
        }
    }
}
