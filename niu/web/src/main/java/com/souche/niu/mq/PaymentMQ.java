package com.souche.niu.mq;

import com.souche.niu.manager.carmaintenance.MaintenanceOrderManager;
import com.souche.niu.manager.carmaintenance.MaintenanceRecordManager;
import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.mq.ConsumeResult;
import com.souche.optimus.mq.MQConsumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

/**
 * mq接收支付成功消息
 *
 * @since 2018-09-10
 * @author ZhangHui
 */
public class PaymentMQ implements MQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PaymentMQ.class);

    @Autowired
    private MaintenanceRecordManager maintenanceRecordManager;

    @Autowired
    private MaintenanceOrderManager maintenanceOrderManager;

    @Override
    public ConsumeResult onRecived(Map<String, Object> map) {
        String orderCode = null;
        try {
            if(map == null || map.size() == 0){
                //消息有异常...直接跳过
                logger.error("支付消息，消息为空");
                return ConsumeResult.CommitMessage;
            }
            logger.info("支付消息 map={}", map);
            //目前收到消息代表支付成功
           Map<String,Object> payInfo=(Map<String, Object>) map.get("content");
            orderCode = (String)payInfo.get("order_code");
            String status = (String)payInfo.get("status");
            // 钱已经到平台方
            if(StringUtils.equals(status,"paid_to_platform")){
                logger.info("支付消息:钱已经到平台方orderCode={},准备更新流水状态",orderCode);
                MaintenanceRecordDO maintenanceRecordDOResult = maintenanceRecordManager.updateCarMaintenance(new MaintenanceRecordDO(orderCode,1,0));
                logger.info("支付消息 更新流水后 maintenanceRecordDOResult={}", JsonUtils.toJson(maintenanceRecordDOResult));
                maintenanceOrderManager.queryWbRequest(orderCode);
            }
            //支付消息记录
            return ConsumeResult.CommitMessage;
        } catch (Exception e) {
            logger.error("支付消息异常 order_code="+orderCode,e);
            //如果是逻辑错误那么跳过
            return ConsumeResult.ReconsumeLater;
        }
    }

}
