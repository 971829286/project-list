package com.souche.niu.spi.adapter;

import com.souche.niu.constant.Constants;
import com.souche.niu.spi.SpaySPI;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.scashier.sdk.facade.refund.RefundFacade;
import com.souche.scashier.sdk.request.refund.UnfrozenRefundRequest;
import com.souche.scashier.sdk.response.refund.UnfrozenRefundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Created by sid on 2018/9/10.
 */
@Component
public class SpaySPIAdapter implements SpaySPI {

    private static final Logger logger = LoggerFactory.getLogger(SpaySPIAdapter.class);

    @Autowired
    private RefundFacade refundFacade;

    @Override
    public boolean refund(String orderId,int refundFens) {
        UnfrozenRefundRequest unfrozenRefundRequest=new UnfrozenRefundRequest();
        unfrozenRefundRequest.setRequestNo(UUID.randomUUID().toString());
        unfrozenRefundRequest.setRefundAmountFens(refundFens);
        unfrozenRefundRequest.setBizOrderId(orderId);
        unfrozenRefundRequest.setMqTag(Constants.BUSSIZE_REFUND_TAG);
        unfrozenRefundRequest.setRefunService(Constants.BUSSIZE_REFUND_SETREFUNSERVICE);
        try {
            logger.info("退款 准备调用订单中心DUBBO接口退款 参数 unfrozenRefundRequest={}",JsonUtils.toJson(unfrozenRefundRequest));
            UnfrozenRefundResponse refundResponse = refundFacade.unfrozenRefund(unfrozenRefundRequest);
            logger.info("退款 请求返回结果:{}", JsonUtils.toJson(refundResponse));
            return refundResponse.isSuccess();
        }catch (Exception e){
            logger.error("发起退款动作失败",e);
            return false;
        }
    }
}
