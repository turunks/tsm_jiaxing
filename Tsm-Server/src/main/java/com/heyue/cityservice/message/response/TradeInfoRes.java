package com.heyue.cityservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 终端交易查询返回结果
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeInfoRes {

    private String transaction_datetime; // 交易日期时间

    private String transaction_num; // 终端交易序号

    private String transaction_type; // 交易类型
}
