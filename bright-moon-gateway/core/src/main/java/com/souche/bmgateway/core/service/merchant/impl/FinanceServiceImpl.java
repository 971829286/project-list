package com.souche.bmgateway.core.service.merchant.impl;

import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.service.merchant.FinanceService;
import com.souche.finance.counter.sdk.facade.AccountOrderFacade;
import com.souche.finance.counter.sdk.request.GetVoucherPicRequest;
import com.souche.finance.counter.sdk.response.Response;
import com.souche.optimus.common.util.Exceptions;
import com.souche.optimus.core.web.Result;
import com.souche.optimus.exception.OptimusExceptionBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 财务服务实现类
 *
 * @author chenwj
 * @since 2018/7/18
 */
@Service("financeService")
@Slf4j(topic = "service")
public class FinanceServiceImpl implements FinanceService {

    @Resource
    private AccountOrderFacade accountOrderFacade;

    /**
     * 查询开户许可证URL
     *
     * @param memberId 维金id/会员id/钱包id
     */
    @Override
    public Result<String> queryAcctOpenLicense(String memberId) {
        log.info("<查询开户许可证>请求参数->{}", memberId);
        GetVoucherPicRequest request = new GetVoucherPicRequest();
        request.setMemberId(memberId);
        Response voucherPic;
        try {
            long beginTime = System.currentTimeMillis();
            voucherPic = accountOrderFacade.getVoucherPic(request);
            long consumeTime = System.currentTimeMillis() - beginTime;
            log.info("<查询开户许可证>， 耗时:{} (ms); 响应结果:{}", consumeTime, voucherPic);
            if (!voucherPic.isSuccess()) {
                return Result.fail(ErrorCodeEnums.QUERY_ACCOUNT_OPEN_LIC_ERROR.getCode(), "<查询开户许可证>查询失败");
            }
            if (voucherPic.getT() == null) {
                return Result.fail(ErrorCodeEnums.QUERY_ACCOUNT_OPEN_LIC_ERROR.getCode(), "<查询开户许可证>查询结果为空");
            }

        } catch (OptimusExceptionBase e) {
            throw Exceptions.fault(ErrorCodeEnums.QUERY_ACCOUNT_OPEN_LIC_ERROR.getCode(), ErrorCodeEnums.QUERY_ACCOUNT_OPEN_LIC_ERROR.getMessage());
        }
        log.info("<查询开户许可证>响应->{}", String.valueOf(voucherPic.getT()));
        return Result.success(String.valueOf(voucherPic.getT()));
    }

}
