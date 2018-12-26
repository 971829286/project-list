package com.souche.bmgateway.core.mq;

import com.souche.optimus.mq.MQConsumer;
import com.souche.bmgateway.core.enums.BusinessTypeConver;
import com.souche.optimus.mq.ConsumeResult;
import com.souche.witness.api.service.WitnessMqSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 交易成功通知mq消费
 * 
 * @author luobing 2018/12/14
 *
 */

public class WitnessTradeDataConsumer implements MQConsumer {

	private final static Logger logger = LoggerFactory.getLogger(WitnessTradeDataConsumer.class);

	@Resource
	private WitnessMqSendService witnessMqSendService;

	@Override
	public ConsumeResult onRecived(Map<String, Object> map) {
		logger.info("start to revice mq...");
		if (map == null || map.size() == 0) {
			logger.error("消息为空");
			return ConsumeResult.CommitMessage;
		}
		String outTradeNo = map.get("outer_trade_no") + "";
		logger.info("开始接受金融基础服务网关服务消息 outTradeNo=>{}, 消息内容=>{}", outTradeNo, map);
		try {

			String tradeType = map.get("business_type") + "";
			// 将上游交易类型映射为我们的交易类型
			BusinessTypeConver businessTypeConver = BusinessTypeConver.getBusinessTypeByCode(tradeType);
			// 交易成功进行同步
			if (businessTypeConver != null
					&& businessTypeConver.getSuccessStatus().equals(map.get(businessTypeConver.getStatusParam()))) {
				logger.info("outTradeNo：" + outTradeNo + " 交易成功调用见证消息同步接口");
				witnessMqSendService.sendMQByVoucherNo(businessTypeConver.getBusinessType(), outTradeNo);
			}
			return ConsumeResult.CommitMessage;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("process notify mq error:", e);
		}
		return ConsumeResult.ReconsumeLater;
	}

}
