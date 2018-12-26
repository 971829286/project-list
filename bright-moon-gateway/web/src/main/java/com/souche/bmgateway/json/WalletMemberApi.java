package com.souche.bmgateway.json;

import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.service.dto.SyncIdCardDTO;
import com.souche.bmgateway.core.service.member.WalletMemberService;
import com.souche.bmgateway.core.util.ParamsValidate;
import com.souche.bmgateway.model.request.CreateEnterpriseMemberRequest;
import com.souche.bmgateway.model.response.CommonResponse;
import com.souche.bmgateway.model.response.WalletMemberResponse;
import com.souche.bmgateway.model.request.ModifyEnterpriseInfoRequest;
import com.souche.optimus.core.annotation.Param;
import com.souche.optimus.core.annotation.Params;
import com.souche.optimus.core.annotation.Rest;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.web.OptimusRequestMethod;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author zs.
 *         Created on 18/7/17.
 */
@View(desc = "钱包资源类")
@Slf4j(topic = "restApi")
public class WalletMemberApi {

    @Resource
    private WalletMemberService walletMemberService;

    @Rest(value = "/walletMember/createAndActivateEnterpriseMember", method = OptimusRequestMethod.POST, desc = "创建并激活企业会员")
    public WalletMemberResponse createAndActivateEnterpriseMember(@Params CreateEnterpriseMemberRequest createEnterpriseMemberRequest) {
        ParamsValidate.ParamsValidateResult<CreateEnterpriseMemberRequest> validateResult = ParamsValidate.validate
                (createEnterpriseMemberRequest);
        if (validateResult.hasError()) {
            log.error("创建并激活企业会员失败", validateResult.getMsgError());
            return WalletMemberResponse.createFailResp(new WalletMemberResponse(), ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(),
                    validateResult.getMsgError());
        }
        return walletMemberService.createAndActivateEnterpriseMember(createEnterpriseMemberRequest);
    }

    @Rest(value = "/walletMember/modifyEnterpriseInfo", method = OptimusRequestMethod.POST, desc = "修改企业信息")
    public CommonResponse modifyEnterpriseInfo(@Params ModifyEnterpriseInfoRequest modifyEnterpriseInfoRequest) {
        ParamsValidate.ParamsValidateResult<ModifyEnterpriseInfoRequest> validateResult = ParamsValidate.validate
                (modifyEnterpriseInfoRequest);
        if (validateResult.hasError()) {
            log.error("修改企业会员失败", validateResult.getMsgError());
            return WalletMemberResponse.createFailResp(new CommonResponse(), ErrorCodeEnums.ILLEGAL_ARGUMENT.getCode(),
                    validateResult.getMsgError());
        }
        return walletMemberService.modifyEnterpriseInfo(modifyEnterpriseInfoRequest);
    }

    @Rest(value = "/walletMember/syncVerifyInfo", method = OptimusRequestMethod.POST, desc = "同步实名身份证信息")
    public CommonResponse syncVerifyInfo(@Param(value = "memberId", required = true) String memberId,
                                         @Param(value = "idCard", required = true) String idCard) {

        SyncIdCardDTO syncIdCardDTO = new SyncIdCardDTO();
        syncIdCardDTO.setMemberId(memberId);
        syncIdCardDTO.setIdCard(idCard);
        return walletMemberService.syncIdCard(syncIdCardDTO);
    }

}
