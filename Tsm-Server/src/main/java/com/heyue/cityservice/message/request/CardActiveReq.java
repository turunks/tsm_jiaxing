package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 卡激活申请
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardActiveReq {

    private String issue_inst; // 发卡机构代码

    private String terminal_code; // 终端编号

    private String transaction_num; // 终端交易序号：本次产生的交易序号

    private String card_no; // 卡应用序列号

    private String card_type; // 卡类型

    private String merchant_num; // 商户号

    private String random; // 随机数

    private String algorithm_id; // 加密算法标识

    private String region_code; // 地区代码

    private String card_species; // 卡种类型

    private String order_no; // 全局唯一。确定唯一一笔交易，业务幂等性判断
}
