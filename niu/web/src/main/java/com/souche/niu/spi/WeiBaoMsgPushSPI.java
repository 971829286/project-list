package com.souche.niu.spi;

public interface WeiBaoMsgPushSPI {
    /**
     * @Description:
     * @Param1: msgPushContent
     * @Param2: orderId
     * @return: void
     * @Author: malin
     * @Date: 2018/9/26
     */
    void MsgPush(String msgPushContent, String orderId);

    /**
     * @Description:
     * @Param1: orderId
     * @return: java.lang.String
     * @Author: malin
     * @Date: 2018/9/26
     */
    String getUserId(String orderId);
}
