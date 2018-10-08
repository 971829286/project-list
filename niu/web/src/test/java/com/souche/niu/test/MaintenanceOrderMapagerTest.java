package com.souche.niu.test;

import com.souche.niu.manager.carmaintenance.MaintenanceOrderManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 单元测试
 *
 * @author ZhangHui
 * @since 2018-09-05
 */
public class MaintenanceOrderMapagerTest extends BaseTest {

    @Autowired
    private MaintenanceOrderManager maintenanceOrderManager;

    //退款测试
    @Test
    public void refundOrderTest(){

        maintenanceOrderManager.refund("889351922880");

    }

}
