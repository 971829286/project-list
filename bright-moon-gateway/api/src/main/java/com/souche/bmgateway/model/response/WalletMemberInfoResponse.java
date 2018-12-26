package com.souche.bmgateway.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author zs.
 *         Created on 18/7/31.
 */
@Getter
@Setter
@ToString
public class WalletMemberInfoResponse extends CommonResponse {
    private static final long serialVersionUID = 402917629822649692L;

    /*** 会员id ***/
    private String memberId;

    /*** 会员名称 ***/
    private String memberName;

    /*** 会员状态(0未激活 1正常 2休眠 3注销) ***/
    private Long status;

    /*** 会员锁定状态(0未锁定 1已锁定) ***/
    private Long lockStatus;

    /*** 会员类型 ***/
    private Long memberType;

    /*** 会员激活时间 ***/
    private Date activeTime;

    /*** 会员创建时间 ***/
    private Date createTime;

    /*** 扩展参数 ***/
    private String extention;
}
