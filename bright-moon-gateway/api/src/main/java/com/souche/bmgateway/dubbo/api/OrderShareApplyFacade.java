package com.souche.bmgateway.dubbo.api;

import com.alibaba.dubbo.doc.annotation.InterfaceDesc;
import com.alibaba.dubbo.doc.annotation.MethodDesc;
import com.alibaba.dubbo.doc.annotation.MethodParamDesc;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.request.OrderShareApplyRequest;
import com.souche.bmgateway.model.response.OrderShareApplyResponse;

/**
 * @Author: huangbin
 * @Description: 分帐请求服务
 * @Date: Created in 2018/07/09
 * @Modified By:
 */

@InterfaceDesc(value="", url="http://null///git.souche-inc.com/spay/bright-moon-gateway/tree/master/api/src/main/java/com/souche/bmgateway/dubbo/api/OrderShareApplyFacade.java")
public interface OrderShareApplyFacade {

    /**
     * 分帐请求申请接口
     *
     * @param orderShareApplyRequest
     * @return
     */
    @MethodParamDesc({
    })
    @MethodDesc("分帐请求申请接口")
    OrderShareApplyResponse doApply(OrderShareApplyRequest orderShareApplyRequest);
}
