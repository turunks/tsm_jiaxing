package com.heyue.cityservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 卡消费记录请求
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardConsumRecordRes {

    private String index; // 索引

    private String Consumeamount; // 消费金额：单位分

    private String Consumetime; // 消费时间

    private String Consumeline; // 消费线路

}
