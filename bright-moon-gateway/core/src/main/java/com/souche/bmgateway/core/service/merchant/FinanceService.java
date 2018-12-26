package com.souche.bmgateway.core.service.merchant;

import com.souche.optimus.core.web.Result;

/**
 * 财务服务
 *
 * @author chenwj
 * @since 2018/7/18
 */
public interface FinanceService {

    /**
     * 查询开户许可证图片url
     *
     * @param memberId 会员号
     * @return Result
     */
    Result<String> queryAcctOpenLicense(String memberId);

}
