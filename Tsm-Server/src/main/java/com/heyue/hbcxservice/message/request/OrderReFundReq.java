package com.heyue.hbcxservice.message.request;

import lombok.Data;

@Data
public class OrderReFundReq {

    /**
     * 业务订单号
     */
    private String serviceOrderId;

    /**
     * 退款金额
     */
    private Integer amount;
}
