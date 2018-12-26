package com.souche.bmgateway.core.manager;
import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.dto.request.InstantTradeRequest;
import com.souche.bmgateway.core.dto.request.TradeCommonRequest;
import com.souche.bmgateway.core.manager.weijin.TradeManager;
import com.souche.bmgateway.core.util.BuilderUtil;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: yeyinxian
 * @Date: 2018/8/4 下午2:27
 */
public class TradeManagerTest extends BaseTest{

    @Resource
    TradeManager tradeManager;

    public static void common(TradeCommonRequest request){
        request.setVersion("1.0");
        request.set_input_charset("UTF-8");
        request.setSign_type("RSA");
        request.setPartner_id("188888888888");
    }

    @Test
    public void TestInstantTrade(){
        InstantTradeRequest instantTradeRequest = new InstantTradeRequest();
        common(instantTradeRequest);
        String instId = UUID.randomUUID().toString().substring(0,18);
        String len = instId.length()+"";
        instantTradeRequest.setService("create_instant_trade");
        instantTradeRequest.setRequest_no(System.currentTimeMillis()+"");
        instantTradeRequest.setTrade_list(len+":"+instId+"~1:0~4:0.01~1:1~4:0.01~0:~12:200000000035~9:MEMBER_ID~" +
                len+":"+instId+"~0:~0:~0:~0:~0:~0:~64:http://spay-inner.stable.proxy.dasouche.com/notify/receive_trade~0:");
        instantTradeRequest.setBuyer_id("200000000012");
        instantTradeRequest.setBuyer_id_type("MEMBER_ID");
        instantTradeRequest.setBuyer_ip("127.0.0.1");
        instantTradeRequest.setPay_method("3:pos^4:0.01^13:TMALLPOS,C,GC");
        instantTradeRequest.setGo_cashier("N");
        instantTradeRequest.setIs_web_access("N");
        instantTradeRequest.setExtension("{\"biz\":\"Pay\",\"biz_code\":\"ZHDD1428199090\",\"productCode\":\"20201\",\"trade_type\":\"TRADE\"}");
        Map<String, String> reqParams = BuilderUtil.build(instantTradeRequest);
        String resp  = tradeManager.sendTradeRequest(reqParams);
        System.out.println(resp);
    }


    public static void main(String[] a){
        String instId = UUID.randomUUID().toString();
        System.out.println(instId);
    }
}
