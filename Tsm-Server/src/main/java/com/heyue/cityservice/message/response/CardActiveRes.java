package com.heyue.cityservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 卡激活申请响应
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardActiveRes {

    private String transaction_datetime; // 交易日期时间

    private String transaction_num; // 终端交易序号

    private String random; // 随机数

    private String apdu; // 卡激活指令数据

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

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getApdu() {
        return apdu;
    }

    public void setApdu(String apdu) {
        this.apdu = apdu;
    }
}
