package com.souche.niu.test;

import com.souche.coupon.model.qo.OperateCouponQO;
import com.souche.niu.manager.carmaintenance.MaintenanceOrderManager;
import com.souche.niu.manager.carmaintenance.MaintenanceRecordManager;
import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.niu.spi.OrderCenterSPI;
import com.souche.order.model.OrderDTO;
import com.souche.order.model.OrderRegisterQO;
import com.souche.order.model.PaymentOrderDTO;
import com.souche.order.model.PaymentOrderQO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 单元测试
 *
 * @author ZhangHui
 * @since 2018-09-05
 */
public class OrderCenterSPITest extends BaseTest {

    @Autowired
    private OrderCenterSPI orderCenterSPI;

    @Autowired
    private MaintenanceOrderManager maintenanceOrderManager;

    @Autowired
    private MaintenanceRecordManager maintenanceRecordManager;
    //创建订单
    @Test
    public void createOrderTest(){

        OrderRegisterQO orderRegisterQO = new OrderRegisterQO();
        // 买家类型
        orderRegisterQO.setBuyerType("cheniu_user");
        // 物品类型
        orderRegisterQO.setItemType("service");
        // 这个物品的ID
        orderRegisterQO.setItemId("0");
        // 订单待支付的金额(单位是分)
        orderRegisterQO.setAmountFens(15L);
        // 卖家类型
        orderRegisterQO.setSellerType("cheniu_user");
        // 卖家ID
        orderRegisterQO.setSellerId("000000");
        // 账单标题
        orderRegisterQO.setTradeInfo("车牛保养查询个人支付订单");
        // 账单描述
        orderRegisterQO.setTradeDesc("车牛保养查询个人支付订单");
        // 账单业务类型
        orderRegisterQO.setTradeWay("车牛保养查询个人支付订单");

        OrderDTO order = orderCenterSPI.createOrder(orderRegisterQO);




    }

    //核销卷
    @Test
    public void createOrder2Test(){
        OperateCouponQO operateCouponQO = new OperateCouponQO();
        String s = orderCenterSPI.finishCoupon(operateCouponQO);

    }

    //修改订单
    @Test
    public void createOrder3Test(){
        PaymentOrderQO paymentOrderQO = new PaymentOrderQO();
        PaymentOrderDTO paymentOrder = orderCenterSPI.createPaymentOrder(paymentOrderQO);
    }


    @Test
    public void refund(){

        maintenanceOrderManager.refund("880851925890");
    }

    @Test
    public void reStatus(){


        maintenanceRecordManager.updateCarMaintenance(new MaintenanceRecordDO("880618559021", 4, "0", "报告已出","",""));

    }



}
