package com.souche.bmgateway.core.enums;

/**
 * @author zs.
 *         Created on 17/12/4.
 */
public enum ServiceKind {

    create_instant_trade("create_instant_trade"),
    create_ensure_trade("create_ensure_trade"),
    create_settle("create_settle"),
    create_pay("create_pay"),
    operator_substitute_recharge("operator_substitute_recharge"),
    create_refund("create_refund"),
    prepay_pay("prepay_pay"),
    prepay_withhold("prepay_withhold"),
    query_trade("query_trade"),
    query_pay("query_pay"),
    create_deposit("create_deposit"),
    change_trade_amount("change_trade_amount"),
    query_operator_recharge("query_operator_recharge"),
    cancel_trade("cancel_trade"),
    create_batched_withdrawal("create_batched_withdrawal"),
    create_withdrawal("create_withdrawal"),
    query_margin_changes("query_margin_changes"),
    balance_transfer("balance_transfer"),
    margin_recharge("margin_recharge"),
    payment_to_card("payment_to_card"),
    frozen_funds("frozen_funds"),
    unfreeze_funds("unfreeze_funds"),
	fundout("fundout");

    private String code;

    private ServiceKind(String code) {
        this.code = code;

    }

    public static ServiceKind getByCode(String code) {
        for (ServiceKind ls : ServiceKind.values()) {
            if (ls.code.equalsIgnoreCase(code)) {
                return ls;
            }
        }
        return null;
    }

    public boolean equals(String code) {
        return getCode().equalsIgnoreCase(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}