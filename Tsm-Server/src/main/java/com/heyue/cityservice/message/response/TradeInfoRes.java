package com.heyue.cityservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 终端交易查询返回结果
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeInfoRes {

    private String transaction_datetime; // 交易日期时间

    private String transaction_num; // 终端交易序号

    private String transaction_type; // 交易类型

    public String getTransaction_datetime() {
        return transaction_datetime;
    }

    public void setTransaction_datetime(String transaction_datetime) {
        this.transaction_datetime = transaction_datetime;
    }

    public String getTransaction_num() {
        return transaction_num;
    }

    public void setTransaction_num(String transaction_num) {
        this.transaction_num = transaction_num;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }
}
