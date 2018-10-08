package com.souche.niu.mq;


import com.alibaba.druid.support.json.JSONUtils;
import com.souche.niu.manager.carmaintenance.MaintenanceRecordManager;
import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.niu.spi.OrderCenterSPI;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.core.annotation.Json;
import com.souche.optimus.mq.ConsumeResult;
import com.souche.optimus.mq.MQConsumer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

/**
 * Created by sid on 2018/9/14.
 */
public class RefundMQ implements MQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PaymentMQ.class);

    @Autowired
    private MaintenanceRecordManager maintenanceRecordManager;

    @Autowired
    private OrderCenterSPI orderCenterSPI;

    @Override
    public ConsumeResult onRecived(Map<String, Object> map) {
        try {
            if (map == null || map.size() == 0) {
                //消息有异常...直接跳过
                logger.error("退款消息，消息为空");
                return ConsumeResult.CommitMessage;
            }
            logger.info("退款消息 收到退款信息 map={}", JsonUtils.toJson(map));
            //目前收到消息代表支付成功
            String jsonObject=(String)map.get("dataObject");
            Map<String,Object> objectMap=(Map<String, Object>) JSONUtils.parse(jsonObject);
            String requestNo = (String) objectMap.get("requestNo");
            String orderCenterNo = (String) objectMap.get("orderCenterNo");
            String status = String.valueOf(objectMap.get("status"));
            String reason = (String) objectMap.get("reason");
            logger.info("退款消息 退款状态 orderCenterNo = {}", orderCenterNo);
            if (StringUtils.equals(status, "1")) {
                MaintenanceRecordDO maintenanceRecordDO = new MaintenanceRecordDO(orderCenterNo, 1);
                MaintenanceRecordDO maintenanceRecordDO1 = maintenanceRecordManager.updateCarMaintenance(maintenanceRecordDO);
                //是否有优惠券
                if(org.apache.commons.lang3.StringUtils.isEmpty(maintenanceRecordDO1.getCouponCode())){
                    logger.info("退款消息 有优惠券 couponCode = {}", maintenanceRecordDO1.getCouponCode());
                    //释放卷
                    orderCenterSPI.realeaseCoupon(maintenanceRecordDO);
                }
                return ConsumeResult.CommitMessage;
            }
            logger.error("订单:{},退款失败,原因:{},请求流水号为:{}", orderCenterNo, reason, requestNo);
            MaintenanceRecordDO maintenanceRecordDO = new MaintenanceRecordDO(orderCenterNo, 2);
            maintenanceRecordManager.updateCarMaintenance(maintenanceRecordDO);
            return ConsumeResult.CommitMessage;
        } catch (Exception e) {
            logger.error("退款消息 存退款结果失败",e);
            return ConsumeResult.ReconsumeLater;
        }
    }
}
