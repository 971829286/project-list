package com.souche.bmgateway.core.service.bill;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.service.bill.service.AllinPayBillHandleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhaojian on 18/7/16.
 */
public class AllinPayBillHandleServiceTest extends BaseTest {

    @Autowired
    private AllinPayBillHandleService allinPayBillHandleService;


    @Test
    public void handleTest() {
        allinPayBillHandleService.handle("20180802");
    }
}
