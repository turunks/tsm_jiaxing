package com.heyue.hbcxservice.message.request;

import lombok.Data;

@Data
public class UserCardActiveReq {

    /**
     * 业务订单号
     */
    private String serviceOrderId;

    private String issue_inst; // 发卡机构代码

    private String terminal_code; // 终端编号

    private String card_no; // 卡应用序列号

    private String card_type; // 卡类型

    private String random; // 随机数

    private String algorithm_id; // 加密算法标识

    private String region_code; // 地区代码

    private String card_species; // 卡种类型

}
