package com.heyue.hbcxservice.message.request;

import lombok.Data;

@Data
public class TransferSubmitReq {

    /**
     * 业务订单号
     */
    private String serviceOrderId;

    private String issue_inst; // 发卡机构代码

    private String card_no; // 卡应用序列号

    private String transaction_datetime; // 交易日期时间

    private String merchant_num; // 商户号

    private String ret_status; // 写卡状态

    private String tac; // 交易验证码





}
