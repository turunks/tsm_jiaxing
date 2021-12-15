package com.heyue.hbcxservice.message.request;

import lombok.Data;

@Data
public class TransferApplyReq {

    /**
     * 业务订单号
     */
    private String serviceOrderId;
    /**
     * 发卡机构代码
     */
    private String issue_inst;

    private String card_no; // 卡应用序列号

    private String card_type; // 卡类型

    private String card_balance; // 卡片交易前余额

    private String card_transaction_num; // 卡交易序号

    private String random; // 随机数：获取卡的4字节随机数

    private String mac; // MAC1

    private String algorithm_id; // 加密算法标识: 00国际/01国密





}
