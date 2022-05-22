package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 退卡通知
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardReturnNotifyReq {

    private String issue_inst; // 发卡机构代码

    private String terminal_code; // 终端编号

    private String transaction_num; // 终端交易序号：本次产生的交易序号

    private String card_no; // 卡应用序列号

    private String card_type; // 卡类型

    private String refund_amount; // 退款金额

    private String transaction_type; // 交易类型：01好卡退卡 02 坏卡退卡

    private String transaction_datetime; // 交易日期时间

    private String merchant_num; // 商户号

    private String algorithm_id; // 加密算法标识: 00国际/01国密

    private String region_code; // 地区代码

    private String card_species; // 卡种类型

    private String order_no; // 全局唯一。确定唯一一笔交易，业务幂等性判断
}
