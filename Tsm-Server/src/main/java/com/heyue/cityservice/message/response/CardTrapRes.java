package com.heyue.cityservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 卡圈存请求
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardTrapRes {

    private String MAC2; // MAC2

    private String transaction_datetime; // 交易日期时间

    private String transaction_num; // 终端交易序号

    public String getMAC2() {
        return MAC2;
    }

    public void setMAC2(String MAC2) {
        this.MAC2 = MAC2;
    }

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
}
