package com.souche.bmgateway.core.service.trade;
import com.netfinworks.common.util.money.Money;
import com.netfinworks.tradeservice.facade.enums.TradeType;
import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.service.dto.TradeInfoDTO;
import com.souche.bmgateway.dubbo.api.WalletTradeFacade;
import com.souche.bmgateway.model.request.trade.WalletFrozenRequest;
import com.souche.bmgateway.model.request.trade.WalletUnFrozenRequest;
import com.souche.optimus.common.util.UUIDUtil;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author zs.
 *         Created on 18/11/20.
 */
public class WalletTradeServiceTest extends BaseTest {

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
	private WalletTradeFacade walletTradeFacade;

    @Test
    public void testcreateInstantAndPayBALANCE (){
        TradeInfoDTO tradeInfoDTO = new TradeInfoDTO();
        tradeInfoDTO.setTradeSourceVoucherNo(UUIDUtil.getID());
        tradeInfoDTO.setTradeVoucherNo(null);//后期生成
        tradeInfoDTO.setBizNo("925274854982");
        tradeInfoDTO.setSplitParameterList(null);
       // tradeInfoDTO.setSubject("弹个车");
        tradeInfoDTO.setBizProductCode("B010401");
        tradeInfoDTO.setProductDesc("一成首付弹个车");
        tradeInfoDTO.setTradeAmount(new Money("2"));
        tradeInfoDTO.setSellerId("200000021114");
        tradeInfoDTO.setSellerAccountNo("200100100120000002111400001");
        tradeInfoDTO.setTradeMemo("车易拍");
        tradeInfoDTO.setExtension("{\"hello\":\"world\"}");
        tradeInfoDTO.setTradeType(TradeType.INSTANT_ACQUIRING);
        tradeInfoDTO.setShowUrl("http://#");
        tradeInfoDTO.setPartnerId("188888888888");
        tradeInfoDTO.setGmtInvalid(null);
        tradeInfoDTO.setBuyerId("200000000056");
        tradeInfoDTO.setBuyerAccountNo("200100100420000000005600001");
        tradeInfoDTO.setPaymentVoucherNo("");
        tradeInfoDTO.setPaymentSourceVoucherNo(UUIDUtil.getID());
        tradeInfoDTO.setPayMethodList(null);
        System.out.println(walletTradeService.createInstantAndPay(tradeInfoDTO));
    }

    @Test
    public void testWalletFrozen(){
        WalletFrozenRequest walletFrozenRequest = new WalletFrozenRequest();
		walletFrozenRequest.setOuterTradeNo("30000043551252390");
		walletFrozenRequest.setMemberId("200000953558");
		walletFrozenRequest.setAccountType("201");
		walletFrozenRequest.setFreezeAmount("1.00");
		walletFrozenRequest.setInputCharset("UTF-8");
		walletFrozenRequest.setPartnerId("188888888888");
		walletFrozenRequest.setAccountIdentity("");
		walletFrozenRequest.setBizProductCode("2011001");
        System.out.println(walletTradeFacade.walletFrozen(walletFrozenRequest));
    }

    @Test
    public void testUnWalletFrozen(){
        WalletUnFrozenRequest walletUnFrozenRequest = new WalletUnFrozenRequest();
		walletUnFrozenRequest.setOuterTradeNo("30000043551252398");
		walletUnFrozenRequest.setOrigOuterTradeNo("30000043551252390");
		//walletUnFrozenRequest.setAmount("1.00");
		walletUnFrozenRequest.setInputCharset("UTF-8");

		//walletUnFrozenRequest.setOrigInnerTradeNo("");
		walletUnFrozenRequest.setBizProductCode("");
		//walletUnFrozenRequest.setInnerTradeNo("");
		walletUnFrozenRequest.setMemo("");
		walletUnFrozenRequest.setVersion("");
		walletUnFrozenRequest.setPartnerId("");
		walletUnFrozenRequest.setInstId("");
		walletUnFrozenRequest.setSign("");
		walletUnFrozenRequest.setSignType("");
        System.out.println(walletTradeFacade.walletUnFrozen(walletUnFrozenRequest));
    }


}
