package com.heyue.hbcxservice.message.request;

import lombok.Data;

@Data
public class UserCardActiveSubmitReq {

    /**
     * 业务订单号
     */
    private String serviceOrderId;

    private String issue_inst; // 发卡机构代码

    private String terminal_code; // 终端编号

    private String card_no; // 卡应用序列号

    private String transaction_datetime; // 交易日期时间

    private String ret_status; // 写卡状态：0成功 1表示失败
}
