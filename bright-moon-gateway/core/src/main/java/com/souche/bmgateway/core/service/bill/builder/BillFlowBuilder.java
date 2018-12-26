package com.souche.bmgateway.core.service.bill.builder;

import com.google.common.collect.Lists;
import com.souche.bmgateway.core.domain.BillFlow;
import com.souche.bmgateway.core.enums.BillTypeEnums;
import com.souche.bmgateway.core.enums.SourceEnums;
import com.souche.bmgateway.core.enums.StatusEnums;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhaojian
 * @date 18/7/16
 */
public class BillFlowBuilder {


    public static List<BillFlow> builderAllinPayBillFlowList(List<String[]> src) {

        List<BillFlow> result = Lists.newArrayListWithCapacity(src.size());
        if (CollectionUtils.isNotEmpty(src)) {
            for (String[] data : src) {
                BillFlow billFlow = new BillFlow();
                billFlow.setBusinessDate(data[15]);
                billFlow.setSerialNo(data[16]);
                billFlow.setOrderCode(data[17]);
                billFlow.setShopCode(data[0]);
                billFlow.setShopName(data[1]);
                billFlow.setBizType(BillTypeEnums.Grand.getCode());
                billFlow.setTradeAmount(new BigDecimal(data[12]));
                billFlow.setFee(new BigDecimal(data[13]));
                billFlow.setTradeType(data[5]);
                billFlow.setTradeStatus(StatusEnums.INITIAL.getCode());
                billFlow.setSource(SourceEnums.TLBILL.getCode());
                result.add(billFlow);
            }
        }

        return result;
    }
}
